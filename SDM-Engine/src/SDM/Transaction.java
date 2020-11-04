package SDM;

import java.util.Date;

public class Transaction
{

    enum Type
    {
        LoadingMoney,ReceivePayment,TransferPayment;
    }

    private Type type;
    Date date;
    double amountOfAction;
    double amountBeforeOperation;
    double amountAfterOperation;

    public Transaction(Type type, Date date, double amountToTransfer, double amount, double amountAfterAction)
    {
        this.type= type;
        this.date=date;
        this.amountOfAction=amountToTransfer;
        this.amountBeforeOperation=amount;
        this.amountAfterOperation=amountAfterAction;
    }


}
