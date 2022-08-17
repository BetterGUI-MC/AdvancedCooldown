package me.hsgamer.bettergui.advancedcooldown;

import me.hsgamer.bettergui.builder.RequirementBuilder;
import me.hsgamer.hscore.bukkit.addon.PluginAddon;
import me.hsgamer.hscore.bukkit.config.BukkitConfig;
import me.hsgamer.hscore.config.Config;

import java.io.File;

public final class Main extends PluginAddon {

    private final Config config = new BukkitConfig(new File(getDataFolder(), "config.yml"));

    @Override
    public boolean onLoad() {
        File cooldownDataFolder = new File(getDataFolder(), "data");
        if (!cooldownDataFolder.exists()) {
            cooldownDataFolder.mkdirs();
        }

        Manager.setFolder(cooldownDataFolder);
        config.setup();
        Manager.setConfig(config);
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
        config.reload();
        Manager.loadData();
    }
}
