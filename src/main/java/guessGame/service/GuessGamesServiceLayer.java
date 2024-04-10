package com.example.guessGame.service;

import com.example.guessGame.models.Game;
import com.example.guessGame.models.Round;

import java.util.List;

public interface GuessGamesServiceLayer {
    //Start the game and generate the initial answers
    public Game generateInitialAnswers();

    //Returns a list of all games
    public List<Game> displayGames() throws DataValidationException;

    //Returns a list of rounds for the specified game sorted by time
    public List<Round> displayRoundsByGame(int gameId) throws DataValidationException;

    //Makes a guess by passing the guess and gameId in as JSON
    public Round guessProcess(int gameId, String guess) throws DataValidationException;

    //Returns a specific game based on ID
    public Game displayGameById(int gameId) throws DataValidationException;

    public Boolean deleteGameById(int gameId) throws DataValidationException;
}
