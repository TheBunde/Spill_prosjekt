package audio;

import java.util.LinkedList;
import java.util.List;

public class ThreadPool extends ThreadGroup {

    private static IDAssigner thrPoolID = new IDAssigner(1);


    private List<Runnable> queue;
    private int thrID;
    private static ThreadPool thisInstance = new ThreadPool(10);
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
    public synchronized void runTask(Runnable task) {
        if(!living) throw new IllegalStateException("Pool:" + thrID + " done");
        if(task != null) {
            queue.add(task);
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
        Thread[] threads = new Thread[activeCount()];
        int count = enumerate(threads);
        for(int i = 0; i < count; i++) {
            try {
                threads[i].join();
            }catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    protected synchronized Runnable getTask() throws InterruptedException{
        while(queue.size() == 0) {
            if (!living) return null;
            wait();
        }
        return queue.remove(0);
    }
}