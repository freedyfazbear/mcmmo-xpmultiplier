package ru.rusekh.xpmultiplier.inventory;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import ru.rusekh.xpmultiplier.PluginConfiguration;
import ru.rusekh.xpmultiplier.XPMultiplier;
import ru.rusekh.xpmultiplier.helper.ChatHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class BoostInfoInventory
{

    public void openGui(Player player) {
        Gui gui = Gui.gui()
                .title(Component.text("Boost Info"))
                .disableAllInteractions()
                .rows(3)
                .create();

        for (int i = 0; i < 26; i++) {
            gui.setItem(i, ItemBuilder.from(Material.BLACK_STAINED_GLASS_PANE).asGuiItem());
        }

        AtomicLong boostTime = new AtomicLong();
        List<String> list = new ArrayList<>();
        
        var userModel = XPMultiplier.getInstance().getRepository().fetchOrCreateModel(player.getUniqueId());
        userModel.whenComplete((model, throwable) -> {
            if (throwable != null) {
                throwable.printStackTrace();
            }
            boostTime.set(model.getBoostTime());
            list.add("Count boost: " + model.getHistoryOfClaims().getEntrys().size());
            for (Date date : model.getHistoryOfClaims().getEntrys()) {
                String dateString = dateToString(date);
                list.add(dateString);
            }
        });

        GuiItem actualBoostInfo = ItemBuilder.from(Material.PAPER)
                .name(Component.text(ChatHelper.color("&7&lActual Boost Info")))
                .setLore(PluginConfiguration.boostInfoItemLore.stream().map(s -> s.replace("{BOOST_TIME}", "" + ChatHelper.parseTime(boostTime.get() - System.currentTimeMillis()))).toArray(String[]::new)).asGuiItem();
        gui.setItem(12, actualBoostInfo);

        GuiItem howManyBoost = ItemBuilder.from(Material.PAPER)
                .name(Component.text(ChatHelper.color("&7&lHistory of claimed boosts")))
                .setLore(list.stream().map(s -> s.replace("{TEST}", "")).toArray(String[]::new))
                .asGuiItem();
        gui.setItem(13, howManyBoost);
        gui.open(player);
    }

    public String dateToString(Date date) {
        String convertedDate = "";
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            convertedDate = dateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertedDate;
    }
}