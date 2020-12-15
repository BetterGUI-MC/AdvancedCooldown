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
        getInstance().getMessageConfig().saveConfig();

        setupConfig();
        getConfig().options().header("Format: \n<name>: <value>\n\nExample:\nsimple-cooldown: 100");
        getConfig().options().copyHeader(true);
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
        Manager.saveData();
        Manager.clearData();
    }

    @Override
    public void onReload() {
        Manager.saveData();
        Manager.clearData();
        reloadConfig();
        Manager.loadData();
    }
}
