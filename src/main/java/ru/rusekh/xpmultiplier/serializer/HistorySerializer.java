package ru.rusekh.xpmultiplier.serializer;

import org.bukkit.Bukkit;
import pl.memexurer.srakadb.sql.mapper.serializer.TableColumnValueDeserializer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HistorySerializer
{
    private final List<Date> historyOfClaims = new ArrayList<>();

    public void addEntryToHistory(Date date) {
        historyOfClaims.add(date);
    }

    public List<Date> getEntrys() {
        return historyOfClaims;
    }

    public static class Deserializer implements TableColumnValueDeserializer<HistorySerializer> {

        @Override
        public HistorySerializer deserialize(ResultSet resultSet, String s) throws SQLException {
            String splitted = resultSet.getString(s);
            HistorySerializer historySerializer = new HistorySerializer();
            historySerializer.historyOfClaims.add(Date.from(Instant.parse(splitted)));
            return historySerializer;
        }

        @Override
        public Object serialize(HistorySerializer historySerializer) {
            StringBuilder stringBuilder = new StringBuilder();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            for (Date historyOfClaim : historySerializer.historyOfClaims) {
                String strDate = dateFormat.format(historyOfClaim);
                stringBuilder.append(strDate);
            }
            if (stringBuilder.isEmpty()) {
                return "None";
            }
            Bukkit.broadcastMessage(stringBuilder.toString());
            return stringBuilder.toString();
        }

        @Override
        public String getDataType() {
            return "text";
        }
    }
}
