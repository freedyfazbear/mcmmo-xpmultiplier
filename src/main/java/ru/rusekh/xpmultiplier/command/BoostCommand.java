package ru.rusekh.xpmultiplier.command;

import me.vaperion.blade.annotation.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import ru.rusekh.xpmultiplier.PluginConfiguration;
import ru.rusekh.xpmultiplier.XPMultiplier;
import ru.rusekh.xpmultiplier.helper.ChatHelper;
import java.time.Instant;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
    @Command(value = "mcma pause")
    @Permission("xpmultiplier.admin")
    public void pauseBoost(@Sender Player player) {
        var userModel = XPMultiplier.getInstance().getRepository().fetchOrCreateModel(player.getUniqueId());
        userModel.whenComplete((model, throwable) -> {
            if (throwable != null) {
                throwable.printStackTrace();
                ChatHelper.sendMessage(player, "&cError!");
            }
            if (model.isPaused()) {
                model.setPaused(false);
                ChatHelper.sendMessage(player, "&5McMMo Booster Resumed");
            } else {
                model.setPaused(true);
                ChatHelper.sendMessage(player, "&5McMMo Booster Paused");
            }
            XPMultiplier.getInstance().getRepository().updateDataModelAsync(model).whenComplete((unused, throwable1) -> {
                if (throwable1 != null) {
                    throwable1.printStackTrace();
                }
            });
        });
    }

    @Command("mcma take")
    @Permission("xpmultiplier.admin")
    public void boostTake(@Sender Player player, @Name("nickname") @Optional String playerName) {
        XPMultiplier.getInstance().getRepository().fetchOrCreateModel(player.getUniqueId()).whenComplete((userModel, throwable) -> {
            if (throwable != null) {
                throwable.printStackTrace();
                ChatHelper.sendMessage(player, "&cError!");
            }
            userModel.setBoostTime(0L);
            ChatHelper.sendMessage(player, "&5Your MCMMO booster has been taken.");
        });
        if (playerName == null) return;
        Player targetPlayer = Bukkit.getPlayer(playerName);
        if (targetPlayer == null) {
            ChatHelper.sendMessage(player, "&cPlayer not found!");
            return;
        }
        XPMultiplier.getInstance().getRepository().fetchOrCreateModel(targetPlayer.getUniqueId()).whenComplete((model, throwable) -> {
            if (throwable != null) {
                throwable.printStackTrace();
                ChatHelper.sendMessage(player, "&cError!");
            }
            model.setBoostTime(0L);
            XPMultiplier.getInstance().getRepository().updateDataModelAsync(model).whenComplete((unused, throwable1) -> {
                if (throwable1 != null) {
                    throwable1.printStackTrace();
                }
                ChatHelper.sendMessage(player, "&5McMMo Booster taken from &d" + targetPlayer.getName());
            });
        });
    }

    @Command("mcma give")
    @Permission("xpmultiplier.admin")
    public void boostGive(@Sender Player player, @Name("nickname") String playerName, @Name("time (like: 1d, 2m") String time) {
        XPMultiplier.getInstance().getRepository().fetchOrCreateModel(player.getUniqueId()).whenComplete((userModel, throwable) -> {
            if (throwable != null) {
                throwable.printStackTrace();
                ChatHelper.sendMessage(player, "&cError!");
            }
            userModel.setBoostTime(ChatHelper.parseTime(time) + System.currentTimeMillis());
            XPMultiplier.getInstance().getRepository().updateDataModelAsync(userModel).whenComplete((unused, throwable1) -> {
                if (throwable1 != null) {
                    throwable1.printStackTrace();
                }
                ChatHelper.sendMessage(player, "&5McMMo Booster gived!");
            });
        });
        if (playerName == null) return;
        Player targetPlayer = Bukkit.getPlayer(playerName);
        if (targetPlayer == null) {
            ChatHelper.sendMessage(player, "&cPlayer not found!");
            return;
        }
        XPMultiplier.getInstance().getRepository().fetchOrCreateModel(targetPlayer.getUniqueId()).whenComplete((model, throwable) -> {
            if (throwable != null) {
                throwable.printStackTrace();
                ChatHelper.sendMessage(player, "&cError!");
            }
            model.setBoostTime(ChatHelper.parseTime(time) + System.currentTimeMillis());
            XPMultiplier.getInstance().getRepository().updateDataModelAsync(model).whenComplete((unused, throwable1) -> {
                if (throwable1 != null) {
                    throwable1.printStackTrace();
                }
                ChatHelper.sendMessage(player, "&5McMMo Booster given to &d" + targetPlayer.getName());
            });
        });
    }

    @Command("mcma stop")
    @Permission("xpmultiplier.admin")
    public void boostStop(@Sender Player player) {
        XPMultiplier.getInstance().getRepository().fetchOrCreateModel(player.getUniqueId()).whenComplete((model, throwable) -> {
            if (throwable != null) {
                throwable.printStackTrace();
                ChatHelper.sendMessage(player, "&cError!");
            }
            model.setBoostTime(0L);
            XPMultiplier.getInstance().getRepository().updateDataModelAsync(model).whenComplete((unused, throwable1) -> {
                if (throwable1 != null) {
                    throwable1.printStackTrace();
                }
                ChatHelper.sendMessage(player, "&5McMMo Booster stopped");
            });
        });
    }

    @Command(value = "mcm reload")
    @Permission("xpmultiplier.admin")
    public void reload(@Sender Player player) {
        XPMultiplier.getInstance().getConfiguration().load();
        ChatHelper.sendMessage(player, "&5McMMo Booster reloaded");
    }

    @Command(value = "mcm")
    public void viewBoostInfo(@Sender Player player) {
        XPMultiplier.getInstance().getRepository().fetchOrCreateModel(player.getUniqueId()).whenComplete((model, throwable) -> {
            if (throwable != null) {
                throwable.printStackTrace();
                ChatHelper.sendMessage(player, "&cError!");
            }
            ChatHelper.sendMessage(player, "&5McMMo Booster Time: &d" + ChatHelper.parseTime(model.getBoostTime()));
            ChatHelper.sendMessage(player, "&5McMMo Booster Paused: &d" + (model.isPaused() ? "" : "&cNot ") + " Paused");
            ChatHelper.sendMessage(player, "&5McMMo Booster Last Claimed: &d" + ChatHelper.parseTime(model.getLastBoostClaimedTime()));
            ChatHelper.sendMessage(player, "&5McMMo Booster Claimed History: &d" + model.getHistoryOfClaims().getEntrys().stream().map(Date::toString).collect(Collectors.joining(", ")));
        });
    }
}
