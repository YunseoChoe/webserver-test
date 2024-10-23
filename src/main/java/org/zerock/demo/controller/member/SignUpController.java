package org.zerock.demo.controller.member;

import org.modelmapper.ModelMapper;
import org.zerock.demo.dao.MemberDAO;
import org.zerock.demo.dto.SignupDTO;
import org.zerock.demo.service.SignupService;
import org.zerock.demo.util.MapperUtil;
import org.zerock.demo.vo.Member;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/signup")
public class SignUpController extends HttpServlet {

    private SignupService signupService = SignupService.INSTANCE;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/member/signup.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 폼 데이터 받아오기
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        SignupDTO signupDTO = new SignupDTO();
        signupDTO.setUsername(username);
        signupDTO.setPassword(password);

        try {
            // 회원 정보 저장
            if(signupService.signUpCheck(signupDTO)) {
                request.getRequestDispatcher("/WEB-INF/views/member/signuperror.jsp").forward(request, response);
            }
            else {
                signupService.signUp(signupDTO);

                System.out.println("success");
                request.getRequestDispatcher("/WEB-INF/views/main.jsp").forward(request, response);
            }

        } catch (Exception e) {
            // 예외 처리 (예외 메시지 출력)
            e.printStackTrace();
            response.sendRedirect("signup.jsp?error=1");  // 오류 시 다시 회원가입 페이지로 리다이렉트
        }
    }
}
