package ru.rusekh.xpmultiplier.handler;

import com.gmail.nossr50.events.experience.McMMOPlayerXpGainEvent;
import eu.okaeri.injector.annotation.Inject;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import ru.rusekh.xpmultiplier.PluginConfiguration;
import ru.rusekh.xpmultiplier.XPMultiplier;
import ru.rusekh.xpmultiplier.helper.ChatHelper;
import ru.rusekh.xpmultiplier.repository.UserRepository;

public class XPHandler implements Listener
{
    @Inject
    private UserRepository userRepository;


    @EventHandler
    private void onPlayerLeave(PlayerQuitEvent event) {
        userRepository.fetchOrCreateModel(event.getPlayer().getUniqueId()).whenComplete((userModel, throwable) -> {
           if (throwable != null) {
               throwable.printStackTrace();
           }
          userModel.setPaused(true);
        });
    }

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent event) {
        userRepository.fetchOrCreateModel(event.getPlayer().getUniqueId()).whenComplete((userModel, throwable) -> {
            if (throwable != null) {
                throwable.printStackTrace();
            }
            if (userModel.isPaused()) {
                userModel.setPaused(false);
                ChatHelper.sendMessage(event.getPlayer(), "&dYour boost has been resumed");
            }
        });
    }

    @EventHandler
    private void onGainExp(McMMOPlayerXpGainEvent event) {
        var player = event.getPlayer();
        var userModel = userRepository.fetchOrCreateModel(player.getUniqueId());
        userModel.whenComplete((model, throwable) -> {
            if (throwable != null) {
                throwable.printStackTrace();
                return;
            }
            if (model.getBoostTime() > System.currentTimeMillis()) {
                event.setRawXpGained((float) (event.getRawXpGained() * PluginConfiguration.boostMultiplier));
                return;
            }
            event.setRawXpGained(event.getRawXpGained());
        });
    }
}
