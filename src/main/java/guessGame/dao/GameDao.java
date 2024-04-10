package com.example.guessGame.dao;

import com.example.guessGame.models.Game;

import java.util.List;

public interface GameDao {

    Game addGame(Game game);

    List<Game> getAllGames();

    Game getGameById(int gameId);

    boolean updateGame(Game game);

    boolean deleteGame(int gameId); //Delete by id
}
