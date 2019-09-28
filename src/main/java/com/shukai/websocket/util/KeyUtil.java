package com.shukai.websocket.util;

import java.util.Random;
import java.util.UUID;

public class KeyUtil {
    //带有字母的随机数
    public static String getUUID(){
        String uuid= UUID.randomUUID().toString().
                replaceAll("-","").substring(0,32);
        return uuid;
    }
    //不带字母的随机数
    public static synchronized String genUniqueKey(){
        Random random=new Random();
        Integer number=random.nextInt(900000)+100000;

        return System.currentTimeMillis()+String.valueOf(number);
    }
    //产生6位验证码
    public static String generate(int digit) {
        Random random = new Random();
        StringBuilder result= new StringBuilder();
        for (int i=0;i<digit;i++)
        {
            result.append(random.nextInt(10));
        }

        return result.toString();
    }
}
