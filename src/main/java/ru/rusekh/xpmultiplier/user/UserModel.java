package ru.rusekh.xpmultiplier.user;

import pl.memexurer.srakadb.sql.mapper.SerializableTableColumn;
import pl.memexurer.srakadb.sql.mapper.TableColumnInfo;
import pl.memexurer.srakadb.sql.mapper.TypedTableColumn;
import pl.memexurer.srakadb.sql.mapper.serializer.UuidValueDeserializer;
import ru.rusekh.xpmultiplier.serializer.HistorySerializer;
import java.util.UUID;

public class UserModel
{
    @TableColumnInfo(primary = true, name = "playerUUID", serialized = @SerializableTableColumn(UuidValueDeserializer.class))
    private UUID uuid;
    @TableColumnInfo(nullable = false, name = "boostTime", typed = @TypedTableColumn("BIGINT(20)"))
    private long boostTime;
    @TableColumnInfo(nullable = false, name = "lastBoostClaimedTime", typed = @TypedTableColumn("BIGINT(20)"))
    private long lastBoostClaimedTime;
    @TableColumnInfo(nullable = false, name = "historyOfClaims", serialized = @SerializableTableColumn(HistorySerializer.Deserializer.class))
    private HistorySerializer historyOfClaims;

    public UserModel(UUID uuid) {
        this.uuid = uuid;
        this.boostTime = 0L;
        this.lastBoostClaimedTime = 0L;
        this.historyOfClaims = new HistorySerializer();
    }

    private UserModel() {}

    public HistorySerializer getHistoryOfClaims() {
        return historyOfClaims;
    }

    public void setLastBoostClaimedTime(long lastBoostClaimedTime) {
        this.lastBoostClaimedTime = lastBoostClaimedTime;
    }

    public long getLastBoostClaimedTime() {
        return lastBoostClaimedTime;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setBoostTime(long boostTime) {
        this.boostTime = boostTime;
    }

    public long getBoostTime() {
        return boostTime;
    }
}
