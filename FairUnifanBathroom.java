import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

// EID 1
// EID 2

public class FairUnifanBathroom {  
    ArrayList<Integer> bathroom = new ArrayList<Integer>();
    int maxSize = 7;
    ReentrantLock bathroomLock = new ReentrantLock();
    final Condition hasUT = bathroomLock.newCondition();
    final Condition hasOU = bathroomLock.newCondition();
    public synchronized void enterBathroomUT() {
      try{
       
        while(!bathroom.isEmpty() && bathroom.get(0) == 0){
          hasOU.await();
        }

        while(bathroom.size() == maxSize){
          hasUT.await();
        }

        bathroom.add(1);
        hasOU.notify();
      }
      catch (InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      // Called when a UT fan wants to enter bathroom	
    }
      
    public synchronized void enterBathroomOU() {
      try{
        while(!bathroom.isEmpty() && bathroom.get(0) == 1){
          hasUT.await();
        }

        while(bathroom.size() == maxSize){
          hasOU.await();
        }
      
        bathroom.add(0);
        hasUT.notify();
      }
      catch (InterruptedException e) {
          // TODO Auto-generated catch block
        e.printStackTrace();
      }
      // Called when a OU fan wants to enter bathroom
      }
      
    public synchronized void leaveBathroomUT() {
      // Called when a UT fan wants to leave bathroom
      try{
          
        while(bathroom.isEmpty()){
          hasUT.await();
        }

        bathroom.remove(0);
        if(bathroom.size() == maxSize - 1){
          hasUT.notify();
        }
        if(bathroom.size() == 0){
          hasUT.notifyAll();
        }
      }
      catch(InterruptedException e){
        e.printStackTrace();
      }
    }
  
    public synchronized void leaveBathroomOU() {
        try{
          
          while(bathroom.isEmpty()){
            hasOU.await();
          }
          
          bathroom.remove(0);
          if(bathroom.size() == maxSize - 1){
            hasOU.notify();
          }
          if(bathroom.size() == 0){
            hasOU.notifyAll();
          }
        }
        catch(InterruptedException e){
          e.printStackTrace();
        }
    }
      // Called when a OU fan wants to leave bathroom
}
      