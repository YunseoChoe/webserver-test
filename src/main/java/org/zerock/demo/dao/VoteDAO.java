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

        @Cleanup PreparedStatement preparedStatementVote = connection.prepareStatement(sql);
        ResultSet resultSetVote = preparedStatementVote.executeQuery();

        ArrayList<Object[]> voteList = new ArrayList<>();

        while (resultSetVote.next()) {
            int vote_id = resultSetVote.getInt("vote_id");
            String title = resultSetVote.getString("vote_title");
            String author = resultSetVote.getString("vote_writer");

            try (PreparedStatement preparedStatementBool = connection.prepareStatement(sql2)) {
                preparedStatementBool.setString(1, vote_writer);
                preparedStatementBool.setInt(2, vote_id);

                try (ResultSet resultSetBool = preparedStatementBool.executeQuery()) {
                    isVoted = resultSetBool.next();
                }
            }

            isMaster = author.equals(vote_writer);

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

        @Cleanup Connection connection = DBConnectionUtil.INSTANCE.getConnection();
        @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);

        preparedStatement.setInt(1, vote_id);

        @Cleanup ResultSet resultSet = preparedStatement.executeQuery();

        String voteTitle = null;
        while (resultSet.next()) {
            if (voteTitle == null) {
                Object[] voteTitleData = new Object[2];
                voteTitleData[0] = "vote_title";
                voteTitleData[1] = resultSet.getString("vote_title");
                voteData.add(voteTitleData);
                voteTitle = resultSet.getString("vote_title");
            }

            Object[] itemData = new Object[3];
            itemData[0] = "item";
            itemData[1] = resultSet.getString("item"); // item 값
            itemData[2] = resultSet.getInt("items_id"); // items_id 값
            voteData.add(itemData);
        }

        return voteData;
    }

    public ArrayList<Object[]> revote(int vote_id, String memberUsername) throws Exception {
        String sql = "SELECT v.vote_title, i.item, i.items_id FROM vote v JOIN items i ON v.vote_id = i.vote_vote_id WHERE v.vote_id = ?";
        String sql2 = "SELECT items_id FROM bools WHERE vote_vote_id = ? AND member_username = ?";

        ArrayList<Object[]> voteData = new ArrayList<>();

        @Cleanup Connection connection = DBConnectionUtil.INSTANCE.getConnection();

        @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, vote_id);
        @Cleanup ResultSet resultSet = preparedStatement.executeQuery();

        String voteTitle = null;
        while (resultSet.next()) {
            if (voteTitle == null) {
                Object[] voteTitleData = new Object[2];
                voteTitleData[0] = "vote_title";
                voteTitleData[1] = resultSet.getString("vote_title");
                voteData.add(voteTitleData);
                voteTitle = resultSet.getString("vote_title");
            }

            Object[] itemData = new Object[3];
            itemData[0] = "item";
            itemData[1] = resultSet.getString("item");
            itemData[2] = resultSet.getInt("items_id");
            voteData.add(itemData);
        }

        @Cleanup PreparedStatement preparedStatement2 = connection.prepareStatement(sql2);
        preparedStatement2.setInt(1, vote_id);
        preparedStatement2.setString(2, memberUsername);
        @Cleanup ResultSet resultSet2 = preparedStatement2.executeQuery();

        if (resultSet2.next()) {
            Object[] revoteData = new Object[2];
            revoteData[0] = "revote_item";
            revoteData[1] = resultSet2.getInt("items_id");
            voteData.add(revoteData);
        }

        return voteData;
    }


    public void voteStoreResult(int items_id, String memberUsername) throws SQLException {
        String sql = "UPDATE items SET item_count = item_count + 1 WHERE items_id = ?";
        String sql2 = "INSERT INTO bools (vote_vote_id, member_username, items_id) " +
                "SELECT vote_vote_id, ?, items_id FROM items WHERE items_id = ?";

        try (Connection connection = DBConnectionUtil.INSTANCE.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, items_id);

                int rowsUpdated = preparedStatement.executeUpdate();

                if (rowsUpdated > 0) {
                    System.out.println("Item count was updated successfully.");
                } else {
                    System.out.println("No rows were updated for item count.");
                }
            }



            try (PreparedStatement preparedStatement2 = connection.prepareStatement(sql2)) {
                preparedStatement2.setString(1, memberUsername);
                preparedStatement2.setInt(2, items_id);

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
        String sql = "UPDATE bools SET items_id = ? WHERE member_username = ? AND vote_vote_id = ?";

        try (Connection connection = DBConnectionUtil.INSTANCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, items_id);
            preparedStatement.setString(2, memberUsername);
            preparedStatement.setInt(3, voteId);

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

        @Cleanup Connection connection = DBConnectionUtil.INSTANCE.getConnection();
        @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, vote_id);
        @Cleanup ResultSet resultSet = preparedStatement.executeQuery();

        String voteTitle = null;
        while (resultSet.next()) {
            if (voteTitle == null) {
                Object[] voteTitleData = new Object[2];
                voteTitleData[0] = "vote_title";
                voteTitleData[1] = resultSet.getString("vote_title");
                voteData.add(voteTitleData);
                voteTitle = resultSet.getString("vote_title");
            }

            Object[] itemData = new Object[3];
            itemData[0] = "item";
            itemData[1] = resultSet.getString("item");
            itemData[2] = resultSet.getInt("item_count");
            voteData.add(itemData);
        }
        return voteData;
    }

}

