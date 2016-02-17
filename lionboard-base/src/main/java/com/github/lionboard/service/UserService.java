package com.github.lionboard.service;

import com.github.lionboard.model.User;

import java.util.List;

/**
 * Created by Lion.k on 16. 2. 3..
 */
public interface UserService {
    void insertNormalUser(User user);

    void hardDeleteAllUsers();

    User getUserByUserId(int userId);

    User getUserByIdentity(String identity);

    User getUserByName(String name);

    void updateUserProfile(int userId, String uploadedUrl);

    void updateUser(User user);

    void hardDeleteUserById(int userId);

    void updateUserStatusByUserId(int userId,String userStatus);

    User existUserByIdentity(String identity);

    void insertOAuthUser(User user);

    List<User> getAllUsers(int offset, int limit, String sort);

    int countUsers();

    void updateUserRole(User user);

    List<User> searchUserWithQuery(String query);
}
