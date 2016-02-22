package com.github.kd4.lionboard.dao;

import com.github.kd4.lionboard.domain.CommentStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by daum on 16. 2. 22..
 */


public class CommentStatusDaoImpl implements CommentStatusDao {
    private DataSource dataSource;

    private JdbcTemplate jdbcTemplate;
    private RowMapper<CommentStatus> rowMapper =
            new RowMapper<CommentStatus>() {
                @Override
                public CommentStatus mapRow(ResultSet resultSet, int i) throws SQLException {
                    CommentStatus commentStatus = new CommentStatus();
                    commentStatus.setCmtId(resultSet.getInt("cmtId"));
                    commentStatus.setPastDays(resultSet.getInt("pastDays"));
                    commentStatus.setCmtStatus(resultSet.getString("cmtStatus"));
                    return commentStatus;
                }
            };


    public CommentStatusDaoImpl() {
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);

    }

    @Override
    public List<CommentStatus> getAll() {
        return this.jdbcTemplate.query("select * from CMT_STATUS_TB order by cmtId", rowMapper);
    }

    @Override
    public void update(CommentStatus commentStatus) {
        this.jdbcTemplate.update("update CMT_STATUS_TB set cmtStatus =?, pastDays = ? where cmtId = ? ",
                commentStatus.getCmtStatus(), commentStatus.getPastDays(), commentStatus.getCmtId());
    }
}
