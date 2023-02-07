import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

class Position{
    Position nextPosition;
    ReentrantLock myLock;
    int myPriority;
    String myName;

    public Position(Position nextPosition, int myPriority, String myName){
        this.nextPosition = nextPosition;
        this.myName = myName;
        this.myLock = new ReentrantLock();
        this.myPriority = myPriority;

    }
}

class myList{
    Position head;
    ReentrantLock myLock;
    final Condition Full;
    final Condition Empty;

    public myList(Position top){
        this.myLock = new ReentrantLock();
        Empty = this.myLock.newCondition();
        Full = this.myLock.newCondition();
        this.head = top;
    }
}

public class PriorityQueue {
    int currentSize;
    int maxSize;
    myList head;
    
    public PriorityQueue(int maxSize) {
        head = new myList(null);
        this.maxSize = maxSize;
        this.currentSize = 0;
        // Creates a Priority queue with maximum allowed size as capacity
    }

    public int add(String name, int priority) {
       
        // Adds the name with its priority to this queue.
        // Returns the current position in the list where the name was inserted;
        // otherwise, returns -1 if the name is already present in the list.
        // This method blocks when the list is full.
        myList top = head;
        int position = 0;
        Position previous, current;
        Position newPosition = new Position(null, priority, name);
        
        try{
            while(currentSize == maxSize){
            
                top.Full.await();
            
            }
            
            if(search(name) != -1){
                return -1;
            }
            top.myLock.lock();
            previous = top.head;
            current = previous.nextPosition;
            previous.myLock.lock();
            top.myLock.unlock();
            if(current != null){
                current.myLock.lock();
            }

            while(current != null){
                if(current.myPriority < priority){
                    break;
                }
                Position temp = previous;
                previous = current;
                current = current.nextPosition;
                temp.myLock.unlock();
                if(current != null){
                    current.myLock.lock();
                }
                position++;
                
            }

            newPosition.nextPosition = current;
            previous.nextPosition = newPosition;
            previous.myLock.unlock();
            currentSize++;
            if(current != null){
                current.myLock.unlock();
            }
            top.Empty.signal();

        }catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        return position;
    }

    public int search(String name) {
        // Returns the position of the name in the list;
        // otherwise, returns -1 if the name is not found.
        if(head.head == null){
            return -1;
        }
        return -1;
    }

    public String getFirst() {
        myList top = head;
        String answer = "";
      
        try{
            top.myLock.lock();
            while(currentSize == 0){
                
                top.Empty.await();
          
            // Retrieves and removes the name with the highest priority in the list,
            // or blocks the thread if the list is empty.
          
            }
            Position previous, current;
            previous = top.head;
            current = previous.nextPosition;
            previous.myLock.lock();
            top.myLock.unlock();
            if(current != null){
                current.myLock.lock();
            }
            answer = current.myName;
            previous.nextPosition = current.nextPosition;
            current = null;
            currentSize--;
            top.Full.signal();
        }
        catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return answer;
    }
}