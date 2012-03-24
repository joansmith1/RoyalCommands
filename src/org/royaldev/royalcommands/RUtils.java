package org.royaldev.royalcommands;

import net.minecraft.server.EntityPlayer;
import net.minecraft.server.PlayerInventory;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.inventory.CraftInventoryPlayer;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.logging.Logger;

public class RUtils {

    static Logger log = Logger.getLogger("Minecraft");

    // Borrowed list of materials from Essentials
    public static final Set<Integer> AIR_MATERIALS = new HashSet<Integer>();
    public static final HashSet<Byte> AIR_MATERIALS_TARGET = new HashSet<Byte>();

    static {
        AIR_MATERIALS.add(Material.AIR.getId());
        AIR_MATERIALS.add(Material.SAPLING.getId());
        AIR_MATERIALS.add(Material.POWERED_RAIL.getId());
        AIR_MATERIALS.add(Material.DETECTOR_RAIL.getId());
        AIR_MATERIALS.add(Material.LONG_GRASS.getId());
        AIR_MATERIALS.add(Material.DEAD_BUSH.getId());
        AIR_MATERIALS.add(Material.YELLOW_FLOWER.getId());
        AIR_MATERIALS.add(Material.RED_ROSE.getId());
        AIR_MATERIALS.add(Material.BROWN_MUSHROOM.getId());
        AIR_MATERIALS.add(Material.RED_MUSHROOM.getId());
        AIR_MATERIALS.add(Material.TORCH.getId());
        AIR_MATERIALS.add(Material.REDSTONE_WIRE.getId());
        AIR_MATERIALS.add(Material.SEEDS.getId());
        AIR_MATERIALS.add(Material.SIGN_POST.getId());
        AIR_MATERIALS.add(Material.WOODEN_DOOR.getId());
        AIR_MATERIALS.add(Material.LADDER.getId());
        AIR_MATERIALS.add(Material.RAILS.getId());
        AIR_MATERIALS.add(Material.WALL_SIGN.getId());
        AIR_MATERIALS.add(Material.LEVER.getId());
        AIR_MATERIALS.add(Material.STONE_PLATE.getId());
        AIR_MATERIALS.add(Material.IRON_DOOR_BLOCK.getId());
        AIR_MATERIALS.add(Material.WOOD_PLATE.getId());
        AIR_MATERIALS.add(Material.REDSTONE_TORCH_OFF.getId());
        AIR_MATERIALS.add(Material.REDSTONE_TORCH_ON.getId());
        AIR_MATERIALS.add(Material.STONE_BUTTON.getId());
        AIR_MATERIALS.add(Material.SUGAR_CANE_BLOCK.getId());
        AIR_MATERIALS.add(Material.DIODE_BLOCK_OFF.getId());
        AIR_MATERIALS.add(Material.DIODE_BLOCK_ON.getId());
        AIR_MATERIALS.add(Material.TRAP_DOOR.getId());
        AIR_MATERIALS.add(Material.PUMPKIN_STEM.getId());
        AIR_MATERIALS.add(Material.MELON_STEM.getId());
        AIR_MATERIALS.add(Material.VINE.getId());
        AIR_MATERIALS.add(Material.NETHER_WARTS.getId());
        AIR_MATERIALS.add(Material.WATER_LILY.getId());
        AIR_MATERIALS.add(Material.SNOW.getId());

        for (Integer integer : AIR_MATERIALS) {
            AIR_MATERIALS_TARGET.add(integer.byteValue());
        }
        AIR_MATERIALS_TARGET.add((byte) Material.WATER.getId());
        AIR_MATERIALS_TARGET.add((byte) Material.STATIONARY_WATER.getId());
    }

    public static Block getTarget(Player p) {
        Block bb = p.getTargetBlock(AIR_MATERIALS_TARGET, 300);
        if (bb == null) {
            return null;
        }
        return bb;
    }

    public static void showEmptyChest(Player player) {
        try {
            final EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
            final CraftInventoryPlayer inv = new CraftInventoryPlayer(new PlayerInventory(((CraftPlayer) player).getHandle()));
            inv.clear();
            entityPlayer.a(inv.getInventory());
        } catch (Exception e) {
            //ignored
        }
    }

    public static boolean chargePlayer(CommandSender cs, double amount) {
        if (RoyalCommands.economy == null) {
            cs.sendMessage(ChatColor.RED + "No economy! Continuing without charging.");
            return true;
        }
        if (!RoyalCommands.economy.hasAccount(cs.getName())) {
            cs.sendMessage(ChatColor.RED + "You don't have a bank account!");
            return false;
        }
        if (RoyalCommands.economy.getBalance(cs.getName()) < amount) {
            cs.sendMessage(ChatColor.RED + "You don't have enough money!");
            return false;
        }
        RoyalCommands.economy.withdrawPlayer(cs.getName(), amount);
        cs.sendMessage(ChatColor.BLUE + "You have had " + ChatColor.GRAY + amount + ChatColor.BLUE + " removed from your account.");
        return true;
    }

    public static void dispNoPerms(CommandSender cs) {
        cs.sendMessage(ChatColor.RED + "You don't have permission for that!");
        log.warning("[RoyalCommands] " + cs.getName() + " was denied access to that!");
    }

    public static void dispNoPerms(CommandSender cs, String message) {
        cs.sendMessage(message);
        log.warning("[RoyalCommands] " + cs.getName() + " was denied access to that!");
    }

    public static Double getDouble(String number) {
        try {
            return Double.valueOf(number);
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean isTimeStampValid(OfflinePlayer p, String title) {
        if (PConfManager.getPVal(p, title) == null) return false;
        long time = new Date().getTime();
        long overall = PConfManager.getPValLong(p, title);
        return time < overall;
    }

    public static void setTimeStamp(OfflinePlayer p, long seconds, String title) {
        PConfManager.setPValLong(p, (seconds * 1000) + new Date().getTime(), title);
    }

    public static long getTimeStamp(OfflinePlayer p, String title) {
        if (PConfManager.getPVal(p, title) == null) return -1;
        return PConfManager.getPValLong(p, title);
    }

    //if it isn't obvious, Essentials wrote this code. no way in hell I could manage this
    public static String formatDateDiff(long date) {
        Calendar c = new GregorianCalendar();
        c.setTimeInMillis(date);
        Calendar now = new GregorianCalendar();
        return formatDateDiff(now, c);
    }

    private static int dateDiff(int type, Calendar fromDate, Calendar toDate, boolean future) {
        int diff = 0;
        long savedDate = fromDate.getTimeInMillis();
        while ((future && !fromDate.after(toDate)) || (!future && !fromDate.before(toDate))) {
            savedDate = fromDate.getTimeInMillis();
            fromDate.add(type, future ? 1 : -1);
            diff++;
        }
        diff--;
        fromDate.setTimeInMillis(savedDate);
        return diff;
    }

    public static String formatDateDiff(Calendar fromDate, Calendar toDate) {
        boolean future = false;
        if (toDate.equals(fromDate)) return " now";
        if (toDate.after(fromDate)) future = true;

        StringBuilder sb = new StringBuilder();
        int[] types = new int[]
                {
                        Calendar.YEAR,
                        Calendar.MONTH,
                        Calendar.DAY_OF_MONTH,
                        Calendar.HOUR_OF_DAY,
                        Calendar.MINUTE,
                        Calendar.SECOND
                };
        String[] names = new String[]
                {
                        "year",
                        "years",
                        "month",
                        "months",
                        "day",
                        "days",
                        "hour",
                        "hours",
                        "minute",
                        "minutes",
                        "second",
                        "seconds"
                };
        for (int i = 0; i < types.length; i++) {
            int diff = dateDiff(types[i], fromDate, toDate, future);
            if (diff > 0) sb.append(" ").append(diff).append(" ").append(names[i * 2 + (diff > 1 ? 1 : 0)]);
        }
        if (sb.length() == 0) return " now";
        return sb.toString();
    }

    public static boolean isTeleportAllowed(OfflinePlayer p) {
        return PConfManager.getPVal(p, "allow-tp") == null || PConfManager.getPValBoolean(p, "allow-tp");
    }

    public static String colorize(String text) {
        return text.replaceAll("(&([a-f0-9kK]))", "\u00A7$2");
    }
}
