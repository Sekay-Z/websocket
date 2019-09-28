package com.shukai.websocket.WebSocket;

import com.alibaba.fastjson.JSONObject;
import com.shukai.websocket.Bean.Message;
import com.shukai.websocket.Bean.User;
import com.shukai.websocket.Service.ChatService;
import com.shukai.websocket.config.HttpSessionConfig;
import com.shukai.websocket.util.CoreUtil;
import com.shukai.websocket.util.Result;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint(value = "/websocket/{id}",configurator = HttpSessionConfig.class)
@Component
public class WebSocketServer {
    private HttpSession httpSession;
    private Session session;
    private static long online = 0;
    private String fromId = "";
    private static CopyOnWriteArraySet<WebSocketServer> webSocketServers=new CopyOnWriteArraySet<>();
    public static ChatService chatService;
    @OnOpen
    public void onOpen(Session session, @PathParam("id")String id, EndpointConfig config){
        httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        this.session=session;
        webSocketServers.add(this);
        addOnlineCount();
        System.out.println("有新窗口开始监听:"+id+",当前在线人数为:"+getOnlineCount());
        this.fromId=id;
        User user= (User) httpSession.getAttribute(fromId);
        Map<String, Object> map = new HashMap<>();
        map.put("online", getOnlineCount());
        map.put("msg", "用户 " + user.getName() + " 已上线");
        sendAllMessage(JSONObject.toJSONString(map));
    }
    @OnClose
    public void onClose(){
        webSocketServers.remove(this);
        subOnLineCount();
        System.out.println("有连接断开，当前在线人数为:"+getOnlineCount());
    }
    @OnError
    public void onError(Throwable error){
        error.printStackTrace();
    }
    @OnMessage
    public void onMessage(String message){
        System.out.println("【websocket消息】收到客户端消息:"+message);
        chatService.pushMessage(fromId,null,message,getOnlineCount(),httpSession);
        sendAllMessage(getMessage(null,message));
    }

    private void sendMessage(String message){
        this.session.getAsyncRemote().sendText(message);
    }
    //群发消息 广播消息
    private void sendAllMessage(String message){
        for(WebSocketServer webSocketServer:webSocketServers){
            try{
                webSocketServer.sendMessage(message);
            }catch(Exception e){
                e.printStackTrace();

            }

        }
    }
    //发给指定用户消息
    public void sendTo(String formId, String toId, String message, HttpSession session){
        this.fromId=formId;
        this.httpSession=session;
        if(toId==null){
            sendMessage(JSONObject.toJSONString(new Result("推送失败!",null)));
        }
        for(WebSocketServer webSocketServer:webSocketServers){
          if(webSocketServer.fromId.equals(toId)){
                webSocketServer.sendMessage(getMessage(toId,message));
                chatService.pushMessage(fromId,toId,message,getOnlineCount(),httpSession);
            }
        }
    }
    private String getMessage(String toId,String message){
        Message m=new Message();
        m.setMessage(message);
        m.setOnline(getOnlineCount());
        m.setFrom((User) httpSession.getAttribute(fromId));
        m.setTo((User) httpSession.getAttribute(toId));
        m.setTime(CoreUtil.format(new Date()));
        return JSONObject.toJSONString(new Result(null,m));
    }

    private synchronized void subOnLineCount() {
        WebSocketServer.online--;
    }

    private synchronized long getOnlineCount() {
        return online;
    }

    private synchronized void addOnlineCount() {
        WebSocketServer.online++;
    }
}
