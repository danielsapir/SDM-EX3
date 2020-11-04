package SDM;

import com.sun.deploy.security.ValidationState;

public class User
{
    enum Type
    {
        CUSTOMER, OWNER;
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


    ///Color c1 = Color.RED;
}




