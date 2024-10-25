package org.zerock.demo.controller.vote;

import org.zerock.demo.service.VoteDeleteService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/voteDelete")
public class VoteDeleteController extends HttpServlet {

    private VoteDeleteService voteDeleteService = VoteDeleteService.INSTANCE;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();

        String vote_session = session.getAttribute("logininfo").toString();
        String[] splitSession = vote_session.split(":");
        String vote_writer = splitSession[0];

        String voteId = req.getParameter("voteId");
        Integer vote_Id = Integer.parseInt(voteId);

        try {
            voteDeleteService.voteDelete(vote_Id);
            System.out.println("삭제 성공!");
            resp.sendRedirect("/main");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }
}
