public class Movie {
    private String title;
    private int id;
    private int year;
    private Map<Stuff> cast;
    private Map<Stuff> crew;
    public Movie(String title, int id, int year){
        this.title = title;
        this.id = id;
        this.year = year; 
    }
    public String getTitle(){
        return title; 
    }
    public int getID(){
        return id; 
    }
    public int getYear(){
        return year; 
    }
    public toString(){
        return title + "(" + year + ")";
    }
}