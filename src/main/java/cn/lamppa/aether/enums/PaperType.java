package cn.lamppa.aether.enums;

/**
 * Created by Administrator on 2016/6/21 0021.
 */
public enum PaperType {
    UNIT_TEST("单元测试"),
    MONTHLY_TEST("月度考试"),
    MIDTERM_TEST("期中考试"),
    FINAL_TEST("期末考试"),
    COLLEGE_ENTRANCE_TEST("高考模拟");

    private final String type;

    PaperType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static PaperType getByDesc(String desc){
        for(PaperType t : PaperType.values()){
            if(t.getType().equals(desc)){
                return t;
            }
        }
        return null;
    }
}
