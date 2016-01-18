package com.github.lionboard.service;

import com.github.lionboard.model.User;

import java.sql.SQLException;

/**
 * Created by daum on 16. 1. 17..
 */
public interface UserService {
    void deleteAllUser();

    int getActiveUsersCount();

    int addNormalUser(User user) throws SQLException;

    User getUserById(int id);
}
