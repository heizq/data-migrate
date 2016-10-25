package cn.lamppa.aether.task;

import cn.lamppa.aether.domain.QuestionJudge;
import cn.lamppa.aether.service.QuestionJudgeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RecursiveTask;

/**
 * Created by Administrator on 16-3-29.
 **/
public class JudgeTask extends RecursiveTask<String> {

    private static Logger logger = LoggerFactory.getLogger(FillingTask.class);

    private QuestionJudgeService judgeService ;

    protected static int STHRESHOLD = 1000;

    // 需要处理的数据的范围
    private int start, end;

    public JudgeTask(QuestionJudgeService judgeService , int start, int end) {
        this.judgeService = judgeService;
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
            JudgeTask task1 = new JudgeTask(this.judgeService,start,mid);
            JudgeTask task2 = new JudgeTask(this.judgeService,mid+1,end);
            // 处理任务
            invokeAll(task1, task2);
            // 计算结果
            try {
                result = groupResults(task1.get(), task2.get());
            } catch (InterruptedException  e) {
                e.printStackTrace();
                logger.error(e.getMessage());
            }catch (ExecutionException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }

    private String processData(int start,int end){
            List<QuestionJudge> list=judgeService.findJudgeByPage(start, (end-start+1));
            String count = judgeService.transferData(list);
            return count;
    }

    private String groupResults(String result1,String result2){
        int  count = Integer.parseInt(result1) + Integer.parseInt(result2);
        return String.valueOf(count);
    }
}
