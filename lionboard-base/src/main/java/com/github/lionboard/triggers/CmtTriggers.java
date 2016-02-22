package com.github.lionboard.triggers;

import org.h2.tools.TriggerAdapter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Lion.k on 16. 1. 22..
 */
public class CmtTriggers extends TriggerAdapter {


    @Override
    public void fire(Connection connection, ResultSet oldRow, ResultSet newRow) throws SQLException {
        //first post seq is 1000.
        PreparedStatement ps = null;
        ResultSet rs = null;
        newRow.next();
        try {
            int cmtNum = 1000;
            int parentCmtNum = newRow.getInt("cmtNum");
            // if parentCmtNum is 0, this Row is parent Row.
            if (parentCmtNum < 1) {
                ps = connection.prepareStatement("SELECT * FROM CMT_TB WHERE depth = 0 AND postId = ? ORDER BY cmtNum DESC LIMIT 1");
                ps.setInt(1, newRow.getInt("postId"));
                rs = ps.executeQuery();
                if (rs.next()) {
                    cmtNum = rs.getInt("cmtNum") + 1000;
                    newRow.updateInt("cmtNum", cmtNum);
                } else {
                    newRow.updateInt("cmtNum", cmtNum);
                }
            } else {
                newRow.updateInt("cmtNum", parentCmtNum - 1);
            }
            connection.commit();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception ex) {
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception ex) {
                }
            }
        }

    }
}
