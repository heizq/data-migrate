package cn.lamppa.aether.app;

import cn.lamppa.aether.domain.Textbook;
import cn.lamppa.aether.service.QuestionFillingService;
import cn.lamppa.aether.service.QuestionSyntheticalService;
import cn.lamppa.aether.service.TextbookService;
import cn.lamppa.aether.task.FillingTask;
import cn.lamppa.aether.task.SyntheticalTask;
import cn.lamppa.aether.task.TextbookTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

/**
 * Created by heizhiqiang on 2016/3/28 0028.
 */
public class ForkjoinMain {
    private static Logger logger = LoggerFactory.getLogger(ForkjoinMain.class);


    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:application-context.xml");
        QuestionFillingService fillingService = ctx.getBean(QuestionFillingService.class);
        TextbookService textbookService = ctx.getBean(TextbookService.class);
        QuestionSyntheticalService syntheticalService = ctx.getBean(QuestionSyntheticalService.class);

        int total = fillingService.getCount();
        FillingTask fillingTask = new FillingTask(fillingService,0,total);

//        List<Textbook>  textbooks = textbookService.getTextBook();
//        TextbookTask textbookTask = new TextbookTask(textbookService,0,textbooks.size());

        int total2 = syntheticalService.getCount();
        SyntheticalTask syntheticalTask = new SyntheticalTask(syntheticalService,0,total2);

        ForkJoinPool pool = new ForkJoinPool(16);

        pool.execute(fillingTask);
        //pool.execute(textbookTask);
        pool.execute(syntheticalTask);
//        do{
//            System.out.printf("******************************************\n");
//            System.out.printf("Main: Parallelism: %d\n", pool.getParallelism());
//            System.out.printf("Main: Active Threads: %d\n", pool.getActiveThreadCount());
//            System.out.printf("Main: Task Count: %d\n", pool.getQueuedTaskCount());
//            System.out.printf("Main: Running Thread Count:%d\n", pool.getRunningThreadCount());
//            System.out.printf("Main: Queued Submission:%d\n", pool.getQueuedSubmissionCount());
//            System.out.printf("Main: Steal Count: %d\n", pool.getStealCount());
//            System.out.printf("******************************************\n");
//            try {
//                TimeUnit.SECONDS.sleep(1);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }while (!syntheticalTask.isDone() );

        pool.shutdown();
        try {
            pool.awaitTermination(1, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

//        try {
//            //  logger.info("Main: finish synthetical success  {} in the total {}",syntheticalTask.get(),total2);
//            //logger.info("Main: finish filling success  {} in the total {}",fillingTask.get(),total);
//            logger.info("Main: finish textbook success  {} in the total {}",textbookTask.get(),textbooks.size());
//        } catch (InterruptedException | ExecutionException e) {
//            e.printStackTrace();
//        }
    }

}
