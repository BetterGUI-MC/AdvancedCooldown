package me.hsgamer.bettergui.advancedcooldown;

import me.hsgamer.bettergui.api.requirement.BaseRequirement;
import me.hsgamer.bettergui.builder.RequirementBuilder;
import me.hsgamer.bettergui.util.StringReplacerApplier;

import java.util.UUID;

public class AdvancedCooldownRequirement extends BaseRequirement<String> {
    public AdvancedCooldownRequirement(RequirementBuilder.Input input) {
        super(input);
    }

    @Override
    protected String convert(Object value, UUID uuid) {
        return StringReplacerApplier.replace(String.valueOf(value).trim(), uuid, this);
    }

    @Override
    protected Result checkConverted(UUID uuid, String value) {
        if (Manager.isInCooldown(value, uuid)) {
            return Result.fail();
        }
        return Result.success(uuid1 -> Manager.startCooldown(value, uuid1));
    }
}
