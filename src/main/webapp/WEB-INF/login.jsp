<%--
  Created by IntelliJ IDEA.
  User: choeyunseo
  Date: 2024. 10. 23.
  Time: 오후 6:23
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>로그인</title>
</head>
<body>
<h2>로그인</h2>

<form action="/" method="post">
  <label for="username">아이디:</label>
  <input type="text" id="username" name="username" required><br><br>

  <label for="password">비밀번호:</label>
  <input type="password" id="password" name="password" required><br><br>

  <input type="submit" value="로그인">
</form>

<a href="/signup">
  <button type="submit">회원가입</button>
</a>
</body>
</html>
