package ru.rusekh.xpmultiplier.command;

import me.vaperion.blade.annotation.Command;
import me.vaperion.blade.annotation.Sender;
import org.bukkit.entity.Player;
import ru.rusekh.xpmultiplier.PluginConfiguration;
import ru.rusekh.xpmultiplier.XPMultiplier;
import ru.rusekh.xpmultiplier.helper.ChatHelper;

import java.time.Instant;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class BoostCommand
{

    @Command(value = "mcm claim")
    public void claimBoost(@Sender Player player) {
        var userModel = XPMultiplier.getInstance().getRepository().fetchOrCreateModel(player.getUniqueId());
        userModel.whenComplete((model, throwable) -> {
            if (throwable != null) {
                throwable.printStackTrace();
                ChatHelper.sendMessage(player, "&cError!");
            }
            if (model.getLastBoostClaimedTime() <= System.currentTimeMillis()) {
                model.setBoostTime(PluginConfiguration.boostTime + System.currentTimeMillis());
                model.setLastBoostClaimedTime(TimeUnit.HOURS.toMillis(24L) + System.currentTimeMillis());
                model.getHistoryOfClaims().addEntryToHistory(Date.from(Instant.now()));
                XPMultiplier.getInstance().getRepository().updateDataModelAsync(model).whenComplete((unused, throwable1) -> {
                    if (throwable1 != null) {
                        throwable1.printStackTrace();
                        return;
                    }
                    ChatHelper.sendMessage(player, "&5McMMo Booster Activated for &d24 Hours");
                });
            } else {
                ChatHelper.sendMessage(player, "&cYou can claim boost only once per day.");
                ChatHelper.sendMessage(player, "&cYou must wait: " + ChatHelper.parseTime(model.getLastBoostClaimedTime()));
            }
        });
    }

    @Command(value = "mcm")
    public void viewBoostInfoGui(@Sender Player player) {
        XPMultiplier.getInstance().getInventory().openGui(player);
    }
}
