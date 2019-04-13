package com.example.Controller;

import com.example.Service.UserService;
import com.example.pojo.Localauth;
import com.example.pojo.Users;
import com.example.utils.JSONResult;
import com.example.utils.MD5Utils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("u")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/registOrLogin")
    public JSONResult registOrLogin(@RequestBody Localauth loginmsg) throws Exception {

        // 判断用户名密码是否为空
        if (StringUtils.isBlank(loginmsg.getUsername()) || StringUtils.isBlank(loginmsg.getPassword())) {
            return JSONResult.errorMsg("用户名密码不能为空！！");
        }

        // 判断用户名是否存在
        boolean usernameIsExist = userService.usernameIsExist(loginmsg.getUsername());
        Users userResult = null;

        if (usernameIsExist) {
            //如果存在，则判断密码是否正确
            userResult = userService.queryForLogin(loginmsg.getUsername(),
                    MD5Utils.getMD5Str(loginmsg.getPassword()));

            if (userResult == null) {
                return JSONResult.errorMsg("用户名密码不正确！");
            }
        } else {
            //如果不存在，则注册
            userResult = userService.registAndSave(loginmsg);
        }

        return JSONResult.ok(userResult);
    }
}
