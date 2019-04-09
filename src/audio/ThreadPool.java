package audio;

import java.util.LinkedList;
import java.util.List;

public class ThreadPool extends ThreadGroup {

    private static IDAssigner thrPoolID = new IDAssigner(1);
    private List<Runnable> queue;
    private int thrID;
    private static ThreadPool thisInstance = new ThreadPool(11);
    private boolean living;

    public static ThreadPool getInstance(){
        return thisInstance;
    }

    public ThreadPool(int numberOfThreads) {
        super("Pool");
        this.thrID = thrPoolID.getID();
        setDaemon(true);
        queue = new LinkedList<Runnable>();
        living = true;
        for(int i = 0; i < numberOfThreads; i++) {
            new PooledThread(this).start();
        }
    }
    public synchronized void runTask(Runnable threadTask) {
        if(!living) throw new IllegalStateException("Pool:" + thrID + " done");
        if(threadTask != null) {
            queue.add(threadTask);
            notify();
        }
    }
    public synchronized void close() {
        if(!living) return;
        living = false;
        queue.clear();
        interrupt();
    }
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
    protected synchronized Runnable getThreadTask() throws InterruptedException{
        while(queue.size() == 0) {
            if (!living) return null;
            wait();
        }
        return queue.remove(0);
    }
}