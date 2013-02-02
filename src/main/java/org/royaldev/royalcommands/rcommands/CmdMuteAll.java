package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.PConfManager;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdMuteAll implements CommandExecutor {

    private RoyalCommands plugin;

    public CmdMuteAll(RoyalCommands instance) {
        this.plugin = instance;
    }

    private boolean allMuted = false;

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("muteall")) {
            if (!plugin.isAuthorized(cs, "rcmds.muteall")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            Player[] ps = plugin.getServer().getOnlinePlayers();
            for (Player p : ps) {
                if (plugin.isVanished(p, cs) || plugin.isAuthorized(p, "rcmds.exempt.mute"))
                    continue;
                if (cs instanceof Player) {
                    if (p == cs) continue;
                }
                PConfManager pcm = plugin.getUserdata(p);
                if (!allMuted) {
                    pcm.set("muted", true);
                    p.sendMessage(ChatColor.RED + "You have been muted!");
                } else {
                    pcm.set("muted", false);
                    p.sendMessage(ChatColor.BLUE + "You have been unmuted!");
                }
            }
            if (!allMuted) {
                cs.sendMessage(ChatColor.BLUE + "You have muted all players.");
                allMuted = true;
            } else {
                cs.sendMessage(ChatColor.BLUE + "You have unmuted all players.");
                allMuted = false;
            }
            return true;
        }
        return false;
    }
}
