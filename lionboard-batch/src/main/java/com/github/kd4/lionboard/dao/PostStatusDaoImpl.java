package com.github.kd4.lionboard.dao;


import com.github.kd4.lionboard.domain.PostStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by daum on 16. 2. 22..
 */


public class PostStatusDaoImpl implements PostStatusDao {


    private DataSource dataSource;

    private JdbcTemplate jdbcTemplate;
    private RowMapper<PostStatus> rowMapper =
            new RowMapper<PostStatus>() {
                @Override
                public PostStatus mapRow(ResultSet resultSet, int i) throws SQLException {
                    PostStatus postReport = new PostStatus();
                    postReport.setPostId(resultSet.getInt("postId"));
                    postReport.setPastDays(resultSet.getInt("pastDays"));
                    postReport.setPostStatus(resultSet.getString("postStatus"));
                    return postReport;
                }
            };


    public PostStatusDaoImpl() {
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);

    }

    @Override
    public List<PostStatus> getAll() {
        return this.jdbcTemplate.query("select * from POST_STATUS_TB order by postId", rowMapper);
    }


    @Override
    public void update(PostStatus postStatus) {
        this.jdbcTemplate.update("update POST_STATUS_TB set postStatus =?, pastDays = ? where postId = ? ",
                postStatus.getPostStatus(), postStatus.getPastDays(), postStatus.getPostId());
    }
}
