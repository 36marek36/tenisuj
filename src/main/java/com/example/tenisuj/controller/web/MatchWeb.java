package com.example.tenisuj.controller.web;

import com.example.tenisuj.model.Match;
import com.example.tenisuj.model.Player;
import com.example.tenisuj.model.dto.UpdateMatchLocationDateAndTimeDto;
import com.example.tenisuj.model.dto.UpdateResultDto;
import com.example.tenisuj.service.MatchService;
import com.example.tenisuj.service.PlayerService;
import com.example.tenisuj.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/matches")
public class MatchWeb {

    private final MatchService matchService;
    private final PlayerService playerService;
    private final UserService userService;

    @Autowired
    public MatchWeb(MatchService matchService, PlayerService playerService, UserService userService) {
        this.matchService = matchService;
        this.playerService = playerService;
        this.userService = userService;
    }

    @GetMapping("/")
    String getAllMatches(Model model, @Param("playerName") String playerName) {
        setDefaultValues(model);
        model.addAttribute("matches", matchService.getMatches(playerName));
        model.addAttribute("player", playerService.getAllPlayers(null));
        model.addAttribute("playerName", playerName);
        return "matches";
    }

    @GetMapping("/my_matches/{playerId}")
    String getMyMatches(Model model, @PathVariable("playerId") String playerId) {
        setDefaultValues(model);
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        playerId = userService.getUser(currentUser).getPlayer().getId();
        model.addAttribute("myMatches", matchService.findAllPlayerMatches(playerId));
        log.info("getMyMatches:" + playerId);
        return "myMatches";
    }

    @GetMapping("/details/{id}")
    String getMatchDetails(@PathVariable("id") String matchId, Model model, HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        if (referer != null) {
            request.getSession().setAttribute("previousPage", referer);
        }
        setDefaultValues(model);
        Match match = matchService.getMatch(matchId);

        UpdateMatchLocationDateAndTimeDto updateMatchLocationDateAndTimeDto = new UpdateMatchLocationDateAndTimeDto();
        updateMatchLocationDateAndTimeDto.setLocation(match.getLocation());
        updateMatchLocationDateAndTimeDto.setDateTime(match.getDateTime());

        UpdateResultDto updateResultDto = getUpdateResultDto(match);

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
                                 Model model,
                                 HttpServletRequest request) {
        setDefaultValues(model);
        Match match = matchService.getMatch(matchId);
        if (updateResultDto.getScratchedPlayerId() != null) {
            Player scratched = playerService.getPlayerById(updateResultDto.getScratchedPlayerId());
            match.setScratched(scratched);
        } else {
            match.setScratched(null);
        }

        matchService.addResult(matchId, updateResultDto.getPlayer1_set1(), updateResultDto.getPlayer2_set1(), updateResultDto.getPlayer1_set2(), updateResultDto.getPlayer2_set2(), updateResultDto.getPlayer1_set3(), updateResultDto.getPlayer2_set3(), updateResultDto.getPlayer1_set4(), updateResultDto.getPlayer2_set4(), updateResultDto.getPlayer1_set5(), updateResultDto.getPlayer2_set5(), updateResultDto.getScratchedPlayerId(), updateResultDto.getWinnerPlayerId());
        log.info("Match result added");
        log.info("Scratched Player ID:" + updateResultDto.getScratchedPlayerId());

        String previousPage = (String) request.getSession().getAttribute("previousPage");
        log.info("previousPage:" + previousPage);

        if (previousPage != null && previousPage.contains("/my_matches/")) {
            return "redirect:/home";
        }

        return "redirect:/matches/";
    }

    private static UpdateResultDto getUpdateResultDto(Match match) {
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
        return updateResultDto;
    }

    private void setDefaultValues(Model model) {
        model.addAttribute("pageTitle", "Matches");
    }

}
