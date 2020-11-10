package SDM;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;


public class MoneyAccount
{
    private double amount=0;
    private List<Transaction> transactions =new LinkedList<>();



    public double getAmount() {
        return amount;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    //noy 9/11
    public void transferPayment(double amountToTransfer, Date date)
    {
        Transaction transactionAccountTransferMoney=new Transaction(Transaction.Type.TransferPayment, date,amountToTransfer,amount,amount-amountToTransfer);
        this.transactions.add(transactionAccountTransferMoney);
        this.amount -= amountToTransfer;
    }

    //noy 9/11
    public void receiveMoney(double amountToReceive,Date date)
    {
        Transaction transactionAccountReceiveMoney=new Transaction(Transaction.Type.ReceivePayment, date,amountToReceive,amount,amount+amountToReceive);
        this.transactions.add(transactionAccountReceiveMoney);
        this.amount += amountToReceive;
    }

/*
    public void TransferMoneyToAnotherAccount(double amountToTransfer, MoneyAccount moneyAccount, Date date)
    {
        Transaction transactionAccountTransferMoney=new Transaction(Transaction.Type.TransferPayment, date,amountToTransfer,amount,amount+amountToTransfer);
        this.transactions.add(transactionAccountTransferMoney);
        this.amount -= amountToTransfer;

        Transaction transactionAccountReceiveMoney=new Transaction(Transaction.Type.ReceivePayment, date,amountToTransfer,moneyAccount.amount,moneyAccount.amount+amountToTransfer);
        moneyAccount.transactions.add(transactionAccountReceiveMoney);
        moneyAccount.amount += amountToTransfer;
    }

 */

    public void LoadingMoneyInMyAccount(double amountToLoading,Date date)
    {
        Transaction transactionLoading=new Transaction(Transaction.Type.LoadingMoney, date, amountToLoading, amount,amount+amountToLoading);
        this.transactions.add(transactionLoading);
        this.amount+=amountToLoading;
    }















}
