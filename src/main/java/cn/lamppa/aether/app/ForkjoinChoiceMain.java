package cn.lamppa.aether.app;


import cn.lamppa.aether.service.PointAndChoiceService;
import cn.lamppa.aether.task.ChoiceTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

/**
 * Created by liupd on 16-3-30.
 **/


public class ForkjoinChoiceMain {

    private static Logger logger = LoggerFactory.getLogger(ForkjoinMain.class);


    public static void main(String[] args) throws Exception {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:application-context.xml");
        PointAndChoiceService pointAndChoiceService = ctx.getBean(PointAndChoiceService.class);

        int total = pointAndChoiceService.getCount();
        ChoiceTask task = new ChoiceTask(pointAndChoiceService,0,total);
        ForkJoinPool pool = new ForkJoinPool(16);

        pool.execute(task);
        do{
            System.out.printf("******************************************\n");
            System.out.printf("Main: Parallelism: %d\n", pool.getParallelism());
            System.out.printf("Main: Active Threads: %d\n", pool.getActiveThreadCount());
            System.out.printf("Main: Task Count: %d\n", pool.getQueuedTaskCount());
            System.out.printf("Main: Running Thread Count:%d\n", pool.getRunningThreadCount());
            System.out.printf("Main: Queued Submission:%d\n", pool.getQueuedSubmissionCount());
            System.out.printf("Main: Steal Count: %d\n", pool.getStealCount());
            System.out.printf("******************************************\n");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }while (!task.isDone());

        pool.shutdown();
        try {
            pool.awaitTermination(1, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
