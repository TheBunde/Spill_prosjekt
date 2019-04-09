package audio;



public class PooledThread extends Thread {

    private static IDAssigner thrID = new IDAssigner(1);

    private ThreadPool thrPool;

    public PooledThread(ThreadPool thrPool) {
        super(thrPool, "Pool:" + thrID.next());
        this.thrPool = thrPool;
    }
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
