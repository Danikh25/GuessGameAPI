$(document).ready(function(){
    var showAllGames = false;

    function getGames() {
        $.ajax({
            type: 'GET',
            url: 'http://localhost:8080/api/guessgame/game',
            success: function(gameArray) {
                var gamesDiv = $('#allGames');
                gamesDiv.empty();  // Clear the div before adding new games

                $.each(gameArray, function(index, game) {
                    if (showAllGames || !game.finished) {
                        var gameInfo = '<p>';
                        gameInfo += 'Game ID: ' + game.gameId + '<br>';
                        gameInfo += 'Answer: ' + game.answer + '<br>';
                        gameInfo += 'Finished: ' + (game.finished ? 'Yes' : 'No') + '<br>';
                        gameInfo += '</p><hr>';

                        gamesDiv.append(gameInfo);
                    }
                });
            },
            error: function() {
                console.log('Unable to display games');
                console.log('Status: ' + textStatus);
                console.log('Error: ' + errorThrown);
            }
        });
    }

    getGames();  // Get games when the page loads

    $('#startGame').click(function() {
        $.ajax({
            type: 'POST',
            url: 'http://localhost:8080/api/guessgame/begin',
            success: function(game) {
                getGames();  // Refresh the games list when a new game is started
            },
            error: function() {
                console.log('Unable to start game');
                console.log('Status: ' + textStatus);
                console.log('Error: ' + errorThrown);
            }
        });
    });

    $('#toggleGames').click(function() {
        showAllGames = !showAllGames;  // Toggle the showAllGames variable
        getGames();  // Refresh the games list
    });

    $('#gameForm').submit(function(event) {
        event.preventDefault();  // Prevent the form from submitting normally

        var gameId = $('#gameId').val();  // Get the entered game ID

        $.ajax({
            type: 'GET',
            url: 'http://localhost:8080/api/guessgame/round/' + gameId,
            success: function(rounds) {
                var roundsDiv = $('#allRounds');
                roundsDiv.empty();  // Clear the div before adding new rounds

                $.each(rounds, function(index, round) {
                    var roundInfo = '<p>';
                    roundInfo += 'Round ID: ' + round.roundId + '<br>';
                    roundInfo += 'Guess: ' + round.guess + '<br>';
                    roundInfo += 'Time of Guess: ' + round.timeOfGuess + '<br>';
                    roundInfo += 'Exact Matches: ' + round.exactMatches + '<br>';
                    roundInfo += 'Partial Matches: ' + round.partialMatches + '<br>';
                    roundInfo += '</p><hr>';

                    roundsDiv.append(roundInfo);
                });
            },
            error: function() {
                console.log('Unable to display rounds');
                console.log('Status: ' + textStatus);
                console.log('Error: ' + errorThrown);
            }
        });
    });

    $('#guessForm').submit(function(event) {
        event.preventDefault();  // Prevent the form from submitting normally
    
        var gameId = $('#gameIdGuess').val();  // Get the entered game ID
        var guess = $('#guess').val();  // Get the entered guess
    
        $.ajax({
            type: 'POST',
            url: 'http://localhost:8080/api/guessgame/guess',
            data: JSON.stringify({ gameId: gameId, guess: guess }),
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function(round) {
                var roundsDiv = $('#currentRounds');
                var roundInfo = '<p>';
                roundInfo += 'Round ID: ' + round.roundId + '<br>';
                roundInfo += 'Guess: ' + round.guess + '<br>';
                roundInfo += 'Time of Guess: ' + round.timeOfGuess + '<br>';
                roundInfo += 'Exact Matches: ' + round.exactMatches + '<br>';
                roundInfo += 'Partial Matches: ' + round.partialMatches + '<br>';
                roundInfo += '</p><hr>';
    
                roundsDiv.append(roundInfo);
            },
            error: function() {
                alert('FAILURE!');
            }
        });
    });
    
});


