public interface Buffer {
    void deposit (String threadName,int value);
    void withdraw(String threadName,int value);
}
