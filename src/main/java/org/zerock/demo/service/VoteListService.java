package org.zerock.demo.service;

import org.modelmapper.ModelMapper;
import org.zerock.demo.dao.VoteDAO;

import java.util.ArrayList;

public enum VoteListService {
    INSTANCE;

    private VoteDAO voteDAO;
    private ModelMapper modelMapper;

    VoteListService() {
        this.voteDAO = new VoteDAO();
        this.modelMapper = new ModelMapper();
    }

    public ArrayList<Object[]> voteList(String vote_writer) throws Exception {
        ArrayList<Object[]> list = new ArrayList<>();

        list = voteDAO.VoteList(vote_writer);

        return list;
    }
}
