package com.example.movieGame;

/**
 * tracks connections between movies (ex: Actor - Leo DiCaprio; Genre - horror, etc.)
 *
 */
public class SingleConnection {
    private String connectionType; //ex: genre, actor, composer, etc.
    private String name;    //ex: Leonardo DiCaprio

    public String getConnectionType() {
        return connectionType;
    }

    public String getName() {
        return name;
    }
}
