package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.PConfManager;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

import java.util.Date;

public class CmdMute implements CommandExecutor {

    RoyalCommands plugin;

    public CmdMute(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("mute")) {
            if (!plugin.isAuthorized(cs, "rcmds.mute")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            if (args.length == 1) {
                Player t = plugin.getServer().getPlayer(args[0].trim());
                if (t == null) {
                    OfflinePlayer t2 = plugin.getServer().getOfflinePlayer(args[0].trim());
                    PConfManager pcm = new PConfManager(t2);
                    if (!pcm.exists()) {
                        cs.sendMessage(ChatColor.RED + "That player does not exist!");
                        return true;
                    }
                    if (t2.isOp()) {
                        cs.sendMessage(ChatColor.RED + "You cannot mute that player!");
                        return true;
                    }
                    if (pcm.getBoolean("muted")) {
                        pcm.setBoolean(false, "muted");
                        cs.sendMessage(ChatColor.BLUE + "You have unmuted " + ChatColor.GRAY + t2.getName() + ChatColor.BLUE + ".");
                        return true;
                    } else {
                        pcm.setBoolean(true, "muted");
                        cs.sendMessage(ChatColor.BLUE + "You have muted " + ChatColor.GRAY + t2.getName() + ChatColor.BLUE + ".");
                        return true;
                    }

                }
                PConfManager pcm = new PConfManager(t);
                if (!RUtils.canActAgainst(cs, t, "mute")) {
                    cs.sendMessage(ChatColor.RED + "You cannot mute that player!");
                    return true;
                }
                if (pcm.getBoolean("muted")) {
                    pcm.setBoolean(false, "muted");
                    t.sendMessage(ChatColor.BLUE + "You have been unmuted by " + ChatColor.GRAY + cs.getName() + ChatColor.BLUE + ".");
                    cs.sendMessage(ChatColor.BLUE + "You have unmuted " + ChatColor.GRAY + t.getName() + ChatColor.BLUE + ".");
                    return true;
                } else {
                    pcm.setBoolean(true, "muted");
                    t.sendMessage(ChatColor.RED + "You have been muted by " + ChatColor.GRAY + cs.getName() + ChatColor.RED + ".");
                    cs.sendMessage(ChatColor.BLUE + "You have muted " + ChatColor.GRAY + t.getName() + ChatColor.BLUE + ".");
                    return true;
                }
            }
            if (args.length > 1) {
                Player t = plugin.getServer().getPlayer(args[0].trim());
                Long time;
                try {
                    time = Long.valueOf(args[1]);
                } catch (Exception e) {
                    cs.sendMessage(ChatColor.RED + "That time was invalid!");
                    return true;
                }
                PConfManager pcm = new PConfManager(t);
                if (t == null) {
                    OfflinePlayer t2 = plugin.getServer().getOfflinePlayer(args[0].trim());
                    if (!pcm.exists()) {
                        cs.sendMessage(ChatColor.RED + "That player does not exist!");
                        return true;
                    }
                    if (t2.isOp()) {
                        cs.sendMessage(ChatColor.RED + "You cannot mute that player!");
                        return true;
                    }
                    if (pcm.getBoolean("muted")) {
                        pcm.setBoolean(false, "muted");
                        cs.sendMessage(ChatColor.BLUE + "You have unmuted " + ChatColor.GRAY + t2.getName() + ChatColor.BLUE + ".");
                        return true;
                    } else {
                        pcm.setBoolean(true, "muted");
                        RUtils.setTimeStamp(t2, time, "mutetime");
                        cs.sendMessage(ChatColor.BLUE + "You have muted " + ChatColor.GRAY + t2.getName() + ChatColor.BLUE + ".");
                        return true;
                    }
                } else {
                    if (!pcm.exists()) {
                        cs.sendMessage(ChatColor.RED + "That player does not exist!");
                        return true;
                    }
                    if (pcm.getBoolean("muted")) {
                        pcm.setBoolean(false, "muted");
                        t.sendMessage(ChatColor.BLUE + "You have been unmuted by " + ChatColor.GRAY + cs.getName() + ChatColor.BLUE + ".");
                        cs.sendMessage(ChatColor.BLUE + "You have unmuted " + ChatColor.GRAY + t.getName() + ChatColor.BLUE + ".");
                        return true;
                    } else {
                        pcm.setBoolean(true, "muted");
                        RUtils.setTimeStamp(t, time, "mutetime");
                        t.sendMessage(ChatColor.RED + "You have been muted by " + ChatColor.GRAY + cs.getName() + ChatColor.RED + " for" + ChatColor.GRAY + RUtils.formatDateDiff(new Date().getTime() + (time * 1000)) + ChatColor.RED + ".");
                        cs.sendMessage(ChatColor.BLUE + "You have muted " + ChatColor.GRAY + t.getName() + ChatColor.BLUE + " for" + ChatColor.GRAY + RUtils.formatDateDiff(new Date().getTime() + (time * 1000)) + ChatColor.BLUE + ".");
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
