//CNT4714
//JIALIN ZHENG
//PROJECT 1

import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static javax.swing.JOptionPane.*;

public class Project1 extends JFrame {
    //two panel
    private final JPanel inputPanel = new JPanel(new GridLayout(9,2));
    private final JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

    //label variable
    private final JLabel numLabel = new JLabel();
    private final JLabel idLabel = new JLabel();
    private final JLabel quantityLabel = new JLabel();
    private final JLabel itemLabel = new JLabel();
    private final JLabel subtotalLabel = new JLabel();

    //text field variable
    private final JTextField numText= new JTextField();
    private final JTextField idText = new JTextField();
    private final JTextField quantityText= new JTextField();
    private final JTextField itemText = new JTextField();
    private final JTextField subtotalText= new JTextField();

    //button variable
    private final JButton processButton = new JButton();
    private final JButton confirmButton = new JButton();
    private final JButton viewButton = new JButton();
    private final JButton finishButton = new JButton();
    private final JButton newOrderButton = new JButton();
    private final JButton exitButton = new JButton();

    //store orders and order total for logic
    private final ReadFile data = new ReadFile();
    private int currentNumOrder = 0;
    private int totalItems = 0;
    private double orderTotal = 0;
    private double currentItemSubtotal =0;

    //store String from current order information
    private final StringBuilder orderOverView = new StringBuilder();
    private final StringBuilder orderInfo = new StringBuilder();
    private final ArrayList<String> itemList = new ArrayList<>();
    private final double taxRate = 0.06;

    Project1(){
        //read text file
        data.readInventory();

        // initialize GUI
        JFrame Gui = new JFrame();
        Gui.setSize(900, 300);
        Gui.getContentPane().setBackground(Color.BLACK);
        Gui.setTitle("Nile Dot Com - Spring 2021");
        inputPanel.setBackground(Color.BLACK);
        buttonPanel.setBackground(Color.BLUE);

        //plug panels
        Gui.add(inputPanel,BorderLayout.NORTH);
        Gui.add(buttonPanel,BorderLayout.SOUTH);

        initLabel();
        initButton();

        //plug label and button to panel
        addInputPanel(inputPanel);
        addButton(buttonPanel);

        //deactivate buttons
        confirmButton.setEnabled(false);
        viewButton.setEnabled(false);
        finishButton.setEnabled(false);

        //user cannot modify item info and subtotal text field
        itemText.setEnabled(false);
        subtotalText.setEnabled(false);

        actionListener(Gui);
        Gui.setLocationRelativeTo(null);
        Gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Gui.setVisible(true);

    }
    public void initLabel(){

        numLabel.setText("Enter number of items in this order:");
        idLabel.setText("Enter item ID for Item #1:");
        quantityLabel.setText("Enter quantity for Item #1:");
        itemLabel.setText("Item #1 Info:");
        subtotalLabel.setText("Order Subtotal for 0 item(s):");

        numLabel.setForeground(Color.yellow);
        numLabel.setHorizontalAlignment(JLabel.RIGHT);

        idLabel.setForeground(Color.yellow);
        idLabel.setHorizontalAlignment(JLabel.RIGHT);

        quantityLabel.setForeground(Color.yellow);
        quantityLabel.setHorizontalAlignment(JLabel.RIGHT);

        itemLabel.setForeground(Color.yellow);
        itemLabel.setHorizontalAlignment(JLabel.RIGHT);

        subtotalLabel.setForeground(Color.yellow);
        subtotalLabel.setHorizontalAlignment(JLabel.RIGHT);
    }

    public void initButton(){
        processButton.setText("Process Item #1");
        confirmButton.setText("Confirm Item #1");
        viewButton.setText("View Order");
        finishButton.setText("Finish Order");
        newOrderButton.setText("New Order");
        exitButton.setText("Exit");
    }

    public void addInputPanel(JPanel panel){

        panel.add(new JLabel(" "));
        panel.add(new JLabel(" "));
        panel.add(new JLabel(" "));
        panel.add(new JLabel(" "));

        panel.add(numLabel);
        panel.add(numText);
        panel.add(idLabel);
        panel.add(idText);
        panel.add(quantityLabel);
        panel.add(quantityText);
        panel.add(itemLabel);
        panel.add(itemText);
        panel.add(subtotalLabel);
        panel.add(subtotalText);


    }

    public void addButton(JPanel panel){

        panel.add(processButton);
        panel.add(confirmButton);
        panel.add(viewButton);
        panel.add(finishButton);
        panel.add(newOrderButton);
        panel.add(exitButton);

    }

    public void actionListener(JFrame GUI){

        processButton.addActionListener(e -> {

            //user must enter information
        if(idText.getText().isEmpty() || quantityText.getText().isEmpty() || numText.getText().isEmpty()){
            showMessageDialog(null,
                    "You need to fill all the text fields",
                    "Message", WARNING_MESSAGE);
        }
            //check if item id exist
            if(!data.itemInfoHashMap.containsKey(idText.getText()) && numText.getText().isEmpty()){
                JOptionPane.showMessageDialog(null, "item ID " +
                        idText.getText() + " not in file.", "Message", JOptionPane.INFORMATION_MESSAGE);
                idText.setText("");
            }
            else{

                int quantity = Integer.parseInt(quantityText.getText());
                totalItems = Integer.parseInt(numText.getText());

                ItemInfo temp = data.itemInfoHashMap.get(idText.getText());

                //check item if on stock
                if(temp.getStatus().equalsIgnoreCase(" false" )){
                    JOptionPane.showMessageDialog(null,
                            "Sorry...that item is out of stock, please try another item",
                            "Message", JOptionPane.INFORMATION_MESSAGE);
                    idText.setText("");
                    quantityText.setText("");
                }
                else{
                    confirmButton.setEnabled(true);
                    processButton.setEnabled(false);
                    currentNumOrder++;

                    //store subtotal and total
                    currentItemSubtotal = subtotalAfterDiscount(quantity,temp.getPrice());
                    orderTotal += currentItemSubtotal;

                    //print item information on item text field
                    itemText.setText(idText.getText() + temp.getDescription() +  " $"
                            + temp.getPrice() + " " + quantity +" " + discount(quantity) + "% "
                            + "$" + subtotalAfterDiscount(quantity, temp.getPrice()));

                }
            }

        });

        confirmButton.addActionListener(e -> {

            if (idText.getText().isEmpty()|| quantityText.getText().isEmpty())
            {
                JOptionPane.showMessageDialog(null,
                        "You need to fill all the text fields", "Message", JOptionPane.WARNING_MESSAGE);
            }
            //using temp to track item info
            ItemInfo temp = data.itemInfoHashMap.get(idText.getText());
            JOptionPane.showMessageDialog(null,
                    "Item #" + (currentNumOrder) + " accepted", "Message", JOptionPane.INFORMATION_MESSAGE);


            processButton.setEnabled(true);
            viewButton.setEnabled(true);
            confirmButton.setEnabled(false);
            finishButton.setEnabled(true);
            itemText.setEnabled(false);

            //update button when No. item increasing
            processButton.setText("Process Item #" + (currentNumOrder+1));
            confirmButton.setText("Confirm Item #" + (currentNumOrder + 1));

            //update text field when No. item increasing
            idLabel.setText("Enter ID for Item #" + (currentNumOrder + 1) + ":");
            quantityLabel.setText("Enter quantity for Item #" + (currentNumOrder  + 1) + ":");
            itemLabel.setText("Item #" + currentNumOrder + " info");
            subtotalLabel.setText("Order subtotal for " + currentNumOrder + " item(s)");

            //orderInfo for print info when view button active
            orderInfo.append(currentNumOrder).append(". ").append(itemInfo(idText.getText(), temp,
                    Integer.parseInt(quantityText.getText())));
            orderInfo.append(System.getProperty("line.separator"));
            itemList.add(setItemListInfo(idText.getText(),temp.getDescription(),temp.getPrice(),
                    Integer.parseInt(quantityText.getText())));

            idText.setText("");
            quantityText.setText("");
            subtotalText.setText("$" +  new DecimalFormat("#0.00").format(orderTotal));

            //when reach total order then no more input needed
            if(currentNumOrder >= totalItems){
                idLabel.setVisible(false);
                quantityLabel.setVisible(false);
                processButton.setEnabled(false);
                confirmButton.setEnabled(false);
            }


        });
        //print info from orderInfo
        viewButton.addActionListener(e ->
        {
            JOptionPane.showMessageDialog(null, orderInfo.toString() + "",
                    "Message", JOptionPane.INFORMATION_MESSAGE);
        });

        //pop overview Panel, print transaction to text file from itemList(Arraylist)
        finishButton.addActionListener(e ->
        {
            orderOverviewPanel();
            printTransactions();
            System.exit(0);
        });

        newOrderButton.addActionListener(e ->
        {
            //dispose frame then pop new window
            GUI.dispose();
            new Project1();
        });

        exitButton.addActionListener(e ->
        {
        System.exit(0);
        });

    }

    public void orderInfoGenerator(String date, String time)
    {
        DecimalFormat df = new DecimalFormat("######.##");

        orderOverView.append("Date: ").append(date).append(" ").append(time);
        orderOverView.append(System.getProperty("line.separator")).append(System.getProperty("line.separator"));
        orderOverView.append("Number of line items: ").append(totalItems);
        orderOverView.append(System.getProperty("line.separator")).append(System.getProperty("line.separator"));
        orderOverView.append("Item# /ID / Price / Qty / Disc% / Subtotal");
        orderOverView.append(System.getProperty("line.separator")).append(System.getProperty("line.separator"));
        orderOverView.append(orderInfo.toString());
        orderOverView.append(System.getProperty("line.separator")).append(System.getProperty("line.separator"));
        orderOverView.append("Order subtotal:   $").append(df.format(orderTotal));
        orderOverView.append(System.getProperty("line.separator")).append(System.getProperty("line.separator"));
        orderOverView.append("Tax rate:     6%");
        orderOverView.append(System.getProperty("line.separator")).append(System.getProperty("line.separator"));
        orderOverView.append("Tax amount:      $").append(df.format(taxRate*orderTotal));
        orderOverView.append(System.getProperty("line.separator")).append(System.getProperty("line.separator"));
        orderOverView.append("Order total:      $").append(df.format(orderTotal*(1+taxRate)));
        orderOverView.append(System.getProperty("line.separator")).append(System.getProperty("line.separator"));
        orderOverView.append("Thanks for shopping at the Nile Dot Com!");
    }
    public void printTransactions()
    {
        Calendar calendar= Calendar.getInstance();
        Date today = calendar.getTime();

        //file exception
        PrintWriter outputStream = null;
        try {
            outputStream = new PrintWriter(new FileWriter("src/transactions.txt", true));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //write to file
        for (String itemList :itemList )
        {
            outputStream.append(new SimpleDateFormat("yyMMddyyHHmm").format(today)).append(", ");
            outputStream.append(itemList).append(",");
            outputStream.append(new SimpleDateFormat("MM/dd/yy").format(today)).append(", ");
            outputStream.append(new SimpleDateFormat("hh:mm:ss a z").format(today));
            outputStream.println();
        }
        outputStream.close();
    }

    public void orderOverviewPanel()
    {
        Calendar calendar= Calendar.getInstance();
        Date today = calendar.getTime();

        //set finishOrder dialog box
        orderInfoGenerator(new SimpleDateFormat("MM/dd/yy").format(today),
                new SimpleDateFormat("hh:mm:ss a z").format(today));
        JOptionPane.showMessageDialog(null, orderOverView.toString() + "",
                "Message", JOptionPane.INFORMATION_MESSAGE);
    }

    //generate string format for itemList arraylist string
    public String setItemListInfo(String id, String description, double price, int quantity)
    {
        return  id + ", "+ description + ", "+ price + ", "+ quantity + ", "+
                (0.01 * discount(quantity))  + ", "+ (subtotalAfterDiscount(quantity, price) + "");
    }

    //generate string format for view button
    public String itemInfo(String id, ItemInfo temp,int quantity){
        return id  + temp.getDescription() + "$" + temp.getPrice() + " " + quantity + " " + discount(quantity) + "% "
                + "$" + subtotalAfterDiscount(quantity, temp.getPrice());
    }

    //discount base on quantity
    public int discount(int quantity)
    {
        // 0% discount
        if(quantity >= 1 && quantity <= 4 )
            return 0;
        // 10% discount
        if(quantity >= 5 && quantity <= 9)
            return 10;
        // 15% discount
        if(quantity >= 10 && quantity <= 14)
            return 15;
        // 20% discount
        if(quantity >= 15)
            return 20;
        return 0;
    }

    public double subtotalAfterDiscount(int quantity, double price)
    {
        DecimalFormat df = new DecimalFormat("######.##");

        return Double.parseDouble(df.format(0.01 * (100 - discount(quantity)) * quantity * price));
    }


    public static void main(String[] args) {

        Project1 window = new Project1();
    }


}
