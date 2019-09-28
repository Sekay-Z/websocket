package com.shukai.websocket.Controller;

import com.shukai.websocket.Bean.Message;
import com.shukai.websocket.Bean.User;
import com.shukai.websocket.Service.ChatServiceImpl;
import com.shukai.websocket.WebSocket.WebSocketServer;
import com.shukai.websocket.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("chat")
public class ChatController {
    @Autowired
    private ChatServiceImpl chatService;
    @Autowired
    private WebSocketServer webSocketServer;

    @GetMapping("/{id}")
    public Result info(@PathVariable("id")String id, HttpServletRequest request){
        User user= (User) request.getSession().getAttribute(id);
        return new Result(null,user);
    }
    @GetMapping("/online/list")
    public Result onlineList(HttpServletRequest request){
        List<User> list=chatService.onlineList(request.getSession());
        return new Result(null,list);
    }
    @GetMapping("/push/{fromId}/{toId}/{message}")
    public Result push(@PathVariable("fromId") String fromId,@PathVariable("toId")String toId,@PathVariable("message")String message,HttpServletRequest request){
        try{
            webSocketServer.sendTo(fromId,toId,message,request.getSession());
            return null;
        }catch (Exception e){
            e.printStackTrace();
            return new Result("消息推送失败!",null);
        }
    }
    @GetMapping("/self/{fromId}/{toId}")
    public Result selfList(@PathVariable("fromId") String fromId, @PathVariable("toId") String toId, HttpServletRequest request) {
        List<Message> list = chatService.selfList(fromId, toId, request.getSession());
        return new Result(null,list);
    }
    @GetMapping("/common")
    public Result commonList(HttpServletRequest request) {
        List<Message> list = chatService.commonList(request.getSession());
        return new Result(null,list);
    }
}
