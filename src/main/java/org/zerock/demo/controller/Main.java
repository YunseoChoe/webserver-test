package org.zerock.demo.controller;

import org.zerock.demo.service.VoteListService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet("/main")
public class Main extends HttpServlet {

    private VoteListService voteListService = VoteListService.INSTANCE;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession();
        String vote_session = session.getAttribute("logininfo").toString();
        String[] splitSession = vote_session.split(":");
        String vote_writer = splitSession[0];

        try {
            ArrayList<Object[]> voteList = voteListService.voteList(vote_writer);
            // request 객체에 voteList를 속성으로 추가
            req.setAttribute("voteList", voteList);

            // JSP로 포워딩
            req.getRequestDispatcher("/WEB-INF/views/main.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
