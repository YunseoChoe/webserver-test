<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>투표 등록</title>
    <script type="text/javascript">
        // 투표 항목을 추가하는 함수
        function addVoteItem() {
            // 투표 항목을 추가할 div
            var container = document.getElementById("vote_items_container");

            // 새로운 div 생성 (input과 x 버튼을 담을 container)
            var newDiv = document.createElement("div");
            newDiv.className = "vote-item";

            // 새로운 input text 생성
            var newInput = document.createElement("input");
            newInput.type = "text";
            newInput.name = "vote_items"; // 이름을 동일하게 설정해 서버로 배열 형태로 전달됨
            newInput.placeholder = "투표 항목을 입력하세요";
            newInput.required = true; // 각 항목이 필수 입력

            // X 버튼 생성
            var removeButton = document.createElement("button");
            removeButton.type = "button";
            removeButton.innerText = "X"; // X 표시
            removeButton.onclick = function() {
                container.removeChild(newDiv); // 클릭 시 항목 삭제
            };

            // div에 input과 x 버튼 추가
            newDiv.appendChild(newInput);
            newDiv.appendChild(removeButton);

            // 새로운 div를 투표 항목 container에 추가
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

    <!-- 투표 항목 입력 (처음에 하나만 표시됨) -->
    <label>투표 항목:</label><br>
    <div id="vote_items_container">
        <!-- 첫 번째 투표 항목은 미리 생성 -->
        <div class="vote-item">
            <input type="text" name="vote_items" placeholder="투표 항목을 입력하세요" required>
            <button type="button" onclick="this.parentNode.remove()">X</button><br>
        </div>
    </div>

    <!-- 투표 항목 추가 버튼 -->
    <button type="button" onclick="addVoteItem()">+ 항목 추가</button><br><br>

    <!-- 폼 제출 버튼 -->
    <input type="submit" value="투표 등록">
</form>

</body>
</html>
