package com.github.lionboard.service;

import com.github.lionboard.model.User;
import com.github.lionboard.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.sql.DataSource;

/**
 * Created by lion.k on 16. 1. 19..
 */

public class UserServiceTx implements UserService{

    PlatformTransactionManager transactionManager;

    UserService userService;


    @Override
    public void deleteAllUser() {
        this.userService.deleteAllUser();
    }

    @Override
    public int countUsers() {
       return this.userService.countUsers();
    }

    @Override
    public int addNormalUser(User user) throws RuntimeException {
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            int insertedUserId = this.userService.addNormalUser(user);
            transactionManager.commit(status);
            return insertedUserId;
        } catch (RuntimeException e) {
            transactionManager.rollback(status);
            throw e;
        }
    }

    @Override
    public User getUserById(int id) {
        return this.userService.getUserById(id);
    }

    @Override
    public int countUsersWithState(int i) {
        return this.userService.countUsersWithState(i);
    }

    @Override
    public void changeStateOfAllUsers(int i) {
        this.userService.changeStateOfAllUsers(i);
    }

    @Override
    public void updateUserInfo(User user) {
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            this.userService.updateUserInfo(user);
            transactionManager.commit(status);
        } catch (RuntimeException e) {
            transactionManager.rollback(status);
            throw e;
        }
    }


    public PlatformTransactionManager getTransactionManager() {
        return this.transactionManager;
    }

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public void setuserServiceImpl(UserService userService) {
        this.userService = userService;
    }
}
