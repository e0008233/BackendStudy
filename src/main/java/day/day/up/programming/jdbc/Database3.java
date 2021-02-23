package day.day.up.programming.jdbc;

import day.day.up.programming.jdbc.domain.IdHolder;
import day.day.up.programming.jdbc.domain.PayPalProWhitelistUserDO;
import day.day.up.programming.upload.ParseTimestamp;
import day.day.up.programming.upload.domain.AccountDevice;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Database3 {
    public static void updateDatabase(String path,int choice){
        String fileName = path+"/EG_UID.csv";

        String jdbcURL = "";
        String username = "";
        String password = "";

        String sql="";



        List<PayPalProWhitelistUserDO> toAddList = new ArrayList<>();




        ICsvBeanReader beanReader = null;

        CellProcessor[] processors = new CellProcessor[]{
                new ParseInt() // id
        };

        try {
            beanReader = new CsvBeanReader(new FileReader(fileName), CsvPreference.STANDARD_PREFERENCE);
            IdHolder bean = null;
            String[] header = {"user_id"};
            int count = 0;

            boolean isStart = false;
            while ((bean = beanReader.read(IdHolder.class, header, processors)) != null) {
                PayPalProWhitelistUserDO toAdd = new PayPalProWhitelistUserDO();
                toAdd.setUserId(bean.getUser_id());
                toAdd.setMaxCreditLimit(0);
                toAdd.setMaxPriceLimit(0);
                toAdd.setMaxTopUpTimes(10);
                toAdd.setOperatorId(248);
                toAdd.setLogId(1);
                toAddList.add(toAdd);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Connection connection = null;

//        String query ="SELECT * FROM rings_account.account where country_code='NL';";
        try {
            connection = DriverManager.getConnection(jdbcURL, username, password);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
//        try (Statement stmt = connection.createStatement()) {
//            ResultSet rs = stmt.executeQuery(query);
//            while (rs.next()) {
//                PayPalProWhitelistUserDO toAdd = new PayPalProWhitelistUserDO();
//                toAdd.setUserId(rs.getInt("id"));
//                toAdd.setMaxCreditLimit(0);
//                toAdd.setMaxPriceLimit(0);
//                toAdd.setMaxTopUpTimes(10);
//                toAdd.setOperatorId(248);
//                toAdd.setLogId(1);
//                toAddList.add(toAdd);
//            }
//        } catch (SQLException e) {
//           System.out.println(e);
//        }
        System.out.println("Read all data from account");


        try {

            PreparedStatement statement = connection.prepareStatement(sql);
            int mapCount = 0;
            int batchSize = 1000;
            for (PayPalProWhitelistUserDO paypalDo: toAddList) {
                mapCount++;
                statement.setInt(1, paypalDo.getUserId());
                statement.setInt(2, paypalDo.getMaxPriceLimit());
                statement.setInt(3, paypalDo.getMaxCreditLimit());
                statement.setInt(4, paypalDo.getOperatorId());
                statement.setInt(5, paypalDo.getLogId());
                statement.addBatch();

                if (mapCount % batchSize == 0) {
                    System.out.println("committing the batch of "+batchSize+": "+ mapCount);
                    statement.executeBatch();
                    statement.clearBatch();
                }
            }
            statement.executeBatch();
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        System.out.println("Success");

    }
}
