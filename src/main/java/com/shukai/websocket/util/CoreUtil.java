package com.shukai.websocket.util;

import com.shukai.websocket.Bean.Message;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class CoreUtil{
    //按照时间顺序向List中push数据
    public static void push(List<Message> list) {
        list.sort(Comparator.comparing(Message::getTime));
    }
    public static String format(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }
}
