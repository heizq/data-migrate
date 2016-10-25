package cn.lamppa.aether.task;

import cn.lamppa.aether.domain.QuestionChoice;
import cn.lamppa.aether.service.PointAndChoiceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RecursiveTask;

/**
 * Created by Administrator on 16-3-29.
 */
public class ChoiceTask extends RecursiveTask<String> {

    private static Logger logger = LoggerFactory.getLogger(FillingTask.class);

    private PointAndChoiceService pointAndChoiceService ;

    protected static int STHRESHOLD = 1000;

    // 需要处理的数据的范围
    private int start, end;

    public ChoiceTask(PointAndChoiceService pointAndChoiceService, int start, int end) {
        this.pointAndChoiceService = pointAndChoiceService;
        this.start = start;
        this.end = end;
    }

    @Override
    protected String compute() {

        String result="";
        if(end - start <=STHRESHOLD){
             processData(start,end);
        }else{
            // 分隔任务
            int mid = (start + end) / 2;
            ChoiceTask task1 = new ChoiceTask(this.pointAndChoiceService,start,mid);
            ChoiceTask task2 = new ChoiceTask(this.pointAndChoiceService,mid+1,end);
            // 处理任务
            invokeAll(task1, task2);
            // 计算结果
            try {
                result = groupResults(task1.get(), task2.get());
            } catch (InterruptedException  e) {
                e.printStackTrace();
                logger.error(e.getMessage());
            }catch (ExecutionException  ex){
                ex.printStackTrace();
            }
        }
        return result;
    }

    private String processData(int start,int end){
        List<QuestionChoice> list = pointAndChoiceService.findQuestionByPage(start, (end-start+1));
        return pointAndChoiceService.transferData(list);

    }

    private String groupResults(String result1,String result2){
        int  count = Integer.parseInt(result1) + Integer.parseInt(result2);

        return String.valueOf(count);
    }

}
