package com.github.lionboard.repository;

import com.github.lionboard.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by lion.k on 16. 1. 18..
 */
public class JdbcUserRepository implements UserRepository {

    private JdbcTemplate jdbcTemplate;

    private RowMapper<User> userMapper =
            new RowMapper<User>() {
                @Override
                public User mapRow(ResultSet resultSet, int i) throws SQLException {
                    User user = new User();
                    user.setId(resultSet.getInt("id"));
                    user.setEmail(resultSet.getString("email"));
                    user.setIsOAuth(resultSet.getInt("isOAuth"));
                    user.setName(resultSet.getString("name"));
                    user.setPassword(resultSet.getString("password"));
                    user.setIdentity(resultSet.getString("identity"));
                    user.setPowerCode(resultSet.getInt("powerCode"));
                    user.setUserStateCode(resultSet.getShort("userStateCode"));
                    user.setProfileUrl(resultSet.getString("profileUrl"));
                    return user;
                }
            };

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public JdbcUserRepository(){
    }


    @Override
    public int countUsers() {
        return this.jdbcTemplate.queryForObject("SELECT count(*) as count FROM ACT_USERS_TB", Integer.class);
    }

    @Override
    public int insertUser(User user) {
        this.jdbcTemplate.update("insert into ACT_USERS_TB(identity) values(?)", user.getIdentity());
        return jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID() FROM ACT_USERS_TB LIMIT 1", Integer.class);
    }

    @Override
    public void insertUserInfo(User user,int insertedUserId) {
        this.jdbcTemplate.update("insert into ACT_USERINFO_TB(id,email,name,profileUrl) VALUES(?,?,?,?)", insertedUserId,user.getEmail(),user.getName(),user.getProfileUrl());
    }

    @Override
    public void insertUserPower(User user,int insertedUserId) {
        this.jdbcTemplate.update("insert into ACT_POWER_TB(id,powerCode) VALUES(?,?)", insertedUserId,user.getPowerCode());
    }

    @Override
    public void insertUserPw(User user,int insertedUserId) {
        this.jdbcTemplate.update("insert into ACT_USER_PW_TB(id,password) VALUES(?,?)", insertedUserId,user.getPassword());
    }

    @Override
    public void insertUserState(User user,int insertedUserId) {
        this.jdbcTemplate.update("insert into ACT_USER_STATE_TB(id,userStateCode) VALUES(?,?)", insertedUserId,user.getUserStateCode());
    }


    @Override
    public User getUserById(int id) {
        return this.jdbcTemplate.queryForObject("select * from ACT_USERS_TB as USERS INNER JOIN ACT_USERINFO_TB as USERINFO on USERS.id = USERINFO.id LEFT JOIN ACT_POWER_TB as POWER " +
                "ON USERS.id = POWER.id " +
                "LEFT JOIN " +
                "ACT_USER_PW_TB as PW " +
                "ON USERS.id = PW.id " +
                "LEFT JOIN " +
                "ACT_USER_STATE_TB as STATE " +
                "ON USERS.id = STATE.id " +
                "where USERS.id = ? ", new Object[]{id}, userMapper);
    }

    @Override
    public void deleteUserFromUsersTB() {
       this.jdbcTemplate.update(
                "delete from ACT_USERS_TB"
       );
    }


    @Override
    public int countUsersWithState(int i) {
        return this.jdbcTemplate.queryForObject("SELECT count(*) FROM ACT_USERS_TB as users INNER JOIN ACT_USER_STATE_TB as state ON users.id=state.id WHERE state.userStateCode="+i, Integer.class);
    }

    @Override
    public void changeStateOfAllUsers(int i) {
        this.jdbcTemplate.update("UPDATE ACT_USER_STATE_TB SET userStateCode=?", i);
    }

    @Override
    public void updateUser(User user) {
        this.jdbcTemplate.update("UPDATE ACT_USERS_TB SET identity=?,isOAuth=? WHERE id=?", user.getIdentity(),user.getIsOAuth(),user.getId());
    }

    @Override
    public void updateUserInfo(User user) {
        this.jdbcTemplate.update("UPDATE ACT_USERINFO_TB SET name=?,email=?,profileUrl=? WHERE id=?", user.getName(),user.getEmail(),user.getProfileUrl(),user.getId());
    }

    @Override
    public void updateUserPw(User user) {
        this.jdbcTemplate.update("UPDATE ACT_USER_PW_TB SET password=? WHERE id=?", user.getPassword(),user.getId());
    }


}
