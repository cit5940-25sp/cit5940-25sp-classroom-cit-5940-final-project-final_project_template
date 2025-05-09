package com.example.movieGame;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class GamePlay
{
    private ArrayList<Integer> moviesUsed; //list of movie IDs ot track if it's been used before

    private int actorConnectionUsage; //# of times an actor connection has been made;
    private int directorConnectionUsage; //# of times a director connection has been made;
    private int writerConnectionUsage; //# of times a writer connection has been made;
    private int cinematographerConnectionUsage; //# of times a cinematographer connection has been made;
    private int composerConnectionUsage; //# of times a composer connection has been made;

    private int numberOfRounds; //tracks # of rounds played (for display)
    private Queue<Movie> lastFiveMovies; //LinkedList of movie objects showing last 5 (FIFO)
    private Movie firstMovie; //first randomly selected movie

    private Player player1;
    private Player player2;
    private String player1Name;
    /**
     * Constructor initializes variables, creates players and designates first active player,
     * sets up index with movies, randomly select movie
     *
     */
    public GamePlay(String player1Name, String player2Name) {
        //initialize variables
        moviesUsed = new ArrayList<>();
        actorConnectionUsage = 0;
        directorConnectionUsage = 0;
        writerConnectionUsage = 0;
        cinematographerConnectionUsage = 0;
        composerConnectionUsage = 0;
        numberOfRounds = 0;
        lastFiveMovies = new LinkedList<>();

        //create and designate players
        player1 = new Player(player1Name, true);
        player2 = new Player(player2Name, false);

        //set up data file
        //TODO (call class here)

        //randomly select movie
        firstMovie = randomMovieSelection();
    }

    /**
     * selects first move for the start of the game from within index
     *
     */
    public Movie randomMovieSelection() {
        //TODO

        this.firstMovie = null; //TODO update
        return firstMovie;
    }

    /**
     * tracks time for each player
     *
     */
    public void thirtySecondTimer() {
        //TODO
        //not sure what it should return/ how this would work
    }

    /**
     * updates game state and variables based on user entry
     *
     * @return error message citing reason for the error
     * @parameter
     */
    public String userEntry(Movie movie) {
        //check for a valid connection (return error message if issue)
        //increment xxxConnectionUsage variables for all matching connections
        //TODO

        //check for duplicate movie (return error if issue)
        if (moviesUsed.contains(movie.getMovieID())) {
            return "Error: main.java.com.example.movieGame.Movie already used.";
        }

        //check for 3+ connections (return error message if issue)
        if (actorConnectionUsage > 3) { //TODO - check - >3 or >=3?
            return "Error: Actor used too many times.";
        }
        if (directorConnectionUsage > 3) { //TODO - check - >3 or >=3?
            return "Error: Director used too many times.";
        }
        if (writerConnectionUsage > 3) { //TODO - check - >3 or >=3?
            return "Error: Writer used too many times.";
        }
        if (cinematographerConnectionUsage > 3) { //TODO - check - >3 or >=3?
            return "Error: Cinematographer used too many times.";
        }
        if (composerConnectionUsage > 3) { //TODO - check - >3 or >=3?
            return "Error: Composer used too many times.";
        }

        //add movie name to moviesUsed variable
        moviesUsed.add(movie.getMovieID());

        //increment number of rounds played
        numberOfRounds++;

        //add main.java.com.example.movieGame.Movie object to lastFiveMovies & remove first item
        lastFiveMovies.add(movie);
        lastFiveMovies.poll();

        //update active player
        if (player1.getIsActive() == false) {
            player1.setIsActive(true);
            player2.setIsActive(false);
        } else {
            player1.setIsActive(false);
            player2.setIsActive(true);
        }

        return "Valid User Entry";
    }


    /**
     *
     *
     */



    //TODO need getters/setters

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

}


