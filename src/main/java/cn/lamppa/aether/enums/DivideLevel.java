package cn.lamppa.aether.enums;

/**
 * Created by Administrator on 2016/5/23 0023.
 */
public enum DivideLevel {
    DISTRICT("区级"),
    SCHOOL("校级");


    private final String type;

    DivideLevel(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static DivideLevel getByDesc(String desc){
        for(DivideLevel t : DivideLevel.values()){
            if(t.getType().equals(desc)){
                return t;
            }
        }
        return null;
    }

    public static void main(String[] args){
        for(DivideLevel level:DivideLevel.values()){
            System.out.println(level.name());
        }
    }


}
