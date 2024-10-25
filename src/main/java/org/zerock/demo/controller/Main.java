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
        System.out.println("!");
        System.out.println(session);
        String vote_session = session.getAttribute("logininfo").toString();
        System.out.println(vote_session);
        String[] splitSession = vote_session.split(":");
        String vote_writer = splitSession[0];


        System.out.println(vote_writer);

        try {
            ArrayList<Object[]> voteList = voteListService.voteList(vote_writer);
            req.setAttribute("voteList", voteList);

            req.getRequestDispatcher("/WEB-INF/views/main.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
