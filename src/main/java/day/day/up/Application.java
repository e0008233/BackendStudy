package day.day.up;

import com.google.zxing.WriterException;
import day.day.up.others.qrcode.QrCodeService;
import day.day.up.others.qrcode.QrCodeServiceImpl;
import day.day.up.programming.jdbc.Database;
import day.day.up.programming.jdbc.Database3;
import day.day.up.programming.upload.ComplexCsv2DbInserter;
import day.day.up.programming.upload.SimpleUpload;
import day.day.up.questions.algorithms.dynamic_programming.number413.Solution;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import day.day.up.programming.comparator.Sort;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
// Interesting question: number32(parentheses), number 84

@SpringBootApplication
public class Application {
    private static final String zone = "Africa/Addis_Ababa"; //utc+3


    public static void main(String[] args){
//        Sort solution = new Sort();
//        solution.sort();
//        System.out.println();
//        String fileName = "";
//        int start = 0;
//        int end = 0;
//        int choice = 0;
//        int size = 0;
//        String path = "";
//        if (args.length > 0) {
//            path=args[0];
//            fileName=args[1];
//            start=Integer.parseInt(args[2]);
//            end=Integer.parseInt(args[3]);
//            choice = Integer.parseInt(args[1]);
//            size = Integer.parseInt(args[5]);

//        }
//        System.out.println(fileName+" " + start+" "+ end);
//        ComplexCsv2DbInserter.upload(path,fileName,start,end,choice,size);
//        Database.updateDatabase();
        // backend


//        LocalDate firstDayOfMonth= YearMonth.from(Instant.now().atZone(ZoneId.of(zone))).atDay(1);
//
//        int startTime =(int) firstDayOfMonth.atStartOfDay(ZoneId.of(zone)).toEpochSecond();
////
//
//        int timezoneDiff= 3*3600;
////        startTime+=timezoneDiff;
//
//        LocalDate currentDate = LocalDate.now(ZoneId.of(zone));
//        int currentDay = currentDate.getDayOfMonth();
//        int endTime = (int)currentDate.atStartOfDay(ZoneId.of(zone)).toEpochSecond();
        Solution solution = new Solution();
        System.out.println(solution.numberOfArithmeticSlices(new int[]{2,6,7,10}));
//        QrCodeService qrCodeService = new QrCodeServiceImpl();
//        try {
//            qrCodeService.generateQrCode();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (WriterException e) {
//            e.printStackTrace();
//        }
//        Database3.updateDatabase(path,choice);
//        SpringApplication.run(Application.class, args);


    }

//    @Bean
//    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
//        return args -> {
//
//            System.out.println("Let's inspect the beans provided by Spring Boot:");
//
//            String[] beanNames = ctx.getBeanDefinitionNames();
//            Arrays.sort(beanNames);
//            for (String beanName : beanNames) {
//                System.out.println(beanName);
//            }
//
//        };
//    }

}