package cn.lamppa.aether.util;

import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertyResolver;
import org.springframework.core.env.PropertySourcesPropertyResolver;
import org.springframework.core.io.support.ResourcePropertySource;

import java.io.File;
import java.io.IOException;

/**
 * Created by Administrator on 2016/8/9 0009.
 */
public class PropertyConfigurer {

    public static final String PROJ_HOME = "${PROJ_HOME}";
    public static final String PROJ_TEST_HOME = "${PROJ_TEST_HOME}";
    public static final String PROJ_PUBLIC_HOME = "${PROJ_PUBLIC_HOME}";

    private  static final   String  FILE_PEX= "file:";

    PropertyResolver propertyResolver;

    MutablePropertySources propertySources;

    public PropertyConfigurer(String... configs) {
        try{
            propertySources = new MutablePropertySources();
            for(String str:configs){
                String fileconfig = "";
                ResourcePropertySource resourcePropertySource = null;
                if(str.contains(FILE_PEX)){
                    fileconfig=evnFilePathParser(str);
                    if(fileconfig != null && fileconfig != "")
                        resourcePropertySource = new ResourcePropertySource(str, fileconfig);
                    else
                        System.out.println("config file :"+str+" not exists");
                }else{
                    resourcePropertySource = new ResourcePropertySource(str, str);
                }

                if(resourcePropertySource != null)
                    propertySources.addFirst(resourcePropertySource);
            }
        }catch (IOException ex){
            ex.printStackTrace();
        }
        propertyResolver = new PropertySourcesPropertyResolver(propertySources);
    }

    public String getPropertyValue(String key) {
        return propertyResolver.getProperty(key);
    }


    private String evnFilePathParser(String config){
        String configpath = "";
        int startindex = config.indexOf("${");
        int endindex = config.indexOf('}');
        if(endindex<1)
            configpath = config;
        String evnKey = config.substring(startindex+2,endindex);
        String evnValue = System.getenv(evnKey);
        if(evnValue != null && evnValue != "")
            configpath= config.replace("${"+evnKey+'}',evnValue);
        else
            configpath = config;
        File file=new File(configpath.substring(configpath.indexOf(FILE_PEX)+5));
        if(file.exists())
            return configpath;
        else return null;

    }

}
