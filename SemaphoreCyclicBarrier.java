import java.util.concurrent.Semaphore;


public class SemaphoreCyclicBarrier implements CyclicBarrier {

    private int parties;
    private int waitCount;
    private int doneCount;
    private boolean barrEnable;
    private Semaphore countLock;
    private Semaphore wait;
    private Semaphore cycle;
    // TODO Add other useful variables

    public SemaphoreCyclicBarrier(int parties){
        this.parties = parties;
        this.waitCount = 0;
        this.doneCount = 0;
        this.barrEnable = true;
        this.countLock = new Semaphore(1);
        this.wait = new Semaphore(parties);
        this.wait.drainPermits();
        this.cycle = new Semaphore(parties);

        // TODO Add any other initialization statements
    }

    /*
     * An active CyclicBarrier waits until all parties have invoked
     * await on this CyclicBarrier. If the current thread is not
     * the last to arrive then it is disabled for thread scheduling
     * purposes and lies dormant until the last thread arrives.
     * An inactive CyclicBarrier does not block the calling thread. It
     * instead allows the thread to proceed by immediately returning.
     * Returns: the arrival index of the current thread, where index 0
     * indicates the first to arrive and (parties-1) indicates
     * the last to arrive.
     */
    public int await() throws InterruptedException {
        // TODO Implement this function
        if(!barrEnable){
            return 0;
        }
        else{
            cycle.acquire();
            countLock.acquire();
            int thisInd = waitCount;
            waitCount++;
            countLock.release();

            if(waitCount >= parties){
                wait.release(parties);
            }

            wait.acquire();
            countLock.acquire();
            doneCount++;
            wait.release();

            if(doneCount >= parties){
                doneCount = 0;
                waitCount = 0;
                wait.drainPermits();
                cycle.release(parties);
            }
            countLock.release();
            return thisInd;

        }


    }

    /*
     * This method activates the cyclic barrier. If it is already in
     * the active state, no change is made.
     * If the barrier is in the inactive state, it is activated and
     * the state of the barrier is reset to its initial value.
     */
    public void activate() throws InterruptedException {
        // TODO Implement this function
        if(barrEnable){
            return;
        }
        else{
            barrEnable = true;
            countLock.acquire();
            waitCount = 0;
            doneCount = 0;
            countLock.release();
            wait.release(parties - wait.availablePermits());
            wait.drainPermits();
            return;
        }
    }

    /*
     * This method deactivates the cyclic barrier.
     * It also releases any waiting threads
     */
    public void deactivate() throws InterruptedException {
        // TODO Implement this function
        barrEnable = false;
        wait.release(waitCount);
        countLock.acquire();
        waitCount = 0;
        doneCount = 0;
        countLock.release();
        cycle.release(parties - cycle.availablePermits());
        return;

    }
}