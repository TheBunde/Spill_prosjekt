package audio;


/**
 * This class is utilized to run the correct thread in the thread pool.
 * @author henrikwt
 */
public class PooledThread extends Thread {

    private static IDAssigner thrID = new IDAssigner(1);

    private ThreadPool thrPool;

    /**
     * Constructor
     * @param thrPool Thread Pool
     */
    public PooledThread(ThreadPool thrPool) {
        super(thrPool, "Pool:" + thrID.nextBID());
        this.thrPool = thrPool;
    }

    /**
     * Run method.
     */
    @Override
    public void run() {
        while (!isInterrupted()) {
            Runnable threadTask = null;
            try {
                threadTask = thrPool.getThreadTask();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(threadTask == null) return;
            try {
                threadTask.run();
            } catch(Throwable thr){
                thrPool.uncaughtException(this, thr);
            }

        }
    }
}

