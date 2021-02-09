package day.day.up.programming.upload;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import day.day.up.programming.upload.domain.AccountDevice;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ParseDouble;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;


//https://www.codejava.net/coding/java-code-example-to-insert-data-from-csv-to-database

public class ComplexCsv2DbInserter {
    public static void upload(String path,String name, int start, int end, int choice, int size) {

        for (int i=start;i<=end;i++) {
            String fileName = name+i+".csv";
//            String csvFilePath = "/Users/zhang/Desktop/account_device/"+fileName;
            String csvFilePath = path+fileName;

            String jdbcURL= "";
            String username="";
            String password="";
            switch (choice){
                case 1:
                    // dt
                    jdbcURL = "jdbc:mysql://localhost:3306/rings_account";
                    username = "123";
                    password = "123";
                    break;
                case 2:
                    //            pre release
                    jdbcURL = "jdbc:mysql://localhost:3307/rings_account";
                    username = "123";
                    password = "123221";
                    break;
                case 3:
                    // release
                    jdbcURL = "jdbc:mysql://localhost:3308/rings_account";
                    username = "123";
                    password = "123";
                    break;
            }


            ICsvBeanReader beanReader = null;

            int batchSize = 1000;
            if (size> 1000) batchSize = size;
            Connection connection = null;
            CellProcessor[] processors = new CellProcessor[]{
                    new NotNull(), // pi
                    new ParseInt(), // num
                    new ParseTimestamp(), // timestamp
                    new ParseTimestamp() // timestamp
            };

            try {
                System.out.println("----------------------");
                List<AccountDevice> list = new ArrayList<AccountDevice>();
                long startTime = System.currentTimeMillis();
                System.out.println(fileName+": uploading starts");
                connection = DriverManager.getConnection(jdbcURL, username, password);
                connection.setAutoCommit(false);

                String sql = "INSERT INTO account_device (pi, num) VALUES (?, ?)";
                PreparedStatement statement = connection.prepareStatement(sql);

                beanReader = new CsvBeanReader(new FileReader(csvFilePath), CsvPreference.STANDARD_PREFERENCE);
//            beanReader.getHeader(false);
                AccountDevice bean = null;
                String[] header = {"pi", "num", "createTime", "updateTime"};
                int count = 0;

                boolean isStart = false;
                while ((bean = beanReader.read(AccountDevice.class, header, processors)) != null) {
                    if (bean.getPi().equals("36b1a614b81dbf3e")) isStart = true;
                    if (isStart && !bean.getPi().equals("36bf02da89270884")) {
                        list.add(bean);
                    }

                }
                for(AccountDevice accountDevice:list){
                    String pi = accountDevice.getPi();
                    int num = accountDevice.getNum();
//                    Timestamp createTime = accountDevice.getCreateTime();
//                    Timestamp updateTime = accountDevice.getUpdateTime();

                    statement.setString(1, pi);
                    statement.setInt(2, num);
//                    statement.setTimestamp(3, createTime);
//                    statement.setTimestamp(4, updateTime);
                    count++;
                    statement.addBatch();
                    if (count % batchSize == 0) {
                        System.out.println("committing the batch of "+batchSize+": "+count);
                        statement.executeBatch();
                        connection.commit();
                        statement.clearBatch();
                    }
                }

                beanReader.close();

                // execute the remaining queries
                statement.executeBatch();

                connection.commit();
                connection.close();

                System.out.println(fileName+": uploading ends");
                long endTime = System.currentTimeMillis();
                System.out.println("Execution Time: " + (endTime - startTime));
            } catch (IOException ex) {
                System.err.println(ex);
            } catch (SQLException ex) {
                ex.printStackTrace();

                try {
                    connection.rollback();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
