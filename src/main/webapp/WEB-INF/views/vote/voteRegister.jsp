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
    <title>투표 등록</title>
    <script type="text/javascript">
        function addVoteItem() {
            var container = document.getElementById("vote_items_container");

            var newDiv = document.createElement("div");
            newDiv.className = "vote-item";

            var newInput = document.createElement("input");
            newInput.type = "text";
            newInput.name = "vote_items";
            newInput.placeholder = "투표 항목을 입력하세요";
            newInput.required = true;

            var removeButton = document.createElement("button");
            removeButton.type = "button";
            removeButton.innerText = "X"; // X 표시
            removeButton.onclick = function() {
                container.removeChild(newDiv);
            };

            newDiv.appendChild(newInput);
            newDiv.appendChild(removeButton);

            container.appendChild(newDiv);
        }
    </script>
</head>
<body>
<h2>투표 등록</h2>

<!-- 투표 등록 폼 -->
<form action="/voteregister" method="post">
    <!-- 투표 제목 입력 -->
    <label for="vote_title">투표 제목:</label>
    <input type="text" id="vote_title" name="vote_title" required><br><br>

    <label>투표 항목:</label><br>
    <div id="vote_items_container">
        <div class="vote-item">
            <input type="text" name="vote_items" placeholder="투표 항목을 입력하세요" required>
            <button type="button" onclick="this.parentNode.remove()">X</button><br>
        </div>
    </div>

    <button type="button" onclick="addVoteItem()">+ 항목 추가</button><br><br>

    <input type="submit" value="투표 등록">
</form>

</body>
</html>
