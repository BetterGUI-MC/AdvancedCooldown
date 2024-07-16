package me.hsgamer.bettergui.advancedcooldown;

import me.hsgamer.bettergui.BetterGUI;
import me.hsgamer.bettergui.config.MessageConfig;
import me.hsgamer.bettergui.util.StringReplacerApplier;
import me.hsgamer.hscore.bukkit.utils.MessageUtils;
import me.hsgamer.hscore.common.StringReplacer;
import me.hsgamer.hscore.common.Validate;
import me.hsgamer.hscore.config.Config;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Cooldown {
    private final Map<UUID, Instant> cooldownMap = new ConcurrentHashMap<>();
    private final String value;
    private final Config dataConfig;

    public Cooldown(String name, String value, Config dataConfig) {
        this.value = value;
        this.dataConfig = dataConfig;

        Manager.getVariableBundle().register("advanced_cooldown_" + name, StringReplacer.of((original, uuid) -> {
            long millis = getCooldown(uuid);
            millis = millis > 0 ? millis : 0;

            if (original.toLowerCase().startsWith("_format_")) {
                return DurationFormatUtils.formatDuration(millis, original.substring("_format_".length()));
            }

            switch (original.toLowerCase()) {
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
        }));
    }

    public void loadData() {
        dataConfig.getNormalizedValues(false).forEach((key, o) -> {
            Instant instant = Instant.parse(String.valueOf(o));
            if (instant.isAfter(Instant.now())) {
                cooldownMap.put(UUID.fromString(key[0]), instant);
            }
        });
    }

    public void saveData() {
        dataConfig.getKeys(false).forEach(dataConfig::remove);
        cooldownMap.forEach((uuid, instant) -> {
            if (getCooldown(uuid) > 0) {
                dataConfig.set(instant.toString(), uuid.toString());
            }
        });
        dataConfig.save();
    }

    private Duration getParsedDuration(UUID uuid) {
        String parsed = StringReplacerApplier.replace(value, uuid, true);
        return Validate.getNumber(parsed).map(BigDecimal::doubleValue).map(d -> (long) (d * 1000)).map(Duration::ofMillis)
                .orElseGet(() -> {
                    Optional.ofNullable(Bukkit.getPlayer(uuid)).ifPresent(player -> MessageUtils.sendMessage(player, BetterGUI.getInstance().get(MessageConfig.class).getInvalidNumber(parsed)));
                    return Duration.ZERO;
                });
    }

    public boolean isInCooldown(UUID uuid) {
        return getCooldown(uuid) > 0;
    }

    public void startCooldown(UUID uuid) {
        Duration cooldownTime = getParsedDuration(uuid);
        if (!(cooldownTime.isNegative() || cooldownTime.isZero())) {
            cooldownMap.put(uuid, Instant.now().plus(cooldownTime));
        }
    }

    public long getCooldown(UUID uuid) {
        return Optional.ofNullable(cooldownMap.get(uuid)).map(instant -> Instant.now().until(instant, ChronoUnit.MILLIS)).orElse(0L);
    }
}
