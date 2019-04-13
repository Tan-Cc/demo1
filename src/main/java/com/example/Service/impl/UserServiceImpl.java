package com.example.Service.impl;

import com.example.Service.UserService;
import com.example.mapper.LocalauthMapper;
import com.example.mapper.UsersMapper;
import com.example.pojo.Localauth;
import com.example.pojo.Users;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private LocalauthMapper localauthMapper;

    @Autowired
    private UsersMapper usersMapper;

    //用以生成唯一标识的ID
    @Autowired
    private Sid sid;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public boolean usernameIsExist(String username) {
        Localauth loginmsg = new Localauth();
        loginmsg.setUsername(username);

        Localauth result = localauthMapper.selectOne(loginmsg);

        return result != null ? true : false;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Users queryForLogin(String username, String pwd) {
        Example loginExample = new Example(Localauth.class);
        Example.Criteria criteria = loginExample.createCriteria();

        criteria.andEqualTo("username", username);
        criteria.andEqualTo("password", pwd);

        Localauth loginMsg = localauthMapper.selectOneByExample(loginExample);

        if (loginMsg == null) {
            return null;
        } else {
            Users user = new Users();
            user.setId(loginMsg.getId());
            Users result = usersMapper.selectOne(user);
            return result;
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Users registAndSave(Localauth loginMsg) {
        // 将login信息写入数据库
        loginMsg.setId(sid.nextShort());
        loginMsg.setUserId(sid.nextShort());
        localauthMapper.insert(loginMsg);

        // 通过userId关联Users表，并将相应的信息写入,nickname默认为username
        Users user = new Users();
        user.setId(loginMsg.getUserId());
        user.setNickname(loginMsg.getUsername());
        usersMapper.insert(user);

        return user;
    }


}
