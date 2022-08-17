package me.hsgamer.bettergui.advancedcooldown;

import me.hsgamer.hscore.common.interfaces.StringReplacer;
import me.hsgamer.hscore.variable.VariableManager;

import java.util.HashSet;
import java.util.Set;

final class CooldownVariableManager {
    private static final Set<String> variables = new HashSet<>();

    private CooldownVariableManager() {
        // EMPTY
    }

    static void register(String prefix, StringReplacer replacer) {
        variables.add(prefix);
        VariableManager.register(prefix, replacer);
    }

    static void unregisterAll() {
        variables.forEach(VariableManager::unregister);
        variables.clear();
    }
}
