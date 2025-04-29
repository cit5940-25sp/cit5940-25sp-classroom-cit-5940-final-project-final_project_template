package model;

/**
 * Represents a person
 * involved in movie production, such as an actor, director, writer, or composer.
 */
public class Person {
    private String name;
    private PersonRole role;

    /**
     * Constructs a Person with a name and a role.
     *
     * @param name the name of the person
     * @param role the role of the person in the movie
     */
    public Person(String name, PersonRole role) {
        this.name = name;
        this.role = role;
    }

    /**
     * Returns the name of the person.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the role of the person.
     *
     * @return the role
     */
    public PersonRole getRole() {
        return role;
    }
}
