package me.hsgamer.bettergui.advancedcooldown;

import java.io.File;
import java.util.Map;
import java.util.UUID;
import me.hsgamer.bettergui.config.PluginConfig;
import me.hsgamer.bettergui.object.LocalVariableManager;
import me.hsgamer.bettergui.util.CaseInsensitiveStringMap;
import me.hsgamer.bettergui.util.CommonUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class Manager {

  private final Map<String, Cooldown> cooldownMap = new CaseInsensitiveStringMap<>();
  private final Main main;

  public Manager(Main main) {
    this.main = main;
  }

  public void loadData() {
    FileConfiguration configuration = main.getConfig();
    configuration.getValues(false).forEach((key, value) -> {
      PluginConfig dataConfig = new PluginConfig(main.getPlugin(),
          new File(Main.getCooldownDataFolder(), key + ".yml"));
      Cooldown cooldown = new Cooldown(key, String.valueOf(value), dataConfig);
      cooldown.loadData();
      cooldownMap.put(key, cooldown);
    });
  }

  public void saveData() {
    cooldownMap.values().forEach(Cooldown::saveData);
  }

  public void reloadData() {
    saveData();
    cooldownMap.clear();
    main.reloadConfig();
    loadData();
  }

  public boolean isInCooldown(String cooldownName, Player player) {
    if (cooldownMap.containsKey(cooldownName)) {
      return cooldownMap.get(cooldownName).isInCooldown(player);
    } else {
      CommonUtils.sendMessage(player, Main.COOLDOWN_NOT_FOUND.getValue());
      return true;
    }
  }

  public void startCooldown(String cooldownName, Player player,
      LocalVariableManager<?> localVariableManager) {
    if (cooldownMap.containsKey(cooldownName)) {
      cooldownMap.get(cooldownName).startCooldown(player, localVariableManager);
    } else {
      CommonUtils.sendMessage(player, Main.COOLDOWN_NOT_FOUND.getValue());
    }
  }

  public long getCooldown(String cooldownName, UUID uuid) {
    if (cooldownMap.containsKey(cooldownName)) {
      return cooldownMap.get(cooldownName).getCooldown(uuid);
    } else {
      return 0;
    }
  }
}
