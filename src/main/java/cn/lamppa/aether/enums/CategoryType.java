package cn.lamppa.aether.enums;

/**
 * Created by heizhiqiang on 2016/2/25 0025.
 */
public enum CategoryType {
    collegeEntrance("高考"),
    sprint("冲刺"),
    simulation("模拟");

    private final String type;

    CategoryType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static CategoryType getByDesc(String desc){
        for(CategoryType t : CategoryType.values()){
            if(t.getType().equals(desc)){
                return t;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(CategoryType.getByDesc("高考"));
        System.out.println(CategoryType.collegeEntrance.toString());
        System.out.println(CategoryType.collegeEntrance.getType());
        System.out.println(CategoryType.collegeEntrance.name());

    }

}
