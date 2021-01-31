import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;


public class ReadFile {
    public final HashMap<String,ItemInfo> itemInfoHashMap = new HashMap<>();
    public void readInventory() {

        try {
            File inventoryObj = new File("src/inventory.txt");
            Scanner scan = new Scanner(inventoryObj);
            while(scan.hasNext()) {
                String data = scan.nextLine();
                inventoryFiling(data);
            }
        }
        catch(FileNotFoundException e) {
            System.out.println("Error");
            e.printStackTrace();

        }

    }
    private void inventoryFiling(String inventoryItem) {

        String[] s = inventoryItem.split(",");
        ItemInfo data = new ItemInfo(s[1],s[2],Double.parseDouble(s[3]));
        //System.out.println(s[0] + " "+ s[1] + " "+ s[2] + " " + Double.valueOf(s[3]));
        itemInfoHashMap.put(s[0],data);

    }

}
