package cn.lamppa.aether.util;


/**
 * Created by Administrator on 2016/8/9 0009.
 */
public class PropertyConfigUtil {

    private static PropertyConfigurer configurer=new PropertyConfigurer("classpath:resources.properties",
            "file:"+PropertyConfigurer.PROJ_TEST_HOME+"/edu_platform/application-config-test.properties",
            "file:"+PropertyConfigurer.PROJ_HOME+"/edu_platform/application-config-production.properties",
            "file:"+PropertyConfigurer.PROJ_PUBLIC_HOME+"/edu_platform/application-config-public.properties");


    public static String getPropertyValue(String key){
        return  configurer.getPropertyValue(key);
    }


}
