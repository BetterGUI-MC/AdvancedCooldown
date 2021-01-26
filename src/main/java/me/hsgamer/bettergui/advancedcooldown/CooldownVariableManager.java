package me.hsgamer.bettergui.advancedcooldown;

import me.hsgamer.bettergui.lib.core.common.interfaces.StringReplacer;
import me.hsgamer.bettergui.lib.core.variable.VariableManager;

import java.util.HashSet;
import java.util.Set;

final class CooldownVariableManager {
    private static final Set<String> variables = new HashSet<>();

    private CooldownVariableManager() {
        // EMPTY
    }

    protected static void register(String prefix, StringReplacer replacer) {
        variables.add(prefix);
        VariableManager.register(prefix, replacer);
    }

    protected static void unregisterAll() {
        variables.forEach(VariableManager::unregister);
        variables.clear();
    }
}
