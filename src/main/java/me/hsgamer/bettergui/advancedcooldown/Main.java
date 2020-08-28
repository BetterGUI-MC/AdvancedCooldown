package me.hsgamer.bettergui.advancedcooldown;

import static me.hsgamer.bettergui.BetterGUI.getInstance;

import java.io.File;
import me.hsgamer.bettergui.builder.RequirementBuilder;
import me.hsgamer.bettergui.object.addon.Addon;
import me.hsgamer.bettergui.util.config.path.StringConfigPath;

public final class Main extends Addon {

  public static final StringConfigPath COOLDOWN_NOT_FOUND = new StringConfigPath(
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
    COOLDOWN_NOT_FOUND.setConfig(getInstance().getMessageConfig());
    getInstance().getMessageConfig().saveConfig();

    setupConfig();
    getConfig().options().header("Format: \n<name>: <value>\n\nExample:\nsimple-cooldown: 100");
    getConfig().options().copyHeader(true);
    saveConfig();
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

    RequirementBuilder.register(AdvancedCooldownRequirement::new, "advanced-cooldown");
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
