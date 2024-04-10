package com.example.guessGame.controller;

import com.example.guessGame.models.Game;
import com.example.guessGame.models.Round;
import com.example.guessGame.service.DataValidationException;
import com.example.guessGame.service.GuessGamesServiceLayer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

@CrossOrigin(origins = "http://127.0.0.1:5500")
@Component
@RestController
@RequestMapping("/api/guessgame")
public class GuessController {

    @Autowired
    public GuessGamesServiceLayer service;


    @PostMapping("/begin")
    @ResponseStatus(HttpStatus.CREATED)
    public String startGame() {
        Game game = service.generateInitialAnswers();
        return "GAME ID: " + game.getGameId() + " STARTED";
    }

    @PostMapping
    @RequestMapping("/guess")
    public Round guess(@RequestBody Round round) throws DataValidationException {
        return service.guessProcess(round.getGameId(), round.getGuess());
    }

    @GetMapping("/game")
    public List<Game> showAllGames() throws DataValidationException {
        return service.displayGames();
    }

    @GetMapping("/game/{gameId}")
    public ResponseEntity<Game> findGameById(@PathVariable int gameId) throws DataValidationException {
        Game game = service.displayGameById(gameId);
        if (game == null) {
            return new ResponseEntity(null, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(game);
    }

    @GetMapping("/round/{gameId}")
    public ResponseEntity<List<Round>> showAllRounds(@PathVariable int gameId) throws DataValidationException {
        List<Round> rounds = service.displayRoundsByGame(gameId);
        if (rounds.isEmpty()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(rounds);
    }

    @DeleteMapping("/delete/{gameId}")
    public ResponseEntity deleteGame(@PathVariable int gameId) throws DataValidationException {
        if(service.deleteGameById(gameId)){
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }


}
