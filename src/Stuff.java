public class Stuff implements Comparable<Stuff>{
    private String name;
    private int id;
    public String getJob(){
        return "";
    }
    public Stuff(String name, int id){
        this.name = name;
        this.id = id;
    }
    public String getName(){
        return name;
    }
    public int getId(){
        return id;
    }
    @Override
    public int compareTo(Stuff o) {
        return Integer.compare(id, o.id);
    }
    @Override
    public String toString(){
        return name;
    }
}
*