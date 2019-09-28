package com.shukai.websocket.Controller;

import com.shukai.websocket.Bean.User;
import com.shukai.websocket.util.KeyUtil;
import com.shukai.websocket.util.Result;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

@RestController
@RequestMapping("/user")
public class UserController {

    @RequestMapping("/login")
    public Result login(@RequestBody User user, HttpServletRequest request){
        Enumeration<String> ids = request.getSession().getAttributeNames();
        while (ids.hasMoreElements()) {
            String id = ids.nextElement();
            if (request.getSession().getAttribute(id) instanceof User) {
                if (((User) request.getSession().getAttribute(id)).getName().equals(user.getName())) {
                    return new Result("该用户名已存在",null);
                }
            }
        }
        user.setId(KeyUtil.genUniqueKey());
        request.getSession().setAttribute(user.getId(),user);
        return new Result("登录成功",user);
    }
    @DeleteMapping("/logout/{id}")
    public Result logout(@PathVariable("id") String id, HttpServletRequest request) {
        if (id != null) {
            request.getSession().removeAttribute(id);
        }
        return new Result("登出成功",null);
    }
}
