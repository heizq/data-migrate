package cn.lamppa.aether.app;

import cn.lamppa.aether.service.ImageDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by Administrator on 2016/9/7 0007.
 */
public class DataPlay {

    private static Logger logger = LoggerFactory.getLogger(DataPlay.class);

    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:application-context.xml");
        ImageDataService imageDataService = ctx.getBean(ImageDataService.class);
        imageDataService.imageDataPlay(imageDataService.getImageDatas());

        System.out.println("数据上场成功！");
    }
}
