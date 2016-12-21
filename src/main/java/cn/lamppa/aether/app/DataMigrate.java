package cn.lamppa.aether.app;

/**
 * 数据迁移，工具使用说明:
 * 零、
 * 数据准备：如果图片没有上传到mongodb，首先要执行ImageApp,
 * 在ImageApp class 中，如果数据库中没有创建存放图片mongodb id 的临时表，
 * 需要先调用createTempTable() 方法，创建临时表。
 *
 * 一：
 * 使用DataMigrate 之前，需要首先执行 DataPlay 让数据上场。
 *
 * 二：
 * 执行完DataMigrate之后，需要人工将以下表的数据转移到新库中去；
 * ks_chapter_section
 * ks_chapter_section_knowledge
 * ks_knowledge_point
 * ks_knowledge_point_relation
 * ks_paper
 * ks_paper_big_item
 * ks_paper_small_item
 * ks_question_chapter_section
 * ks_textbook
 */

import cn.lamppa.aether.service.*;
import cn.lamppa.aether.task.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2016/9/7 0007.
 */
public class DataMigrate {

    private static Logger logger = LoggerFactory.getLogger(DataMigrate.class);

    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:application-context.xml");
        QuestionFillingService fillingService = ctx.getBean(QuestionFillingService.class);
        TextbookService textbookService = ctx.getBean(TextbookService.class);
        QuestionSyntheticalService syntheticalService = ctx.getBean(QuestionSyntheticalService.class);
        PointAndChoiceService pointAndChoiceService = ctx.getBean(PointAndChoiceService.class);
        QuestionJudgeService judgeService = ctx.getBean(QuestionJudgeService.class);
        QuestionShortanswerService shortanswerService = ctx.getBean(QuestionShortanswerService.class);

        JudgeTask judgeTask = new JudgeTask(judgeService,0,judgeService.getCount());
        ChoiceTask choiceTask = new ChoiceTask(pointAndChoiceService,0,pointAndChoiceService.getCount());
        FillingTask fillingTask = new FillingTask(fillingService,0,fillingService.getCount());
        ShortAnswerTask shortAnswerTask = new ShortAnswerTask(shortanswerService,0,shortanswerService.getCount());
        SyntheticalTask syntheticalTask = new SyntheticalTask(syntheticalService,0,syntheticalService.getCount());

//        TextbookTask textbookTask = new TextbookTask(textbookService,0,textbookService.getTextBook().size());

        ForkJoinPool pool = new ForkJoinPool(16);

        pool.execute(judgeTask);
        pool.execute(choiceTask);
        pool.execute(fillingTask);
        pool.execute(shortAnswerTask);
        pool.execute(syntheticalTask);

        //pool.execute(textbookTask);

        pool.shutdown();
        try {
            pool.awaitTermination(1, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
