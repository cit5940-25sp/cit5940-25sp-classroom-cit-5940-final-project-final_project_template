package com.example.movieGame;

/**
 * tracks connections between movies (ex: Actor - Leo DiCaprio; Genre - horror, etc.)
 *
 */
public class SingleConnection {
    private final String connectionType; //ex: genre, actor, composer, etc.
    private final String name;    //ex: Leonardo DiCaprio

    public SingleConnection(String connectionType, String name) {
        this.connectionType = connectionType;
        this.name = name;
    }

    public String getConnectionType() {
        return connectionType;
    }

    public String getName() {
        return name;
    }
}
