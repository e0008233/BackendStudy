package day.day.up.programming.upload;

import java.io.*;
import java.sql.*;

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
    static void upload() {
        String csvFilePath = "/Users/zhang/Desktop/study/test.csv";
        ICsvBeanReader beanReader = null;
        int batchSize = 20;
        Connection connection = null;
        CellProcessor[] processors = new CellProcessor[]{
                new NotNull(), // pi
                new ParseInt(), // num
                new ParseTimestamp(), // timestamp
                new ParseTimestamp() // timestamp
        };

        try {
            long start = System.currentTimeMillis();
            beanReader = new CsvBeanReader(new FileReader(csvFilePath),CsvPreference.STANDARD_PREFERENCE);


            long end = System.currentTimeMillis();
            System.out.println("Execution Time: " + (end - start));
        } catch (IOException ex) {
            System.err.println(ex);
        }
//        catch (SQLException ex) {
//            ex.printStackTrace();
//
//            try {
//                connection.rollback();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }

    }

}
