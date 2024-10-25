package org.zerock.demo.service;

import org.modelmapper.ModelMapper;
import org.zerock.demo.dao.VoteDAO;

public enum VoteDeleteService {
    INSTANCE;

    private VoteDAO voteDAO;
    private ModelMapper modelMapper;

    VoteDeleteService() {
        this.voteDAO = new VoteDAO();
        this.modelMapper = new ModelMapper();
    }

    public void voteDelete(int vote_Id) throws Exception {
        voteDAO.voteDelete(vote_Id);
    }
}
