package day.day.up;

import day.day.up.programming.upload.ComplexCsv2DbInserter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import day.day.up.programming.comparator.Sort;
// Interesting question: number32(parentheses), number 84

@SpringBootApplication
public class Application {
    public static void main(String[] args){
//        Sort solution = new Sort();
//        solution.sort();
//        System.out.println();
        String fileName = "";
        int start = 0;
        int end = 0;
        int choice = 0;
        int size = 0;
        String path = "";
        if (args.length > 0) {
            path=args[0];
            fileName=args[1];
            start=Integer.parseInt(args[2]);
            end=Integer.parseInt(args[3]);
            choice = Integer.parseInt(args[4]);
            size = Integer.parseInt(args[5]);

        }
        System.out.println(fileName+" " + start+" "+ end);
        ComplexCsv2DbInserter.upload(path,fileName,start,end,choice,size);
        // backend
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