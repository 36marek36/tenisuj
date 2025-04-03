package com.example.tenisuj.repository;

import com.example.tenisuj.model.League;
import com.example.tenisuj.model.enums.LeagueStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeagueRepository extends JpaRepository<League, String> {
    boolean existsByName(String name);

    List<League> findByStatus(LeagueStatus status);

    List<League> findByStatusNot(LeagueStatus status);
}
