package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class List implements CommandExecutor {

    RoyalCommands plugin;

    public List(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("list")) {
            if (!plugin.isAuthorized(cs, "rcmds.list")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            Player p[] = plugin.getServer().getOnlinePlayers();
            String ps = "";
            int hid = 0;
            for (Player aP : p) {
                String name = aP.getDisplayName() + ChatColor.WHITE;
                if (!plugin.isVanished(aP)) {
                    if (plugin.whoPrefix) name = RoyalCommands.chat.getPlayerPrefix(aP).replaceAll("(&([a-f0-9kK]))", "\u00A7$2") + name;
                    if (Afk.afkdb.containsKey(aP)) {
                        name = ChatColor.GRAY + "[AFK]" + ChatColor.WHITE + name;
                    }
                    if (ps.equals("")) {
                        ps = ps.concat(name);
                    } else {
                        ps = ps.concat(", " + name);
                    }
                } else {
                    hid++;
                }
            }
            if (plugin.isAuthorized(cs, "rcmds.seehidden")) {
                if (hid > 0) {
                    cs.sendMessage(ChatColor.BLUE + "There are currently " + ChatColor.GRAY + (p.length - hid) + "/" + hid + ChatColor.BLUE + " out of " + ChatColor.GRAY + plugin.getServer().getMaxPlayers() + ChatColor.BLUE + " players online.");
                } else {
                    cs.sendMessage(ChatColor.BLUE + "There are currently " + ChatColor.GRAY + p.length + ChatColor.BLUE + " out of " + ChatColor.GRAY + plugin.getServer().getMaxPlayers() + ChatColor.BLUE + " players online.");
                }
                for (Player aP : p) {
                    String name = aP.getDisplayName() + ChatColor.WHITE;
                    if (plugin.isVanished(aP)) {
                        if (plugin.whoPrefix) name = RoyalCommands.chat.getPlayerPrefix(aP).replaceAll("(&([a-f0-9kK]))", "\u00A7$2") + name;
                        name = ChatColor.GRAY + "[HIDDEN]" + ChatColor.WHITE + name;
                        if (ps.equals("")) {
                            ps = ps.concat(name);
                        } else {
                            ps = ps.concat(", " + name);
                        }
                    }
                }
            } else {
                cs.sendMessage(ChatColor.BLUE + "There are currently " + ChatColor.GRAY + (p.length - hid) + ChatColor.BLUE + " out of " + ChatColor.GRAY + plugin.getServer().getMaxPlayers() + ChatColor.BLUE + " players online.");
            }
            cs.sendMessage("Online Players: " + ps);
            return true;
        }
        return false;
    }
}
