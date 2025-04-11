
public class View {
    private Model Model;
    private Control Control;

    public Model getModel() {
        return Model;
    }

    public Control getControl() {
        return Control;
    }

    void setModel(Model Model){
        this.Model = Model;
    }

    public void setControl(Control Control) {
        this.Control = Control;
    }
}
