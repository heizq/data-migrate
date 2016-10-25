package cn.lamppa.aether.task;

import cn.lamppa.aether.domain.QuestionFilling;
import cn.lamppa.aether.service.QuestionFillingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RecursiveTask;

/**
 * Created by heizhiqiang on 2016/3/28 0028.
 */
public class FillingTask extends RecursiveTask<String> {

    private static Logger logger = LoggerFactory.getLogger(FillingTask.class);

    private  QuestionFillingService fillingService ;

    protected static int STHRESHOLD = 1000;

    // 需要处理的数据的范围
    private int start, end;

    public FillingTask(QuestionFillingService fillingService, int start, int end) {
        this.fillingService = fillingService;
        this.start = start;
        this.end = end;
    }

    @Override
    protected String compute() {

        String result="";
        if(end - start < STHRESHOLD){
            result = processData(start ,end);
        }else{
            // 分隔任务
            int mid = (start + end) / 2;
            FillingTask task1 = new FillingTask(this.fillingService,start,mid);
            FillingTask task2 = new FillingTask(this.fillingService,mid+1,end);

            // 处理任务
            invokeAll(task1, task2);
            // 计算结果
            try {
                result = groupResults(task1.get(), task2.get());
            } catch (InterruptedException |ExecutionException e) {
                e.printStackTrace();
                logger.error(e.getMessage());
            }
        }

        return result;
    }

    private String processData(int start,int end){

        List<QuestionFilling> list = fillingService.findQuestionByPage(start, (end-start+1));
        String count = fillingService.transferData(list);

        return count;
    }

    private String groupResults(String result1,String result2){
        int  count = Integer.parseInt(result1) + Integer.parseInt(result2);

        return String.valueOf(count);
    }


}

