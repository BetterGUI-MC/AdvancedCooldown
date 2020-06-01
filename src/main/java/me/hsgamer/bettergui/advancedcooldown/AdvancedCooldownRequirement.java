package me.hsgamer.bettergui.advancedcooldown;

import me.hsgamer.bettergui.object.LocalVariable;
import me.hsgamer.bettergui.object.LocalVariableManager;
import me.hsgamer.bettergui.object.Requirement;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class AdvancedCooldownRequirement extends Requirement<String, String> implements
    LocalVariable {

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

  @Override
  public String getIdentifier() {
    return "advanced_cooldown";
  }

  @Override
  public LocalVariableManager<?> getInvolved() {
    return getVariableManager();
  }

  @Override
  public String getReplacement(OfflinePlayer offlinePlayer, String s) {
    long millis = Main.getManager()
        .getCooldown(getVariableManager().setVariables(value, offlinePlayer),
            offlinePlayer.getUniqueId());
    millis = millis > 0 ? millis : 0;
    int divide;

    switch (s.toLowerCase()) {
      case "_s":
      case "_seconds":
        divide = 1000;
        break;
      case "_m":
      case "_minutes":
        divide = 60000;
        break;
      case "_h":
      case "_hours":
        divide = 3600000;
        break;
      default:
        divide = 1;
        break;
    }

    return String.valueOf(millis / divide);
  }
}
