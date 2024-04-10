DROP database IF EXISTS GuessTheNumber;

CREATE DATABASE GuessTheNumber;

USE GuessTheNumber;

CREATE TABLE game(
gameId INT PRIMARY KEY AUTO_INCREMENT,
answer CHAR(4) NOT NULL,
finished BOOL DEFAULT 0
);

CREATE TABLE round(
roundId INT PRIMARY KEY AUTO_INCREMENT,
guess CHAR(4) NOT NULL,
guessTime DATETIME NOT NULL,
exactMatches INT,
partialMatches INT,
gameId INT NOT NULL,
FOREIGN KEY(gameId) REFERENCES game(gameId));