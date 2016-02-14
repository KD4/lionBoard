package com.github.lionboard.service;

import com.github.lionboard.model.User;
import com.github.lionboard.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Lion.k on 16. 2. 3..
 */

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    UserRepository userRepository;


    /**
     * OAuth 정보가 없는 일반 회원정보를 저장합니다.
     *
     * @param user
     */
    @Override
    public void insertNormalUser(User user) {
        userRepository.insertUser(user);
    }

    /**
     * 모든 User를 Delete합니다.
     * -for test case-
     */
    @Override
    public void hardDeleteAllUsers() {
        userRepository.deleteAll();
    }

    /**
     * UserId에 해당하는 User 정보를 반환합니다.
     *
     * @param userId
     * @return User
     */
    @Override
    public User getUserByUserId(int userId) {
        return userRepository.findUserByUserId(userId);
    }

    /**
     * Identity에 해당하는 User 정보를 반환합니다.
     *
     * @param identity
     * @return User
     */
    @Override
    public User getUserByIdentity(String identity) {
        return userRepository.findUserByIdentity(identity);
    }

    /**
     * name에 해당하는 User 정보를 반환합니다.
     *
     * @param name
     * @return User
     */
    @Override
    public User getUserByName(String name) {
        return userRepository.findUserByName(name);
    }

    /**
     * 이미 등록된 User의 profile정보를 업데이트합니다.
     *
     * @param userId
     * @param uploadedUrl
     */
    @Override
    public void updateUserProfile(int userId, String uploadedUrl) {
        User user = new User();
        user.setId(userId);
        user.setProfileUrl(uploadedUrl);
        userRepository.updateProfileInfo(user);
    }

    /**
     * 이미 등록된 User의 정보를 업데이트합니다.
     * -for test case-
     * @param user
     */
    @Override
    public void updateUser(User user) {
        userRepository.updateUser(user);
    }

    /**
     * 이미 등록된 User의 정보를 Delete합니다.
     * -for test case-
     * @param userId
     */
    @Override
    public void hardDeleteUserById(int userId) {
        userRepository.deleteUserById(userId);
    }

    /**
     * 이미 등록된 User의 상태를 변경합니다.
     * -for test case-
     * @param userId
     */
    @Override
    public void updateUserStatusByUserId(int userId,String userStatus) {
        User user =new User();
        user.setId(userId);
        user.setUserStatus(userStatus);
        userRepository.updateUserStatus(user);

    }

    /**
     * identity 정보로 등록된 유저가 있는지 확인합니다.
     *
     * @param identity
     */
    @Override
    public User existUserByIdentity(String identity) {
        return userRepository.findUserByIdentity(identity);
    }


    /**
     * OAuth 사용자 정보를 저장합니다.
     *
     * @param user
     */
    @Override
    public void insertOAuthUser(User user) {
        //TODO OAuth 유저 구별 저장과 패스워드 정책 수립.
        userRepository.insertUser(user);
    }


    /**
     * 모든 사용자 목록을 가져옵니다.
     *
     * @param offset
     * @param limit
     * @param sort
     */
    @Override
    public List<User> getAllUsers(int offset, int limit, String sort) {
        HashMap<String,Object> params = new HashMap<>();
        params.put("offset",offset);
        params.put("limit",limit);
        params.put("sort",sort);

        return userRepository.findAll(params);
    }


    /**
     * 사용자 수를 반환합니다.
     *
     */
    @Override
    public int countUsers() {
        return userRepository.countAll();
    }

    /**
     * 해당 유저의 권한을 변경합니다.
     *
     * @param user
     */
    @Override
    public void updateUserRole(User user) {
        userRepository.updateUserRole(user);
    }


    /**
     * query를 이용해서 사용자를 검색합니다.
     *
     * @param query
     */
    @Override
    public List<User> searchUserWithQuery(String query) {
        return userRepository.getUserByQuery(query);
    }

}
