package org.zerock.demo.controller.vote;

import org.zerock.demo.service.VoteService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/voteResult")
public class VoteResultController extends HttpServlet {
    private VoteService voteService = VoteService.INSTANCE;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String voteIdParam = req.getParameter("voteId");

        try {
            int voteId = Integer.parseInt(voteIdParam);

            ArrayList<Object[]> voteData = voteService.vote(voteId);

            if (voteData != null && !voteData.isEmpty()) {
                List<String> item = new ArrayList<>();
                List<Integer> item_count = new ArrayList<>();


                for (Object[] data : voteData) {
                    if (data[0].equals("vote_title")) {
                        req.setAttribute("vote_title", data[1]);
                    } else if (data[0].equals("item")) {
                        item.add((String) data[1]);
                    }

                    if (data.length > 2 && data[2] != null) {
                        item_count.add((Integer) data[2]); // 수정: item_count를 Integer로 처리
                    }
                }


                System.out.println(item_count);

                req.setAttribute("item", item);
                req.setAttribute("item_count", item_count);
            } else {
                req.setAttribute("error_message", "해당 투표를 찾을 수 없습니다.");
            }


            RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/vote/voteResult.jsp");
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
}
