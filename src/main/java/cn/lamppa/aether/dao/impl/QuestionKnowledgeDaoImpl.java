package cn.lamppa.aether.dao.impl;

import cn.lamppa.aether.dao.QuestionKnowledgeDao;
import cn.lamppa.aether.domain.QuestionKnowledge;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by heizhiqiang on 2016/2/24 0024.
 */
@Configurable
@Repository(value="questionKnowledgeDao")
public class QuestionKnowledgeDaoImpl implements QuestionKnowledgeDao {

    @Resource
    private JdbcTemplate resJdbcTemplate;

    @Resource
    private JdbcTemplate platformJdbcTemplate;

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    public int addQuestionKnowledge(QuestionKnowledge model) {

        Map<String,Object> params = new HashMap<String,Object>();
        params.put("id",model.getId());
        params.put("question_id",model.getQuestionId());
        params.put("type",model.getType());
        params.put("knowledge_id",model.getKnowledgeId());
        params.put("is_main",model.getIsMain());
        params.put("cognize_level_id",model.getCognizeLevelId());
        params.put("ability_id",model.getAbilityId());
        params.put("sort",model.getSort());

        SimpleJdbcInsert insert = new SimpleJdbcInsert(platformJdbcTemplate);
        insert.withTableName("ks_question_knowledge");
        int num =  insert.execute(params);

        return num;
    }

    public int[] addQuestionKnowledge(List<QuestionKnowledge> list) {
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(platformJdbcTemplate);
        SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(list.toArray());

        StringBuilder sql = new StringBuilder("");
        sql.append("INSERT INTO ks_question_knowledge (id,question_id,type,knowledge_id,is_main,cognize_level_id,ability_id,sort) VALUES");
        sql.append("(:id,:questionId,:type,:knowledgeId,:isMain,:cognizeLevelId,:abilityId,:sort)");
        int[] updateCounts = namedParameterJdbcTemplate.batchUpdate(sql.toString(),batch);
        return updateCounts;
    }

    public QuestionKnowledge findMajorKnowledge(String questionId) {
        StringBuilder sql = new StringBuilder("");
        sql.append("SELECT  id, question_id, TYPE, knowledge_id, is_main, cognize_level_id, ability_id, sort ");
        sql.append(" FROM  ks_question_knowledge   where question_id = ? and is_main = true");
        List<QuestionKnowledge> list = this.resJdbcTemplate.query(
                sql.toString(),
                new Object[]{questionId},
                new RowMapper<QuestionKnowledge>() {
                    public QuestionKnowledge mapRow(ResultSet rs, int rowNum) throws SQLException {
                        QuestionKnowledge model = new QuestionKnowledge();
                        model.setId(rs.getString("id"));
                        model.setQuestionId(rs.getString("question_id"));
                        model.setType(rs.getString("type"));
                        model.setKnowledgeId(rs.getString("knowledge_id"));
                        model.setIsMain(rs.getBoolean("is_main"));
                        model.setCognizeLevelId(rs.getString("cognize_level_id"));
                        model.setAbilityId(rs.getString("ability_id"));
                        model.setSort(rs.getInt("sort"));
                        return model;
                    }
                });

        if(list!=null &&list.size()>0){
            return list.get(0);
        }else {
            return null;
        }
    }

    public List<QuestionKnowledge> findMajorKnowledgeList(String questionId) {
        StringBuilder sql = new StringBuilder("");
        sql.append("SELECT  id, question_id, TYPE, knowledge_id, is_main, cognize_level_id, ability_id, sort ");
        sql.append(" FROM  ks_question_knowledge   where question_id = ? and is_main = true");
        List<QuestionKnowledge> list = this.resJdbcTemplate.query(
                sql.toString(),
                new Object[]{questionId},
                new RowMapper<QuestionKnowledge>() {
                    public QuestionKnowledge mapRow(ResultSet rs, int rowNum) throws SQLException {
                        QuestionKnowledge model = new QuestionKnowledge();
                        model.setId(rs.getString("id"));
                        model.setQuestionId(rs.getString("question_id"));
                        model.setType(rs.getString("type"));
                        model.setKnowledgeId(rs.getString("knowledge_id"));
                        model.setIsMain(rs.getBoolean("is_main"));
                        model.setCognizeLevelId(rs.getString("cognize_level_id"));
                        model.setAbilityId(rs.getString("ability_id"));
                        model.setSort(rs.getInt("sort"));
                        return model;
                    }
                });

        return list;
    }

    public List<QuestionKnowledge> findMinorKnowledges(String questionId) {
        StringBuilder sql = new StringBuilder("");
        sql.append("SELECT  id, question_id, TYPE, knowledge_id, is_main, cognize_level_id, ability_id, sort ");
        sql.append(" FROM  ks_question_knowledge   where question_id = ? and is_main = false");
        List<QuestionKnowledge> list = this.resJdbcTemplate.query(
                sql.toString(),
                new Object[]{questionId},
                new RowMapper<QuestionKnowledge>() {
                    public QuestionKnowledge mapRow(ResultSet rs, int rowNum) throws SQLException {
                        QuestionKnowledge model = new QuestionKnowledge();
                        model.setId(rs.getString("id"));
                        model.setQuestionId(rs.getString("question_id"));
                        model.setType(rs.getString("type"));
                        model.setKnowledgeId(rs.getString("knowledge_id"));
                        model.setIsMain(rs.getBoolean("is_main"));
                        model.setCognizeLevelId(rs.getString("cognize_level_id"));
                        model.setAbilityId(rs.getString("ability_id"));
                        model.setSort(rs.getInt("sort"));
                        return model;
                    }
                });

        return list;
    }
}
