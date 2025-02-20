package com.example.tenisuj;

import com.example.tenisuj.model.League;
import com.example.tenisuj.model.Match;
import com.example.tenisuj.model.Player;
import com.example.tenisuj.model.User;
import com.example.tenisuj.repository.LeagueRepository;
import com.example.tenisuj.repository.MatchRepository;
import com.example.tenisuj.repository.PlayerRepository;
import com.example.tenisuj.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@SpringBootApplication
public class TenisujApplication implements CommandLineRunner {


    private final UserRepository userRepository;
    private final PlayerRepository playerRepository;
    private final MatchRepository matchRepository;
    private final LeagueRepository leagueRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public TenisujApplication(UserRepository userRepository, PlayerRepository playerRepository, MatchRepository matchRepository, LeagueRepository leagueRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.playerRepository = playerRepository;
        this.matchRepository = matchRepository;
        this.leagueRepository = leagueRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public static void main(String[] args) {
        SpringApplication.run(TenisujApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        List<User> users = new ArrayList<>(List.of(
                new User("Marek", "USER", passwordEncoder.encode("123"))
        ));

        List<Player> players = new ArrayList<>(List.of(
                new Player("00000000-0000-0000-0000-000000000000", "Marek", "priezvisko", "email@email.com", "gender", LocalDate.now(), true,"00000000-0000-0000-0000-000000000100", "L", 100, LocalDate.now()),
                new Player("00000000-0000-0000-0000-000000000001", "Peter", "priezvisko", "email@email.com", "gender", LocalDate.now(), true,"00000000-0000-0000-0000-000000000100", "L", 75, LocalDate.now()),
                new Player("00000000-0000-0000-0000-000000000002", "Viktor", "priezvisko", "email@email.com", "gender", LocalDate.now(), true,"00000000-0000-0000-0000-000000000100", "L", 100, LocalDate.now()),
                new Player("00000000-0000-0000-0000-000000000003", "David", "priezvisko", "email@email.com", "gender", LocalDate.now(), true,"00000000-0000-0000-0000-000000000100", "L", 25, LocalDate.now()),
                new Player("00000000-0000-0000-0000-000000000004", "Oktan", "priezvisko", "email@email.com", "gender", LocalDate.now(), true,"00000000-0000-0000-0000-000000000100", "L", 20, LocalDate.now())
        ));

        Player player1 = new Player("00000000-0000-0000-0000-000000000005", "Adam", "priezvisko", "email@email.com", "gender", LocalDate.of(2000,1,31), true,null, "L", 0, LocalDate.now());
        Player player2 = new Player("00000000-0000-0000-0000-000000000006", "Martin", "priezvisko", "email@email.com", "gender", LocalDate.of(2010,1,31), true,null, "L", 0, LocalDate.now());

        Match match1 = new Match("00000000-0000-0000-0000-000000000010",players.get(0),player1,"Stara cesta 5, Bratislava, kurt 10", LocalDateTime.of(2025,1,31,10,30),7,5,6,4,null,null,null,null,null,null,null,players.get(0));
        Match match2 = new Match("00000000-0000-0000-0000-000000000011",players.get(1),players.get(2),null,null,7,5,0,6,3,0,null,null,null,null,players.get(1),players.get(2));

        League league = new League("00000000-0000-0000-0000-000000000100","League",players,null);

        users.getFirst().setPlayer(players.get(1));
        league.setPlayers(players);

        playerRepository.saveAll(players);
        playerRepository.save(player1);
        playerRepository.save(player2);

        for (User user : users) {
            userRepository.save(user);
        }

        matchRepository.save(match1);
        matchRepository.save(match2);

        leagueRepository.save(league);


    }


}
