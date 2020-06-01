package me.hsgamer.bettergui.advancedcooldown;

import java.io.File;
import me.hsgamer.bettergui.builder.RequirementBuilder;
import me.hsgamer.bettergui.config.ConfigPath;
import me.hsgamer.bettergui.object.addon.Addon;

public final class Main extends Addon {

  public static final ConfigPath<String> COOLDOWN_NOT_FOUND = new ConfigPath<>(String.class,
      "cooldown-not-found", "&cCan't find the cooldown with the name '{input}'");
  private static Manager manager;
  private static File cooldownDataFolder;

  public static Manager getManager() {
    return manager;
  }

  public static File getCooldownDataFolder() {
    return cooldownDataFolder;
  }

  @Override
  public boolean onLoad() {
    COOLDOWN_NOT_FOUND.setConfig(getPlugin().getMessageConfig());
    getPlugin().getMessageConfig().saveConfig();

    setupConfig();
    cooldownDataFolder = new File(getDataFolder(), "data");
    if (!cooldownDataFolder.exists()) {
      cooldownDataFolder.mkdirs();
    }
    return true;
  }

  @Override
  public void onEnable() {
    manager = new Manager(this);
    manager.loadData();

    RequirementBuilder.register("advanced-cooldown", AdvancedCooldownRequirement.class);
  }

  @Override
  public void onDisable() {
    manager.saveData();
  }

  @Override
  public void onReload() {
    manager.reloadData();
  }
}
