package com.example.Service;

import com.example.pojo.Localauth;
import com.example.pojo.Users;

public interface UserService {

    boolean usernameIsExist(String username);

    Users queryForLogin(String username, String pwd);

    Users registAndSave(Localauth loginMsg) throws Exception;
}
