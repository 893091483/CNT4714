import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SynchronizedBuffer implements Buffer {

        private final Random generator = new Random();
        private final Lock accessLock = new ReentrantLock();
        private final Condition withdrawMoney = accessLock.newCondition();
        private final Condition depositMoney = accessLock.newCondition();

        private int currentBalance = 0;
        private boolean occupied = false;


        public void deposit(String threadName, int amount)
        {
            accessLock.lock();
            try
            {
                while(occupied)
                {
                    depositMoney.await();
                }

                currentBalance = currentBalance +  amount;
                occupied = true;
                displayDeposit(threadName + " deposits $" + amount);
                withdrawMoney.signal();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            finally
            {
                accessLock.unlock();
            }
        }


        public void withdraw(String threadName, int amount)
        {
            accessLock.lock();
            try
            {
                while(!occupied)
                {
                    depositMoney.await();
                }

                displayWithdraw(threadName + " withdraws $" + amount, amount);

                if (generator.nextInt(8) == 1)
                {
                    occupied = false;
                }
               depositMoney.signal();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            finally
            {
                accessLock.unlock();
            }
        }

        public void displayDeposit(String operation)
        {
            System.out.println(operation + "\t\t\t\t\t\t\t\t\t\t" + "(+) Balance is $" + currentBalance);
        }

        public void displayWithdraw(String operation, int amount)
        {
            if (currentBalance - amount >= 0)
            {
                currentBalance = currentBalance - amount;
                System.out.println("\t\t\t\t\t\t\t" + operation + "\t\t\t" + "(-) Balance is $" + currentBalance);
            }
            else
            {
                System.out.println("\t\t\t\t\t\t\t" + operation + "\t\t\t(******) WITHDRAWAL BLOCKED - INSUFFICIENT FUNDS!!!!");
            }
        }

}
