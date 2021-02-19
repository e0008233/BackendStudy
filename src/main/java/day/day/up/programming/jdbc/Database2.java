package day.day.up.programming.jdbc;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class Database2 {
    public static void updateDatabase(){
        String jdbcURL = "";
        String username = "";
        String password = "";

        Map<String,Integer> toAdd = new HashMap<>();
        Connection connection = null;

        String query ="SELECT pi, COUNT(*) FROM rings_account.account  where register_time >= '2021-02-04 10:00:00' GROUP BY pi;";
        try {
            connection = DriverManager.getConnection(jdbcURL, username, password);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String pi = rs.getString("pi");
                int count = rs.getInt("COUNT(*)");
                toAdd.put(pi,count);
            }
        } catch (SQLException e) {
           System.out.println(e);
        }
        System.out.println("Read all data from account");


        try {
            String sql = "INSERT INTO account_device (pi, num) VALUES (?, ?) ON DUPLICATE KEY UPDATE num=num+VALUES(num);";

            PreparedStatement statement = connection.prepareStatement(sql);
            int mapCount = 0;
            int batchSize = 100;
            for (String key : toAdd.keySet()) {
                mapCount++;
                statement.setString(1, key);
                statement.setInt(2, toAdd.get(key));
                statement.addBatch();

                if (mapCount % batchSize == 0) {
                    System.out.println("committing the batch of "+batchSize+": "+ mapCount);
                    statement.executeBatch();
                    statement.clearBatch();
                }
            }
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        System.out.println("Success");

    }
}
