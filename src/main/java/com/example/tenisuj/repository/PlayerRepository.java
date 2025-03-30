package com.example.tenisuj.repository;

import com.example.tenisuj.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerRepository extends JpaRepository<Player, String> {

    //    @Query(value = "SELECT * FROM players WHERE LOWER(CONCAT(first_name, ' ', last_name)) LIKE LOWER(CONCAT('%', :keyword, '%'))", nativeQuery = true)
    @Query("select p from players p where lower(concat(p.firstName,'',p.lastName)) like lower(concat('%', :keyword, '%'))")
    List<Player> search(@Param("keyword") String keyword);

    List<Player> findByLeagueIdOrderByLeagueRatingDesc(String leagueId);

    List<Player> findByLeagueIdIsNull();
}