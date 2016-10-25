package cn.lamppa.aether.service.impl;

import cn.lamppa.aether.cache.RedisCache;
import cn.lamppa.aether.domain.ImageData;
import cn.lamppa.aether.domain.QuestionTestPoint;
import cn.lamppa.aether.monogdao.MongoDao;
import cn.lamppa.aether.service.ImageDataService;
import cn.lamppa.aether.util.ImgFileUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Service;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by liupd on 16-2-25.
 **/
@Service
public class ImageDataServiceImpl implements ImageDataService {

    private static final Logger logger = LoggerFactory.getLogger(ImageDataServiceImpl.class);

    @Autowired
    private JdbcTemplate resJdbcTemplate;

    @Autowired
    private MongoDao mongoDao;

    @Autowired
    private RedisCache cache;

    @Override
    public void saveImageToMongo(String path,String fileName) {
        try {
            File file = new File(path);
            Pattern pattern = Pattern.compile(".*(Upload.*)");
            Matcher m = pattern.matcher(path);
            if(m.find()){
                path = m.group(1);
            }
            path = path.replaceAll("\\\\","/");
            if(getCount(path)<= 0){
                String id = mongoDao.save(file);
                addImgResource(path,id);
                ImgFileUtil.writeToSqlFile(id+"="+path );
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }

    }

    private int getCount(String path) {
        StringBuilder sql = new StringBuilder("SELECT  count(id)");
        sql.append(" FROM  img_resource  where path= '"+path+"'");
        return this.resJdbcTemplate.queryForObject(sql.toString(),Integer.class);
    }

    private int addImgResource(String path,String mongoId ) {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("id", UUID.randomUUID().toString().replace("-",""));
        params.put("path",path);
        params.put("mongoId",mongoId);

        SimpleJdbcInsert insert = new SimpleJdbcInsert(resJdbcTemplate);
        insert.withTableName("img_resource");
        int num =  insert.execute(params);
        return num;
    }

    @Override
    public void createTempTable()  throws Exception{
        String tableName = "img_resource";
        Connection conn = resJdbcTemplate.getDataSource().getConnection();
        DatabaseMetaData dbMetaData = conn.getMetaData();
        String[]   types   =   { "TABLE" };
        ResultSet tabs = dbMetaData.getTables(null, null, tableName, types);
        if (!tabs.next()) {
            StringBuffer sql = new StringBuffer();
            resJdbcTemplate.update(" DROP TABLE IF EXISTS `img_resource`;");
            sql.append(" CREATE TABLE `img_resource` (");
            sql.append(" `id` varchar(255) NOT NULL ,");
            sql.append(" `path`  varchar(255) NOT NULL  COMMENT '路径' ,");
            sql.append(" `mongoId` varchar(255) NOT NULL  COMMENT 'mongoId',");
            sql.append(" PRIMARY KEY (`id`)");
            sql.append(" ) CHARACTER SET=utf8 COLLATE=utf8_general_ci;");
            resJdbcTemplate.update(sql.toString());
        }
        tabs.close();
        conn.close();
    }

    @Override
    public List<ImageData> getImageDatas() {
        String cache_key = RedisCache.CACHE_NAME + "|getImageDatas|";
        List<ImageData> result_cache = cache.getListCache(cache_key, ImageData.class);
        if (result_cache == null) {
            // 缓存中没有再去数据库取，并插入缓存（缓存时间为60秒）
            result_cache = findImageDatas();
            cache.putListCacheWithExpireTime(cache_key, result_cache, RedisCache.CACHE_TIME);
            logger.info("put cache with key:" + cache_key);
            return result_cache;
        } else {
            logger.info("get cache with key:" + cache_key);
        }
        return result_cache;
    }


    public void imageDataPlay(List<ImageData> list_cache){
        for(ImageData item:list_cache){
            String key = item.getPath();
            String mongoId = cache.getCache(key,String.class);
            if(StringUtils.isEmpty(mongoId)){
                cache.putCacheWithExpireTime(key,item.getMongoId(),RedisCache.CACHE_TIME);
            }else{
                logger.info("get cache with key:" + key);
            }
        }
    }


    public String getMongoId(String path){
        return cache.getCache(path,String.class);
    }


    private List<ImageData> findImageDatas() {
        StringBuilder sql = new StringBuilder("");
        sql.append("SELECT  path, mongoId FROM  img_resource ");
        List<ImageData> list = this.resJdbcTemplate.query(
                sql.toString(),
                new Object[]{},
                new RowMapper<ImageData>() {
                    public ImageData mapRow(ResultSet rs, int rowNum) throws SQLException {
                        ImageData model = new ImageData();
                        model.setPath(rs.getString("path"));
                        model.setMongoId(rs.getString("mongoId"));
                        return model;
                    }
                });

        return list;
    }

}
