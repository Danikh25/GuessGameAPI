package com.example.guessGame.service;

import com.example.guessGame.dao.GameDao;
import com.example.guessGame.dao.RoundDao;
import com.example.guessGame.models.Game;
import com.example.guessGame.models.Round;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Repository
public class GuessGameServiceLayerimpl implements GuessGamesServiceLayer{

    @Autowired
    GameDao gameDao;
    @Autowired
    RoundDao roundDao;

    @Override
    public Game generateInitialAnswers() {
        List<Integer> answerList = new ArrayList<>();

        //Generating four random digits for answer
        while (answerList.size() < 4) {
            int num = (int) (Math.random() * 9 + 1);
            //Making sure every digit is different
            if (!answerList.contains(num)) {
                answerList.add(num);
            }
        }
        //Converting List to one String
        String answer = answerList.get(0).toString()
                + answerList.get(1).toString()
                + answerList.get(2).toString()
                + answerList.get(3).toString();

        //Creating new Game with answer
        Game currentGame = new Game();
        currentGame.setAnswer(answer);
        currentGame.setFinished(false);

        return gameDao.addGame(currentGame);
    }

    @Override
    public List<Game> displayGames() throws DataValidationException {
        List<Game> allGames = gameDao.getAllGames();

        if (allGames.isEmpty()) {
            throw new DataValidationException("No Games Found");
        }
        //Hiding answer until solution is found
        for (Game game : allGames) {
            if (!game.isFinished()) {
                game.setAnswer("(Solution Hidden)");
            }
        }
        return allGames;
    }

    @Override
    public List<Round> displayRoundsByGame(int gameId) throws DataValidationException {
        List<Round> allRounds = roundDao.getAllRounds();
        List<Round> gameRounds = new ArrayList<>();

        if (allRounds.isEmpty()) {
            throw new DataValidationException("No Rounds Found");
        }
        //Adding rounds to new List for specific gameId
        for (Round round : allRounds) {
            if (round.getGameId() == gameId) {
                gameRounds.add(round);
            }
        }
        return gameRounds;
    }

    @Override
    public Round guessProcess(int gameId, String guess) throws DataValidationException {
        //Retrieving the game with the gameId
        Game game = gameDao.getGameById(gameId);
        //Retrieving the answer
        String answer = game.getAnswer();
        Round round = new Round();
        //exact matches
        AtomicInteger eCounter = new AtomicInteger();
        //Partial matches
        AtomicInteger pCounter = new AtomicInteger();

        //Validation checks
        if (game == null) {
            throw new DataValidationException("No game found");
        }
        if (game.isFinished()) {
            throw new DataValidationException("A solution has already been found for this game");
        }
        try {
            Integer.parseInt(String.valueOf(guess));
        } catch (NumberFormatException e) {
            throw new DataValidationException("Your guess must be numeric");
        }

        //Check if its a 4-digit number
        if (guess.length() == 4 && !guess.isBlank()) {
            //Converting answer and guess into a char array
            char[] answerChar = Arrays.stream(answer.split("(?!^)"))
                    .collect(Collectors.joining()).toCharArray();
            char[] guessChar = Arrays.stream(guess.split("(?!^)"))
                    .collect(Collectors.joining()).toCharArray();

            //checks if it’s an exact match or a partial match, and increments the appropriate counter
            IntStream.range(0, 4).forEach(i -> {
                IntStream.range(0, 4).forEach(j -> {
                    if (guessChar[i] == answerChar[j]) {
                        if (i == j) {
                            eCounter.getAndIncrement();
                        } else {
                            pCounter.getAndIncrement();
                        }
                    }
                });
            });


            //Adding round to DB
            round.setTimeOfGuess(LocalDateTime.now());
            round.setGuess(guess);
            round.setExactMatches(eCounter.get());
            round.setPartialMatches(pCounter.get());
            round.setGameId(game.getGameId());
            roundDao.addRound(round);

            if (eCounter.get() == 4) {
                game.setFinished(true);
                gameDao.updateGame(game);
            }
        } else {
            throw new DataValidationException("Your guess must be 4 digits");
        }
        return round;
    }

    @Override
    public Game displayGameById(int gameId) throws DataValidationException {
        Game game;
        try {
            game = gameDao.getGameById(gameId);

            //Hiding answer until solution is found
            if (!game.isFinished()) {
                game.setAnswer("(Solution Hidden)");
            }
        } catch (NullPointerException e) {
            throw new DataValidationException("No Game Found");
        }
        return game;
    }

    @Override
    public Boolean deleteGameById(int gameId) throws DataValidationException {
        return gameDao.deleteGame(gameId);
    }
}
