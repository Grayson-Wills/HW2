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
    public void enterBathroomUT() {
      try{
        bathroomLock.lock();
        while(!bathroom.isEmpty() && bathroom.get(0) == 0){
          hasOU.await();
        }

        while(bathroom.size() == maxSize){
          hasUT.await();
        }

        bathroom.add(1);
    
        bathroomLock.unlock();
      }
      catch (InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      // Called when a UT fan wants to enter bathroom	
    }
      
    public void enterBathroomOU() {
      try{
        bathroomLock.lock();
        while(!bathroom.isEmpty() && bathroom.get(0) == 1){
          hasUT.await();
        }

        while(bathroom.size() == maxSize){
          hasOU.await();
        }

        bathroom.add(0);
        bathroomLock.unlock();
       
      }
      catch (InterruptedException e) {
          // TODO Auto-generated catch block
        e.printStackTrace();
      }
      // Called when a OU fan wants to enter bathroom
      }
      
    public void leaveBathroomUT() {
      // Called when a UT fan wants to leave bathroom
      try{
        bathroomLock.lock();
        System.out.println(bathroom.size());
        while(bathroom.isEmpty() || bathroom.get(0) == 0){
          hasUT.await();
        }

        bathroom.remove(0);
        if(bathroom.size() == maxSize - 1){
          hasUT.signal();
        }
        if(bathroom.size() == 0){
          hasUT.signalAll();
        }
        bathroomLock.unlock();
      }
      catch(InterruptedException e){
        e.printStackTrace();
      }
    }
  
    public void leaveBathroomOU() {
        try{
          bathroomLock.lock();
          while(bathroom.isEmpty() || bathroom.get(0) == 1){
            hasOU.await();
          }
          
          bathroom.remove(0);
          if(bathroom.size() == maxSize - 1){
            hasOU.signal();
          }
          if(bathroom.size() == 0){
            hasOU.signalAll();
          }
          bathroomLock.unlock();
        }
        catch(InterruptedException e){
          e.printStackTrace();
        }
    }
      // Called when a OU fan wants to leave bathroom
}
      