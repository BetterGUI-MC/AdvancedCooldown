package me.hsgamer.bettergui.advancedcooldown;

import me.hsgamer.bettergui.api.requirement.BaseRequirement;
import me.hsgamer.bettergui.lib.core.variable.VariableManager;

import java.util.UUID;

public class AdvancedCooldownRequirement extends BaseRequirement<String> {
    public AdvancedCooldownRequirement(String name) {
        super(name);
    }

    @Override
    public String getParsedValue(UUID uuid) {
        return VariableManager.setVariables(String.valueOf(value), uuid);
    }

    @Override
    public boolean check(UUID uuid) {
        return !Manager.isInCooldown(getParsedValue(uuid), uuid);
    }

    @Override
    public void take(UUID uuid) {
        Manager.startCooldown(getParsedValue(uuid), uuid);
    }
}
