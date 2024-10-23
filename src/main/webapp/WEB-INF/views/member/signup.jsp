<%--
  Created by IntelliJ IDEA.
  User: choeyunseo
  Date: 2024. 10. 23.
  Time: 오후 5:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>회원가입</title>
</head>
<body>
<h2>회원가입</h2>

<form action="signup" method="post">
    <label for="username">아이디:</label>
    <input type="text" id="username" name="username" required><br><br>

    <label for="password">비밀번호:</label>
    <input type="password" id="password" name="password" required><br><br>

    <input type="submit" value="제출">
</form>

<!-- 오류 메시지 처리 -->
<%--<% String error = request.getParameter("error");--%>
<%--    if (error != null && error.equals("1")) { %>--%>
<%--<p style="color: red;">회원가입 중 오류가 발생했습니다. 다시 시도해 주세요.</p>--%>
<%--<% } %>--%>
</body>
</html>

