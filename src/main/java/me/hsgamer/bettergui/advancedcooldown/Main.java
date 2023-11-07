package me.hsgamer.bettergui.advancedcooldown;

import me.hsgamer.bettergui.api.addon.Reloadable;
import me.hsgamer.bettergui.builder.RequirementBuilder;
import me.hsgamer.hscore.bukkit.config.BukkitConfig;
import me.hsgamer.hscore.config.Config;
import me.hsgamer.hscore.expansion.common.Expansion;
import me.hsgamer.hscore.expansion.extra.expansion.DataFolder;

import java.io.File;

public final class Main implements Expansion, DataFolder, Reloadable {

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
        Manager.saveData();
        Manager.clearData();
    }

    @Override
    public void onReload() {
        Manager.saveData();
        Manager.clearData();
        config.reload();
        Manager.loadData();
    }
}
