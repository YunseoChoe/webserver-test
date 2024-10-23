package org.zerock.demo.controller.member;

import lombok.SneakyThrows;
import org.zerock.demo.dto.LoginDTO;
import org.zerock.demo.service.LoginService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/")
public class LoginController extends HttpServlet {
    private LoginService loginService = LoginService.INSTANCE;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
//        HttpSession session = request.getSession();
//
//        if(session.isNew()) {
//            response.sendRedirect("/");
//            return;
//        }
//
//        if(session.getAttribute("logininfo") == null) {
//            response.sendRedirect("/");
//            return;
//        }

        // 정상적인 경우


        request.getRequestDispatcher("WEB-INF/login.jsp").forward(request,response);
    }

    @SneakyThrows
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String str = username + ":"+ password;

        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername(username);
        loginDTO.setPassword(password);

        if (loginService.login(loginDTO)) {
            HttpSession session = request.getSession();
            session.setAttribute("logininfo", str);
            System.out.println("로그인 성공");

//            response.sendRedirect("/main");
            request.getRequestDispatcher("WEB-INF/views/main.jsp").forward(request,response);
        }
        else {
            request.getRequestDispatcher("/WEB-INF/views/member/loginerror.jsp").forward(request, response);
            System.out.println("로그인 에러");
        }

    }


}
