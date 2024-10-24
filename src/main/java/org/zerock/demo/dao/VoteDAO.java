package org.zerock.demo.dao;

import lombok.Cleanup;
import org.zerock.demo.util.DBConnectionUtil;
import org.zerock.demo.vo.ItemVO;
import org.zerock.demo.vo.VoteVO;

import java.sql.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


    public ArrayList<Object[]> VoteList(String vote_writer) throws Exception {
        String sql = "SELECT * FROM vote";
        String sql2 = "SELECT * FROM bools WHERE member_username = ? AND vote_vote_id = ?";

        boolean isMaster = false;
        boolean isVoted = false;

        @Cleanup Connection connection = DBConnectionUtil.INSTANCE.getConnection();

        // 첫 번째 쿼리: vote 테이블에서 모든 투표 정보 가져오기
        @Cleanup PreparedStatement preparedStatementVote = connection.prepareStatement(sql);
        ResultSet resultSetVote = preparedStatementVote.executeQuery();

        ArrayList<Object[]> voteList = new ArrayList<>();

        while (resultSetVote.next()) {
            Integer vote_id = resultSetVote.getInt("vote_id");
            String title = resultSetVote.getString("vote_title");
            String author = resultSetVote.getString("vote_writer");

            // 두 번째 쿼리: 해당 유저가 이 투표에 참여했는지 확인
            try (PreparedStatement preparedStatementBool = connection.prepareStatement(sql2)) {
                preparedStatementBool.setString(1, vote_writer);
                preparedStatementBool.setInt(2, vote_id);

                try (ResultSet resultSetBool = preparedStatementBool.executeQuery()) {
                    isVoted = resultSetBool.next(); // 결과가 있으면 유저가 이미 투표한 것
                }
            }

            // 작성자와 로그인 유저가 같으면 삭제 버튼 허용
            isMaster = author.equals(vote_writer);

            // Vote 데이터를 리스트에 추가
            voteList.add(new Object[]{vote_id, title, author, isMaster, isVoted});
        }

        return voteList;
    }


//    public ArrayList<Object[]> voteDetailList(int detail) throws Exception {
//        String sql = "SELECT * FROM items WHERE vote_id LIKE ?";
//        @Cleanup Connection connection = DBConnectionUtil.INSTANCE.getConnection();
//        @Cleanup PreparedStatement preparedStatement2 = connection.prepareStatement(sql);
//
//
//
//
//    }

}
