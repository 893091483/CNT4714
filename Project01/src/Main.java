/* Name: <Zefeng Yao>
 Course: CNT 4714 – Spring 2021
 Assignment title: Project 1 – Event-driven Enterprise Simulation
 Date: Sunday January 31, 2021
*/

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.TreeMap;

public class Main
{
    // read the inventory.txt and store all the info to a Treemap<String, Item>
    // in this map, the key is "id" and the value is the Item itself
    // So i can use Treemap.get(id) to find value, it reduces lines and saves time
    public static TreeMap<String, Item> setUpItems(String filename)
    {
        TreeMap<String, Item> myItemMap = new TreeMap<>();
        try
        {
            FileReader myObj = new FileReader(filename);
            BufferedReader myReader = new BufferedReader(myObj);
            String line;
            while ((line=myReader.readLine()) != null)
            {
                String [] tokens = line.split(", ");
                double price = Double.parseDouble(tokens[3]);
                myItemMap.put(tokens[0], new Item(tokens[0], tokens[1], tokens[2], price));
            }
            myReader.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return myItemMap;
    }

    // just make a new Store, and java "garbage collector" is gonna takes care for me
    public static void main(String[] args)
    {
        new Store(setUpItems("src/inventory.txt"), "src/transactions.txt");
    }
}
