package me.hsgamer.bettergui.advancedcooldown;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import me.hsgamer.bettergui.config.PluginConfig;
import me.hsgamer.bettergui.config.impl.MessageConfig;
import me.hsgamer.bettergui.object.LocalVariableManager;
import me.hsgamer.bettergui.util.CommonUtils;
import me.hsgamer.bettergui.util.ExpressionUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class Cooldown {

  private final Map<UUID, Instant> cooldownMap = new HashMap<>();
  private final String name;
  private final String value;
  private final PluginConfig dataConfig;

  public Cooldown(String name, String value, PluginConfig dataConfig) {
    this.name = name;
    this.value = value;
    this.dataConfig = dataConfig;
  }

  public void loadData() {
    dataConfig.getConfig().getValues(false).forEach((key, o) -> {
      Instant instant = Instant.parse(String.valueOf(o));
      if (instant.isAfter(Instant.now())) {
        cooldownMap.put(UUID.fromString(key), instant);
      }
    });
  }

  public void saveData() {
    FileConfiguration file = dataConfig.getConfig();
    file.getKeys(false).forEach(key -> file.set(key, null));
    cooldownMap.forEach((uuid, instant) -> {
      if (getCooldown(uuid) > 0) {
        file.set(uuid.toString(), instant.toString());
      }
    });
    dataConfig.saveConfig();
  }

  private Duration getParsedDuration(Player player, LocalVariableManager<?> localVariableManager) {
    String parsed = localVariableManager.setVariables(String.valueOf(value).trim(), player);
    if (ExpressionUtils.isValidExpression(parsed)) {
      return Duration.ofMillis((long) ExpressionUtils.getResult(parsed).doubleValue() * 1000);
    } else {
      CommonUtils
          .sendMessage(player, MessageConfig.INVALID_NUMBER.getValue().replace("{input}", parsed));
      return Duration.ZERO;
    }
  }

  public boolean isInCooldown(Player player) {
    return getCooldown(player.getUniqueId()) > 0;
  }

  public void startCooldown(Player player, LocalVariableManager<?> localVariableManager) {
    Duration cooldownTime = getParsedDuration(player, localVariableManager);
    if (!(cooldownTime.isNegative() || cooldownTime.isZero())) {
      cooldownMap.put(player.getUniqueId(), Instant.now().plus(cooldownTime));
    }
  }

  public String getName() {
    return name;
  }

  public long getCooldown(UUID uuid) {
    if (cooldownMap.containsKey(uuid)) {
      return Instant.now().until(cooldownMap.get(uuid), ChronoUnit.MILLIS);
    } else {
      return 0;
    }
  }
}
