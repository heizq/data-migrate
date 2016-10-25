package cn.lamppa.aether.service.impl;

import cn.lamppa.aether.dao.QuestionBaseDao;
import cn.lamppa.aether.dao.QuestionKnowledgeDao;
import cn.lamppa.aether.domain.*;
import cn.lamppa.aether.enums.QuestionAuditStatus;
import cn.lamppa.aether.enums.QuestionType;
import cn.lamppa.aether.enums.QuestionsTables;
import cn.lamppa.aether.service.ImageDataService;
import cn.lamppa.aether.service.PointAndChoiceService;
import cn.lamppa.aether.util.Constants;
import cn.lamppa.aether.util.HtmlStrUtil;
import cn.lamppa.aether.util.IdSequence;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sun.util.logging.resources.logging;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by liupd on 16-2-26.
 **/

@Configurable
@Repository(value="pointAndChoiceService")
public class PointAndChoiceServiceImpl implements PointAndChoiceService {

    private static Logger logger = LoggerFactory.getLogger(PointAndChoiceServiceImpl.class);

    public static final String choice="CHOICE";

    public static final String audit_status="APPROVED";

    public static final String status="SUBMITED";


    @Resource
    private QuestionBaseDao questionBaseDao;

    @Resource
    private QuestionKnowledgeDao questionKnowledgeDao;

    @Autowired
    private JdbcTemplate resJdbcTemplate;

    @Autowired
    private JdbcTemplate platformJdbcTemplate;

    @Resource
    private ImageDataService imageDataService;

    //@Transactional
    @Override
    public void createSynQuestion() throws Exception {
        String tableName = QuestionsTables.syn_table.toString();
        Connection conn = resJdbcTemplate.getDataSource().getConnection();
        DatabaseMetaData dbMetaData = conn.getMetaData();
        String[]   types   =   { "TABLE" };
        ResultSet tabs = dbMetaData.getTables(null, null, tableName, types);
        logger.info("create syn_table start..");
        if (!tabs.next()) {
            StringBuffer sql = new StringBuffer();
            sql.append(" CREATE TABLE `" + tableName + "` (");
            sql.append(" `id`  bigint(20) NOT NULL AUTO_INCREMENT ,");
            sql.append("`table_name`  varchar(255) NULL DEFAULT NULL COMMENT '表名称' ,");
            sql.append("`source_id`  varchar(255) NULL DEFAULT NULL COMMENT '源数据id',");
            sql.append("`desc_id`  varchar(255) NULL DEFAULT NULL COMMENT '目标数据id',");
            sql.append(" PRIMARY KEY (`id`)");
            sql.append(") CHARACTER SET=utf8 COLLATE=utf8_general_ci;");
            resJdbcTemplate.update(sql.toString());
            resJdbcTemplate.update(" insert into "+QuestionsTables.syn_table.name()+"(table_name,source_id,desc_id) values(?,?,?)",new Object[]{QuestionsTables.ks_knowledge_point.name(),"0","0"});
            resJdbcTemplate.update(" insert into "+QuestionsTables.syn_table.name()+"(table_name,source_id,desc_id) values(?,?,?)",new Object[]{QuestionsTables.ks_knowledge_point_relation.name(),"0","0"});
            resJdbcTemplate.update(" insert into "+QuestionsTables.syn_table.name()+"(table_name,source_id,desc_id) values(?,?,?)",new Object[]{QuestionsTables.ks_question_base.name(),"0","0"});
            resJdbcTemplate.update(" insert into "+QuestionsTables.syn_table.name()+"(table_name,source_id,desc_id) values(?,?,?)",new Object[]{QuestionsTables.ks_question_choice.name(),"0","0"});
            resJdbcTemplate.update(" insert into "+QuestionsTables.syn_table.name()+"(table_name,source_id,desc_id) values(?,?,?)",new Object[]{QuestionsTables.ks_question_choice_answer.name(),"0","0"});
            resJdbcTemplate.update(" insert into "+QuestionsTables.syn_table.name()+"(table_name,source_id,desc_id) values(?,?,?)",new Object[]{QuestionsTables.ks_question_knowledge.name(),"0","0"});
            resJdbcTemplate.update(" insert into "+QuestionsTables.syn_table.name()+"(table_name,source_id,desc_id) values(?,?,?)",new Object[]{QuestionsTables.ks_question_choice.name(),"0","0"});
            resJdbcTemplate.update(" insert into "+QuestionsTables.syn_table.name()+"(table_name,source_id,desc_id) values(?,?,?)",new Object[]{QuestionsTables.bd_ability.name(),"0","0"});
            resJdbcTemplate.update(" insert into "+QuestionsTables.syn_table.name()+"(table_name,source_id,desc_id) values(?,?,?)",new Object[]{QuestionsTables.bd_cognize_level.name(),"0","0"});
            logger.info("create syn_middle success!");
        }
        logger.info("syn_table exists");
        tabs.close();
        conn.close();
    }

    @Override
    public void createSynQuestionMiddleTable() throws Exception {
        String tableName = QuestionsTables.ks_question_flag.toString();
        Connection conn = platformJdbcTemplate.getDataSource().getConnection();
        DatabaseMetaData dbMetaData = conn.getMetaData();
        String[]   types   =   { "TABLE" };
        logger.info("create ks_question_flag start..");
        ResultSet tabs = dbMetaData.getTables(null, null, tableName, types);
        if (!tabs.next()) {
            StringBuffer sql = new StringBuffer();
            sql.append(" CREATE TABLE `" + tableName + "` (");
            sql.append(" `id`  bigint(20) NOT NULL AUTO_INCREMENT ,");
            sql.append("`question_id`  varchar(255) NULL DEFAULT NULL COMMENT '题id',");
            sql.append("`src_flag`  varchar(50) NULL DEFAULT NULL COMMENT '题来源标志说明',");
            sql.append("`data_bak`  varchar(50) NULL DEFAULT NULL COMMENT '预留字段备用',");
            sql.append(" PRIMARY KEY (`id`) ");
            sql.append(") CHARACTER SET=utf8 COLLATE=utf8_general_ci;");
            platformJdbcTemplate.update(sql.toString());
            logger.info("create ks_question_flag success!");
        }
        logger.info("syn_table exists");
        tabs.close();
        conn.close();
    }

    @Override
    public void synAbility() throws Exception {
        logger.info(".....bd_ability start import......");
        String srcSql="select source_id from syn_table where table_name='bd_ability' and id in(select max(id) from syn_table where table_name='bd_ability' )";
        String max_source_id = resJdbcTemplate.queryForObject(srcSql,String.class);
        Long start=System.currentTimeMillis();
        Integer i=0;
        List<Map<String,Object>> list=null;
        if(StringUtils.isNotBlank(max_source_id)&&!max_source_id.equals("0")){
            list = resJdbcTemplate.queryForList("select * from "+QuestionsTables.bd_ability.name()+" where id>? ",new Object[]{max_source_id});
        }else{
            list=resJdbcTemplate.queryForList("select * from "+QuestionsTables.bd_ability.name()+"");
        }
        System.out.println(list);
        if(list.size()>0){
            String errorIds="id contains";
            for(Map map:list){
                i++;
                String srcId=map.get("id").toString();
                String destId= IdSequence.nextId();
                logger.info("bd_ability.."+srcId+"正在导入");
                String name = map.get("name")==null?null:map.get("name").toString().trim();
                String phaseName=map.get("phases_business_key")==null?null:map.get("phases_business_key").toString().trim();
                String subjectName=map.get("subject_business_key")==null?null:map.get("subject_business_key").toString().trim();
                Date update_time =new Date();
                String phases_id=(String)platformJdbcTemplate.query("SELECT p.business_key FROM bd_phases p where p.name=?",new Object[]{phaseName},new ResultSetExtractor<Object>() {
                    @Override
                    public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
                        while(rs.next()){
                            return rs.getString("business_key");
                        }
                        return null;
                    }
                });
                String subjects_id=(String)platformJdbcTemplate.query("SELECT p.business_key FROM bd_subjects p where p.name=?",new Object[]{subjectName},new ResultSetExtractor<Object>() {
                    @Override
                    public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
                        while(rs.next()){
                            return rs.getString("business_key");
                        }
                        return null;
                    }
                });
                StringBuffer sql=new StringBuffer();
                sql.append(" insert into ").append(QuestionsTables.bd_ability.name()).append("  ");
                sql.append(" (id,version,business_key,create_time,is_delete,name,phases_business_key,subject_business_key,update_time) ");
                sql.append(" values(?,?,?,?,?,?,?,?,?)");
                Object[] param={destId,"0",destId,update_time,"N",name,phases_id,subjects_id,update_time};
                platformJdbcTemplate.update(sql.toString(), param);
                resJdbcTemplate.update(" insert into "+QuestionsTables.syn_table.name()+"(table_name,source_id,desc_id) values(?,?,?)",new Object[]{QuestionsTables.bd_ability.name(),srcId,destId});
                logger.info("bd_ability .."+srcId+"导入成功");
            }
        }
        logger.info("用时:"+(System.currentTimeMillis()-start));
    }

    @Override
    public  void synAbility2(int start,int end) throws Exception {
        //logger.info(".....bd_ability start import......");
        Long starts=System.currentTimeMillis();
        Integer i=0;
        List<Map<String,Object>> list=null;
        String sqlStr="select * from "+QuestionsTables.bd_ability.name()+ "  limit   "+start+"  ,  "+end+"";
        list=resJdbcTemplate.queryForList(sqlStr);
       /* System.out.println("sql:"+sqlStr);
        System.out.println("start:"+start+","+"end:"+end+"size():"+list.size());*/
        //System.out.println(list.size());
        if(list.size()>0){
            String errorIds="id contains";
            for(Map map:list){
                i++;
                String srcId=map.get("id").toString();
                String destId= IdSequence.nextId();
                //logger.info("bd_ability.."+srcId+"正在导入");
                String name = map.get("name")==null?null:map.get("name").toString().trim();
                String phaseName=map.get("phases_business_key")==null?null:map.get("phases_business_key").toString().trim();
                String subjectName=map.get("subject_business_key")==null?null:map.get("subject_business_key").toString().trim();
                Date update_time =new Date();
                String phases_id=(String)platformJdbcTemplate.query("SELECT p.business_key FROM bd_phases p where p.name=?",new Object[]{phaseName},new ResultSetExtractor<Object>() {
                    @Override
                    public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
                        while(rs.next()){
                            return rs.getString("business_key");
                        }
                        return null;
                    }
                });
                String subjects_id=(String)platformJdbcTemplate.query("SELECT p.business_key FROM bd_subjects p where p.name=?",new Object[]{subjectName},new ResultSetExtractor<Object>() {
                    @Override
                    public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
                        while(rs.next()){
                            return rs.getString("business_key");
                        }
                        return null;
                    }
                });
                StringBuffer sql=new StringBuffer();
                sql.append(" insert into ").append(QuestionsTables.bd_ability.name()).append("  ");
                sql.append(" (id,version,business_key,create_time,is_delete,name,phases_business_key,subject_business_key,update_time) ");
                sql.append(" values(?,?,?,?,?,?,?,?,?)");
                Object[] param={destId,"0",destId,update_time,"N",name,phases_id,subjects_id,update_time};
                platformJdbcTemplate.update(sql.toString(), param);
                //resJdbcTemplate.update(" insert into "+QuestionsTables.syn_table.name()+"(table_name,source_id,desc_id) values(?,?,?)",new Object[]{QuestionsTables.bd_ability.name(),srcId,destId});
                //logger.info("bd_ability .."+srcId+"导入成功");
            }
        }
        logger.info("用时:"+(System.currentTimeMillis()-starts));
    }

    @Override
    public void synCognizelevel() throws Exception {
        logger.info(".....bd_cognize_level start import......");
        String srcSql="select source_id from syn_table where table_name='bd_cognize_level' and id in(select max(id) from syn_table where table_name='bd_cognize_level' )";
        String max_source_id = resJdbcTemplate.queryForObject(srcSql,String.class);
        Long start=System.currentTimeMillis();
        Integer i=0;
        List<Map<String,Object>> list=null;
        if(StringUtils.isNotBlank(max_source_id)&&!max_source_id.equals("0")){
            list = resJdbcTemplate.queryForList("select * from "+QuestionsTables.bd_cognize_level.name()+" where id>? ",new Object[]{max_source_id});
        }else{
            list=resJdbcTemplate.queryForList("select * from "+QuestionsTables.bd_cognize_level.name()+"");
        }
        System.out.println(list);
        if(list.size()>0){
            String errorIds="id contains";
            for(Map map:list){
                i++;
                String srcId=map.get("id").toString();
                String destId= IdSequence.nextId();
                logger.info("bd_cognize_level.."+srcId+"正在导入");
                String name = map.get("name")==null?null:map.get("name").toString().trim();
                String phaseName=map.get("phases_business_key")==null?null:map.get("phases_business_key").toString().trim();
                String subjectName=map.get("subject_business_key")==null?null:map.get("subject_business_key").toString().trim();
                Date update_time =new Date();
                String phases_id=(String)platformJdbcTemplate.query("SELECT p.business_key FROM bd_phases p where p.name=?",new Object[]{phaseName},new ResultSetExtractor<Object>() {
                    @Override
                    public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
                        while(rs.next()){
                            return rs.getString("business_key");
                        }
                        return null;
                    }
                });
                String subjects_id=(String)platformJdbcTemplate.query("SELECT p.business_key FROM bd_subjects p where p.name=?",new Object[]{subjectName},new ResultSetExtractor<Object>() {
                    @Override
                    public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
                        while(rs.next()){
                            return rs.getString("business_key");
                        }
                        return null;
                    }
                });
                StringBuffer sql=new StringBuffer();
                sql.append(" insert into ").append(QuestionsTables.bd_cognize_level.name()).append("  ");
                sql.append(" (id,version,business_key,create_time,is_delete,name,phases_business_key,subject_business_key,update_time) ");
                sql.append(" values(?,?,?,?,?,?,?,?,?)");
                Object[] param={destId,"0",destId,update_time,"N",name,phases_id,subjects_id,update_time};
                platformJdbcTemplate.update(sql.toString(), param);
                resJdbcTemplate.update(" insert into "+QuestionsTables.syn_table.name()+"(table_name,source_id,desc_id) values(?,?,?)",new Object[]{QuestionsTables.bd_cognize_level.name(),srcId,destId});
                logger.info("bd_cognize_level .."+srcId+"导入成功");
            }
        }
        logger.info("用时:"+(System.currentTimeMillis()-start));
    }

    public String getTableNameMaxId(String tableName) {
        StringBuilder sql = new StringBuilder("");
        sql.append(" select source_id from syn_table where table_name='"+tableName+"' and id in(select max(id) from syn_table where table_name='"+tableName+"' )");
        List<String> list = this.resJdbcTemplate.query(sql.toString(), new RowMapper<String>() {
            public String mapRow(ResultSet rs, int rowNum)
                    throws SQLException {
                return rs.getString(1);
            }
        });
        if (list != null && list.size() > 0) {
            return list.get(0);
        }else {
            return null;
        }
    }

    @Override
    public void synKnowledge() throws Exception {
        logger.info("Knowledge point start import");
        List<Map<String,Object>> list= resJdbcTemplate.queryForList("select * from ks_knowledge_point ");
        if(list.size()>0){
            for(Map map:list){
                String srcId=map.get("id").toString();
                String destId= IdSequence.nextId();
                logger.info("ks_knowledge_point.."+srcId+"正在导入");
                String name = map.get("name")==null?null:map.get("name").toString().trim();
                String phaseName=map.get("phase_name")==null?null:map.get("phase_name").toString().trim();
                String subjectName=map.get("subject_name")==null?null:map.get("subject_name").toString().trim();
                String instruction = map.get("instruction")==null?null:(String)map.get("instruction");
                String remark = map.get("remark")==null?null:map.get("remark").toString();
                Date update_time =new Date();
                String phases_id=(String)platformJdbcTemplate.query("SELECT p.business_key FROM bd_phases p where p.name=?",new Object[]{phaseName},new ResultSetExtractor<Object>() {
                        @Override
                        public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
                            while(rs.next()){
                                return rs.getString("business_key");
                            }
                            return null;
                        }
                    });
                String subjects_id=(String)platformJdbcTemplate.query("SELECT p.business_key FROM bd_subjects p where p.name=?",new Object[]{subjectName},new ResultSetExtractor<Object>() {
                    @Override
                    public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
                        while(rs.next()){
                            return rs.getString("business_key");
                        }
                        return null;
                    }
                });
                StringBuffer sql=new StringBuffer();
                sql.append(" insert into ").append(QuestionsTables.ks_knowledge_point.name()).append("  ");
                sql.append(" (id,name,subject_id,subject_name,phase_id,phase_name,instruction,remark,update_time) ");
                sql.append(" values(?,?,?,?,?,?,?,?,?)");
                Object[] param={destId,name,subjects_id,subjectName,phases_id,phaseName,instruction,remark,update_time};
                platformJdbcTemplate.update(sql.toString(), param);
                logger.info("ks_knowledge_point .."+srcId+"导入成功");
            }
        }
    }

    @Override
    public void synKnowledgeRelation() throws Exception {

        logger.info("Knowledge_point_relation start import");
        List<Map<String,Object>> list= resJdbcTemplate.queryForList("select * from "+QuestionsTables.ks_knowledge_point_relation.name()+" where parent_id!=0 ");
        if(list.size()>0){
            String errorIds="id contains";
            for(Map map:list){
                String srcId=map.get("id").toString();
                String destId= IdSequence.nextId();
                logger.info("ks_knowledge_point_relation.."+srcId+"正在导入");
                String knowledgeId = map.get("knowledge_id")==null?null:map.get("knowledge_id").toString().trim();
                String parentId=map.get("parent_id")==null?null:map.get("parent_id").toString().trim();
                StringBuffer sql=new StringBuffer();
                sql.append(" insert into ").append(QuestionsTables.ks_knowledge_point_relation.name()).append("  ");
                sql.append(" (id,knowledge_id,parent_id) ");
                sql.append(" values(?,?,?)");
                Object[] param={destId,knowledgeId,parentId};
                platformJdbcTemplate.update(sql.toString(), param);
                logger.info("ks_knowledge_point .."+srcId+"导入成功");
            }
        }
    }




    public String transferData(List<QuestionChoice> resList)  {
        String errerId = "";
        int successCount = 0;
        try{
            for(QuestionChoice res:resList){
                errerId = res.getId();
                System.out.println("start import choice id="+res.getId());

                QuestionBase base = questionBaseDao.findQuestionBase(res.getBaseId());
                QuestionKnowledge major = questionKnowledgeDao.findMajorKnowledge(res.getBaseId());
                List<QuestionKnowledge> minor = questionKnowledgeDao.findMinorKnowledges(res.getBaseId());
                List<QuestionChoiceAnswer> answers = findQuestionAnswer(res.getId());

                if(base == null){
                    logger.error("error on question No={} 缺少 base 信息",errerId);
                }else{
                    base.setType(QuestionType.CHOICE.toString());
                    base.setAuditStatus(QuestionAuditStatus.APPROVED.toString());
                    base.setCreateTime(new Date());
                    base.setCreateUserId(Constants.user_key);
                    base.setCreateUserName(Constants.user_name);
                    questionBaseDao.addQuestionBase(base);
                }
                if(major == null){
                    logger.error("error on question No={} 缺少 knowledge 信息",errerId);
                }else{
                    major.setType(QuestionType.CHOICE.toString());
                    questionKnowledgeDao.addQuestionKnowledge(major);
                }
                for(QuestionKnowledge v:minor){
                    v.setType(QuestionType.CHOICE.toString());
                    questionKnowledgeDao.addQuestionKnowledge(v);
                }

                if(answers == null){
                    logger.error("error on question No={} 缺少 answer 信息",errerId);
                }else{
                    for(QuestionChoiceAnswer v: answers){
                        v.setChoiceId(res.getBaseId());
                        List<String> imgPath3 = HtmlStrUtil.getImgSrc(v.getTopic());
                        String str3=v.getTopic();
                        for(String s:imgPath3){
                            if(imageDataService.getMongoId(s) != null){
                                str3 = str3.replace(s, Constants.MONGO_PREFIX+imageDataService.getMongoId(s));
                            }

                        }
                        v.setTopic(str3);

                        addQuestionAnswer(v);
                    }
                }

                List<String> imgPath = HtmlStrUtil.getImgSrc(res.getTopic());
                String str=res.getTopic();
                for(String s:imgPath){
                    if(imageDataService.getMongoId(s) != null){
                        str = str.replace(s, Constants.MONGO_PREFIX+imageDataService.getMongoId(s));
                    }
                }
                res.setTopic(str);

                List<String> imgPath2 = HtmlStrUtil.getImgSrc(res.getAnalysis());
                String str2=res.getAnalysis();
                for(String s:imgPath2){
                    if(imageDataService.getMongoId(s) != null){
                        str2 = str2.replace(s, Constants.MONGO_PREFIX+imageDataService.getMongoId(s));
                    }

                }
                res.setAnalysis(str2);

                res.setId(res.getBaseId());
                addQuestion(res);
                successCount++;
            }
        }catch (Exception e){
            logger.error("error on question No={} ",errerId);
            logger.error(e.getMessage());

        }
        return String.valueOf(successCount);
    }

    public void syncItemQuestion(String questionId) {
        logger.info("start transfer filling item  ");
        List<QuestionChoice> list = findQuestionChoice(questionId,true);

        if(list != null && list.size() > 0){
            transferItemData(list);
        }else {
            logger.error("Synthetical filling item  id={} not found",questionId);
        }
    }

    private void transferItemData(List<QuestionChoice> resList){
        try{
            QuestionChoice res = resList.get(0);
            List<QuestionChoiceAnswer> answers = findQuestionAnswer(res.getId());

            for(QuestionChoiceAnswer v: answers){
                v.setChoiceId(res.getBaseId());
                List<String> imgPath3 = HtmlStrUtil.getImgSrc(v.getTopic());
                String str3=v.getTopic();
                for(String s:imgPath3){
                    if(imageDataService.getMongoId(s) != null){
                        str3 = str3.replace(s, Constants.MONGO_PREFIX+imageDataService.getMongoId(s));
                    }
                }
                v.setTopic(str3);

                addQuestionAnswer(v);
            }


            List<String> imgPath = HtmlStrUtil.getImgSrc(res.getTopic());
            String str=res.getTopic();
            for(String s:imgPath){
                if(imageDataService.getMongoId(s) != null){
                    str = str.replace(s, Constants.MONGO_PREFIX+imageDataService.getMongoId(s));
                }
            }
            res.setTopic(str);

            List<String> imgPath2 = HtmlStrUtil.getImgSrc(res.getAnalysis());
            String str2=res.getAnalysis();
            for(String s:imgPath2){
                if(imageDataService.getMongoId(s) != null){
                    str2 = str2.replace(s, Constants.MONGO_PREFIX+imageDataService.getMongoId(s));
                }
            }
            res.setAnalysis(str2);
            res.setId(res.getBaseId());
            addQuestion(res);

        }catch (Exception e){
            logger.error(e.getMessage());
        }
    }


    private List<QuestionChoiceAnswer> findQuestionAnswer(String questionId) {
        StringBuilder sql = new StringBuilder("");
        sql.append("SELECT  id, topic, sort, correct_answer,choice_id ");
        sql.append(" from ks_question_choice_answer where choice_id=?");
        List<QuestionChoiceAnswer> list = this.resJdbcTemplate.query(
                sql.toString(),
                new Object[]{questionId},
                new RowMapper<QuestionChoiceAnswer>() {
                    public QuestionChoiceAnswer mapRow(ResultSet rs, int rowNum) throws SQLException {
                        QuestionChoiceAnswer model = new QuestionChoiceAnswer();
                        model.setId(rs.getString("id"));
                        model.setChoiceId(rs.getString("choice_id"));
                        model.setSort(rs.getInt("sort"));
                        model.setTopic(rs.getString("topic"));
                        model.setIsCorrectAnswer(rs.getBoolean("correct_answer"));
                        return model;
                    }
                });

        return list;
    }

    private String addQuestion(QuestionChoice model){
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("id",model.getId());
        params.put("topic",model.getTopic());
        params.put("analysis",model.getAnalysis());
        params.put("base_id",model.getBaseId());
        params.put("is_small",model.getIsSmall());
        params.put("is_multi",model.getIsMulti());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(platformJdbcTemplate);

        insert.withTableName("ks_question_choice");
        insert.execute(params);
        return model.getId();
    }

    private String addQuestionAnswer(QuestionChoiceAnswer model) {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("id",model.getId());
        params.put("choice_id",model.getChoiceId());
        params.put("topic",model.getTopic());
        params.put("sort",model.getSort());
        params.put("correct_answer",model.getIsCorrectAnswer());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(platformJdbcTemplate);

        insert.withTableName("ks_question_choice_answer");
        insert.execute(params);
        return model.getId();
    }

    public int getCount() {
        StringBuilder sql = new StringBuilder("");
        sql.append("SELECT  count(1) ");
        sql.append(" FROM  ks_question_choice  where is_small= false");
        return this.resJdbcTemplate.queryForObject(sql.toString(),Integer.class);
    }

    @Override
    public List<QuestionChoice> findQuestionByPage(int start, int end) {
        StringBuilder sql = new StringBuilder("");
        sql.append("SELECT  id,  topic,  analysis,  base_id,  is_small,is_multi ");
        sql.append(" FROM  ks_question_choice where is_small = false  limit ?,?");

        List<QuestionChoice> list = this.resJdbcTemplate.query(
                sql.toString(),
                new Object[]{start,end},
                new RowMapper<QuestionChoice>() {
                    public QuestionChoice mapRow(ResultSet rs, int rowNum) throws SQLException {
                        QuestionChoice model = new QuestionChoice();
                        model.setId(rs.getString("id"));
                        model.setTopic(rs.getString("topic"));
                        model.setAnalysis(rs.getString("analysis"));
                        model.setBaseId(rs.getString("base_id"));
                        model.setIsSmall(rs.getBoolean("is_small"));
                        model.setIsMulti(rs.getBoolean("is_multi"));
                        return model;
                    }
                });

        return list;
    }

    public List<QuestionChoice> findQuestionChoice(String questionId,Boolean isSmall) {
        StringBuilder sql = new StringBuilder("");
        List<Object> params = new ArrayList<Object>();
        params.add(isSmall);
        sql.append("SELECT  id,  topic,  analysis,  base_id,  is_small,is_multi ");
        sql.append(" FROM  ks_question_choice  where is_small=?");

        if(StringUtils.isNotEmpty(questionId)){
            sql.append(" and base_id=?");
            params.add(questionId);
        }
        List<QuestionChoice> list = this.resJdbcTemplate.query(
                sql.toString(),
                params.toArray(),
                new RowMapper<QuestionChoice>() {
                    public QuestionChoice mapRow(ResultSet rs, int rowNum) throws SQLException {
                        QuestionChoice model = new QuestionChoice();
                        model.setId(rs.getString("id"));
                        model.setTopic(rs.getString("topic"));
                        model.setAnalysis(rs.getString("analysis"));
                        model.setBaseId(rs.getString("base_id"));
                        model.setIsSmall(rs.getBoolean("is_small"));
                        model.setIsMulti(rs.getBoolean("is_multi"));

                        return model;
                    }
                });

        return list;
    }

}
