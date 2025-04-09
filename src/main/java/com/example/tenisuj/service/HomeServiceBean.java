package com.example.tenisuj.service;

import com.example.tenisuj.model.Vote;
import com.example.tenisuj.repository.VoteRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class HomeServiceBean implements HomeService {

   private final VoteRepository voteRepository;

    public HomeServiceBean(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    @Override
    public String getHome() {
        return "Nebud chuj, tenisuj!";
    }

    @Override
    public void initializeVotes() {
        if (voteRepository.count() == 0) {
            voteRepository.save(new Vote("Roger",0));
            voteRepository.save(new Vote("Rafa",0));
            voteRepository.save(new Vote("Novak",0));
            voteRepository.save(new Vote("Marek",0));
        }

    }

    @Override
    public Vote getVotes(String name) {
        Optional<Vote> vote = voteRepository.findById(name);
        return vote.orElse(new Vote(name,0));
    }

    @Override
    public void addVote(String name) {
        Vote vote = getVotes(name);
        vote.setVote(vote.getVote()+1);
        voteRepository.save(vote);

    }

    @Override
    public void resetVotes() {
        String[] players = {"Roger", "Rafa", "Novak", "Marek"};

        for (String player : players) {
            Vote vote = voteRepository.findById(player).orElse(new Vote(player,0));
            vote.setVote(0);
            voteRepository.save(vote);
        }
    }
}
