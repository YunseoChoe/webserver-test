<%--
  Created by IntelliJ IDEA.
  User: choeyunseo
  Date: 2024. 10. 23.
  Time: 오후 5:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="java.util.List" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>투표</title>
</head>
<body>

<h1>투표</h1>

<h2>투표 제목: <%= request.getAttribute("vote_title") %></h2>

<h3>투표 항목:</h3>

<form action="/vote" method="post">
  <%
    List<String> item = (List<String>) request.getAttribute("item");
    List<Integer> item_id = (List<Integer>) request.getAttribute("item_id");
    for(int i = 0; i < item.size(); i++) {
  %>
  <input type="radio" name="selectedItem" value="<%= item_id.get(i) %>" id="item<%= item_id.get(i) %>" required>
  <label for="item<%= item_id.get(i) %>"><%= item.get(i) %></label>
  <%
    }
  %>

  <button type="submit">완료</button>
</form>


</body>
</html>
