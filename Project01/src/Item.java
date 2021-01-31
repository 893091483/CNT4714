/* Name: <Zefeng Yao>
 Course: CNT 4714 – Spring 2021
 Assignment title: Project 1 – Event-driven Enterprise Simulation
 Date: Sunday January 31, 2021
*/

public class Item
{
    // all variables
    private final String id;
    private final String description;
    private final String status;
    private final double price;

    public Item(
            String id,
            String description,
            String status,
            double price)
    {
        this.id = id;
        this.description = description;
        this.status = status;
        this.price = price;
    }

    // all the getters, no setters. because either the user or the coder
    // should change the data in Item
    public String getId()
    {
        return id;
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
