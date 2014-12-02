package org.royaldev.royalcommands.rcommands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.rcommands.home.Home;
import org.royaldev.royalcommands.wrappers.RPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

// TODO: Allow setting homes for other people

@ReflectCommand
public class CmdSetHome extends BaseCommand {

    private final Map<UUID, Map<String, Long>> overwrites = new HashMap<>();

    public CmdSetHome(final RoyalCommands instance, final String name) {
        super(instance, name, true);
    }

    @Override
    public boolean runCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (args.length > 0 && !this.ah.isAuthorized(cs, "rcmds.sethome.multi")) {
            RUtils.dispNoPerms(cs, MessageColor.NEGATIVE + "You don't have permission for multiple homes!");
            return true;
        }
        if (!(cs instanceof Player)) {
            cs.sendMessage(MessageColor.NEGATIVE + "This command is only available to players!");
            return true;
        }
        final Player p = (Player) cs;
        final RPlayer rp = RPlayer.getRPlayer(p);
        final Location l = p.getLocation();
        String name = "";
        if (args.length > 0) name = args[0];
        if (name.contains(":")) {
            cs.sendMessage(MessageColor.NEGATIVE + "The name of your home cannot contain \":\"!");
            return true;
        }
        final int limit = rp.getHomeLimit();
        final int curHomes = rp.getHomes().size();
        if (limit == 0) {
            RUtils.dispNoPerms(cs, MessageColor.NEGATIVE + "Your home limit is set to " + MessageColor.NEUTRAL + "0" + MessageColor.NEGATIVE + "!");
            cs.sendMessage(MessageColor.NEGATIVE + "You can't set any homes!");
            return true;
        } else if (curHomes >= limit && limit > -1) {
            RUtils.dispNoPerms(cs, MessageColor.NEGATIVE + "You've reached your max number of homes! (" + MessageColor.NEUTRAL + limit + MessageColor.NEGATIVE + ")");
            return true;
        }
        if (name.isEmpty()) name = "home";
        Home h = Home.fromPConfManager(rp.getPConfManager(), name);
        if (h != null) {
            boolean overwrite = false;
            Map<String, Long> hto = this.overwrites.get(p.getUniqueId());
            if (this.overwrites.containsKey(p.getUniqueId())) {
                if (hto.containsKey(h.getName()) && hto.get(h.getName()) != null) {
                    final long expiresAt = hto.get(h.getName()) + 10000;
                    overwrite = System.currentTimeMillis() < expiresAt;
                }
            }
            if (!overwrite) {
                cs.sendMessage(MessageColor.NEGATIVE + "Warning! " + MessageColor.POSITIVE + "The home " + MessageColor.NEUTRAL + h.getName() + MessageColor.POSITIVE + " is already set. To re-set it, do this command again in the next ten seconds.");
                if (hto == null) hto = new HashMap<>();
                hto.put(h.getName(), System.currentTimeMillis());
                this.overwrites.put(p.getUniqueId(), hto);
                return true;
            } else {
                hto.remove(h.getName());
                this.overwrites.put(p.getUniqueId(), hto);
            }
        } else {
            h = new Home(rp.getUUID(), name, l);
        }
        h.setLocation(l);
        h.save();
        if (args.length > 0) {
            p.sendMessage(MessageColor.POSITIVE + "Home " + MessageColor.NEUTRAL + h.getName() + MessageColor.POSITIVE + " set.");
        } else p.sendMessage(MessageColor.POSITIVE + "Home set.");
        return true;
    }
}
