package org.zerock.demo.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/submit")
public class FormController extends HttpServlet {

    // GET 요청을 처리하는 메소드
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 만약 폼 페이지로 리다이렉트하거나, 폼을 보여주고 싶다면 여기에 추가
        request.getRequestDispatcher("/WEB-INF/views/form.jsp").forward(request, response);
    }

    // POST 요청을 처리하는 메소드
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        String username = request.getParameter("username");

        // 데이터를 JSP로 전달
        request.setAttribute("username", username);

        // JSP로 포워딩
        request.getRequestDispatcher("/WEB-INF/views/result.jsp").forward(request, response);
    }
}
