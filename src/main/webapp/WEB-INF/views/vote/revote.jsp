<%@ page import="java.util.List" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>재투표</title>
</head>
<body>

<h1>재투표</h1>

<!-- 투표 제목 출력 -->
<h2>투표 제목: <%= request.getAttribute("vote_title") %></h2>

<!-- 투표 항목 출력 -->
<h3>투표 항목:</h3>


<form action="/revote" method="post">
  <%
    List<String> item = (List<String>) request.getAttribute("item");
    List<Integer> item_id = (List<Integer>) request.getAttribute("item_id");
    Integer revoteItemId = (Integer) request.getAttribute("revoteItemId");
    Integer voteId = (Integer) request.getAttribute("voteId");
    for(int i = 0; i < item.size(); i++) {
      if(item_id.get(i).equals(revoteItemId)) {

  %>
  <input type="hidden" name="voteId" value="<%= voteId %>">
  <!-- 각 항목에 대해 radio 버튼과 label 출력 -->
  <input type="radio" name="selectedItem" value="<%= item_id.get(i) %>" id="item<%= item_id.get(i) %>" checked="checked" required>
  <label for="item<%= item_id.get(i) %>"><%= item.get(i) %></label>
  <% }
  else {

  %>
  <input type="radio" name="selectedItem" value="<%= item_id.get(i) %>" id="item<%= item_id.get(i) %>"required>
  <label for="item<%= item_id.get(i) %>"><%= item.get(i) %></label>
  <% }%>
  <%
    }
  %>

  <button type="submit">완료</button>
</form>


</body>
</html>