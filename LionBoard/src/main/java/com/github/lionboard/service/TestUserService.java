package com.github.lionboard.service;

import com.github.lionboard.model.User;
import com.github.lionboard.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.sql.DataSource;

/**
 * Created by Lion on 16. 1. 18..
 *
 * this class used as Mock Object.
 */


public class TestUserService implements UserService {

    UserRepository userRepository;

    DataSource dataSource;

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

            //generate transaction exception.
            throw new TestUserServiceException();

        } catch (RuntimeException e) {
            transactionManager.rollback(status);
            throw e;
        }
    }

    @Override
    public User getUserById(int id) {
        return userRepository.getUserById(id);
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setTransactionManager(PlatformTransactionManager transactionManager){
        this.transactionManager = transactionManager;
    }

    static class TestUserServiceException extends RuntimeException{

    }
}
