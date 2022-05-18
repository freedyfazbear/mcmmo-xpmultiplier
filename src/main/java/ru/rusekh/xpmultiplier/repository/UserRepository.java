package ru.rusekh.xpmultiplier.repository;

import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.plugin.Plugin;
import pl.memexurer.srakadb.sql.table.DatabaseTable;
import pl.memexurer.srakadb.sql.table.query.DatabaseFetchQuery;
import pl.memexurer.srakadb.sql.table.query.DatabaseInsertQuery;
import ru.rusekh.xpmultiplier.user.UserModel;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class UserRepository
{
    private final DatabaseTable<UserModel> databaseTable;
    private final Plugin plugin;

    public UserRepository(Plugin plugin, HikariDataSource dataSource) {
        this.databaseTable = new DatabaseTable<>("xpmultiplier", dataSource, UserModel.class);
        this.plugin = plugin;
        databaseTable.initializeTable();
    }

    public CompletableFuture<UserModel> fetchOrCreateModel(UUID uuid) {
        CompletableFuture<UserModel> future = new CompletableFuture<>();
        plugin.getServer().getScheduler().runTask(plugin, () -> {
            try {
                UserModel dataModel = fetchDataModel(uuid);
                future.complete(dataModel == null ? new UserModel(uuid) : dataModel);
            } catch (Exception ex) {
                future.completeExceptionally(ex);
            }
        });
        return future;
    }

    public CompletableFuture<Void> updateDataModelAsync(UserModel dataModel) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        plugin.getServer().getScheduler().runTask(plugin, () -> {
            try {
                updateDataModel(dataModel);
                future.complete(null);
            } catch (Exception ex) {
                ex.printStackTrace();
                future.completeExceptionally(ex);
            }
        });
        return future;
    }

    private UserModel fetchDataModel(UUID uuid) {
        return new DatabaseFetchQuery()
                .and(databaseTable.getModelMapper().createQueryPair("uuid", uuid))
                .executeFetchQuerySingle(databaseTable).orElse(null);
    }

    private void updateDataModel(UserModel dataModel) {
        new DatabaseInsertQuery(DatabaseInsertQuery.UpdateType.REPLACE)
                .values(databaseTable.getModelMapper().createQueryPairs(dataModel))
                .execute(databaseTable);
    }
}
