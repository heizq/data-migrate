package cn.lamppa.aether.dao.impl;

import cn.lamppa.aether.dao.CommonDao;
import cn.lamppa.aether.util.Constants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by liupd on 16-3-1.
 **/
@Configurable
@Repository(value="commonDao")
public class CommonDaoImpl implements CommonDao {

    @Autowired
    private JdbcTemplate resJdbcTemplate;

    @Autowired
    private JdbcTemplate platformJdbcTemplate;

    @Override
    public String getKnowledgeId(String srcKnowledgeId) {
       String sql=" SELECT desc_id as knowledge_id FROM syn_table where source_id=? and table_name='ks_knowledge_point' ";
       return (String)resJdbcTemplate.query(sql,new Object[]{srcKnowledgeId},new ResultSetExtractor<Object>() {
            @Override
            public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
                while(rs.next()){
                    return rs.getString("knowledge_id");
                }
                return null;
            }
        });
    }

    @Override
    public void insertMiddleTable(String questionId) {
        platformJdbcTemplate.update("insert into ks_question_flag(question_id,src_flag,data_bak) values(?,?,?)",new Object[]{questionId, Constants.src_flag,null});
    }

    @Override
    public String getAbliity(String id) {
        String abliityKey="";
        if(StringUtils.isNotBlank(id)){
            abliityKey= (String)resJdbcTemplate.query("SELECT desc_id FROM syn_table where table_name='bd_ability' and source_id=?  ",new Object[]{id},new ResultSetExtractor<Object>() {
                @Override
                public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
                    while(rs.next()){
                        return rs.getString("desc_id");
                    }
                    return null;
                }
            });
        }
        return abliityKey;
    }

    @Override
    public String getCognizeLevel(String id) {
        String  cognize_level_id="";
        if(StringUtils.isNotBlank(id)){
            cognize_level_id=(String)resJdbcTemplate.query("SELECT desc_id FROM syn_table where table_name='bd_cognize_level' and source_id=?",new Object[]{id},new ResultSetExtractor<Object>() {
                @Override
                public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
                    while(rs.next()){
                        return rs.getString("desc_id");
                    }
                    return null;
                }
            });
        }
        return cognize_level_id;
    }
}
