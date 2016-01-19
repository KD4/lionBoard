package com.github.lionboard.service;

import com.github.lionboard.model.User;
import org.springframework.transaction.PlatformTransactionManager;

import java.sql.SQLException;

/**
 * Created by lion.k on 16. 1. 17..
 */


public interface UserService {
    void deleteAllUser();

    int countUsers();

    int addNormalUser(User user) throws RuntimeException;

    User getUserById(int id);

    int countUsersWithState(int i);

    void changeStateOfAllUsers(int i);

    void updateUserInfo(User user);

}
