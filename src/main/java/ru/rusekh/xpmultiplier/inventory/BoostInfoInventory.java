package ru.rusekh.xpmultiplier.inventory;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import ru.rusekh.xpmultiplier.PluginConfiguration;
import ru.rusekh.xpmultiplier.XPMultiplier;
import ru.rusekh.xpmultiplier.helper.ChatHelper;

import java.util.Collections;
import java.util.stream.Collectors;

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
        XPMultiplier.getInstance().getRepository().fetchOrCreateModel(player.getUniqueId()).whenComplete((model, throwable) -> {
            if (throwable != null) {
                throwable.printStackTrace();
            }
            GuiItem boostTime = ItemBuilder.from(Material.PAPER)
                    .setLore(PluginConfiguration.boostInfoItemLore.stream().map(s -> s.replace("{BOOST_TIME}", "" + ChatHelper.parseTime(model.getBoostTime()))).collect(Collectors.toList()))
                    .asGuiItem();
            gui.setItem(12, boostTime);

            GuiItem howManyBoost = ItemBuilder.from(Material.PAPER)
                    .setLore(Collections.singletonList("Number of boosters taken: " + model.getHistoryOfClaims().getEntrys().size()))
                    .asGuiItem();
            gui.setItem(13, howManyBoost);
        });
        gui.open(player);
    }
}