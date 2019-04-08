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
            Runnable task = null;
            try {
                task = thrPool.getTask();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(task == null) return;
            try {
                task.run();
            } catch(Throwable t){
                thrPool.uncaughtException(this, t);
            }

        }
    }
}
