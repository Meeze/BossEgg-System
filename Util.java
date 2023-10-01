package de.potera.teamhardcore.utils;

import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;


    public static boolean isInt(String integer) {
        try {
            Integer.parseInt(integer);
        } catch (NumberFormatException exception) {
            return false;
        }
        return Integer.parseInt(integer) >= 1;
    }

    public static boolean isDouble(String s) {
        try {
            Double.parseDouble(s);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return Double.parseDouble(s) >= 0.1;
    }

    public static String getCustomName(ItemStack itemStack) {
        if (!itemStack.hasItemMeta()) return CraftItemStack.asNMSCopy(itemStack).getName();
        if (!itemStack.getItemMeta().hasDisplayName()) return CraftItemStack.asNMSCopy(itemStack).getName();
        return itemStack.getItemMeta().getDisplayName();
    }

    public static String stringBuilder(int offset, String[] args) {
        StringBuilder msg = new StringBuilder();
        for (int i = offset; i < args.length; i++) {
            msg.append(args[i]).append(" ");
        }
        return msg.toString();
    }