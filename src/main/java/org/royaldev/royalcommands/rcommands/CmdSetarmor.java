package org.royaldev.royalcommands.rcommands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

@ReflectCommand
public class CmdSetarmor implements CommandExecutor {

    private final RoyalCommands plugin;

    public CmdSetarmor(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("setarmor")) {
            if (!plugin.ah.isAuthorized(cs, "rcmds.setarmor")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }

            if (!(cs instanceof Player)) {
                cs.sendMessage(MessageColor.NEGATIVE + "This command is only available to players!");
                return true;
            }

            Player player;

            if (args.length > 1) {
                if (!plugin.ah.isAuthorized(cs, "rcmds.others.setarmor")) {
                    RUtils.dispNoPerms(cs);
                    return true;
                }
                player = plugin.getServer().getPlayer(args[1]);
                if (player == null || plugin.isVanished(player, cs)) {
                    cs.sendMessage(MessageColor.NEGATIVE + "That player does not exist!");
                    return true;
                }
                if (plugin.ah.isAuthorized(player, "rcmds.exempt.setarmor")) {
                    cs.sendMessage(MessageColor.NEGATIVE + "You can't modify that player's armor!");
                    return true;
                }
            } else player = (Player) cs;

            String set = args[0];

            ItemStack[] diamond;
            diamond = new ItemStack[4];
            diamond[3] = new ItemStack(Material.DIAMOND_HELMET);
            diamond[2] = new ItemStack(Material.DIAMOND_CHESTPLATE);
            diamond[1] = new ItemStack(Material.DIAMOND_LEGGINGS);
            diamond[0] = new ItemStack(Material.DIAMOND_BOOTS);

            ItemStack[] gold;
            gold = new ItemStack[4];
            gold[3] = new ItemStack(Material.GOLD_HELMET);
            gold[2] = new ItemStack(Material.GOLD_CHESTPLATE);
            gold[1] = new ItemStack(Material.GOLD_LEGGINGS);
            gold[0] = new ItemStack(Material.GOLD_BOOTS);

            ItemStack[] iron;
            iron = new ItemStack[4];
            iron[3] = new ItemStack(Material.IRON_HELMET);
            iron[2] = new ItemStack(Material.IRON_CHESTPLATE);
            iron[1] = new ItemStack(Material.IRON_LEGGINGS);
            iron[0] = new ItemStack(Material.IRON_BOOTS);

            ItemStack[] leather;
            leather = new ItemStack[4];
            leather[3] = new ItemStack(Material.LEATHER_HELMET);
            leather[2] = new ItemStack(Material.LEATHER_CHESTPLATE);
            leather[1] = new ItemStack(Material.LEATHER_LEGGINGS);
            leather[0] = new ItemStack(Material.LEATHER_BOOTS);

            ItemStack[] chain;
            chain = new ItemStack[4];
            chain[3] = new ItemStack(Material.CHAINMAIL_HELMET);
            chain[2] = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
            chain[1] = new ItemStack(Material.CHAINMAIL_LEGGINGS);
            chain[0] = new ItemStack(Material.CHAINMAIL_BOOTS);

            ItemStack[] none;
            none = new ItemStack[4];
            none[3] = new ItemStack(0);
            none[2] = new ItemStack(0);
            none[1] = new ItemStack(0);
            none[0] = new ItemStack(0);

            if (set.equalsIgnoreCase("diamond")) {
                if (!plugin.ah.isAuthorized(cs, "rcmds.setarmor.diamond")) {
                    cs.sendMessage(MessageColor.NEGATIVE + "You don't have permission for that type of material!");
                    return true;
                } else {
                    player.getInventory().setArmorContents(diamond);
                    cs.sendMessage(MessageColor.POSITIVE + "Armor was set to " + set + ".");
                    return true;
                }
            } else if (set.equalsIgnoreCase("gold")) {
                if (!plugin.ah.isAuthorized(cs, "rcmds.setarmor.gold")) {
                    cs.sendMessage(MessageColor.NEGATIVE + "You don't have permission for that type of material!");
                    return true;
                } else {
                    player.getInventory().setArmorContents(gold);
                    cs.sendMessage(MessageColor.POSITIVE + "Armor was set to " + set + ".");
                    return true;
                }
            } else if (set.equalsIgnoreCase("iron")) {
                if (!plugin.ah.isAuthorized(cs, "rcmds.setarmor.iron")) {
                    cs.sendMessage(MessageColor.NEGATIVE + "You don't have permission for that type of material!");
                    return true;
                } else {
                    player.getInventory().setArmorContents(iron);
                    cs.sendMessage(MessageColor.POSITIVE + "Armor was set to " + set + ".");
                    return true;
                }
            } else if (set.equalsIgnoreCase("leather")) {
                if (!plugin.ah.isAuthorized(cs, "rcmds.setarmor.leather")) {
                    cs.sendMessage(MessageColor.NEGATIVE + "You don't have permission for that type of material!");
                    return true;
                } else {
                    player.getInventory().setArmorContents(leather);
                    cs.sendMessage(MessageColor.POSITIVE + "Armor was set to " + set + ".");
                    return true;
                }
            } else if (set.equalsIgnoreCase("chain")) {
                if (!plugin.ah.isAuthorized(cs, "rcmds.setarmor.chain")) {
                    cs.sendMessage(MessageColor.NEGATIVE + "You don't have permission for that type of material!");
                    return true;
                } else {
                    player.getInventory().setArmorContents(chain);
                    player.sendMessage(MessageColor.POSITIVE + "Armor was set to " + set + ".");
                    return true;
                }
            } else if (set.equalsIgnoreCase("none")) {
                if (!plugin.ah.isAuthorized(cs, "rcmds.setarmor.none")) {
                    cs.sendMessage(MessageColor.NEGATIVE + "You don't have permission for that type of material!");
                    return true;
                } else {
                    player.getInventory().setArmorContents(none);
                    cs.sendMessage(MessageColor.POSITIVE + "Armor was cleared.");
                    return true;
                }
            } else {
                cs.sendMessage(MessageColor.NEGATIVE + "The armor type must be diamond, gold, iron, leather, chain, or none.");
                return true;
            }
        }
        return false;
    }

}
