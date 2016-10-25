package cn.lamppa.aether.service;


import cn.lamppa.aether.domain.Textbook;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/2/25.
 */
public interface TextbookService {
    public void addTextbook();

    public List<Textbook> getTextBook();

    public String transferData(List<Textbook> list);

    public List<Textbook> findTextbookByPage(int start, int end);
}
