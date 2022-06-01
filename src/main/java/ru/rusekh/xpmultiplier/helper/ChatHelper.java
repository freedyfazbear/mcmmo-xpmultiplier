package ru.rusekh.xpmultiplier.helper;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Stack;
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

    public static String parseTime(long time) {
        time -= System.currentTimeMillis();
        if (time <= 0L) {
            return "0L";
        }
        final StringBuilder stringBuilder = new StringBuilder();
        final long days = time / 86400000L;
        final long hours = time / 3600000L % 24L;
        final long minutes = time / 60000L % 60L;
        final long seconds = time / 1000L % 60L;
        final long milis = time % 1000L;
        if (days > 0L) {
            stringBuilder.append(days).append("d");
        }
        if (hours > 0L) {
            stringBuilder.append(hours).append("hrs");
        }
        if (minutes > 0L) {
            stringBuilder.append(minutes).append("mins");
        }
        if (seconds > 0L) {
            stringBuilder.append(seconds).append("s");
        }
        if (days == 0L && hours == 0L && minutes == 0L && seconds == 0L && milis > 0L) {
            stringBuilder.append(milis).append("ms");
        }
        return stringBuilder.toString();
    }

    public static long parseTime(String string) {
        if (string == null || string.isEmpty()) {
            return 0;
        }

        Stack<Character> type = new Stack<>();
        StringBuilder value = new StringBuilder();

        boolean calc = false;
        long time = 0;

        for (char c : string.toCharArray()) {
            switch (c) {
                case 'd':
                case 'h':
                case 'm':
                case 's':
                    if (!calc) {
                        type.push(c);
                    }

                    try {
                        long i = Integer.parseInt(value.toString());
                        switch (type.pop()) {
                            case 'd':
                                time += i * 86400000L;
                                break;
                            case 'h':
                                time += i * 3600000L;
                                break;
                            case 'm':
                                time += i * 60000L;
                                break;
                            case 's':
                                time += i * 1000L;
                                break;
                        }
                    }
                    catch (NumberFormatException e) {
                        System.out.println("Unknown number: " + value);
                        return time;
                    }

                    type.push(c);
                    calc = true;
                    break;
                default:
                    value.append(c);
                    break;
            }
        }

        return time;
    }

    public static List<String> color(List<String> list) {
        return list.stream().map(s -> {
            s = color(s);
            return s;
        }).collect(Collectors.toList());
    }
}
