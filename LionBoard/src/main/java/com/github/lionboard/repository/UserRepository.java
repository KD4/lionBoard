package com.github.lionboard.repository;

import com.github.lionboard.model.User;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Created by daum on 16. 1. 17..
 */


public interface UserRepository {

    int countUsers();

    int insertUser(User user);

    void insertUserInfo(User user,int insertedUserId);

    void insertUserPower(User user,int insertedUserId);

    void insertUserPw(User user,int insertedUserId);

    void insertUserState(User user,int insertedUserId);

    User getUserById(int id);

    void deleteUserFromUsersTB();

    int countUsersWithState(int i);

    void changeStateOfAllUsers(int i);

    void updateUser(User user);

    void updateUserInfo(User user);

    void updateUserPw(User user);

}
