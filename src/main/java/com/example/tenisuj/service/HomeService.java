package com.example.tenisuj.service;

import com.example.tenisuj.model.Vote;

public interface HomeService {
    String getHome();

    void initializeVotes();

    Vote getVotes(String name);

    void addVote(String name);

    void resetVotes();
}
