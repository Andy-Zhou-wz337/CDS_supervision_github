import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class RunnableDemo implements Runnable {
    public Thread t;
    private int start, end;
    private String threadName;
    private int[] arr;
    private int[] squared_arr;
    private int[] result;
    private Lock lock;

    RunnableDemo(String name, int start, int end, int[] arr, int[] squared_arr, int[] result, Lock lock) {
        this.threadName = name;
        this.start = start;
        this.end = end;
        this.arr = arr;
        this.squared_arr = squared_arr;
        this.result = result;
        this.lock = lock;
        System.out.println("Creating " +  threadName);
    }

    public void run() {
        int temp = 0;
        for (int i = start; i <= end; i++) {
            squared_arr[i] = arr[i] * arr[i];
            temp += squared_arr[i];
        }
        lock.lock();
        result[0] += temp;
        lock.unlock();
    }

    public void start () {
        System.out.println("Starting " +  threadName );
        if (t == null) {
            t = new Thread (this, threadName);
            t.start();
        }
    }
}

public class Concurrent {

    public static void main(String args[]) {
        final int NUM_ARRAY = 1000000000;
        int[] arr = new int[NUM_ARRAY];
        int[] squared_arr = new int[NUM_ARRAY];
        int[] result = new int[]{0};
        Lock lock = new ReentrantLock();


        Random ran = new Random();
        for (int i = 0; i < NUM_ARRAY; i++) {
            arr[i] = ran.nextInt(1, 4);
        }

        RunnableDemo R1 = new RunnableDemo( "Thread-1", 0, NUM_ARRAY/2, arr, squared_arr, result, lock);
        RunnableDemo R2 = new RunnableDemo( "Thread-2", NUM_ARRAY/2+1, NUM_ARRAY-1, arr, squared_arr, result, lock);

        long startTime = System.currentTimeMillis();
        R1.start();

        R2.start();

        // join threads
        try {
            R1.t.join();
            R2.t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Result: " + result[0]);
        long elapsedTimeMillis = System.currentTimeMillis() - startTime;
        System.out.println("Time: " + elapsedTimeMillis);


        int r = 0;
        startTime = System.currentTimeMillis();
        for (int i = 0; i < NUM_ARRAY; i++) {
//            System.out.print(squared_arr[i] + " ");
            r += arr[i] * arr[i];
        }
        System.out.println();
        System.out.println("Result: " + r);
        elapsedTimeMillis = System.currentTimeMillis() - startTime;
        System.out.println("Time: " + elapsedTimeMillis);
    }
}