package org.royaldev.royalcommands.rcommands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.PConfManager;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdBan implements CommandExecutor {

    RoyalCommands plugin;

    public CmdBan(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    /**
     * Bans a player. Message is not sent to banned player or person who banned.
     * Message is broadcasted to those with rcmds.see.ban
     * Kicks banned player if they're online
     *
     * @param t      Player to ban
     * @param cs     CommandSender who issued the ban
     * @param reason Reason for the ban
     */
    public static void banPlayer(OfflinePlayer t, CommandSender cs, String reason) {
        reason = RUtils.colorize(reason);
        t.setBanned(true);
        Bukkit.getServer().broadcast(ChatColor.RED + "The player " + ChatColor.GRAY + t.getName() + ChatColor.RED + " has been banned for " + ChatColor.GRAY + reason + ChatColor.RED + " by " + ChatColor.GRAY + cs.getName() + ChatColor.RED + ".", "rcmds.see.ban");
        if (t.isOnline()) ((Player) t).kickPlayer(reason);
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("ban")) {
            if (!plugin.isAuthorized(cs, "rcmds.ban")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            OfflinePlayer t = plugin.getServer().getPlayer(args[0]);
            if (t == null) t = plugin.getServer().getOfflinePlayer(args[0]);
            PConfManager pcm = new PConfManager(t);
            if (!pcm.getConfExists()) {
                pcm.createFile();
                if (!pcm.getConfExists()) {
                    cs.sendMessage(ChatColor.RED + "Couldn't make userdata file for that player!");
                    return true;
                }
            }
            if (!RUtils.canActAgainst(cs, t.getName(), "ban")) {
                RUtils.dispNoPerms(cs, ChatColor.RED + "You can't ban that player!");
                return true;
            }
            String banreason = (args.length > 1) ? RoyalCommands.getFinalArg(args, 1) : plugin.banMessage;
            banreason = RUtils.colorize(banreason);
            pcm.setString(banreason, "banreason");
            pcm.setString(cs.getName(), "banner");
            cs.sendMessage(ChatColor.BLUE + "You have banned " + ChatColor.GRAY + t.getName() + ChatColor.BLUE + ".");
            banPlayer(t, cs, banreason);
            return true;
        }
        return false;
    }
}
