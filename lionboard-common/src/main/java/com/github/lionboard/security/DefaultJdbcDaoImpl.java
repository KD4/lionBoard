package com.github.lionboard.security;

//import com.github.lionboard.model.User;
import com.github.lionboard.error.InvalidUserException;
import com.github.lionboard.repository.UserRepository;
import org.apache.ibatis.session.SqlSession;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Lion.k on 16. 1. 28..
 */
public class DefaultJdbcDaoImpl implements UserDetailsService {

    private SqlSession sqlSession;
    public void setSqlSession(SqlSession sqlSession){
        this.sqlSession=sqlSession;
    }

    @Override
    public UserDetails loadUserByUsername(String identity)
            throws UsernameNotFoundException {
        System.out.println(sqlSession);
        com.github.lionboard.model.User user = sqlSession.getMapper(UserRepository.class).findUserByIdentity(identity);
        if(user.getIsOAuth().equals("T")){
            throw new InvalidUserException("소셜 가입 유저입니다. 소셜 로그인을 이용해주세요.");
        }
        String password=user.getPassword();
        Collection<SimpleGrantedAuthority> roles = new ArrayList<SimpleGrantedAuthority>();
        roles.add(new SimpleGrantedAuthority(user.getRoles()));
        UserDetails userDetail = new User(identity, password, roles);
        return userDetail;
    }
}