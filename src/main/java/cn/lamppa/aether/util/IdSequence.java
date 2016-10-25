package cn.lamppa.aether.util;

/**
 * Created by heizhiqiang on 2016/2/24 0024.
 **/
import org.springframework.util.StringUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class IdSequence {

    private static int idLength = 16;
    private static long ONE_STEP = 0;
    private static final Lock LOCK = new ReentrantLock();
    private static long lastTime = System.currentTimeMillis();
    private static short lastCount = 0;
    private static int count = 0;
    private static String format = "";

    private static String localIp = "";

    private Object lock = new Object();

    static {
        int ln = idLength - 13;
        StringBuffer sb = new StringBuffer();
        sb.append(1);
        for (int i = 0; i < ln; i++) {
            sb.append(0);
        }
        System.out.println(sb.toString());

        ONE_STEP = Long.parseLong(sb.toString());
        format = "%0" + (idLength - 13) + "d";
    }

    @SuppressWarnings("finally")
    public static String nextId() {
//        LOCK.lock();
//        try {
//            if (lastCount == ONE_STEP) {
//                boolean done = false;
//                while (!done) {
//                    long now = System.currentTimeMillis();
//                    if (now == lastTime) {
//                        try {
//                            Thread.currentThread();
//                            Thread.sleep(1);
//                        } catch (java.lang.InterruptedException e) {
//                        }
//                        continue;
//                    } else {
//                        lastTime = now;
//                        lastCount = 0;
//                        done = true;
//                    }
//                }
//            }
//            count = lastCount++;
//        } finally {
//            LOCK.unlock();
//            return lastTime + "" + String.format(format, count);
//        }

//        String s = UUID.randomUUID().toString().toUpperCase();
//        int p = 0;
//        int j = 0;
//        char[] buf = new char[32];
//        while(p<s.length()){
//            char c = s.charAt(p);
//            p+=1;
//            if(c=='-')continue;
//            buf[j]=c;
//            j+=1;
//        }
//        return new String(buf);

        return String.valueOf(IdWorker.getId());
    }


    public static String getId() {
        StringBuffer sb = new StringBuffer();
        sb.append(getLocalIp());
        sb.append(nextId());
        return sb.toString();
    }

    public static String getLocalIp() {
        StringBuffer sb = new StringBuffer();
        if (StringUtils.isEmpty(localIp)) {
            String localAddress = getIp();
            if (localAddress.length() < 7) {
                localIp = "";
            } else {
                String str[] = localAddress.split("\\.");
                for (String s : str) {
                    if (s.length() < 3) {
                        for (int i = 0; i < 3 - s.length(); i++) {
                            sb.append(0);
                        }
                    }
                    sb.append(s);
                }
                localIp = sb.toString();
            }

        }
        return localIp;
    }


    public static void main(String[] args) {

        String str = nextId();
        System.out.println(str);

        for(int i=0;i<10;i++){
            System.out.println(nextId());
        }



    }

    public static String getIp() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }
}