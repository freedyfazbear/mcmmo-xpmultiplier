package ru.rusekh.xpmultiplier.helper;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public final class ChatHelper
{
    private ChatHelper() {}

    public static String fixColor(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static void sendMessage(Player player, String message) {
        player.sendMessage(fixColor(message));
    }

    public static String color(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static boolean isAlphaNumeric(String text) {
        return text.matches("^[a-zA-Z0-9]*$");
    }

    public static List<String> color(List<String> list) {
        return list.stream().map(s -> {
            s = color(s);
            return s;
        }).collect(Collectors.toList());
    }
}
