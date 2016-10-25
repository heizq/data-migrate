package cn.lamppa.aether.dao;


import cn.lamppa.aether.domain.Textbook;

import java.util.List;

/**
 * Created by Administrator on 2016/2/25.
 */
public interface TextbookDao {
    public String addTextbook(Textbook textbook);

    public List<Textbook> getTextBook();
}
