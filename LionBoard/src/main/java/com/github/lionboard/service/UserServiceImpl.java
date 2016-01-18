package com.github.lionboard.service;

import com.github.lionboard.model.User;
import com.github.lionboard.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by daum on 16. 1. 17..
 */

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    UserRepository userRepository;

    @Autowired
    DataSource dataSource;

    @Autowired
    PlatformTransactionManager transactionManager;

    @Override
    public void deleteAllUser() {
        userRepository.deleteUserFromUsersTB();
    }

    @Override
    public int getActiveUsersCount() {
        return userRepository.countUsers();
    }

    @Override
    public int addNormalUser(User user) throws RuntimeException {
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            int insertedUserId = userRepository.insertUser(user);
            userRepository.insertUserInfo(user, insertedUserId);
            userRepository.insertUserPower(user, insertedUserId);
            userRepository.insertUserPw(user, insertedUserId);
            userRepository.insertUserState(user, insertedUserId);
            transactionManager.commit(status);
            return insertedUserId;
        } catch (RuntimeException e) {
            transactionManager.rollback(status);
            throw e;
        }
    }

    @Override
    public User getUserById(int id) {
        return userRepository.getUserById(id);
    }
}
