package org.zerock.demo.service;

import org.modelmapper.ModelMapper;
import org.zerock.demo.dao.VoteDAO;
import org.zerock.demo.dto.VoteRegisterDTO;
import org.zerock.demo.vo.ItemVO;
import org.zerock.demo.vo.VoteVO;

public enum VoteRegisterService {
    INSTANCE;

    private VoteDAO voteDAO;
    private ModelMapper modelMapper;

    VoteRegisterService() {
        this.voteDAO = new VoteDAO();
        this.modelMapper = new ModelMapper();
    }

    public void voteRegister(VoteRegisterDTO voteRegisterDTO) throws Exception {
        VoteVO voteVO = modelMapper.map(voteRegisterDTO, VoteVO.class);
        ItemVO itemVO = modelMapper.map(voteRegisterDTO, ItemVO.class);

        int num = voteDAO.voteInsert(voteVO);
        voteDAO.itemInsert(itemVO, num);
    }

}
