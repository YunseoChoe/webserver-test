<%@ page import="java.util.List" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>투표결과</title>
</head>
<body>

<h1>투표결과</h1>

<!-- 투표 제목 출력 -->
<h2>투표 제목: <%= request.getAttribute("vote_title") %></h2>

<!-- 투표 항목 출력 -->
<h3>투표 항목:</h3>


<table>
  <tr>
    <th>항목</th>
    <th>득표수</th>
  </tr>

  <%
    List<String> item = (List<String>) request.getAttribute("item");
    List<Integer> item_count = (List<Integer>) request.getAttribute("item_count");
    for(int i = 0; i < item.size(); i++) {
  %>

  <tr>
    <td>
      <%=item.get(i)%>
    </td>
    <td>
      <%=item_count.get(i)%>
    </td>
  </tr>

  <% }%>

</table>


  <a href="/main"><button>완료</button></a>



</body>
</html>
