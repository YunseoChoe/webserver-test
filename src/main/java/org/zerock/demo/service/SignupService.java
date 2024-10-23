package org.zerock.demo.service;

import org.modelmapper.ModelMapper;
import org.zerock.demo.dao.MemberDAO;
import org.zerock.demo.dto.SignupDTO;
import org.zerock.demo.vo.Member;

public enum SignupService {
    INSTANCE;

    private MemberDAO memberDAO;
    private ModelMapper modelMapper;

    SignupService() {
        this.memberDAO = new MemberDAO();
        this.modelMapper = new ModelMapper();
    }

    public void signUp(SignupDTO signupDTO) throws Exception {
        Member member = modelMapper.map(signupDTO, Member.class);

        memberDAO.insert(member);
    }

    public boolean signUpCheck(SignupDTO signupDTO) throws Exception {
        Member member = modelMapper.map(signupDTO, Member.class);
        return memberDAO.duplicate(member);
    }
}
