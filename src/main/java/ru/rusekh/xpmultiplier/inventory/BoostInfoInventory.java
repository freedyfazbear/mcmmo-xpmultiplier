package ru.rusekh.xpmultiplier.inventory;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.rusekh.xpmultiplier.PluginConfiguration;
import ru.rusekh.xpmultiplier.XPMultiplier;
import ru.rusekh.xpmultiplier.helper.ChatHelper;
import java.util.Comparator;

public class BoostInfoInventory
{

    public void openGui(Player player) {
        Gui gui = Gui.gui()
                .title(Component.text("Boost Info"))
                .disableAllInteractions()
                .rows(3)
                .create();

        var userModel = XPMultiplier.getInstance().getRepository().fetchOrCreateModel(player.getUniqueId());
        userModel.whenComplete((model, throwable) -> {
            if (throwable != null) {
                throwable.printStackTrace();
            }

            GuiItem actualBoostInfo = ItemBuilder.from(Material.PAPER)
                    .name(Component.text(ChatHelper.color("&7&lActual Boost Info")))
                    .setLore(String.valueOf(PluginConfiguration.boostInfoItemLore.stream().map(s -> s.replace("{BOOST_TIME}", "" + model.getBoostTime()))))
                    .asGuiItem();
            gui.setItem(12, actualBoostInfo);

            GuiItem howManyBoost = ItemBuilder.from(Material.PAPER)
                    .name(Component.text(ChatHelper.color("&7History of claimed boosts")))
                    .setLore(String.valueOf(model.getHistoryOfClaims().getEntrys().stream().sorted(Comparator.comparing(o -> o)).limit(10).toList()))
                    .asGuiItem();
            gui.setItem(13, howManyBoost);
        });
        gui.open(player);
    }
}
