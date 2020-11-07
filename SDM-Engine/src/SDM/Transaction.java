package SDM;

import java.util.Date;

public class Transaction
{

    public enum Type
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

    public Type getType() {
        return type;
    }

    public Date getDate() {
        return date;
    }

    public double getAmountOfAction() {
        return amountOfAction;
    }

    public double getAmountBeforeOperation() {
        return amountBeforeOperation;
    }

    public double getAmountAfterOperation() {
        return amountAfterOperation;
    }
}
