package day.day.up.programming.upload;

import day.day.up.programming.upload.domain.SbcmSalary;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SimpleUpload {
    public static void upload(){
        String csvFilePath ="/Users/zhang/Desktop/input.csv";

        String jdbcURL = "";
        String username = "";
        String password = "";
        String sql = "";

        int choice = 3;

        ICsvBeanReader beanReader = null;

        int batchSize = 1000;
        Connection connection = null;
        CellProcessor[] processors = new CellProcessor[]{
                new ParseInt(), // userId
                new ParseInt(), // Salary
//                new NotNull(), // time
        };

        try {
            System.out.println("----------------------");
            List<SbcmSalary> list = new ArrayList<>();

            connection = DriverManager.getConnection(jdbcURL, username, password);
            connection.setAutoCommit(false);
            System.out.println("Connection success");
            PreparedStatement statement = connection.prepareStatement(sql);

            beanReader = new CsvBeanReader(new FileReader(csvFilePath), CsvPreference.STANDARD_PREFERENCE);
//            beanReader.getHeader(false);
            SbcmSalary bean = null;
            String[] header = {"userId", "remainingSalary"};
            int count = 0;

            while ((bean = beanReader.read(SbcmSalary.class, header, processors)) != null) {

                list.add(bean);

            }
            System.out.println("try uploading");

            for(SbcmSalary sbcmSalary:list){
                int userId = sbcmSalary.getUserId();
                int remainingSalary = sbcmSalary.getRemainingSalary();

                statement.setInt(1, userId);
                statement.setInt(2, remainingSalary);
                statement.setString(3, "6-2021-upload");

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
