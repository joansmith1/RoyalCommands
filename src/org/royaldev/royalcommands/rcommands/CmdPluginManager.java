package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.UnknownDependencyException;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

import java.io.File;
import java.util.List;

public class CmdPluginManager implements CommandExecutor {

    RoyalCommands plugin;

    public CmdPluginManager(RoyalCommands instance) {
        plugin = instance;
    }

    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("pluginmanager")) {
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            String subcmd = args[0];
            PluginManager pm = plugin.getServer().getPluginManager();
            if (subcmd.equalsIgnoreCase("load")) {
                if (!plugin.isAuthorized(cs, "rcmds.pluginmanager.load")) {
                    RUtils.dispNoPerms(cs);
                    return true;
                }
                if (args.length < 2) {
                    cs.sendMessage(ChatColor.RED + "Please provide the name of the jar to load!");
                    return true;
                }
                File f = new File(plugin.getDataFolder().getParentFile() + File.separator + args[1]);
                if (!f.exists()) {
                    cs.sendMessage(ChatColor.RED + "That file does not exist!");
                    return true;
                }
                if (!f.canRead()) {
                    cs.sendMessage(ChatColor.RED + "Can't read that file!");
                    return true;
                }
                Plugin p;
                try {
                    p = pm.loadPlugin(f);
                    if (p == null) {
                        cs.sendMessage(ChatColor.RED + "Could not load plugin: plugin was invalid.");
                        cs.sendMessage(ChatColor.RED + "Make sure it ends with .jar!");
                        return true;
                    }
                    pm.enablePlugin(p);
                } catch (InvalidPluginException e) {
                    cs.sendMessage(ChatColor.RED + "That file is not a plugin!");
                    return true;
                } catch (UnknownDependencyException e) {
                    cs.sendMessage(ChatColor.RED + "Missing dependency: " + e.getMessage());
                    return true;
                } catch (InvalidDescriptionException e) {
                    cs.sendMessage(ChatColor.RED + "That plugin contained an invalid description!");
                    return true;
                }
                cs.sendMessage(ChatColor.BLUE + "Loaded and enabled " + ChatColor.GRAY + p.getName() + ChatColor.BLUE + " successfully.");
                return true;
            } else if (subcmd.equalsIgnoreCase("disable")) {
                if (!plugin.isAuthorized(cs, "rcmds.pluginmanager.disable")) {
                    RUtils.dispNoPerms(cs);
                    return true;
                }
                if (args.length < 2) {
                    cs.sendMessage(ChatColor.RED + "Please provide the name of the plugin to disable!");
                    return true;
                }
                Plugin p = pm.getPlugin(args[1]);
                if (p == null) {
                    cs.sendMessage(ChatColor.RED + "No such plugin!");
                    return true;
                }
                if (!p.isEnabled()) {
                    cs.sendMessage(ChatColor.GRAY + p.getName() + ChatColor.RED + "is already disabled!");
                }
                pm.disablePlugin(p);
                if (!p.isEnabled())
                    cs.sendMessage(ChatColor.BLUE + "Disabled " + ChatColor.GRAY + p.getName() + ChatColor.BLUE + " successfully!");
                else cs.sendMessage(ChatColor.RED + "Could not disabled that plugin!");
                return true;
            } else if (subcmd.equalsIgnoreCase("enable")) {
                if (!plugin.isAuthorized(cs, "rcmds.pluginmanager.enable")) {
                    RUtils.dispNoPerms(cs);
                    return true;
                }
                if (args.length < 2) {
                    cs.sendMessage(ChatColor.RED + "Please provide the name of the plugin to enable!");
                    return true;
                }
                Plugin p = pm.getPlugin(args[1]);
                if (p == null) {
                    cs.sendMessage(ChatColor.RED + "No such plugin!");
                    return true;
                }
                if (p.isEnabled()) {
                    cs.sendMessage(ChatColor.RED + "Plugin is already enabled!");
                    return true;
                }
                pm.enablePlugin(p);
                if (p.isEnabled())
                    cs.sendMessage(ChatColor.BLUE + "Successfully enabled " + ChatColor.GRAY + p.getName() + ChatColor.BLUE + "!");
                else cs.sendMessage(ChatColor.RED + "Could not enable that plugin.");
                return true;
            } else if (subcmd.equalsIgnoreCase("reload")) {
                if (!plugin.isAuthorized(cs, "rcmds.pluginmanager.reload")) {
                    RUtils.dispNoPerms(cs);
                    return true;
                }
                if (args.length < 2) {
                    cs.sendMessage(ChatColor.RED + "Please provide the name of the plugin to reload!");
                    return true;
                }
                Plugin p = pm.getPlugin(args[1]);
                if (p == null) {
                    cs.sendMessage(ChatColor.RED + "No such plugin!");
                    return true;
                }
                pm.disablePlugin(p);
                pm.enablePlugin(p);
                cs.sendMessage(ChatColor.BLUE + "Reloaded " + ChatColor.GRAY + p.getName() + ChatColor.BLUE + ".");
            } else if (subcmd.equalsIgnoreCase("update")) {
                if (!plugin.isAuthorized(cs, "rcmds.pluginmanager.update")) {
                    RUtils.dispNoPerms(cs);
                    return true;
                }
                if (args.length < 3) {
                    cs.sendMessage(ChatColor.RED + "Please provide the name of the plugin to update and its filename!");
                    return true;
                }
                Plugin p = pm.getPlugin(args[1]);
                if (p == null) {
                    cs.sendMessage(ChatColor.RED + "No such plugin!");
                    return true;
                }
                File f = new File(plugin.getDataFolder().getParentFile() + File.separator + args[2]);
                if (!f.exists()) {
                    cs.sendMessage(ChatColor.RED + "That file does not exist!");
                    return true;
                }
                if (!f.canRead()) {
                    cs.sendMessage(ChatColor.RED + "Can't read that file!");
                    return true;
                }
                pm.disablePlugin(p);
                try {
                    p = pm.loadPlugin(f);
                    if (p == null) {
                        cs.sendMessage(ChatColor.RED + "Could not load plugin: plugin was invalid.");
                        cs.sendMessage(ChatColor.RED + "Make sure it ends with .jar!");
                        return true;
                    }
                    pm.enablePlugin(p);
                } catch (InvalidPluginException e) {
                    cs.sendMessage(ChatColor.RED + "That file is not a plugin!");
                    return true;
                } catch (UnknownDependencyException e) {
                    cs.sendMessage(ChatColor.RED + "Missing dependency: " + e.getMessage());
                    return true;
                } catch (InvalidDescriptionException e) {
                    cs.sendMessage(ChatColor.RED + "That plugin contained an invalid description!");
                    return true;
                }
                cs.sendMessage(ChatColor.BLUE + "Updated " + ChatColor.GRAY + p.getName() + ChatColor.BLUE + " successfully.");
                return true;
            } else if (subcmd.equalsIgnoreCase("reloadall")) {
                if (!plugin.isAuthorized(cs, "rcmds.pluginmanager.reloadall")) {
                    RUtils.dispNoPerms(cs);
                    return true;
                }
                for (Plugin p : pm.getPlugins()) {
                    pm.disablePlugin(p);
                    pm.enablePlugin(p);
                }
                cs.sendMessage(ChatColor.BLUE + "Reloaded all plugins!");
                return true;
            } else if (subcmd.equalsIgnoreCase("list")) {
                if (!plugin.isAuthorized(cs, "rcmds.pluginmanager.list")) {
                    RUtils.dispNoPerms(cs);
                    return true;
                }
                Plugin[] ps = pm.getPlugins();
                StringBuilder list = new StringBuilder();
                int enabled = 0;
                int disabled = 0;
                for (Plugin p : ps) {
                    String name = p.getName();
                    if (!p.isEnabled()) {
                        name = name + " (disabled)";
                        disabled = disabled + 1;
                    } else enabled = enabled + 1;
                    list.append(ChatColor.GRAY);
                    list.append(name);
                    list.append(ChatColor.RESET);
                    list.append(", ");
                }
                cs.sendMessage(ChatColor.BLUE + "Plugins (" + ChatColor.GRAY + enabled + ((disabled > 0) ? ChatColor.BLUE + "/" + ChatColor.GRAY + disabled + " disabled" : "") + ChatColor.BLUE + "): " + list.substring(0, list.length() - 4));
                return true;
            } else if (subcmd.equalsIgnoreCase("info")) {
                if (!plugin.isAuthorized(cs, "rcmds.pluginmanager.info")) {
                    RUtils.dispNoPerms(cs);
                    return true;
                }
                if (args.length < 2) {
                    cs.sendMessage(ChatColor.RED + "Please provide the name of the plugin to update and its filename!");
                    return true;
                }
                Plugin p = pm.getPlugin(args[1]);
                if (p == null) {
                    cs.sendMessage(ChatColor.RED + "No such plugin!");
                    return true;
                }
                PluginDescriptionFile pdf = p.getDescription();
                if (pdf == null) {
                    cs.sendMessage(ChatColor.RED + "Can't get information from " + ChatColor.GRAY + p.getName() + ChatColor.RED + ".");
                    return true;
                }
                String version = pdf.getVersion();
                List<String> authors = pdf.getAuthors();
                String site = pdf.getWebsite();
                List<String> softDep = pdf.getSoftDepend();
                List<String> dep = pdf.getDepend();
                String name = pdf.getName();
                String desc = pdf.getDescription();
                if (name != null && !name.isEmpty())
                    cs.sendMessage(ChatColor.BLUE + "Name: " + ChatColor.GRAY + name);
                if (version != null && !version.isEmpty())
                    cs.sendMessage(ChatColor.BLUE + "Version: " + ChatColor.GRAY + version);
                if (site != null && !site.isEmpty())
                    cs.sendMessage(ChatColor.BLUE + "Site: " + ChatColor.GRAY + site);
                if (desc != null && !desc.isEmpty())
                    cs.sendMessage(ChatColor.BLUE + "Description: " + ChatColor.GRAY + desc.replaceAll("\r?\n", ""));
                if (authors != null && !authors.isEmpty())
                    cs.sendMessage(ChatColor.BLUE + "Author" + ((authors.size() > 1) ? "s" : "") + ": " + ChatColor.GRAY + RUtils.join(authors, ChatColor.RESET + ", " + ChatColor.GRAY));
                if (softDep != null && !softDep.isEmpty())
                    cs.sendMessage(ChatColor.BLUE + "Soft Dependencies: " + ChatColor.GRAY + RUtils.join(softDep, ChatColor.RESET + ", " + ChatColor.GRAY));
                if (dep != null && !dep.isEmpty())
                    cs.sendMessage(ChatColor.BLUE + "Dependencies: " + ChatColor.GRAY + RUtils.join(dep, ChatColor.RESET + ", " + ChatColor.GRAY));
                return true;
            } else if (subcmd.equalsIgnoreCase("help")) {
                if (!plugin.isAuthorized(cs, "rcmds.pluginmanager.help")) {
                    RUtils.dispNoPerms(cs);
                    return true;
                }
                cs.sendMessage(ChatColor.BLUE + "RoyalCommands PluginManager Help");
                cs.sendMessage(ChatColor.BLUE + "================================");
                cs.sendMessage("* " + ChatColor.GRAY + "/" + label + " load [jar]" + ChatColor.BLUE + " - Loads and enables a new plugin");
                cs.sendMessage("* " + ChatColor.GRAY + "/" + label + " disable [plugin]" + ChatColor.BLUE + " - Disables an already loaded plugin");
                cs.sendMessage("* " + ChatColor.GRAY + "/" + label + " enable [plugin]" + ChatColor.BLUE + " - Enables a disabled plugin");
                cs.sendMessage("* " + ChatColor.GRAY + "/" + label + " reload [plugin]" + ChatColor.BLUE + " - Disables then enables a plugin");
                cs.sendMessage("* " + ChatColor.GRAY + "/" + label + " reloadall" + ChatColor.BLUE + " - Reloads every plugin");
                cs.sendMessage("* " + ChatColor.GRAY + "/" + label + " update [plugin] [jar]" + ChatColor.BLUE + " - Disables the plugin and loads the new jar");
                cs.sendMessage("* " + ChatColor.GRAY + "/" + label + " list" + ChatColor.BLUE + " - Lists all the plugins");
                cs.sendMessage("* " + ChatColor.GRAY + "/" + label + " info [plugin]" + ChatColor.BLUE + " - Displays information about a plugin");
                return true;
            } else {
                cs.sendMessage(ChatColor.RED + "Unknown subcommand!");
                cs.sendMessage(ChatColor.RED + "Try " + ChatColor.GRAY + "/" + label + " help");
                return true;
            }
        }
        return false;
    }
}
