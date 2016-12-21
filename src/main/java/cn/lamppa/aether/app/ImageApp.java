package cn.lamppa.aether.app;

import cn.lamppa.aether.service.ImageDataService;
import cn.lamppa.aether.task.ImageTask;
import cn.lamppa.aether.util.PropertyConfigUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ForkJoinPool;

/**
 * Created by Administrator on 2016/8/31 0031.
 */
@Component
public class ImageApp {

    public static void main(String[] args) {
        try {
            ForkJoinPool pool = new ForkJoinPool(2);

            ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:application-context.xml");
            ImageDataService imageDataService = ctx.getBean(ImageDataService.class);

            //imageDataService.createTempTable();

            ImageTask imageTask = new ImageTask(imageDataService
                    ,PropertyConfigUtil.getPropertyValue("img.resource.path")
                    , PropertyConfigUtil.getPropertyValue("img.extension"));
            pool.execute(imageTask);

            pool.shutdown();

            List<String> results;

            results = imageTask.join();
            System.out.printf("Image: %d files found.\n", results.size());
        } catch (Exception e) {
            e.printStackTrace();
        }



    }

}
