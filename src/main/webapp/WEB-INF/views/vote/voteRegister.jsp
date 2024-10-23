<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>투표 등록</title>
</head>
<body>
<h2>투표 등록</h2>

<!-- 투표 등록 폼 -->
<form action="voteregister" method="post">
    <!-- 투표 제목 입력 -->
    <label for="vote_title">투표 제목:</label>
    <input type="text" id="vote_title" name="vote_title" required><br><br>

    <!-- 투표 항목 선택 -->
    <label>투표 항목:</label><br>
    <input type="checkbox" name="vote_items" value="항목 1"> 항목 1<br>
    <input type="checkbox" name="vote_items" value="항목 2"> 항목 2<br>
    <input type="checkbox" name="vote_items" value="항목 3"> 항목 3<br>
    <input type="checkbox" name="vote_items" value="항목 4"> 항목 4<br><br>

    <!-- 폼 제출 버튼 -->
    <input type="submit" value="투표 등록">
</form>

</body>
</html>