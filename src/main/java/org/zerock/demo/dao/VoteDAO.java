package org.zerock.demo.dao;

import lombok.Cleanup;
import lombok.extern.log4j.Log4j;
import org.zerock.demo.util.DBConnectionUtil;
import org.zerock.demo.vo.ItemVO;
import org.zerock.demo.vo.VoteVO;

import java.sql.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mysql.cj.conf.PropertyKey.logger;

public class VoteDAO {
    public int voteInsert(VoteVO voteVO) throws Exception {
        String sql = "INSERT INTO vote (vote_title, vote_writer) VALUES (?,?)";

        @Cleanup Connection connection = DBConnectionUtil.INSTANCE.getConnection();
        @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        System.out.println(voteVO.getVote_title());
        System.out.println(voteVO.getVote_writer());


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
            int vote_id = resultSetVote.getInt("vote_id");
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

    public void voteDelete(int vote_id) throws Exception {
        String sql = "DELETE FROM vote WHERE vote_id = ?";
        @Cleanup Connection connection = DBConnectionUtil.INSTANCE.getConnection();
        @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, vote_id);
        preparedStatement.executeUpdate();
    }

    public ArrayList<Object[]> vote(int vote_id) throws Exception {
        String sql = "SELECT v.vote_title, i.item, i.items_id FROM vote v JOIN items i ON v.vote_id = i.vote_vote_id WHERE v.vote_id = ?";

        ArrayList<Object[]> voteData = new ArrayList<>();

        // DB 연결 및 쿼리 실행
        @Cleanup Connection connection = DBConnectionUtil.INSTANCE.getConnection();
        @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);

        // SQL 쿼리에 vote_id 값을 설정
        preparedStatement.setInt(1, vote_id);

        // 쿼리 실행
        @Cleanup ResultSet resultSet = preparedStatement.executeQuery();

        String voteTitle = null;
        while (resultSet.next()) {
            if (voteTitle == null) {
                // 첫 번째 행에서만 vote_title을 설정
                Object[] voteTitleData = new Object[2];
                voteTitleData[0] = "vote_title";
                voteTitleData[1] = resultSet.getString("vote_title");
                voteData.add(voteTitleData);
                voteTitle = resultSet.getString("vote_title");
            }

            // 각 행에서 item과 items_id 추가
            Object[] itemData = new Object[3]; // 3개의 요소를 저장할 배열 생성
            itemData[0] = "item";
            itemData[1] = resultSet.getString("item"); // item 값
            itemData[2] = resultSet.getInt("items_id"); // items_id 값
            voteData.add(itemData);
        }

        return voteData;
    }

    public ArrayList<Object[]> revote(int vote_id, String memberUsername) throws Exception {
        // 첫 번째 쿼리: vote와 items 테이블을 조인해 vote_title, item, items_id를 가져오는 쿼리
        String sql = "SELECT v.vote_title, i.item, i.items_id FROM vote v JOIN items i ON v.vote_id = i.vote_vote_id WHERE v.vote_id = ?";

        // 두 번째 쿼리: bools 테이블에서 vote_vote_id와 member_username을 기준으로 items_id를 가져오는 쿼리
        String sql2 = "SELECT items_id FROM bools WHERE vote_vote_id = ? AND member_username = ?";

        ArrayList<Object[]> voteData = new ArrayList<>();

        // DB 연결 및 첫 번째 쿼리 실행
        @Cleanup Connection connection = DBConnectionUtil.INSTANCE.getConnection();

        // 첫 번째 쿼리 실행
        @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, vote_id);
        @Cleanup ResultSet resultSet = preparedStatement.executeQuery();

        String voteTitle = null;
        while (resultSet.next()) {
            if (voteTitle == null) {
                // 첫 번째 행에서만 vote_title을 설정
                Object[] voteTitleData = new Object[2];
                voteTitleData[0] = "vote_title";
                voteTitleData[1] = resultSet.getString("vote_title");
                voteData.add(voteTitleData);
                voteTitle = resultSet.getString("vote_title");
            }

            // 각 행에서 item과 items_id 추가
            Object[] itemData = new Object[3];
            itemData[0] = "item";
            itemData[1] = resultSet.getString("item");
            itemData[2] = resultSet.getInt("items_id");
            voteData.add(itemData);
        }

        // 두 번째 쿼리 실행: 해당 회원이 이미 투표한 항목을 가져오기
        @Cleanup PreparedStatement preparedStatement2 = connection.prepareStatement(sql2);
        preparedStatement2.setInt(1, vote_id);
        preparedStatement2.setString(2, memberUsername);
        @Cleanup ResultSet resultSet2 = preparedStatement2.executeQuery();

        if (resultSet2.next()) {
            // 이미 투표한 항목이 있을 경우
            Object[] revoteData = new Object[2];
            revoteData[0] = "revote_item";
            revoteData[1] = resultSet2.getInt("items_id"); // 이미 투표한 items_id
            voteData.add(revoteData);
        }

        return voteData;
    }


    public void voteStoreResult(int items_id, String memberUsername) throws SQLException {
        // 첫 번째 쿼리: items 테이블의 item_count를 1 증가시키는 쿼리
        String sql = "UPDATE items SET item_count = item_count + 1 WHERE items_id = ?";

        // 두 번째 쿼리: items 테이블에서 vote_vote_id와 member_username을 bools 테이블에 삽입하는 쿼리
        String sql2 = "INSERT INTO bools (vote_vote_id, member_username, items_id) " +
                "SELECT vote_vote_id, ?, items_id FROM items WHERE items_id = ?";

        try (Connection connection = DBConnectionUtil.INSTANCE.getConnection()) {

            // 첫 번째 쿼리 실행: item_count 증가
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                // items_id 값을 첫 번째 쿼리의 파라미터에 설정
                preparedStatement.setInt(1, items_id);

                // 첫 번째 쿼리 실행
                int rowsUpdated = preparedStatement.executeUpdate();

                if (rowsUpdated > 0) {
                    System.out.println("Item count was updated successfully.");
                } else {
                    System.out.println("No rows were updated for item count.");
                }
            }

            // 두 번째 쿼리 실행: vote_vote_id와 member_username을 bools 테이블에 삽입
            try (PreparedStatement preparedStatement2 = connection.prepareStatement(sql2)) {
                // 두 번째 쿼리의 파라미터에 member_username과 items_id 값을 설정
                preparedStatement2.setString(1, memberUsername);
                preparedStatement2.setInt(2, items_id);

                // 두 번째 쿼리 실행
                int rowsInserted = preparedStatement2.executeUpdate();

                if (rowsInserted > 0) {
                    System.out.println("Vote_vote_id and member_username were inserted successfully.");
                } else {
                    System.out.println("No rows were inserted into bools.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Failed to update item count or insert vote_vote_id and member_username", e);
        }
    }

    public void revoteStoreResult(int items_id, String memberUsername, int voteId) throws SQLException {


        // SQL 쿼리: 사용자의 투표 항목을 업데이트하는 쿼리
        String sql = "UPDATE bools SET items_id = ? WHERE member_username = ? AND vote_vote_id = ?";

        try (Connection connection = DBConnectionUtil.INSTANCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            // 파라미터 설정
            preparedStatement.setInt(1, items_id); // 선택한 항목의 items_id
            preparedStatement.setString(2, memberUsername); // 사용자 이름
            preparedStatement.setInt(3, voteId);

            // 쿼리 실행
            int rowsUpdated = preparedStatement.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("User vote updated successfully.");
            } else {
                System.out.println("No matching user found for update.");
            }
        } catch (SQLException e) {
            throw new SQLException("Failed to update user vote", e);
        }
    }

    public ArrayList<Object[]> voteResult(int vote_id) throws SQLException {
        String sql = "SELECT v.vote_title, i.item, i.items_id, i.item_count FROM vote v JOIN items i ON v.vote_id = i.vote_vote_id WHERE v.vote_id = ?";
        ArrayList<Object[]> voteData = new ArrayList<>();

        // DB 연결 및 첫 번째 쿼리 실행
        @Cleanup Connection connection = DBConnectionUtil.INSTANCE.getConnection();

        // 첫 번째 쿼리 실행
        @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, vote_id);
        @Cleanup ResultSet resultSet = preparedStatement.executeQuery();

        String voteTitle = null;
        while (resultSet.next()) {
            if (voteTitle == null) {
                // 첫 번째 행에서만 vote_title을 설정
                Object[] voteTitleData = new Object[2];
                voteTitleData[0] = "vote_title";
                voteTitleData[1] = resultSet.getString("vote_title");
                voteData.add(voteTitleData);
                voteTitle = resultSet.getString("vote_title");
            }

            // 각 행에서 item과 items_id 추가
            Object[] itemData = new Object[3];
            itemData[0] = "item";
            itemData[1] = resultSet.getString("item");
            itemData[2] = resultSet.getString("item_count");
            voteData.add(itemData);
        }
        return voteData;
        }
    }

