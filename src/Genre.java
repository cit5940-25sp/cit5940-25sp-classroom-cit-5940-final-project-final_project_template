public class Genre implements Comparable<Genre>{
    private int id;
    private String name;

    public Genre(String name, int id){
        this.name = name;
        this.id = id;
    }
    public String getName(){
        return name;
    }
    public int getId(){
        return id;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setId(int id){
        this.id = id;
    }
    @Override
    public int compareTo(Genre other){
        return Integer.compare(this.id, other.id);
    }

    @Override
    public String toString() {
        return name;
    }
}
*