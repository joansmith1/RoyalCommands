package tk.royalcraf.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import tk.royalcraf.royalcommands.RoyalCommands;

public class SetSpawn implements CommandExecutor {

	RoyalCommands plugin;

	public SetSpawn(RoyalCommands plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label,
			String[] args) {
		if (cmd.getName().equalsIgnoreCase("setspawn")) {
			if (!plugin.isAuthorized(cs, "rcmds.setspawn")) {
				cs.sendMessage(ChatColor.RED
						+ "You don't have permission for that!");
				plugin.log.warning("[RoyalCommands] " + cs.getName()
						+ " was denied access to the command!");
				return true;
			}
			if (!(cs instanceof Player)) {
				cs.sendMessage(ChatColor.RED
						+ "This command is only available to players!");
				return true;
			}
			Player p = (Player) cs;
			int x = (int) p.getLocation().getX();
			int y = (int) p.getLocation().getY();
			int z = (int) p.getLocation().getZ();
			p.getWorld().setSpawnLocation(x, y, z);
			cs.sendMessage(ChatColor.BLUE + "The spawn point of "
					+ ChatColor.GRAY + p.getWorld().getName() + ChatColor.BLUE
					+ " is set.");
			return true;
		}
		return false;
	}

}
