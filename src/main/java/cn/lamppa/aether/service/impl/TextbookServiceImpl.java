package cn.lamppa.aether.service.impl;


import cn.lamppa.aether.dao.ChapterSectionDao;
import cn.lamppa.aether.dao.CommonDao;
import cn.lamppa.aether.dao.TextbookDao;
import cn.lamppa.aether.domain.ChapterSection;
import cn.lamppa.aether.domain.ChapterSectionKnowledge;
import cn.lamppa.aether.domain.Textbook;
import cn.lamppa.aether.service.ChapterSectionService;
import cn.lamppa.aether.service.TextbookService;
import cn.lamppa.aether.util.Constants;
import cn.lamppa.aether.util.IdSequence;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Administrator on 2016/2/25.
 */
@Service
public class TextbookServiceImpl implements TextbookService {

    private static Logger logger = LoggerFactory.getLogger(TextbookServiceImpl.class);
    @Resource
    private TextbookDao textbookDao;

    @Resource
    private ChapterSectionService chapterSectionService;

    @Resource
    private ChapterSectionDao chapterSectionDao;

    @Resource
    private CommonDao commonDao;

    @Autowired
    private JdbcTemplate resJdbcTemplate;

    @Autowired
    private JdbcTemplate platformJdbcTemplate;

    // 控制访问队列的锁
    //private final Lock lock = new ReentrantLock();

    public void addTextbook() {

        List<Textbook> list = textbookDao.getTextBook();
        transferData(list);
    }


    public List<Textbook> getTextBook(){
        return textbookDao.getTextBook();
    }

    @Override
    public String transferData(List<Textbook> list) {
        int successCount = 0;
        String errerId = "";
        try{
            for(Textbook book:list){
                errerId = book.getId();
                String bId = IdSequence.nextId();
                logger.info("start old textbook id={} and new id={}",book.getId(),bId);
                List<ChapterSection> chapterSectionList = chapterSectionService.findChapterSectionByTextbook(book.getId());
                if(null != chapterSectionList && chapterSectionList.size() > 0) {
                    logger.info("ChapterSection size={}",chapterSectionList.size());
                    addChapterSection(chapterSectionList, Constants.user_key, bId, null);
                }

                book.setId(bId);

                String phases_id=(String)platformJdbcTemplate.query("SELECT p.business_key FROM bd_phases p where p.name=?",new Object[]{book.getPhaseName()},new ResultSetExtractor<Object>() {
                    @Override
                    public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
                        while(rs.next()){
                            return rs.getString("business_key");
                        }
                        return null;
                    }
                });
                book.setPhaseId(phases_id);
                String subjects_id=(String)platformJdbcTemplate.query("SELECT p.business_key FROM bd_subjects p where p.name=?",new Object[]{book.getSubjectName()},new ResultSetExtractor<Object>() {
                    @Override
                    public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
                        while(rs.next()){
                            return rs.getString("business_key");
                        }
                        return null;
                    }
                });
                book.setSubjectId(subjects_id);


                String gradeId = getGradeId(book.getGradeName());
                book.setGradeId(gradeId);

                String textbookVersionId = getTextbookVersionId(book.getTextbookVersionName());
                if(StringUtils.isEmpty(textbookVersionId)){
                    String newId = addPress(book.getTextbookVersionName());
                    book.setTextbookVersionId(newId);
                }else {
                    book.setTextbookVersionId(textbookVersionId);
                }
                String fasciculeId = getFasciculeId(book.getFasciculeName(),gradeId,subjects_id,phases_id,book.getTextbookVersionId());
                if(StringUtils.isEmpty(fasciculeId)){
                    String newId = addFascicule(book.getFasciculeName(),gradeId,subjects_id,phases_id,textbookVersionId);
                    book.setFasciculeId(newId);
                }else{
                    book.setFasciculeId(fasciculeId);
                }

                book.setStatus("1");
                book.setCreateTime(new Date());
                book.setCreateUser(Constants.user_key);
                textbookDao.addTextbook(book);

                successCount += 1;
                logger.info("end  textbook id={} ",bId);
            }

        }catch (Exception e){
            e.printStackTrace();
            logger.error("error in textbook id={}",errerId);
            logger.error(e.getMessage());
        }
        return  String.valueOf(successCount);
    }

    @Override
    public List<Textbook> findTextbookByPage(int start, int end) {
        StringBuilder sql = new StringBuilder("");
        sql.append(" select id,subject_id,subject_name,grade_id,grade_name,phase_id,phase_name,textbook_version_id,textbook_version_name,");
        sql.append(" fascicule_id,fascicule_name,publish_time,cover,status,create_time,create_user from ks_textbook  limit ?,?");
        List<Textbook> textbookList = this.resJdbcTemplate.query(
                sql.toString(),
                new Object[]{start,end},
                new RowMapper<Textbook>() {
                    public Textbook mapRow(ResultSet rs, int rowNum) throws SQLException {
                        Textbook model = new Textbook();
                        model.setId(rs.getString("id"));
                        model.setSubjectId(rs.getString("subject_id"));
                        model.setSubjectName(rs.getString("subject_name"));
                        model.setGradeId(rs.getString("grade_id"));
                        model.setGradeName(rs.getString("grade_name"));
                        model.setPhaseId(rs.getString("phase_id"));
                        model.setPhaseName(rs.getString("phase_name"));
                        model.setTextbookVersionId(rs.getString("textbook_version_id"));
                        model.setTextbookVersionName(rs.getString("textbook_version_name"));
                        model.setFasciculeId(rs.getString("fascicule_id"));
                        model.setFasciculeName(rs.getString("fascicule_name"));
                        model.setPublishTime(rs.getDate("publish_time"));
                        model.setCover(rs.getString("cover"));
                        model.setStatus(rs.getString("status"));
                        model.setCreateTime(rs.getDate("create_time"));
                        model.setCreateUser(rs.getString("create_user"));
                        return model;
                    }
                });

        return textbookList;
    }

    public String getTextbookVersionId(String name) {
        StringBuilder sql = new StringBuilder("");
        sql.append("SELECT  business_key");
        sql.append(" FROM  bd_textbook_version   where name = '"+name+"'");
        List<String> list = this.platformJdbcTemplate.query(sql.toString(), new RowMapper<String>() {
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

    public String getFasciculeId(String name,String gradeId,String subjectId,String phaseId,String textbookVersionId) {
        StringBuilder sql = new StringBuilder("");
        sql.append("SELECT  business_key");
        sql.append(" FROM  bd_fascicule   where fascicule = '"+name+"' and grade_business_key='" +gradeId+"'" );
        sql.append(" and phases_business_key='" +phaseId+"' and subject_business_key='" +subjectId+"' and text_book_version_business_key='"+textbookVersionId+"'");
        List<String> list = this.platformJdbcTemplate.query(sql.toString(), new RowMapper<String>() {
            public String mapRow(ResultSet rs, int rowNum)
                    throws SQLException {
                return rs.getString(1);
            }
        });

        if(list!=null &&list.size()>0){
            return list.get(0);
        }else {
            return null;
        }
    }

    public String addPress(String name){
        String id = IdSequence.nextId();
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("name",name);
        params.put("version",0);
        params.put("business_key",id);
        params.put("create_time",new  Date());
        params.put("is_delete","N");
        params.put("update_time",new  Date());

        SimpleJdbcInsert insert = new SimpleJdbcInsert(platformJdbcTemplate)
                .withTableName("bd_textbook_version")
                .usingGeneratedKeyColumns("id");
        insert.executeAndReturnKey(params);
        return id;
    }

    public String addFascicule(String name,String gradeId,String subjectId,String phaseId,String textbookVersionId){
        String id = IdSequence.nextId();
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("name",name);
        params.put("version",0);
        params.put("business_key",id);
        params.put("fascicule",name);
        params.put("grade_business_key",gradeId);
        params.put("phases_business_key",phaseId);
        params.put("subject_business_key",subjectId);
        params.put("text_book_version_business_key",textbookVersionId);
        params.put("create_time",new  Date());
        params.put("is_delete","N");
        params.put("update_time",new  Date());

        SimpleJdbcInsert insert = new SimpleJdbcInsert(platformJdbcTemplate)
                .withTableName("bd_fascicule")
                .usingGeneratedKeyColumns("id");
        insert.executeAndReturnKey(params);
        return id;
    }

    public String getGradeId(String name){
        StringBuilder sql = new StringBuilder("");
        sql.append("SELECT  business_key ");
        sql.append(" FROM  bd_base_grade  where name= '"+name+"'");

        return this.platformJdbcTemplate.queryForObject(sql.toString(),String.class);
    }

    public  void addChapterSection(List<ChapterSection> chapterSectionList,String updateUser,String textbookId,String parentId){
        for(ChapterSection chapterSection:chapterSectionList){
            String chapterSectionId = IdSequence.nextId();

            chapterSection.setParentId(parentId);
            chapterSection.setName(chapterSection.getName());
            if(StringUtils.isNotBlank(chapterSection.getSpecialType())){
                chapterSection.setSpecialType("1");
            }else{
                chapterSection.setSpecialType("0");
            }
            chapterSection.setTextbookId(textbookId);
            chapterSection.setSort(chapterSection.getSort());
            chapterSection.setUpdateTime(new Date());
            chapterSection.setUpdateUser(updateUser);
            chapterSection.setId(chapterSectionId);

            logger.info("start old ChapterSection id={} and new id={}",chapterSection.getId(),chapterSectionId);
            chapterSectionDao.addChapterSection(chapterSection);

            List<ChapterSectionKnowledge> knowledgeList=chapterSection.getKnowledge();
            for(ChapterSectionKnowledge knowledge:knowledgeList){
                String knowledgeId = IdSequence.nextId();
                knowledge.setId(knowledgeId);
                knowledge.setChapterSectionId(chapterSectionId);
                String knowId = knowledge.getKnowledgeId();
                if(null == knowId){
                    logger.error("chapter id={} 没有knowledge_id",chapterSectionId);

                }else{
                    String knoId = commonDao.getKnowledgeId(knowId);
                    if(StringUtils.isEmpty(knoId) ){
                        logger.error("chapter id={} 的 knowledge_id={} 在知识点中对不上",chapterSectionId,knowId);

                    }else {
                        knowledge.setKnowledgeId(knoId);
                    }
                }
                knowledge.setAbilityId(commonDao.getAbliity(knowledge.getAbilityId()));
                knowledge.setCognizeLevelId(commonDao.getCognizeLevel(knowledge.getCognizeLevelId()));
                chapterSectionDao.addChapterSectionKnowledge(knowledge);

            }

            List<ChapterSection> childChapterSectionList=chapterSection.getChildren();
            if(childChapterSectionList.size()>0){
                this.addChapterSection(childChapterSectionList, updateUser, textbookId, chapterSectionId);
            }

        }
    }
}
