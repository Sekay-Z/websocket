package com.shukai.websocket.Service;

import com.shukai.websocket.Bean.Message;
import com.shukai.websocket.Bean.User;

import javax.servlet.http.HttpSession;
import java.util.List;

public interface ChatService {
    public List<User> onlineList(HttpSession session);
    public void pushMessage(String fromId, String toId, String message,Long online, HttpSession session);
    public void push(Message message,String key,HttpSession session);
    public List<Message> selfList(String fromId, String toId, HttpSession session);
    public List<Message> commonList(HttpSession session);
}
