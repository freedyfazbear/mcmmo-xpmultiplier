package ru.rusekh.xpmultiplier.handler;

import com.gmail.nossr50.events.experience.McMMOPlayerXpGainEvent;
import eu.okaeri.injector.annotation.Inject;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import ru.rusekh.xpmultiplier.PluginConfiguration;
import ru.rusekh.xpmultiplier.repository.UserRepository;

import java.util.concurrent.ExecutionException;

public class XPHandler implements Listener
{
    @Inject
    private UserRepository userRepository;

    @EventHandler
    private void onGainExp(McMMOPlayerXpGainEvent event) {
        var player = event.getPlayer();
        var userModel = userRepository.fetchOrCreateModel(player.getUniqueId());
        try {
            if (userModel.get().getBoostTime() > 0) {
                    event.setRawXpGained((float) (event.getRawXpGained() * PluginConfiguration.boostMultiplier));
            } else {
                event.setRawXpGained(event.getRawXpGained());
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
