package org.zerock.demo.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public enum DBConnectionUtil {
    INSTANCE;
    // 데이터베이스 URL (MySQL 사용)
    private static final String URL = "jdbc:mysql://localhost:3306/sky09508_db";
    private static final String USER = "root";  // 사용자 이름
    private static final String PASSWORD = "1111";  // 비밀번호

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");  // MySQL 드라이버 로드
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL JDBC Driver not found", e);
        }
    }

    // 데이터베이스 연결을 가져오는 메서드
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
