package com.github.lionboard.triggers;

import org.h2.tools.TriggerAdapter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by daum on 16. 1. 22..
 */
public class PostTriggers extends TriggerAdapter {


    @Override
    public void fire(Connection connection, ResultSet oldRow, ResultSet newRow) throws SQLException {
        //first post seq is 1000.
        PreparedStatement ps = null;
        ResultSet rs = null;
        newRow.next();
        try {
            int postNum = 1000;
            if (newRow.getInt("depth") < 1) {
                ps = connection.prepareStatement("SELECT * FROM POST_TB WHERE depth = 0 ORDER BY postNum DESC LIMIT 1");
                rs = ps.executeQuery();
                if (rs.next()) {
                    postNum = rs.getInt("postNum") + 1000;
                    newRow.updateInt("postNum",postNum);
                } else {
                    newRow.updateInt("postNum",postNum);
                }
            } else {
                newRow.updateInt("postNum",postNum);
            }
            connection.commit();
        }catch (Exception e) {
            if(rs != null){
                try{
                    rs.close();
                }catch (Exception ex){}
            }
            if(ps != null) {
                try {
                    ps.close();
                }catch (Exception ex){}
            }
        }

    }
}
