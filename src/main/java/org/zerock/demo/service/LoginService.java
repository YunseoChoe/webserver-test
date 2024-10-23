package org.zerock.demo.service;

import org.modelmapper.ModelMapper;
import org.zerock.demo.dao.MemberDAO;
import org.zerock.demo.dto.LoginDTO;
import org.zerock.demo.vo.LoginVO;

public enum LoginService {
    INSTANCE;

    private MemberDAO memberDAO;
    private ModelMapper modelMapper;

    LoginService() {
        this.memberDAO = new MemberDAO();
        this.modelMapper = new ModelMapper();
    }

    public boolean login(LoginDTO loginDTO) throws Exception {
        LoginVO loginVO = this.modelMapper.map(loginDTO, LoginVO.class);
        return memberDAO.equal(loginVO);
    }
}
