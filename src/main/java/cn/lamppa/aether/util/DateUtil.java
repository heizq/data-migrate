package cn.lamppa.aether.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/10/26 0026.
 */
public class DateUtil {
    // List of all date formats that we want to parse.
    // Add your own format here.
    private static List<SimpleDateFormat> dateFormats = new ArrayList<SimpleDateFormat>() {{
        add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        add(new SimpleDateFormat("yyyy.MM.dd HH:mm:ss"));
        add(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"));
        add(new SimpleDateFormat("yyyy-MM-dd"));
        add(new SimpleDateFormat("yyyy.MM.dd"));
        add(new SimpleDateFormat("yyyy/MM/dd"));

        }
    };

    public static boolean isValidDate(String inDate) {
        Date date = null;
        for (SimpleDateFormat format : dateFormats) {
            format.setLenient(false);
            try {
                if(inDate.length()<12 && format.toPattern().length() < 12){
                    date = format.parse(inDate.trim());
                }else if(inDate.length()>12 && format.toPattern().length() > 12){
                    date = format.parse(inDate.trim());
                }

            } catch (ParseException e) {
               continue;
            }
            if(date != null){
                break;
            }
        }
        if(date == null){
            return false;
        }
        return true;
    }

    public static Date convertToDate(String inDate) {
        Date date = null;
        for (SimpleDateFormat format : dateFormats) {
            format.setLenient(false);
            try {
                if(inDate.length()<12 && format.toPattern().length() < 12){
                    date = format.parse(inDate.trim());
                }else if(inDate.length()>12 && format.toPattern().length() > 12){
                    date = format.parse(inDate.trim());
                }

            } catch (ParseException e) {
                continue;
            }
            if(date != null){
                break;
            }
        }

        return date;
    }

    public static void main(String[] args) {

        System.out.println(isValidDate("2004-02-29"));
        System.out.println(isValidDate("2005-02-29"));

        System.out.println(isValidDate("2005-02-22 15:33:34"));

        System.out.println(isValidDate("2005/02/22 55:33:34"));
        System.out.println(new Date("2005/02/22 55:33:34"));
    }
}
