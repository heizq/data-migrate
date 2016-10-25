package cn.lamppa.aether.dao.impl;

import cn.lamppa.aether.dao.TextbookDao;
import cn.lamppa.aether.domain.Textbook;
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
 * Created by Administrator on 2016/2/25.
 */
@Configurable
@Repository(value="textbookDao")
public class TextbookDaoImpl implements TextbookDao {

    @Resource
    private JdbcTemplate resJdbcTemplate;

    @Resource
    private JdbcTemplate platformJdbcTemplate;

    public String addTextbook(Textbook textbook) {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("id",textbook.getId());
        params.put("subject_id",textbook.getSubjectId());
        params.put("subject_name",textbook.getSubjectName());
        params.put("grade_id",textbook.getGradeId());
        params.put("grade_name",textbook.getGradeName());
        params.put("phase_id",textbook.getPhaseId());
        params.put("phase_name",textbook.getPhaseName());
        params.put("textbook_version_id",textbook.getTextbookVersionId());
        params.put("textbook_version_name",textbook.getTextbookVersionName());
        params.put("fascicule_id",textbook.getFasciculeId());
        params.put("fascicule_name",textbook.getFasciculeName());
        params.put("publish_time",textbook.getPublishTime());
        params.put("cover",textbook.getCover());
        params.put("status",textbook.getStatus());
        params.put("create_time",textbook.getCreateTime());
        params.put("create_user",textbook.getCreateUser());

        SimpleJdbcInsert insert = new SimpleJdbcInsert(platformJdbcTemplate);
        insert.withTableName("ks_textbook");
        insert.execute(params);
        return textbook.getId();
    }

    public List<Textbook> getTextBook(){
        StringBuilder sql = new StringBuilder("");
        sql.append(" select id,subject_id,subject_name,grade_id,grade_name,phase_id,phase_name,textbook_version_id,textbook_version_name,");
        sql.append(" fascicule_id,fascicule_name,publish_time,cover,status,create_time,create_user from ks_textbook where 1= 1");
        List<Textbook> textbookList = this.resJdbcTemplate.query(
                sql.toString(),
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

}
