public class Crew extends Stuff{
    private String job;
    public Crew(String name, int id, String job){
        super(name, id);
        this.job = job;
    }
    public String getJob(){
        return job;
    }
    public void setJob(String job){
        this.job = job;
    }

    @Override
    public String toString() {
        String str = getJob() + super.toString();
        return str;
    }
}
*