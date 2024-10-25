package org.zerock.demo.service;

import org.modelmapper.ModelMapper;
import org.zerock.demo.dao.VoteDAO;

import java.sql.SQLException;
import java.util.ArrayList;

public enum VoteService {
    INSTANCE;

    private VoteDAO voteDAO;
    private ModelMapper modelMapper;

    VoteService() {
        this.voteDAO = new VoteDAO();
        this.modelMapper = new ModelMapper();
    }

    public ArrayList<Object[]> vote(int vote_id) throws Exception {
        ArrayList<Object[]> list = new ArrayList<>();

        list = voteDAO.vote(vote_id);

        return list;
    }

    public void voteStoreResult(int items_id, String memberUsername) throws Exception {
        voteDAO.voteStoreResult(items_id, memberUsername);
    }

    public ArrayList<Object[]> revote(int vote_id, String memberUsername) throws Exception {
        ArrayList<Object[]> list = new ArrayList<>();
        list = voteDAO.revote(vote_id, memberUsername);

        return list;
    }

    public void revoteStoreResult(int items_id, String memberUsername, int voteId) throws Exception {
        voteDAO.revoteStoreResult(items_id, memberUsername, voteId);
    }

    public ArrayList<Object[]> voteResult(int vote_id) throws Exception {
        ArrayList<Object[]> list = new ArrayList<>();
        list = voteDAO.voteResult(vote_id);
        return list;
    }
}
