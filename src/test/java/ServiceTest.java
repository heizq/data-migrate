import cn.lamppa.aether.domain.ImageData;
import cn.lamppa.aether.service.ImageDataService;
import cn.lamppa.aether.util.Constants;
import junit.framework.TestCase;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/9/2 0002.
 */
public class ServiceTest extends TestCase {

    private ImageDataService imageDataService;

    protected void setUp(){
        ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:application-context.xml");
        //questionFillingService = (QuestionFillingService) ctx.getBean("questionFillingService");
        imageDataService = ctx.getBean(ImageDataService.class);
    }

    @Test
    public void testCreateSyn()  throws Exception{
        List<ImageData> list =  imageDataService.getImageDatas();
        System.out.println(list.size());

    }

    @Test
    public void testrewr()  throws Exception{


        String id = imageDataService.getMongoId("Upload/2016/09/21/14/2016092114304187464_ST/2016092114304187464_ST.001.jpeg");
        System.out.println(id);
    }

    @Test
    public void testewr()  throws Exception{
        String str = "<p>Wh<img src=\"Upload/2016/08/20/21/2016082021393545748_ST/2016082021393545748_ST.001.png\" width=\"2\" height=\"2\"  />at are his <img src=\"Upload/2016/08/20/21/2016082021393545748_ST/2016082021393545748_ST.002.png\" width=\"3\" height=\"2\"  />________？</p><p>kjj<img src=\"Upload/2016/08/20/21/2016082021393545748_ST/2016082021393545748_ST.001.png\" width=\"2\" height=\"2\"  />at";

        List<String> list = getImgSrc(str);

        for(String s:list){
            str = str.replace(s, Constants.MONGO_PREFIX+"57c7e8e7306c00833ceeae34");
        }

//        for(String s:list){
//            str = str.replace(s, null);
//        }

        System.out.println(str);
        //57c7e8e7306c00833ceeae34
       // String id = imageDataService.getMongoId("Upload/2016/08/20/21/2016082021390401071_ST/2016082021390401071_ST.001.png");
       // System.out.println(id);
    }


    public  List<String> getImgSrc(String htmlStr) {
        String img = "";
        Pattern p_image;
        Matcher m_image;
        List<String> pics = new ArrayList<String>();
        String regEx_img = ".*<img.*src\\s*=\\s*(.*?)[^>]*?>";
        p_image = Pattern.compile(regEx_img, Pattern.CASE_INSENSITIVE);
        m_image = p_image.matcher(htmlStr);
        while (m_image.find()) {
            img = img + "," + m_image.group();
            Matcher m = Pattern.compile("src\\s*=\\s*\"?(.*?)(\"|>|\\s+)").matcher(img);
            while (m.find()) {
                pics.add(m.group(1));
            }
        }
        return pics;
    }
 }
