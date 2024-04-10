package com.example.guessGame.dao;

import com.example.guessGame.models.Round;

import java.util.List;

public interface RoundDao {
    Round addRound(Round round);

    List<Round> getAllRounds();

    public Round getRoundById(int roundId);

    boolean updateRound(Round round);

    boolean deleteRound(int id); //Delete by ID
}
