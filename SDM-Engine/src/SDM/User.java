package SDM;

import com.sun.deploy.security.ValidationState;

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


    ///Color c1 = Color.RED;
}




