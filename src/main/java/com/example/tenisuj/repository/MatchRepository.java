package com.example.tenisuj.repository;

import com.example.tenisuj.model.Match;
import com.example.tenisuj.model.enums.MatchStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface MatchRepository extends JpaRepository<Match, String> {

    @Query("select m from Match m where m.player1.id= :playerId or m.player2.id= :playerId")
    List<Match> findAllPlayerMatches(String playerId);

    @Query("select m from Match m where m.winner.id= :playerId")
    List<Match> findWonPlayerMatches(String playerId);

    @Query("select m from Match m where (m.player1.id= :playerId or m.player2.id= :playerId) and m.winner.id is not null")
    List<Match> findAllPlayedPlayerMatches(String playerId);

    @Query("select m from Match m where lower(concat(m.player1.firstName,'',m.player1.lastName, ' ',m.player2.firstName, m.player2.lastName)) like lower(concat('%', :playerName, '%'))")
    List<Match> search(@Param("playerName") String playerName);

    @Query("select m from Match m where m.leagueId= :leagueId and (m.player1.id = :playerId or m.player2.id = :playerId)")
    List<Match> findAllPlayerMatchesInLeague(@Param("playerId") String playerId, @Param("leagueId") String leagueId);

    @Query("select m from Match m where m.winner.id= :playerId and m.leagueId = :leagueId")
    List<Match> findWonPlayerMatchesInLeague(@Param("playerId") String playerId, @Param("leagueId") String leagueId);

    @Query("select m from Match m where (m.player1.id= :playerId or m.player2.id= :playerId) and m.winner.id is not null and m.leagueId = :leagueId")
    List<Match> findAllPlayedPlayerMatchesInLeague(@Param("playerId") String playerId, @Param("leagueId") String leagueId);

    @Query("select m from Match m where m.status = :status")
    List<Match> findMatchesByStatus(@Param("status") MatchStatus status);

    @Query("select m from Match m where m.leagueId =:leagueId")
    List<Match> findAllLeagueMatches(@Param("leagueId") String leagueId);

    @Query("select m from Match m where m.leagueId =:leagueId and m.winner is not null")
    List<Match> findAllPlayedLeagueMatches(@Param("leagueId") String leagueId);
}
