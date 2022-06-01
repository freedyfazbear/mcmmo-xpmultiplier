package ru.rusekh.xpmultiplier;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import eu.okaeri.configs.ConfigManager;
import eu.okaeri.configs.yaml.bukkit.YamlBukkitConfigurer;
import eu.okaeri.configs.yaml.bukkit.serdes.SerdesBukkit;
import eu.okaeri.injector.Injector;
import eu.okaeri.injector.OkaeriInjector;
import me.vaperion.blade.Blade;
import me.vaperion.blade.container.impl.BukkitCommandContainer;
import org.bukkit.plugin.java.JavaPlugin;
import ru.rusekh.xpmultiplier.command.BoostCommand;
import ru.rusekh.xpmultiplier.inventory.BoostInfoInventory;
import ru.rusekh.xpmultiplier.repository.UserRepository;
import ru.rusekh.xpmultiplier.runnable.BoostRunnable;

import java.io.File;

public class XPMultiplier extends JavaPlugin
{
    private HikariDataSource dataSource;
    private final PluginConfiguration configuration = (PluginConfiguration) ConfigManager.create(
                    PluginConfiguration.class)
            .withConfigurer(new YamlBukkitConfigurer(), new SerdesBukkit())
            .withBindFile(new File(getDataFolder(), "config.yml"))
            .saveDefaults()
            .load(true);
    private BoostInfoInventory inventory;
    private UserRepository repository;

    @Override
    public void onEnable() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://147.135.73.218:3306/serverpro_db");
        config.setUsername("root");
        config.setPassword("FbyRdlCJufsHG7k");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("useServerPrepStmts", "true");
        config.addDataSourceProperty("useLocalSessionState", "true");
        config.addDataSourceProperty("rewriteBatchedStatements", "true");
        config.addDataSourceProperty("cacheResultSetMetadata", "true");
        config.addDataSourceProperty("cacheServerConfiguration", "true");
        config.addDataSourceProperty("elideSetAutoCommits", "true");
        config.addDataSourceProperty("maintainTimeStats", "false");
        dataSource = new HikariDataSource(config);
        Blade.of()
                .fallbackPrefix("xpmultiplier")
                .containerCreator(BukkitCommandContainer.CREATOR)
                .build().register(new BoostCommand());

        inventory = new BoostInfoInventory();

        repository = new UserRepository(this, dataSource);

        new BoostRunnable().runTaskTimer(this, 0L, 20L);
    }

    @Override
    public void onDisable() {
        dataSource.close();
        Blade.of().clearBindings().clearCustomProviderMap().clearCustomProviderMap().build();
    }


    public PluginConfiguration getConfiguration() {
        return configuration;
    }

    public HikariDataSource getDataSource() {
        return dataSource;
    }

    public BoostInfoInventory getInventory() {
        return inventory;
    }

    public UserRepository getRepository() {
        return repository;
    }

    public static XPMultiplier getInstance() {
        return JavaPlugin.getPlugin(XPMultiplier.class);
    }
}
