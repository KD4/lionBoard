package com.github.lionboard.service;

import com.github.lionboard.model.User;
import com.github.lionboard.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;
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


    @Override
    public void deleteAllUser() {
        userRepository.deleteUserFromUsersTB();
    }

    @Override
    public int getActiveUsersCount() {
        return userRepository.countUsers();
    }

    @Override
    public int addNormalUser(User user) throws SQLException {
        TransactionSynchronizationManager.initSynchronization();
        Connection c = DataSourceUtils.getConnection(dataSource);
        c.setAutoCommit(false);
        try {
            int insertedUserId = userRepository.insertUser(user);
            userRepository.insertUserInfo(user, insertedUserId);
            userRepository.insertUserPower(user, insertedUserId);
            userRepository.insertUserPw(user, insertedUserId);
            userRepository.insertUserState(user, insertedUserId);
            c.commit();
            return insertedUserId;
        } catch (SQLException e) {
            c.rollback();
            throw e;
        }finally {
            DataSourceUtils.releaseConnection(c,dataSource);
            TransactionSynchronizationManager.unbindResource(this.dataSource);
            TransactionSynchronizationManager.clearSynchronization();
        }
    }

    @Override
    public User getUserById(int id) {
        return userRepository.getUserById(id);
    }
}
