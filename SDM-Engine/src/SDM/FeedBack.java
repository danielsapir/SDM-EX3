package SDM;

import java.util.Date;

public class FeedBack
{
    private String customerName;
    private Date date;
    private int rate;
    private String description=null;

    public FeedBack(String customerName, Date date, int rate, String description) {
        this.customerName = customerName;
        this.date = date;
        this.rate = rate;
        this.description = description;

    }

    public int getRate() {
        return rate;
    }

    public String getDescription() {
        return description;
    }
    /*
    public FeedBack(String customerName, Date date, int rate)
    {
        this.customerName = customerName;
        this.date = date;
        this.rate = rate;
        this.description=null;
    }

    */
}
