
public class FairUnifanBathroomTest implements Runnable{

    FairUnifanBathroom thisBathroom;
    
    public FairUnifanBathroomTest(FairUnifanBathroom thisBathroom){
        this.thisBathroom = thisBathroom;
    }
    
    public void run(){
        thisBathroom.enterBathroomOU();
        thisBathroom.leaveBathroomOU();      
    }
    
    public static void main(String[] args) throws InterruptedException{
        Thread[] t = new Thread[500];
        FairUnifanBathroom bathroom = new FairUnifanBathroom();

        Thread[] k = new Thread[500];


        for(int i = 0; i < 500; i++){
            t[i] = new Thread(new FairUnifanBathroomTest(bathroom));

            Thread UTFan = new Thread(){
                public void run(){
                    bathroom.enterBathroomUT();
                    bathroom.leaveBathroomUT();
                }
            };
            k[i] = UTFan;
        }

        for(int i = 0; i < 500; i++){
            t[i].start();
            k[i].start();
            //bathroom.leaveBathroomOU();
        }
        
    
        for(int i = 0; i < 500; i++){
            t[i].join();
            k[i].join();
        }
    }
}

