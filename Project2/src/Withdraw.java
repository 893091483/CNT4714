import java.util.Random;
public class Withdraw implements  Runnable{

    private static Random generator = new Random();
    private final Buffer thread;
    private final String threadName;

    public Withdraw(String name,Buffer instance){
        threadName = name;
        thread = instance;
    }
    public void run() {
        try{
            while (true){
                Thread.sleep(generator.nextInt(50));
                int withdrawalAmount = 1 + generator.nextInt(49);
                thread.withdraw(threadName, withdrawalAmount);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
