package org.zerock.demo.dao;

import lombok.Cleanup;
import org.zerock.demo.vo.LoginVO;
import org.zerock.demo.vo.Member;  // Member VO 클래스
import org.zerock.demo.util.DBConnectionUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;

import java.sql.PreparedStatement;

public class MemberDAO {
    // 회원 정보 삽입 메서드
    public void insert(Member member) throws Exception {
        String sql = "INSERT INTO member (username, password) VALUES (?, ?)";

        @Cleanup Connection connection = DBConnectionUtil.INSTANCE.getConnection();
        @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);

        // PreparedStatement에 데이터 설정
        preparedStatement.setString(1, member.getUsername());
        preparedStatement.setString(2, member.getPassword());

        // 쿼리 실행
        preparedStatement.executeUpdate();
    }

    public boolean equal(LoginVO loginVO) throws SQLException {
        String sql = "SELECT password FROM member WHERE username = ?";
        boolean isLoginSuccessful = false;

        @Cleanup Connection connection = DBConnectionUtil.INSTANCE.getConnection();
        @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, loginVO.getUsername());

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String dbPassword = rs.getString("password");

                    // DB에서 가져온 비밀번호와 입력받은 비밀번호 비교
                    if (dbPassword.equals(loginVO.getPassword())) {
                        isLoginSuccessful = true;
                    }
                }
            }
        }
        return isLoginSuccessful;
    }

    public boolean duplicate(Member member) throws SQLException {
        String sql = "SELECT username FROM member where username = ?";
        boolean isDuplicated = false;
        @Cleanup Connection connection = DBConnectionUtil.INSTANCE.getConnection();
        @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, member.getUsername());

            ResultSet rs = pstmt.executeQuery();
            System.out.println(rs.next());

            return rs.next();
        }
    }
}
