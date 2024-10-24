package org.zerock.demo.controller.vote;

import org.zerock.demo.dto.VoteRegisterDTO;
import org.zerock.demo.service.VoteRegisterService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

@WebServlet("/voteregister")
public class VoteRegisterController extends HttpServlet {
    private VoteRegisterService voteRegisterService = VoteRegisterService.INSTANCE;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("투표 작성하기로 이동 중 ..");
        req.getRequestDispatcher("/WEB-INF/views/vote/voteRegister.jsp").forward(req, resp);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws SecurityException, IOException {

        HttpSession session = request.getSession();

        String vote_title = request.getParameter("vote_title");
        String vote_session = session.getAttribute("logininfo").toString();
        String[] voteItemsArray = request.getParameterValues("vote_items");

        String[] splitSession = vote_session.split(":");
        String vote_writer = splitSession[0];

        // 배열을 ArrayList로 변환
        ArrayList<String> vote_items = new ArrayList<>();

        if (voteItemsArray != null) {
            vote_items.addAll(Arrays.asList(voteItemsArray));
        }

        VoteRegisterDTO voteRegisterDTO = new VoteRegisterDTO();
        voteRegisterDTO.setVote_title(vote_title);
        voteRegisterDTO.setVote_writer(vote_writer);
        voteRegisterDTO.setVote_items(vote_items);

        try {
            voteRegisterService.voteRegister(voteRegisterDTO);
            request.getRequestDispatcher("WEB-INF/views/main.jsp");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
