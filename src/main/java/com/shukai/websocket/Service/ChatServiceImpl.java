package com.shukai.websocket.Service;

import com.shukai.websocket.Bean.Message;
import com.shukai.websocket.Bean.User;
import com.shukai.websocket.Constant.CommonConstant;
import com.shukai.websocket.util.CoreUtil;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

@Service
public class ChatServiceImpl implements ChatService {
    @Override
    public List<User> onlineList(HttpSession session) {
        Enumeration<String> ids=session.getAttributeNames();
        List<User> list=new ArrayList<>();
        while(ids.hasMoreElements()){
            String id=ids.nextElement();
            if (id.startsWith(CommonConstant.CHAT_FROM_PREFIX) || id.indexOf(CommonConstant.CHAT_TO_PREFIX) > 0) {
                list.add(null);
            }else{
                User user= (User) session.getAttribute(id);
                list.add(user);
            }
        }
        return list;
    }

    @Override
    public void pushMessage(String fromId, String toId, String message,Long online,HttpSession session) {
        User from= (User) session.getAttribute(fromId);
        Message m=new Message();
        m.setFrom(from);
        m.setOnline(online);
        m.setMessage(message);
        m.setTime(CoreUtil.format(new Date()));
        if(toId!=null){
            User to= (User) session.getAttribute(toId);
            m.setTo(to);
            push(m, CommonConstant.CHAT_FROM_PREFIX+fromId+CommonConstant.CHAT_TO_PREFIX+toId,session);
        }else{
            m.setTo(null);
            push(m,CommonConstant.CHAT_COMMON_PREFIX+fromId,session);
        }
    }
    @Override
    public void push(Message message, String key,HttpSession session) {
        List<Message> oldList= (List<Message>) session.getAttribute(key);
        if(oldList==null){
            List<Message> newList=new ArrayList<>();
            newList.add(message);
            session.setAttribute(key,newList);
        }else{
            oldList.add(message);
            session.setAttribute(key,oldList);
        }
    }

    @Override
    public List<Message> selfList(String fromId, String toId, HttpSession session) {
        List<Message> list = new ArrayList<>();
        Enumeration<String> ids = session.getAttributeNames();
        while (ids.hasMoreElements()) {
            String id = ids.nextElement();
            //指定前缀标识
            if (id.startsWith(CommonConstant.CHAT_FROM_PREFIX) || id.indexOf(CommonConstant.CHAT_TO_PREFIX) > 0) {
                Object attribute = session.getAttribute(id);
                if (attribute instanceof List) {
                    list = (List<Message>) attribute;
                }
            }
        }
        return list;
    }
    @Override
    public List<Message> commonList(HttpSession session) {
        List<Message> list = new ArrayList<>();
        Enumeration<String> ids = session.getAttributeNames();
        while (ids.hasMoreElements()) {
            String id = ids.nextElement();
            //指定前缀标识
            if (id.startsWith(CommonConstant.CHAT_COMMON_PREFIX)) {
                Object attribute = session.getAttribute(id);
                if (attribute instanceof List) {
                    List<Message> data = (List<Message>) attribute;
                    list.addAll(data);
                    CoreUtil.push(list);
                }
            }
        }
        return list;
    }
}
