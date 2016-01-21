package com.github.lionboard.repository;

import com.github.lionboard.model.User;
import org.springframework.stereotype.Repository;

/**
 * Created by daum on 16. 1. 21..
 */

@Repository
public interface UserRepository {
    void deleteAll();

    void insertUser(User user);

    User findUserByUserId(int userId);
}
