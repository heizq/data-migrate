package cn.lamppa.aether.dao.impl;

import cn.lamppa.aether.dao.ChapterSectionDao;
import cn.lamppa.aether.domain.ChapterSection;
import cn.lamppa.aether.domain.ChapterSectionKnowledge;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by heizhiqiang on 2016/2/24 0024.
 */
@Configurable
@Repository(value="chapterSectionDao")
public class ChapterSectionDaoImpl implements ChapterSectionDao {

    @Resource
    private JdbcTemplate resJdbcTemplate;


    @Resource
    private JdbcTemplate platformJdbcTemplate;

    public String addChapterSection(ChapterSection model) {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("id",model.getId());
        params.put("name",model.getName());
        params.put("parent_id",model.getParentId());
        params.put("sort",model.getSort());
        params.put("special_type",model.getSpecialType());
        params.put("textbook_id",model.getTextbookId());
        params.put("update_time",model.getUpdateTime());
        params.put("update_user", model.getUpdateUser());

        SimpleJdbcInsert insert = new SimpleJdbcInsert(platformJdbcTemplate);
        insert.withTableName("ks_chapter_section");
        insert.execute(params);
        return model.getId();
    }

    public List<ChapterSection> findChapterSectionByTextbookAndParentId(ChapterSection model) {
        StringBuilder sql = new StringBuilder("");
        List preObject = new ArrayList();
        sql.append("select id, name, parent_id , special_type , textbook_id ,update_time, " +
                "update_user,sort from ks_chapter_section where textbook_id = ?");
        preObject.add(model.getTextbookId());
        if(StringUtils.isEmpty(model.getParentId())){
            sql.append(" and (parent_id is null or parent_id='')");
        }else {
            sql.append(" and parent_id = ?");
            preObject.add(model.getParentId());
        }
        sql.append(" order by sort ASC");
        List<ChapterSection> list = this.resJdbcTemplate.query(
                sql.toString(),
                preObject.toArray(),
                new RowMapper<ChapterSection>() {
                    public ChapterSection mapRow(ResultSet rs, int rowNum) throws SQLException {
                        ChapterSection model = new ChapterSection();
                        model.setId(rs.getString("id"));
                        model.setName(rs.getString("name"));
                        model.setParentId(rs.getString("parent_id"));
                        model.setSpecialType(rs.getString("special_type"));
                        model.setTextbookId(rs.getString("textbook_id"));
                        model.setUpdateTime(rs.getDate("update_time"));
                        model.setUpdateUser(rs.getString("update_user"));
                        model.setSort(rs.getInt("sort"));
                        return model;
                    }
                });

        return list;

    }

    public List<ChapterSectionKnowledge> findChapterSectionKnowledge(String chapterSectionId) {
        StringBuilder sql = new StringBuilder("");
        sql.append("SELECT  t.id , t.chapter_section_id,  t.knowledge_id, t.cognize_level_id,t.ability_id," +
                "t.sort,kp.name FROM ks_chapter_section_knowledge t LEFT JOIN ks_knowledge_point kp on kp.id = t.knowledge_id where   t.chapter_section_id = ?");
        List<ChapterSectionKnowledge> list = this.resJdbcTemplate.query(
                sql.toString(),
                new Object[]{chapterSectionId},
                new RowMapper<ChapterSectionKnowledge>() {
                    public ChapterSectionKnowledge mapRow(ResultSet rs, int rowNum) throws SQLException {
                        ChapterSectionKnowledge model = new ChapterSectionKnowledge();
                        model.setId(rs.getString("id"));
                        model.setChapterSectionId(rs.getString("chapter_section_id"));
                        model.setKnowledgeId(rs.getString("knowledge_id"));
                        model.setCognizeLevelId(rs.getString("cognize_level_id"));
                        model.setAbilityId(rs.getString("ability_id"));
                        model.setSort(rs.getInt("sort"));
                        model.setName(rs.getString("name"));
                        return model;
                    }
                });

        return list;
    }

    public String addChapterSectionKnowledge(ChapterSectionKnowledge model) {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("id",model.getId());
        params.put("ability_id",model.getAbilityId());
        params.put("chapter_section_id",model.getChapterSectionId());
        params.put("sort",model.getSort());
        params.put("cognize_level_id",model.getCognizeLevelId());
        params.put("knowledge_id",model.getKnowledgeId());

        SimpleJdbcInsert insert = new SimpleJdbcInsert(platformJdbcTemplate);
        insert.withTableName("ks_chapter_section_knowledge ");
        insert.execute(params);
        return model.getId();
    }

}
