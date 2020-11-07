package SDM;

import com.sun.deploy.security.ValidationState;

import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

public class User
{
    public enum Type
    {
        CUSTOMER, OWNER;

        public static Type stringToType(String type) {
            if(type==null || type.isEmpty()) {
                return CUSTOMER;
            }
            else {
                if(type.equals(CUSTOMER.toString())) {
                    return CUSTOMER;
                }
                else {
                    return OWNER;
                }
            }
        }
    }

    protected Type type;
    protected String name;
    protected int id;
    protected MoneyAccount moneyAccount;
    protected PriorityQueue<Notification> notificationsQueue = new PriorityQueue<Notification>();
    private static int  idCounter=0;


    public User(String userName, Type userType)
    {
        idCounter++;
        this.id=idCounter;
        this.name=userName;
        this.type=userType;
        this.moneyAccount=new MoneyAccount();
    }

    public String getName() {
        return name;
    }

    public int getId() {return id;}

    public Type getType() {
        return type;
    }

    public MoneyAccount getMoneyAccount() {
        return moneyAccount;
    }

    public synchronized void addNotification(Notification notification)
    {
        this.notificationsQueue.add(notification);
    }


    public synchronized List getAndRemoveAllNotifications()
    {
        List<Notification>notificationsList=new LinkedList<>();

        while(!(this.notificationsQueue.isEmpty()))
        {
            notificationsList.add(this.notificationsQueue.poll());
        }

        return(notificationsList);
    }




}




