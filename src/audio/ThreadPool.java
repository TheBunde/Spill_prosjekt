package audio;

import java.util.LinkedList;
import java.util.List;

/**
 * This class is used to create a thread pool and includes methods
 * to utilize the pool.
 * @author henrikwt
 */

public class ThreadPool extends ThreadGroup {

    private static IDAssigner thrPoolID = new IDAssigner(1);
    private List<Runnable> queue;
    private int thrID;
    private static ThreadPool thisInstance = new ThreadPool(11);
    private boolean living;

    /**
     * Used to access this single instance.
     * @return Instance of the thread pool.
     */
    public static ThreadPool getInstance(){
        return thisInstance;
    }

    /**
     * Constructor
     * @param numberOfThreads Number of threads in the pool.
     */
    public ThreadPool(int numberOfThreads) {
        super("Pool");
        this.thrID = thrPoolID.getBID();
        setDaemon(true);
        queue = new LinkedList<Runnable>();
        living = true;
        for(int i = 0; i < numberOfThreads; i++) {
            new PooledThread(this).start();
        }
    }

    /**
     * Adds a task to the queue in the thread pool.
     * @param threadTask Specified task.
     */
    public synchronized void runTask(Runnable threadTask) {
        if(!living) throw new IllegalStateException("Pool:" + thrID + " done");
        if(threadTask != null) {
            queue.add(threadTask);
            notify();
        }
    }

    /**
     * Closes the thread pool.
     */
    public synchronized void closePool() {
        if(!living) return;
        living = false;
        queue.clear();
        interrupt();
    }

    /**
     * Allows the threads to run together by waking up all threads that are waiting.
     */
    public void join() {
        synchronized(this) {
            notifyAll();
        }
        Thread[] threadList = new Thread[activeCount()];
        int number = enumerate(threadList);
        for(int i = 0; i < number; i++) {
            try {
                threadList[i].join();
            }catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Makes thread wait while queue is empty.
     * @return Queue
     * @throws InterruptedException
     */
    protected synchronized Runnable getThreadTask() throws InterruptedException{
        while(queue.size() == 0) {
            if (!living) return null;
            wait();
        }
        return queue.remove(0);
    }
}