package ru.rusekh.xpmultiplier;

import eu.okaeri.configs.OkaeriConfig;
import ru.rusekh.xpmultiplier.helper.ChatHelper;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PluginConfiguration extends OkaeriConfig
{
    public static List<String> boostInfoItemLore = ChatHelper.color(Arrays.asList("&dTime of actual boost: {BOOST_TIME}", "IDK"));
    public static List<String> howManyBoostActivatedItemLore = ChatHelper.color(Arrays.asList("&dHow many boosts you activated: {BOOST_COUNT}", "IDK"));
    public static double boostMultiplier = 4.0;
    public static long boostTime = TimeUnit.HOURS.toMillis(24L);
}
