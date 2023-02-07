
public class FairUnifanBathroomTest implements Runnable{

    int count;
    
    public FairUnifanBathroomTest(){
        count = 0;
    }
    
    public void run(){
        enterBathroomUT();
        enterBathroomUT();
        leaveBathroomUT();
        leaveBathroomUT();
    }
    
    public static void main(String[] args) throws InterruptedException{
        Thread[] t = new Thread[10];
    
        for(int i = 0; i < 10; i++){
            t[i] = new Thread(new FairUnifanBathroomTest());
        }
        for(int i = 0; i < 10; i++){
            t[i].start();
        }
        for(int i = 0; i < 10; i++){
            t[i].join();
        }
    }
}

