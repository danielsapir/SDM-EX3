package SDM;

public class Notification
{

    public enum Type
    {
        NewOrder, Feedback,NewStore ;
    }


    private Type type;
    private String message;

    public Notification(Type type, String message)
    {
        this.type = type;
        this.message = message;
    }
}
