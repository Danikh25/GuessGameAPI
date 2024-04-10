# Guess the Number REST Service

This is the API for the game **Bulls And Cows**. 

## Game Rules

- A game has a status, which can be either "in progress" or "finished".
- The answer will be a 4-digit number with no duplicate digits.
- While the game is in progress, users will not be able to see the answer.

## REST Endpoints

1. **Begin** 
    - Method: `POST`
    - Description: Starts a game, generates an answer, and sets the correct status. 
    - Response: Should return a `201 CREATED` message as well as the created `gameId`.

2. **Guess**
    - Method: `POST`
    - Description: Makes a guess by passing the guess and `gameId` in as JSON. The program must calculate the results of the guess and mark the game finished if the guess is correct. 
    - Response: Returns the `Round` object with the results filled in.

3. **Game**
    - Method: `GET`
    - Description: Returns a list of all games. Be sure in-progress games do not display their answer.

4. **Game/{gameId}**
    - Method: `GET`
    - Description: Returns a specific game based on `ID`. Be sure in-progress games do not display their answer.

5. **Rounds/{gameId}**
    - Method: `GET`
    - Description: Returns a list of rounds for the specified game sorted by time.
## Technologies Used




