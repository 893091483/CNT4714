/* Name: <Zefeng Yao>
 Course: CNT 4714 – Spring 2021
 Assignment title: Project 1 – Event-driven Enterprise Simulation
 Date: Sunday January 31, 2021
*/

import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class Order
{
    // all variables
    private int currentNumItems = 0;
    private double orderSubtotal = 0;
    private double orderTotal = 0;
    private int totalItems = 0;
    private final String outputFilename;

    private final ArrayList<String> items = new ArrayList<>();
    private final StringBuilder viewOrder = new StringBuilder();
    private final StringBuilder finishOrder = new StringBuilder();

    public Order(String outputFilename)
    {
        this.outputFilename = outputFilename;
    }

    public String getViewOrder()
    {
        return this.viewOrder.toString();
    }

    public void setViewOrder(String order)
    {
        viewOrder.append(currentNumItems).append(". ").append(order);
        viewOrder.append(System.getProperty("line.separator"));
    }

    public String getFinishOrder()
    {
        return finishOrder.toString();
    }

    public int getCurrentNumItems()
    {
        return currentNumItems;
    }

    public void incrementCurrentNumItems()
    {
        currentNumItems = currentNumItems + 1;
    }

    public double getOrderSubtotal()
    {
        return orderSubtotal;
    }

    public void setOrderSubtotal(int quantity, double price)
    {
        orderSubtotal = orderSubtotal + findTotalDiscount(quantity, price);
    }

    public int getTotalItems()
    {
        return totalItems;
    }

    public void setTotalItems(int totalItems)
    {
        this.totalItems = totalItems;
    }

    public double taxAmount()
    {
        return .06 * orderSubtotal;
    }

    public void setOrderTotal()
    {
        orderTotal = orderSubtotal + (taxAmount());
    }

    public void setFinishOrder(String date, String time)
    {
        DecimalFormat df = new DecimalFormat("######.##");
        setOrderTotal();
        finishOrder.append("Date: ").append(date).append(" ").append(time);
        finishOrder.append(System.getProperty("line.separator")).append(System.getProperty("line.separator"));
        finishOrder.append("Number of line items: ").append(totalItems);
        finishOrder.append(System.getProperty("line.separator")).append(System.getProperty("line.separator"));
        finishOrder.append("Item# /ID / Price / Qty / Disc% / Subtotal");
        finishOrder.append(System.getProperty("line.separator")).append(System.getProperty("line.separator"));
        finishOrder.append(this.getViewOrder());
        finishOrder.append(System.getProperty("line.separator")).append(System.getProperty("line.separator"));
        finishOrder.append("Order subtotal:   $").append(df.format(orderSubtotal));
        finishOrder.append(System.getProperty("line.separator")).append(System.getProperty("line.separator"));
        finishOrder.append("Tax rate:     6%");
        finishOrder.append(System.getProperty("line.separator")).append(System.getProperty("line.separator"));
        finishOrder.append("Tax amount:      $").append(df.format(taxAmount()));
        finishOrder.append(System.getProperty("line.separator")).append(System.getProperty("line.separator"));
        finishOrder.append("Order total:      $").append(df.format(orderTotal));
        finishOrder.append(System.getProperty("line.separator")).append(System.getProperty("line.separator"));
        finishOrder.append("Thanks for shopping at the Nile Dot Com!");
    }

    public double findTotalDiscount(int quantity, double price)
    {
        DecimalFormat df = new DecimalFormat("######.##");

        return Double.parseDouble(df.format(0.01 * (100 - findDiscountPercentage(quantity)) * quantity * price));
    }

    public int findDiscountPercentage(int quantity)
    {
        if(quantity >= 1 && quantity <= 4 )
            return 0; // 0% discount
        if(quantity >= 5 && quantity <= 9)
            return 10; // 10% discount
        if(quantity >= 10 && quantity <= 14)
            return 15; // 15% discount
        if(quantity >= 15)
            return 20; // 20% discount
        return 0;
    }

    public void setItems(String id, String description, double price, int quantity)
    {
        String itemInfo;
        itemInfo =  id + ", "+
                description + ", "+
                (price + "") + ", "+
                (quantity + "") + ", "+
                ((0.01 * findDiscountPercentage(quantity)) + "") + ", "+
                (findTotalDiscount(quantity, price) + "");
        items.add(itemInfo);
    }

    public void printTransactions()
    {
        try
        {
            Calendar calendar= Calendar.getInstance();
            Date today = calendar.getTime();

            //append only if file exist
            PrintWriter outputStream = new PrintWriter(new FileWriter(outputFilename, true));

            //write to file
            for (String item : this.items)
            {
                outputStream.append(new SimpleDateFormat("yyMMddyyHHmm").format(today)).append(", ");
                outputStream.append(item).append(", ");
                outputStream.append(new SimpleDateFormat("MM/dd/yy").format(today)).append(", ");
                outputStream.append(new SimpleDateFormat("hh:mm:ss a z").format(today));
                outputStream.println();
            }

            outputStream.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

}
