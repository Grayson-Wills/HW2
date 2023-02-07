
public class FairUnifanBathroomTest implements Runnable{

    FairUnifanBathroom thisBathroom;
    
    public FairUnifanBathroomTest(FairUnifanBathroom thisBathroom){
        this.thisBathroom = thisBathroom;
    }
    
    public void run(){
        thisBathroom.enterBathroomUT();
        thisBathroom.enterBathroomUT();
        thisBathroom.leaveBathroomUT();
        thisBathroom.leaveBathroomUT();
      
      
    }
    
    public static void main(String[] args) throws InterruptedException{
        Thread[] t = new Thread[10];
        FairUnifanBathroom bathroom = new FairUnifanBathroom();

        for(int i = 0; i < 10; i++){
            t[i] = new Thread(new FairUnifanBathroomTest(bathroom));
        }
        for(int i = 0; i < 10; i++){
            t[i].start();
        }
      
 
        
      
        for(int i = 0; i < 10; i++){
            t[i].join();
        }
    }
}

