package cn.lamppa.aether.dao.impl;

import cn.lamppa.aether.dao.QuestionCategoryDao;
import cn.lamppa.aether.domain.QuestionCategory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by heizhiqiang on 2016/2/25 0025.
 */
@Configurable
@Repository(value="questionCategoryDao")
public class QuestionCategoryDaoImpl implements QuestionCategoryDao {


    @Resource
    private JdbcTemplate resJdbcTemplate;

    @Resource
    private JdbcTemplate platformJdbcTemplate;

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    public int[] addQuestionCategory(List<QuestionCategory> list) {
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(platformJdbcTemplate);
        SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(list.toArray());

        StringBuilder sql = new StringBuilder("");
        sql.append("INSERT INTO ks_question_categroy (id,question_id,categroy,categroy_name) VALUES");
        sql.append("(:id,:questionId,:categroy,:categroyName)");
        int[] updateCounts = namedParameterJdbcTemplate.batchUpdate(sql.toString(),batch);
        return updateCounts;
    }

    public List<QuestionCategory>  findQuestionCategoryByQuestionId(String questionId){
        StringBuilder sql = new StringBuilder("");
        sql.append("SELECT  id, question_id, categroy, categroy_name FROM  ks_question_categroy  where question_id = ?");
        List<QuestionCategory> list = this.resJdbcTemplate.query(
                sql.toString(),
                new Object[]{questionId},
                new RowMapper<QuestionCategory>() {
                    public QuestionCategory mapRow(ResultSet rs, int rowNum) throws SQLException {
                        QuestionCategory model = new QuestionCategory();
                        model.setId(rs.getString("id"));
                        model.setQuestionId(rs.getString("question_id"));
                        model.setCategroy(rs.getString("categroy"));
                        model.setCategroyName("categroy_name");
                        return model;
                    }
                });

        return list;
    }
}

