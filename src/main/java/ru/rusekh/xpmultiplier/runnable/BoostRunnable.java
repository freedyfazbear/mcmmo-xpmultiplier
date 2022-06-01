package ru.rusekh.xpmultiplier.runnable;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import ru.rusekh.xpmultiplier.XPMultiplier;
import ru.rusekh.xpmultiplier.helper.ChatHelper;

import java.util.concurrent.TimeUnit;

public class BoostRunnable extends BukkitRunnable
{

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            XPMultiplier.getInstance().getRepository().fetchOrCreateModel(player.getUniqueId()).whenComplete((userModel, throwable) -> {
                if (throwable != null) {
                    throwable.printStackTrace();
                }
                if (userModel.getBoostTime() > 0L) {
                    if (userModel.isPaused()) {
                        userModel.setBoostTime(userModel.getBoostTime() + TimeUnit.SECONDS.toMillis(2L));
                        XPMultiplier.getInstance().getRepository().updateDataModelAsync(userModel).whenComplete((unused, throwable1) -> {
                            if (throwable1 != null) {
                                throwable1.printStackTrace();
                            }
                        });
                        return;
                    }
                    XPMultiplier.getInstance().getRepository().updateDataModelAsync(userModel).whenComplete((unused, throwable1) -> {
                        if (throwable1 != null) {
                            throwable1.printStackTrace();
                        }
                    });
                }
            });
        }
    }
}
