package com.github.lionboard.repository;

import com.github.lionboard.model.User;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Lion.k on 16. 1. 21..
 */

@Repository
public interface UserRepository {
    void deleteAll();

    void insertUser(User user) throws DuplicateKeyException;

    User findUserByUserId(int userId);

    User findUserByIdentity(String identity);

    void updateUser(User user);

    void deleteUserById(int userId);

    User findUserByName(String name);

    void updateProfileInfo(User user);

    void updateUserStatus(User user);

    List<User> findAll(HashMap<String, Object> params);

    int countAll();

    void updateUserRole(User user);

    List<User> getUserByQuery(String query);
}
