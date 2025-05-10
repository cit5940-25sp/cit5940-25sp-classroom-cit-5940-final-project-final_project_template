/**
 * The View class represents the view component in the MVC (Model-View-Controller) architecture.
 * It is responsible for displaying the application's user interface and interacting with the user.
 * This class holds references to the Model and Control components, allowing it to access the model's data
 * and trigger actions through the controller.
 */
public class View {
    /**
     * Reference to the Model component.
     * The model holds the application's data and business logic.
     */
    private Model Model;

    /**
     * Reference to the Control component.
     * The controller manages the interaction between the view and the model.
     */
    private Control Control;

    /**
     * Retrieves the Model component associated with this view.
     *
     * @return The Model object.
     */
    public Model getModel() {
        return Model;
    }

    /**
     * Retrieves the Control component associated with this view.
     *
     * @return The Control object.
     */
    public Control getControl() {
        return Control;
    }

    /**
     * Sets the Model component associated with this view.
     *
     * @param Model The Model object to be associated with this view.
     */
    void setModel(Model Model){
        this.Model = Model;
    }

    /**
     * Sets the Control component associated with this view.
     *
     * @param Control The Control object to be associated with this view.
     */
    public void setControl(Control Control) {
        this.Control = Control;
    }
}

