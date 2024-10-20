<%--
  Created by IntelliJ IDEA.
  User: choeyunseo
  Date: 2024. 10. 20.
  Time: 오후 3:52
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>User Form</title>
</head>
<body>
<h1>이름을 적어주세요</h1>
<form action="submit" method="post">
  <label for="username">이름: </label>
  <input type="text" id="username" name="username">
  <button type="submit">제출</button>
</form>
</body>
</html>