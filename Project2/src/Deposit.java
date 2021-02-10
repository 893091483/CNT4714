import java.util.Random;

public class Deposit implements  Runnable{

    private static Random generator = new Random();
    private final Buffer thread;
    private final String threadName;

    public Deposit(String name,Buffer instance){
        threadName = name;
        thread = instance;
    }
    public void run() {
        try{
            while (true){
                Thread.sleep(generator.nextInt(50));
                int depositAmount = 1 + generator.nextInt(249);
                thread.deposit(threadName, depositAmount);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
