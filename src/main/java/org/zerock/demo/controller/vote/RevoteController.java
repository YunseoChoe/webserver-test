package org.zerock.demo.controller.vote;

import org.zerock.demo.service.VoteService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/revote")
public class RevoteController extends HttpServlet {

    private VoteService voteService = VoteService.INSTANCE;
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String voteIdParam = req.getParameter("voteId");

        HttpSession session = req.getSession();

        String vote_session = session.getAttribute("logininfo").toString();
        String[] splitSession = vote_session.split(":");
        String vote_writer = splitSession[0];


        try {

            int voteId = Integer.parseInt(voteIdParam);


            ArrayList<Object[]> voteData = voteService.revote(voteId, vote_writer);

            if (voteData != null && !voteData.isEmpty()) {
                List<String> item = new ArrayList<>();
                List<Integer> item_id = new ArrayList<>();
                Integer revoteItemId = null;

                for (Object[] data : voteData) {
                    if (data[0].equals("vote_title")) {
                        // vote_title 설정
                        req.setAttribute("vote_title", data[1]);
                    } else if (data[0].equals("item")) {
                        // item 값 추가
                        item.add((String) data[1]);
                    }


                    if (data.length > 2 && data[2] != null) {
                        item_id.add((Integer) data[2]);
                    }

                    if (data[0].equals("revote_item")) {
                        revoteItemId = (Integer) data[1];
                    }
                }


                // 리스트를 request 객체에 설정
                req.setAttribute("item", item);
                req.setAttribute("item_id", item_id);
                req.setAttribute("revoteItemId", revoteItemId);
                req.setAttribute("voteId", voteId);
            } else {
                req.setAttribute("error_message", "해당 투표를 찾을 수 없습니다.");
            }

            // 결과를 JSP로 포워딩
            RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/vote/revote.jsp");
            dispatcher.forward(req, resp);

        } catch (NumberFormatException e) {
            req.setAttribute("error_message", "유효하지 않은 투표 ID입니다.");
            RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/error.jsp");
            dispatcher.forward(req, resp);
        } catch (SQLException e) {
            e.printStackTrace();
            req.setAttribute("error_message", "데이터베이스 오류가 발생했습니다.");
            RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/error.jsp");
            dispatcher.forward(req, resp);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession();

        String vote_session = session.getAttribute("logininfo").toString();
        String[] splitSession = vote_session.split(":");
        String vote_writer = splitSession[0];

        String selectedItemId = req.getParameter("selectedItem");
        Integer voteId = Integer.valueOf(req.getParameter("voteId"));

        if (selectedItemId != null && !selectedItemId.isEmpty()) {

            int itemId = Integer.parseInt(selectedItemId);

            try {
                voteService.revoteStoreResult(itemId, vote_writer, voteId);
                resp.sendRedirect("/main");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            req.setAttribute("error_message", "항목을 선택하지 않았습니다.");
            RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/vote/revote.jsp");
            dispatcher.forward(req, resp);
        }
    }
}
