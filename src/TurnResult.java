public class TurnResult {
    private boolean sucess;
    private String message;

    public TurnResult(boolean sucess, String message) {
        this.sucess = sucess;
        this.message = message;
    }

    public boolean isSucess() {
        return sucess;
    }

    public String getMessage() {
        return message;
    }
}
