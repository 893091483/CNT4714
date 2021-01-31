public class ItemInfo {


    private final String description;
    private final String status;
    private final double price;

    public ItemInfo(String description, String status, double price)
    {
        this.description = description;
        this.status = status;
        this.price = price;
    }



    public String getDescription()
    {
        return description;
    }

    public String getStatus()
    {
        return status;
    }

    public double getPrice()
    {
        return price;
    }
}
