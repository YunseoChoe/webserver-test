package org.zerock.demo.dao;

import lombok.Cleanup;
import org.zerock.demo.util.DBConnectionUtil;
import org.zerock.demo.vo.ItemVO;
import org.zerock.demo.vo.VoteVO;

import java.sql.*;

import java.util.ArrayList;

public class VoteDAO {
    public int voteInsert(VoteVO voteVO) throws Exception {
        String sql = "INSERT INTO vote (vote_title, vote_writer) VALUES (?,?)";

        @Cleanup Connection connection = DBConnectionUtil.INSTANCE.getConnection();
        @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);


        //voteRegister
        preparedStatement.setString(1, voteVO.getVote_title());
        preparedStatement.setString(2, voteVO.getVote_writer());
        preparedStatement.executeUpdate();

        ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
        if (generatedKeys.next()) {
            return generatedKeys.getInt(1); // 생성된 vote_id 반환
        } else {
            throw new SQLException("Creating vote failed, no ID obtained.");
        }

    }

    public void itemInsert(ItemVO itemVO, int vote_vote_id) throws Exception {
        String sql2 = "INSERT INTO items (item, vote_vote_id) VALUES (?, ?)";

        @Cleanup Connection connection = DBConnectionUtil.INSTANCE.getConnection();
        @Cleanup PreparedStatement preparedStatement2 = connection.prepareStatement(sql2);

        ArrayList<String> items = itemVO.getVote_items();


        for(String item : items) {
            preparedStatement2.setString(1, item);
            preparedStatement2.setInt(2, vote_vote_id);
            preparedStatement2.executeUpdate();
        }
    }

}
