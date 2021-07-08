package me.hsgamer.bettergui.advancedcooldown;

import me.hsgamer.bettergui.api.addon.BetterGUIAddon;
import me.hsgamer.bettergui.builder.RequirementBuilder;
import me.hsgamer.bettergui.lib.core.config.path.StringConfigPath;

import java.io.File;

import static me.hsgamer.bettergui.BetterGUI.getInstance;

public final class Main extends BetterGUIAddon {

    public static final StringConfigPath COOLDOWN_NOT_FOUND = new StringConfigPath("cooldown-not-found", "&cCan't find the cooldown with the name '{input}'");

    @Override
    public boolean onLoad() {
        COOLDOWN_NOT_FOUND.setConfig(getInstance().getMessageConfig());
        getInstance().getMessageConfig().save();

        setupConfig();
        saveConfig();

        File cooldownDataFolder = new File(getDataFolder(), "data");
        if (!cooldownDataFolder.exists()) {
            cooldownDataFolder.mkdirs();
        }

        Manager.setFolder(cooldownDataFolder);
        Manager.setConfig(getConfig());
        return true;
    }

    @Override
    public void onEnable() {
        Manager.loadData();
        RequirementBuilder.INSTANCE.register(AdvancedCooldownRequirement::new, "advanced-cooldown");
    }

    @Override
    public void onDisable() {
        CooldownVariableManager.unregisterAll();
        Manager.saveData();
        Manager.clearData();
    }

    @Override
    public void onReload() {
        CooldownVariableManager.unregisterAll();
        Manager.saveData();
        Manager.clearData();
        reloadConfig();
        Manager.loadData();
    }
}
