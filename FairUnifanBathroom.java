import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

// EID 1
// EID 2

public class FairUnifanBathroom {  
    ArrayList<Integer> bathroom = new ArrayList<Integer>();
    int maxSize = 7;
    ReentrantLock bathroomLock = new ReentrantLock();
    final Condition UT = bathroomLock.newCondition();
    final Condition OU = bathroomLock.newCondition();
    volatile int currentTicket = 0;
    volatile int ticketNum = 0;

    public void enterBathroomUT() {
      bathroomLock.lock();
      try{
        int ticket = ticketNum++;
        //System.out.println("UT " + ticket + " " + currentTicket);
        //System.out.println(bathroom.size());
        while(bathroom.size() == maxSize || ticket > currentTicket || (!bathroom.isEmpty() && bathroom.get(0) == 0)){
          UT.await();
        }
       
        currentTicket++;
        bathroom.add(1);
        UT.signal();
       
      }
      catch (InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      finally{
        bathroomLock.unlock();
      }
      // Called when a UT fan wants to enter bathroom	
    }
      
    public void enterBathroomOU() {
      bathroomLock.lock();
      try{  
        int ticket = ticketNum++;
        //System.out.println("OU " + ticket + " " + currentTicket);
        while(bathroom.size() == maxSize || ticket > currentTicket || (!bathroom.isEmpty() && bathroom.get(0) == 1)){
          OU.await();
        }
        
        currentTicket++;
        //System.out.println("number is " + currentTicket);
        bathroom.add(0);
        OU.signal();
        //System.out.println(bathroom.size())
      }
      catch (InterruptedException e) {
          // TODO Auto-generated catch block
        e.printStackTrace();
      }
      finally{
        bathroomLock.unlock();
      }
      // Called when a OU fan wants to enter bathroom
      }
      
    public void leaveBathroomUT() {
      // Called when a UT fan wants to leave bathroom
    
        bathroomLock.lock();
        //System.out.println(bathroom.size());
        
        bathroom.remove(0);
       
        if(bathroom.size() == 0){
          UT.signalAll();
          OU.signalAll();
        }
        bathroomLock.unlock();
    
   
    }
  
    public void leaveBathroomOU() {
        
          bathroomLock.lock();
          //System.out.println("here");
          bathroom.remove(0);
          if(bathroom.size() == 0){
            OU.signalAll();
            UT.signalAll();
          }
          bathroomLock.unlock();
        
       
    }
      // Called when a OU fan wants to leave bathroom
}
      