// EID 1
// EID 2
/* Use only Java monitors to accomplish the required synchronization */
public class MonitorCyclicBarrier implements CyclicBarrier {



    private int parties;
    private int waitCount;
    private boolean barrEnable;
    // TODO Add other useful variables

    public MonitorCyclicBarrier(int parties) {
        this.parties = parties;
        this.waitCount = 0;
        this.barrEnable = true;
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
        synchronized(this){
            if(!barrEnable){
                return 0;
            }
            else{
                int thisInd = waitCount;
                waitCount++;
                if(waitCount >= parties){
                    this.notifyAll();
                    waitCount = 0;
                }
                else{
                    this.wait();
                }
                return thisInd;
            }
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
            synchronized(this){
                barrEnable = true;
                waitCount = 0;
                this.notifyAll();
            }
            return;
        }
    }

    /*
     * This method deactivates the cyclic barrier.
     * It also releases any waiting threads
     */
    public void deactivate() throws InterruptedException {
        // TODO Implement this function
        synchronized(this){
            barrEnable = false;
            waitCount = 0;
            this.notifyAll();
        }
        return;
    }
}