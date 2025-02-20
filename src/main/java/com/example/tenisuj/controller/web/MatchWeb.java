package com.example.tenisuj.controller.web;

import com.example.tenisuj.model.Match;
import com.example.tenisuj.model.dto.UpdateMatchLocationDateAndTimeDto;
import com.example.tenisuj.model.dto.UpdateResultDto;
import com.example.tenisuj.service.MatchService;
import com.example.tenisuj.service.PlayerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/matches")
public class MatchWeb {

    private final MatchService matchService;
    private final PlayerService playerService;

    @Autowired
    public MatchWeb(MatchService matchService, PlayerService playerService) {
        this.matchService = matchService;
        this.playerService = playerService;
    }

    @GetMapping("/")
    String getAllMatches(Model model, @Param("playerName") String playerName) {
        setDefaultValues(model);
        model.addAttribute("matches", matchService.getMatches(playerName));
        model.addAttribute("player", playerService.getAllPlayers(null));
        model.addAttribute("playerName", playerName);
        return "matches";
    }

    @GetMapping("/details/{id}")
    String getMatchDetails(@PathVariable("id") String matchId, Model model) {
        setDefaultValues(model);
        Match match = matchService.getMatch(matchId);

        UpdateMatchLocationDateAndTimeDto updateMatchLocationDateAndTimeDto = new UpdateMatchLocationDateAndTimeDto();
        updateMatchLocationDateAndTimeDto.setLocation(match.getLocation());
        updateMatchLocationDateAndTimeDto.setDateTime(match.getDateTime());

        UpdateResultDto updateResultDto = new UpdateResultDto();
        updateResultDto.setPlayer1_set1(match.getPlayer1_set1());
        updateResultDto.setPlayer2_set1(match.getPlayer2_set1());
        updateResultDto.setPlayer1_set2(match.getPlayer1_set2());
        updateResultDto.setPlayer2_set2(match.getPlayer2_set2());
        updateResultDto.setPlayer1_set3(match.getPlayer1_set3());
        updateResultDto.setPlayer2_set3(match.getPlayer2_set3());
        updateResultDto.setPlayer1_set4(match.getPlayer1_set4());
        updateResultDto.setPlayer2_set4(match.getPlayer2_set4());
        updateResultDto.setPlayer1_set5(match.getPlayer1_set5());
        updateResultDto.setPlayer2_set5(match.getPlayer2_set5());
        if (match.getScratched() != null) {
            updateResultDto.setScratchedPlayerId(match.getScratched().getId());
        }
        if (match.getWinner() != null) {
            updateResultDto.setWinnerPlayerId(match.getWinner().getId());
        }

        model.addAttribute("match", match);
        model.addAttribute("updateMatchLocationDateAndTimeDto", updateMatchLocationDateAndTimeDto);
        model.addAttribute("updateResultDto", updateResultDto);
        return "matchDetails";
    }

    @PostMapping("/details/{id}/add_lad")
    public String addMatchLocationAndDateTime(@PathVariable("id") String matchId,
                                              UpdateMatchLocationDateAndTimeDto updateMatchLocationDateAndTimeDto,
                                              Model model) {
        setDefaultValues(model);
        matchService.addLocation(matchId, updateMatchLocationDateAndTimeDto.getLocation(), updateMatchLocationDateAndTimeDto.getDateTime());
        log.info("Match location, date, and time updated");
        return "redirect:/matches/";
    }

    @PostMapping("/details/{id}/add_result")
    public String addMatchResult(@PathVariable("id") String matchId,
                                              UpdateResultDto updateResultDto,
                                              Model model) {
        setDefaultValues(model);
        //validacia
        matchService.addResult(matchId, updateResultDto.getPlayer1_set1(), updateResultDto.getPlayer2_set1(), updateResultDto.getPlayer1_set2(), updateResultDto.getPlayer2_set2(), updateResultDto.getPlayer1_set3(), updateResultDto.getPlayer2_set3(), updateResultDto.getPlayer1_set4(), updateResultDto.getPlayer2_set4(), updateResultDto.getPlayer1_set5(), updateResultDto.getPlayer2_set5(), updateResultDto.getScratchedPlayerId(), updateResultDto.getWinnerPlayerId());
        log.info("Match result added");
        return "redirect:/matches/";
    }

    private void setDefaultValues(Model model) {
        model.addAttribute("pageTitle", "Matches");
    }

}
