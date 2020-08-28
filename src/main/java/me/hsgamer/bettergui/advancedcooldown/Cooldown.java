package me.hsgamer.bettergui.advancedcooldown;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import me.hsgamer.bettergui.config.MessageConfig;
import me.hsgamer.bettergui.manager.VariableManager;
import me.hsgamer.bettergui.object.variable.GlobalVariable;
import me.hsgamer.bettergui.object.variable.LocalVariableManager;
import me.hsgamer.bettergui.util.MessageUtils;
import me.hsgamer.bettergui.util.config.PluginConfig;
import me.hsgamer.bettergui.util.expression.ExpressionUtils;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class Cooldown implements GlobalVariable {

  private final Map<UUID, Instant> cooldownMap = new HashMap<>();
  private final String name;
  private final String value;
  private final PluginConfig dataConfig;

  public Cooldown(String name, String value, PluginConfig dataConfig) {
    this.name = name;
    this.value = value;
    this.dataConfig = dataConfig;

    VariableManager.register("advanced_cooldown_" + name, this);
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
      MessageUtils
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

  @Override
  public String getReplacement(OfflinePlayer offlinePlayer, String s) {
    long millis = getCooldown(offlinePlayer.getUniqueId());
    millis = millis > 0 ? millis : 0;

    if (s.toLowerCase().startsWith("_format")) {
      return DurationFormatUtils.formatDuration(millis, s.substring(8));
    }

    switch (s.toLowerCase()) {
      case "_s":
      case "_seconds":
        return String.valueOf(millis / 1000);
      case "_m":
      case "_minutes":
        return String.valueOf(millis / 60000);
      case "_h":
      case "_hours":
        return String.valueOf(millis / 3600000);
      default:
        return String.valueOf(millis);
    }
  }
}
