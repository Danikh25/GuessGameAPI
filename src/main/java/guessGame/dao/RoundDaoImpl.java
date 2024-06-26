package com.example.guessGame.dao;

import com.example.guessGame.models.Round;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.*;
import java.util.List;
@Repository
public class RoundDaoImpl implements RoundDao {

    private final JdbcTemplate jdbc;

    public RoundDaoImpl(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Round addRound(Round round) {

        final String sql = "INSERT INTO round (guess, timeOfGuess, exactMatches, partialMatches, gameId) VALUES(?,?,?,?,?);";

        //grabbing keys that are auto-generated by DB
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        //Connection to DB(conn)
        jdbc.update((Connection conn) -> {
            PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, round.getGuess());
            statement.setTimestamp(2, Timestamp.valueOf(round.getTimeOfGuess()));
            statement.setInt(3, round.getExactMatches());
            statement.setInt(4, round.getPartialMatches());
            statement.setInt(5, round.getGameId());
            return statement;
        }, keyHolder);
        round.setRoundId(keyHolder.getKey().intValue());
        return round;
    }

    @Override
    public List<Round> getAllRounds() {
        final String sql = "SELECT * FROM round ORDER BY timeOfGuess;";
        return jdbc.query(sql, new RoundMapper());
    }

    @Override
    public Round getRoundById(int roundId) {
        return null;
    }

    @Override
    public boolean updateRound(Round round) {
        return false;
    }

    //Add get round by id?

    @Override
    @Transactional
    public boolean deleteRound(int roundId) {
        final String sql = "DELETE FROM round WHERE roundId = ?;";
        return jdbc.update(sql, roundId) > 0;
    }

    private final class RoundMapper implements RowMapper<Round> {

        @Override
        public Round mapRow(ResultSet rs, int index) throws SQLException {
            Round round = new Round();
            round.setRoundId(rs.getInt("roundId"));
            round.setGuess(rs.getString("guess"));
            round.setTimeOfGuess(rs.getTimestamp("timeOfGuess").toLocalDateTime());
            round.setExactMatches(rs.getInt("exactMatches"));
            round.setPartialMatches(rs.getInt("partialMatches"));
            round.setGameId(rs.getInt("gameId"));
            return round;
        }
    }
}
