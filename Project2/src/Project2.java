
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Project2 {

    public static final int numWithdraw = 9;
    public static final int numDeposit = 5;

    public static void main(String[] args) {


        ExecutorService application = Executors.newFixedThreadPool(numWithdraw + numDeposit);
        Buffer account = new SynchronizedBuffer();

        System.out.println("Deposit Threads\t\t\t\tWithdrawal Thread\t\t\t\tBalance");
        System.out.println("----------------\t\t\t-----------------\t\t\t\t--------");


        try {

            for (int i = 1; i <= numDeposit; i++)
            {
                application.execute(new Deposit("Thread D" + i,account ));
            }
            for (int i = 0; i < numWithdraw; i++) {
                application.execute(new Withdraw("Thread W" + i,account));
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        application.shutdown();
    }
}
