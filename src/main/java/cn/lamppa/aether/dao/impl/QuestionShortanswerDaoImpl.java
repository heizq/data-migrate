package cn.lamppa.aether.dao.impl;

import cn.lamppa.aether.dao.QuestionShortanswerDao;
import cn.lamppa.aether.domain.QuestionShortanswer;
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
 * Created by Administrator on 2016/2/25.
 */
@Configurable
@Repository(value="questionShortanswerDao")
public class QuestionShortanswerDaoImpl implements QuestionShortanswerDao {
    @Resource
    private JdbcTemplate resJdbcTemplate;

    @Resource
    private JdbcTemplate platformJdbcTemplate;

    public String addQuestionShortanswer(QuestionShortanswer questionShortanswer) {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("id",questionShortanswer.getId());
        params.put("topic",questionShortanswer.getTopic());
        params.put("analysis",questionShortanswer.getAnalysis());
        params.put("answer",questionShortanswer.getAnswer());
        params.put("base_id",questionShortanswer.getBaseId());
        params.put("is_small",questionShortanswer.getIsSmall());

        SimpleJdbcInsert insert = new SimpleJdbcInsert(platformJdbcTemplate);
        insert.withTableName("ks_question_shortanswer");
        insert.execute(params);
        return questionShortanswer.getId();

    }


    public List<QuestionShortanswer> findQuestionShortanswer(String questionId,Boolean isSmall) {
        StringBuilder sql = new StringBuilder("");
        List<Object> params = new ArrayList<Object>();
        params.add(isSmall);
        sql.append(" select id,topic,analysis,answer,base_id,is_small" );
        sql.append("  from ks_question_shortanswer ");
        sql.append(" where is_small = ?");

        if(StringUtils.isNotEmpty(questionId)){
            sql.append(" and base_id=?");
            params.add(questionId);
        }
        List<QuestionShortanswer> list = this.resJdbcTemplate.query(
                sql.toString(),
                params.toArray(),
                new RowMapper<QuestionShortanswer>() {
                    public QuestionShortanswer mapRow(ResultSet rs, int rowNum) throws SQLException {
                        QuestionShortanswer model = new QuestionShortanswer();
                        model.setId(rs.getString("id"));
                        model.setTopic(rs.getString("topic"));
                        model.setAnalysis(rs.getString("analysis"));
                        model.setAnswer(rs.getString("answer"));
                        model.setBaseId(rs.getString("base_id"));
                        model.setIsSmall(rs.getBoolean("is_small"));
                        return model;
                    }
                });

        return list;
    }

    @Override
    public int getCount() {
        StringBuffer sql=new StringBuffer();
        sql.append(" SELECT  count(1) ");
        sql.append(" FROM  ks_question_shortanswer where is_small= false");
        return this.resJdbcTemplate.queryForObject(sql.toString(),Integer.class);
    }

    @Override
    public List<QuestionShortanswer> findShortanswerByPage(int start, int end) {
        StringBuilder sql = new StringBuilder("");
        sql.append(" select id,topic,analysis,answer,base_id,is_small" );
        sql.append(" from ks_question_shortanswer ");
        sql.append(" where is_small = false limit ?,? ");
        List<QuestionShortanswer> list = this.resJdbcTemplate.query(
                sql.toString(),
                new Object[]{start,end},
                new RowMapper<QuestionShortanswer>() {
                    public QuestionShortanswer mapRow(ResultSet rs, int rowNum) throws SQLException {
                        QuestionShortanswer model = new QuestionShortanswer();
                        model.setId(rs.getString("id"));
                        model.setTopic(rs.getString("topic"));
                        model.setAnalysis(rs.getString("analysis"));
                        model.setAnswer(rs.getString("answer"));
                        model.setBaseId(rs.getString("base_id"));
                        model.setIsSmall(rs.getBoolean("is_small"));
                        return model;
                    }
                });
        return list;
    }


}
