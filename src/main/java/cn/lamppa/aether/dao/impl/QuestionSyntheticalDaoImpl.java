package cn.lamppa.aether.dao.impl;

import cn.lamppa.aether.dao.QuestionSyntheticalDao;
import cn.lamppa.aether.domain.QuestionSynthetical;
import cn.lamppa.aether.domain.QuestionSyntheticalItem;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by heizhiqiang on 2016/2/29 0029.
 */
@Configurable
@Repository(value="questionSyntheticalDao")
public class QuestionSyntheticalDaoImpl implements QuestionSyntheticalDao {

    @Resource
    private JdbcTemplate resJdbcTemplate;

    @Resource
    private JdbcTemplate platformJdbcTemplate;

    public String addQuestion(QuestionSynthetical model) {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("id",model.getId());
        params.put("topic",model.getTopic());
        params.put("base_id",model.getBaseId());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(platformJdbcTemplate);

        insert.withTableName("ks_question_synthetical");
        insert.execute(params);
        return model.getId();
    }

    public String addItem(QuestionSyntheticalItem model) {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("id",model.getId());
        params.put("synthetical_id",model.getSyntheticalId());
        params.put("question_id",model.getQuestionId());
        params.put("TYPE",model.getType());
        params.put("sort",model.getSort());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(platformJdbcTemplate);

        insert.withTableName("ks_question_synthetical_item");
        insert.execute(params);
        return model.getId();
    }


    public List<QuestionSyntheticalItem> findItems(String questionId) {
        StringBuilder sql = new StringBuilder("");
        sql.append("SELECT   id,  synthetical_id,  question_id,  TYPE,  sort FROM ks_question_synthetical_item ");
        sql.append("  where synthetical_id=? ");
        List<QuestionSyntheticalItem> list = this.resJdbcTemplate.query(
                sql.toString(),
                new Object[]{questionId},
                new RowMapper<QuestionSyntheticalItem>() {
                    public QuestionSyntheticalItem mapRow(ResultSet rs, int rowNum) throws SQLException {
                        QuestionSyntheticalItem model = new QuestionSyntheticalItem();
                        model.setId(rs.getString("id"));
                        model.setQuestionId(rs.getString("question_id"));
                        model.setSyntheticalId(rs.getString("synthetical_id"));
                        model.setType(rs.getString("type"));
                        model.setSort(rs.getString("sort"));
                        return model;
                    }
                });
        return list;
    }

    @Override
    public List<QuestionSynthetical> findQuestionByPage(int start, int end) {
        StringBuilder sql = new StringBuilder("");
        sql.append("SELECT   id,  topic,  base_id");
        sql.append("  FROM  ks_question_synthetical limit ?,?");
        List<QuestionSynthetical> list = this.resJdbcTemplate.query(
                sql.toString(),
                new Object[]{start,end},
                new RowMapper<QuestionSynthetical>() {
                    public QuestionSynthetical mapRow(ResultSet rs, int rowNum) throws SQLException {
                        QuestionSynthetical model = new QuestionSynthetical();
                        model.setId(rs.getString("id"));
                        model.setTopic(rs.getString("topic"));
                        model.setBaseId(rs.getString("base_id"));
                        return model;
                    }
                });
        return list;
    }

    @Override
    public int getCount() {
        StringBuilder sql = new StringBuilder("");
        sql.append("SELECT  count(1) ");
        sql.append(" FROM  ks_question_synthetical ");

        int count = this.resJdbcTemplate.queryForObject(sql.toString(),Integer.class);
        return count;
    }


}
