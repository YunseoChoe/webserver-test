<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="java.util.ArrayList" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>메인 페이지</title>
</head>
<body>
<table border="1">
    <tr>
        <th>title</th>
        <th>writer</th>
        <th>let's go</th>
        <th>삭제</th>
    </tr>
    <%
        Object voteListObj = request.getAttribute("voteList");
        if (voteListObj instanceof ArrayList) {
            ArrayList<Object[]> voteList = (ArrayList<Object[]>) voteListObj;
            if (voteList != null && !voteList.isEmpty()) {
                for (Object[] vote : voteList) {
                    // vote[0] = vote_id, vote[1] = title, vote[2] = author (예시)
    %>
    <tr>
        <td><%= vote[1] %></td> <!-- 제목 -->
        <td><%= vote[2] %></td> <!-- 작성자 -->
        <td>
            <!-- 투표 상세로 이동하는 버튼 -->
            <form action="/voteDetail" method="get" style="margin: 0;">
                <input type="hidden" name="voteId" value="<%= vote[0] %>">
                <% boolean isVoted = (boolean) vote[4];
                    if(isVoted) {
                %>
                <button type="submit">재투표하기</button>
                <% } else {%>
                <button type="submit">투표하기</button>
                <% } %>
            </form>
        </td>
        <td>
            <!-- 삭제 버튼 -->
            <form action="/deleteVote" method="post" style="margin: 0;">
                <input type="hidden" name="voteId" value="<%= vote[0] %>">
                <button type="submit" onclick="return confirm('정말 삭제하시겠습니까?');">삭제</button>
            </form>
        </td>
    </tr>
    <%
        }
    } else {
    %>
    <tr>
        <td colspan="4">투표 목록이 없습니다.</td>
    </tr>
    <%
        }
    } else {
    %>
    <tr>
        <td colspan="4">데이터를 불러오지 못했습니다.</td>
    </tr>
    <%
        }
    %>
</table>
</body>
</html>
