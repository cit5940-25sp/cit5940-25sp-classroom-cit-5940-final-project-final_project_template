public class Links {
    private List<Link> links;
    
    public Links(){
        links = new ArrayList<Link>();
    }
    public void addLink(Link link){
        links.add(link);
    }
    public void removeLink(Link link){
        links.remove(link);
    }
    public Link getLink(int index){
        return links.get(index);
    }
}
