package me.hsgamer.bettergui.advancedcooldown;

import me.hsgamer.hscore.bukkit.config.BukkitConfig;
import me.hsgamer.hscore.bukkit.utils.MessageUtils;
import me.hsgamer.hscore.collections.map.CaseInsensitiveStringHashMap;
import me.hsgamer.hscore.config.Config;
import me.hsgamer.hscore.variable.VariableBundle;
import org.bukkit.Bukkit;

import java.io.File;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class Manager {
    private static final Map<String, Cooldown> cooldownMap = new CaseInsensitiveStringHashMap<>();
    private static final VariableBundle variableBundle = new VariableBundle();
    private static Config config;
    private static File folder;

    private Manager() {
        // EMPTY
    }

    public static VariableBundle getVariableBundle() {
        return variableBundle;
    }

    public static void setConfig(Config config) {
        Manager.config = config;
    }

    public static void setFolder(File folder) {
        Manager.folder = folder;
    }

    public static void loadData() {
        config.getNormalizedValues(false).forEach((path, value) -> {
            String key = path[0];
            Config dataConfig = new BukkitConfig(new File(folder, key + ".yml"));
            dataConfig.setup();
            Cooldown cooldown = new Cooldown(key, String.valueOf(value), dataConfig);
            cooldown.loadData();
            cooldownMap.put(key, cooldown);
        });
    }

    public static void saveData() {
        cooldownMap.values().forEach(Cooldown::saveData);
    }

    public static void clearData() {
        cooldownMap.clear();
        variableBundle.unregisterAll();
    }

    public static boolean isInCooldown(String cooldownName, UUID uuid) {
        return Optional.ofNullable(cooldownMap.get(cooldownName))
                .map(cooldown -> cooldown.isInCooldown(uuid))
                .orElseGet(() -> {
                    Optional.ofNullable(Bukkit.getPlayer(uuid)).ifPresent(player -> MessageUtils.sendMessage(player, "Invalid cooldown name: " + cooldownName));
                    return true;
                });
    }

    public static void startCooldown(String cooldownName, UUID uuid) {
        if (cooldownMap.containsKey(cooldownName)) {
            cooldownMap.get(cooldownName).startCooldown(uuid);
        } else {
            Optional.ofNullable(Bukkit.getPlayer(uuid)).ifPresent(player -> MessageUtils.sendMessage(player, "Invalid cooldown name: " + cooldownName));
        }
    }

    public static long getCooldown(String cooldownName, UUID uuid) {
        return Optional.ofNullable(cooldownMap.get(cooldownName))
                .map(cooldown -> cooldown.getCooldown(uuid))
                .orElse(0L);
    }
}
