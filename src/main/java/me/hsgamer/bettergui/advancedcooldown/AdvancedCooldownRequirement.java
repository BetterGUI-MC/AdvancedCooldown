package me.hsgamer.bettergui.advancedcooldown;

import me.hsgamer.bettergui.object.Requirement;
import org.bukkit.entity.Player;

public class AdvancedCooldownRequirement extends Requirement<String, String> {

  public AdvancedCooldownRequirement() {
    super(true);
  }

  @Override
  public String getParsedValue(Player player) {
    return parseFromString(value, player);
  }

  @Override
  public boolean check(Player player) {
    return !Main.getManager().isInCooldown(getParsedValue(player), player);
  }

  @Override
  public void take(Player player) {
    Main.getManager().startCooldown(getParsedValue(player), player, getVariableManager());
  }
}
