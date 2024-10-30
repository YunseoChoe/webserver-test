<%@ page import="java.util.List" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>투표결과</title>
  <style>
    .button-link {
      display: inline-block;
      padding: 10px 20px;
      background-color: #007bff;
      color: white;
      text-decoration: none;
      border-radius: 5px;
    }
    .button-link:hover {
      background-color: #0056b3;
    }
  </style>
</head>
<body>

<h1>투표결과</h1>

<h2>투표 제목: <%= request.getAttribute("vote_title") %></h2>

<h3>투표 항목:</h3>

<table>
  <tr>
    <th>항목</th>
    <th>득표수</th>
  </tr>

  <%
    List<String> item = (List<String>) request.getAttribute("item");
    List<Integer> item_count = (List<Integer>) request.getAttribute("item_count");

    if (item == null || item.isEmpty() || item_count == null || item_count.isEmpty()) {
  %>
  <tr>
    <td colspan="2">투표 항목이 없습니다.</td>
  </tr>
  <%
  } else if (item.size() != item_count.size()) {
  %>
  <tr>
    <td colspan="2">항목과 득표수의 개수가 일치하지 않습니다.</td>
  </tr>
  <%
  } else {
    for (int i = 0; i < item.size(); i++) {
  %>
  <tr>
    <td><%= item.get(i) %></td>
    <td><%= item_count.get(i) %></td>
  </tr>
  <%
      }
    }
  %>
</table>

<a href="/main" class="button-link">완료</a>

</body>
</html>
