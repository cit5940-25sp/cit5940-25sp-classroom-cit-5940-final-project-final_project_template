public class Cast extends Stuff{
    private String character;
    private int order;
    public Cast(String name, int id, String character, int order){
        super(name, id);
        this.character = character;
        this.order = order;
    }

    public String getCharacter(){
        return character;
    }
    public void setCharacter(String character){
        this.character = character;
    }
    public int getOrder(){
        return order;
    }

    @Override
    public String toString() {
        String str = "Cast:" + super.toString();
        return str;
    }
    @Override
    public String getJob() {
        return "Actor";
    }
}


*