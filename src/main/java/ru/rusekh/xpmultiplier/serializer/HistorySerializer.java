package ru.rusekh.xpmultiplier.serializer;

import pl.memexurer.srakadb.sql.mapper.serializer.TableColumnValueDeserializer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

        public Deserializer() {}

        @Override
        public HistorySerializer deserialize(ResultSet resultSet, String s) throws SQLException {
            String[] splitted = resultSet.getString(s).split(",");
            HistorySerializer historySerializer = new HistorySerializer();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = null;
            try {
                date = dateFormat.parse(splitted[0]);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            historySerializer.historyOfClaims.add(date);
            return historySerializer;
        }

        @Override
        public Object serialize(HistorySerializer historySerializer) {
            StringBuilder stringBuilder = new StringBuilder();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            for (Date historyOfClaim : historySerializer.historyOfClaims) {
                String strDate = dateFormat.format(historyOfClaim);
                stringBuilder.append(strDate).append(",");
            }
            if (stringBuilder.isEmpty()) {
                return "None";
            } else {
                return stringBuilder.substring(0, stringBuilder.length() - 1);
            }
        }

        @Override
        public String getDataType() {
            return "text";
        }
    }
}
