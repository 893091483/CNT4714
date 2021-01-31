/* Name: <Zefeng Yao>
 Course: CNT 4714 – Spring 2021
 Assignment title: Project 1 – Event-driven Enterprise Simulation
 Date: Sunday January 31, 2021
*/

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TreeMap;


public class Store extends JFrame
{
    // create Panels
    private final JPanel topPanel = new JPanel(new GridLayout(11,2));
    private final JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

    // create labels
    private final JLabel labelNumItems = new JLabel();
    private final JLabel labelItemID = new JLabel();
    private final JLabel labelQuantity = new JLabel();
    private final JLabel labelItemInfo = new JLabel();
    private final JLabel labelSubtotal = new JLabel();

    private final JTextField textNumItems = new JTextField();
    private final JTextField textItemID = new JTextField();
    private final JTextField textQuantity = new JTextField();
    private final JTextField textItemInfo = new JTextField();
    private final JTextField textSubtotal = new JTextField();

    // create Buttons
    private final JButton buttonProcessItem = new JButton();
    private final JButton buttonConfirmItem = new JButton();
    private final JButton buttonViewOrder = new JButton();
    private final JButton buttonFinishOrder = new JButton();
    private final JButton buttonNewOrder = new JButton();
    private final JButton buttonExit = new JButton();

    //get the storage
    private final TreeMap<String, Item> myStorage;

    private final Order order;

    String idInProcess;
    String quantityInProcess;
    String outputFilename;

    Store(TreeMap<String, Item> storage, String outputFilename)
    {
        myStorage = storage;
        this.outputFilename = outputFilename;
        order = new Order(this.outputFilename);

        setLabels();
        setButtons();
        setTopPanel();
        setBottomPanel();

        //deactivate buttons for first time
        buttonConfirmItem.setEnabled(false);
        buttonViewOrder.setEnabled(false);
        buttonFinishOrder.setEnabled(false);

        //user cannot modify item info and subtotal no matter when
        textItemInfo.setEnabled(false);
        textSubtotal.setEnabled(false);

        ActionListeners();

        //add the panels to the frame
        this.add(topPanel, BorderLayout.NORTH);
        this.add(bottomPanel, BorderLayout.SOUTH);

        this.pack();
        this.setTitle("Nile Dot Com - Spring 2021");
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setSize(900,290);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    public void ActionListeners()
    {
        buttonProcessItem.addActionListener(e ->
        {
            try
            {
                // check if use put all the infos
                if (textItemID.getText().equalsIgnoreCase("") ||
                    textQuantity.getText().equalsIgnoreCase("") ||
                    textNumItems.getText().equalsIgnoreCase(""))
                {
                    JOptionPane.showMessageDialog(null, "You need to fill all the text fields", "Message", JOptionPane.WARNING_MESSAGE);
                    throw new Exception();
                }

                int numOfItems = Integer.parseInt(textNumItems.getText());
                int quantity = Integer.parseInt(textQuantity.getText());

                // set the the number of total items
                if (order.getTotalItems() == 0 && numOfItems > 0)
                {
                    order.setTotalItems(numOfItems);
                    textNumItems.setEnabled(false);
                }

                idInProcess = textItemID.getText();
                quantityInProcess = textQuantity.getText();

                // get the item from inventory,
                // if not it is null
                Item currentItem = myStorage.get(idInProcess);
                if (currentItem != null)
                {
                    if (currentItem.getStatus().equalsIgnoreCase("true"))
                    {
                        order.setItems(currentItem.getId(), currentItem.getDescription(), currentItem.getPrice(), quantity);

                        // show the item info to user
                        textItemInfo.setText(currentItem.getId() + currentItem.getDescription() +  " $" + currentItem.getPrice() + " " + quantity + " " + order.findDiscountPercentage(quantity) + "% " + "$" + order.findTotalDiscount(quantity, currentItem.getPrice()));

                        // enable confirm item
                        buttonConfirmItem.setEnabled(true);
                        // disable process item
                        buttonProcessItem.setEnabled(false);

                        // calculate subtotal
                        order.setOrderSubtotal(quantity, currentItem.getPrice());

                        // change the label text
                        if(order.getCurrentNumItems() < order.getTotalItems())
                        {
                            labelItemInfo.setText("Item #" + (order.getCurrentNumItems() + 1) + " info:");
                        }
                    }
                    else if (currentItem.getStatus().equalsIgnoreCase("false"))
                    {
                        JOptionPane.showMessageDialog(null, "Sorry...that item is out of stock, please try another item", "Message", JOptionPane.INFORMATION_MESSAGE);
                        textItemID.setText("");
                        textQuantity.setText("");
                    }
                }
                else
                {
                    JOptionPane.showMessageDialog(null, "item ID " + textItemID.getText() + " not in file.", "Message", JOptionPane.INFORMATION_MESSAGE);
                }
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }

        });

        buttonConfirmItem.addActionListener(e ->
        {
            try
            {
                // check if use put all the infos
                if (textItemID.getText().equalsIgnoreCase("") ||
                    textQuantity.getText().equalsIgnoreCase(""))
                {
                    JOptionPane.showMessageDialog(null, "You need to fill all the text fields", "Warning", JOptionPane.WARNING_MESSAGE);
                    throw new Exception();
                }

                if(!idInProcess.equals(textItemID.getText()) || !quantityInProcess.equals(textQuantity.getText()))
                {
                    JOptionPane.showMessageDialog(null, "the data in your id text field or quantity text field  if different than the data before you click the process button, so we take the oldest data.", "Warning", JOptionPane.WARNING_MESSAGE);
                }

                //increment currentNumItems
                order.incrementCurrentNumItems();

                JOptionPane.showMessageDialog(null, "Item #" + (order.getCurrentNumItems()) + " accepted", "Message", JOptionPane.INFORMATION_MESSAGE);

                //add item to viewOrder
                order.setViewOrder(textItemInfo.getText());

                //enable buttons
                buttonProcessItem.setEnabled(true);
                buttonViewOrder.setEnabled(true);
                buttonFinishOrder.setEnabled(true);
                buttonConfirmItem.setEnabled(false);
                textNumItems.setEnabled(false);


                //update button text
                buttonProcessItem.setText("Process Item #" + (order.getCurrentNumItems() + 1));
                buttonConfirmItem.setText("Confirm Item #" + (order.getCurrentNumItems() + 1));

                //update textFields
                textItemID.setText("");
                textQuantity.setText("");
                textSubtotal.setText("$" +  new DecimalFormat("#0.00").format(order.getOrderSubtotal()));

                //update labels
                labelItemID.setText("Enter Book ID for Item #" + (order.getCurrentNumItems() + 1) + ":");
                labelQuantity.setText("Enter quantity for Item #" + (order.getCurrentNumItems() + 1) + ":");
                labelSubtotal.setText("Order subtotal for " + (order.getCurrentNumItems())+ " item(s)");


                if(order.getCurrentNumItems() >= order.getTotalItems())
                {
                    labelItemID.setVisible(false);
                    labelQuantity.setVisible(false);
                    buttonProcessItem.setEnabled(false);
                    buttonConfirmItem.setEnabled(false);
                }
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        });

        buttonViewOrder.addActionListener(e ->
        {
            try
            {
                // view order window
                JOptionPane.showMessageDialog(null, order.getViewOrder() + "", "Message", JOptionPane.INFORMATION_MESSAGE);
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        });

        buttonFinishOrder.addActionListener(e ->
        {
            try
            {
                // set and show finish order window
                showFinishOrder();

                // print output
                order.printTransactions();

                System.exit(0);
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        });

        buttonNewOrder.addActionListener(e ->
        {
            try
            {
                // close the current one
                Store.super.dispose();

                // open a new window
                new Store(myStorage, this.outputFilename);
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        });

        buttonExit.addActionListener(e ->
        {
            try
            {
                System.exit(0);
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        });
    }

    public void showFinishOrder()
    {
        Calendar calendar= Calendar.getInstance();
        Date today = calendar.getTime();

        //set finishOrder string builder for the JOptionPane
        order.setFinishOrder(new SimpleDateFormat("MM/dd/yy").format(today), new SimpleDateFormat("hh:mm:ss a z").format(today));
        JOptionPane.showMessageDialog(null, order.getFinishOrder() + "", "Message", JOptionPane.INFORMATION_MESSAGE);
    }

    public void setLabels()
    {
        labelNumItems.setText("Enter number of items in this order:");
        labelItemID.setText("Enter item ID for Item #1:");
        labelQuantity.setText("Enter quantity for Item #1:");
        labelItemInfo.setText("Item #1 Info:");
        labelSubtotal.setText("Order Subtotal for 0 item(s):");

        labelNumItems.setForeground(Color.yellow);
        labelNumItems.setHorizontalAlignment(JLabel.RIGHT);

        labelSubtotal.setForeground(Color.yellow);
        labelSubtotal.setHorizontalAlignment(JLabel.RIGHT);

        labelItemID.setForeground(Color.yellow);
        labelItemID.setHorizontalAlignment(JLabel.RIGHT);

        labelQuantity.setForeground(Color.yellow);
        labelQuantity.setHorizontalAlignment(JLabel.RIGHT);

        labelItemInfo.setForeground(Color.yellow);
        labelItemInfo.setHorizontalAlignment(JLabel.RIGHT);
    }

    public void setButtons()
    {
        buttonProcessItem.setText("Process Item #1");
        buttonConfirmItem.setText("Confirm Item #1");
        buttonViewOrder.setText("View Order");
        buttonFinishOrder.setText("Finish Order");
        buttonNewOrder.setText("New Order");
        buttonExit.setText("Exit");
    }

    public void setTopPanel()
    {
        for(int i=0; i < 3; i++)
        {
            topPanel.add(new JLabel("")); // place holder
            topPanel.add(new JLabel("")); // place holder
        }

        topPanel.setBackground(Color.BLACK);
        topPanel.add(labelNumItems);
        topPanel.add(textNumItems);
        topPanel.add(labelItemID);
        topPanel.add(textItemID);
        topPanel.add(labelQuantity);
        topPanel.add(textQuantity);
        topPanel.add(labelItemInfo);
        topPanel.add(textItemInfo);
        topPanel.add(labelSubtotal);
        topPanel.add(textSubtotal);
    }

    public void setBottomPanel()
    {
        bottomPanel.setBackground(Color.blue);
        bottomPanel.add(buttonProcessItem);
        bottomPanel.add(buttonConfirmItem);
        bottomPanel.add(buttonViewOrder);
        bottomPanel.add(buttonFinishOrder);
        bottomPanel.add(buttonNewOrder);
        bottomPanel.add(buttonExit);
    }
}


