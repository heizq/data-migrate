package cn.lamppa.aether.task;

import cn.lamppa.aether.service.ImageDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

/**
 * Created by Administrator on 2016/8/31 0031.
 */
public class ImageTask extends RecursiveTask<List<String>> {

    private static Logger logger = LoggerFactory.getLogger(ImageTask.class);

    private ImageDataService imageDataService ;

    protected static int STHRESHOLD = 500;

    private String path;

    private String extension;

    public ImageTask(String path, String extension) {
        this.path = path;
        this.extension = extension;
    }

    public ImageTask(ImageDataService imageDataService, String path, String extension) {
        this.imageDataService = imageDataService;
        this.path = path;
        this.extension = extension;
    }

    @Override
    protected List<String> compute() {
        List<String> list = new ArrayList<>();
        List<ImageTask> tasks = new ArrayList<>();

        File file = new File(path);
        File[] content = file.listFiles();
        if (content != null) {
            for (int i = 0; i < content.length; i++) {
                if (content[i].isDirectory()) {
                    ImageTask task = new ImageTask(imageDataService,content[i].getAbsolutePath(), extension);
                    task.fork();
                    tasks.add(task);
                } else {
                    if (checkFile(content[i].getName())) {
                        //list.add(content[i].getAbsolutePath());
                        imageDataService.saveImageToMongo(content[i].getAbsolutePath(),content[i].getName());
                    }
                }
            }
            if (tasks.size() > 50) {
                System.out.printf("%s: %d tasks ran.\n", file.getAbsolutePath(), tasks.size());
            }

            addResultsFromTasks(list, tasks);
        }

        return list;
    }


    private void addResultsFromTasks(List<String> list, List<ImageTask> tasks) {
        for (ImageTask item : tasks) {
            list.addAll(item.join());
        }
    }

    private boolean checkFile(String name) {
        String[] extensions = extension.split(";");
        for(String s:extensions){
            if (name.toLowerCase().endsWith(s)) {
                return true;
            }
        }
        return false;
    }


}
