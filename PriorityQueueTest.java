import java.util.concurrent.TimeUnit;

public class PriorityQueueTest implements Runnable {
	// final static int numParties = 5;
	// static int waitCount = 0;
	// static int leaveCount = 0;
	// static boolean barrierBroken = false;
	private static PriorityQueue queue;

	int numRounds;

	// public static void reset() {
	// 	waitCount = -1;
	// 	leaveCount = -1;
	// 	barrierBroken = false;
	// }

	public PriorityQueueTest(int numRounds) {
		this.numRounds = numRounds;
	}

	public void run() {
		// int index = -1;

		// for (int round = 0; round < numRounds; ++round) {
		// 	synchronized(CyclicBarrier.class) {
		// 		if(round<waitCount)
		// 			barrierBroken = true;
		// 		waitCount = Integer.max(waitCount, round);
		// 	}
		// 	// UNCOMMENT BELOW LINE FOR DEBUGGING PRINT STATEMENTS
		// 	System.out.println("Thread " + Thread.currentThread().getId() + " is waiting round:" + round);
		// 	try {
		// 		index = barrier.await();
		// 	} catch (InterruptedException e) {
		// 		e.printStackTrace();
		// 	}
		// 	synchronized(CyclicBarrier.class) {
		// 		if(round<leaveCount)
		// 			barrierBroken = true;
		// 		leaveCount = Integer.max(leaveCount, round);
		// 	}
		// 	// UNCOMMENT BELOW LINE FOR DEBUGGING PRINT STATEMENTS
		// 	System.out.println("Thread " + Thread.currentThread().getId() + " is leaving round:" + round);
		// }
	}

	public static void main(String[] args) throws InterruptedException {
		
		// int numRounds = 20;
		// CyclicBarrier barrier;
		// barrier = new SemaphoreCyclicBarrier(numParties);
		// Thread[] t = new Thread[numParties];

		/*
		 * Testing #1: Use two threads to search for nonexistent values, add values, check busy_wait for getFirst(), and search for existing values
		 */
		int maxSize = 3;
		final PriorityQueue queue;
		queue = new PriorityQueue(maxSize);

		Thread testThread1 = new Thread(
			new Runnable(){
			public void run(){
				int tests_passed = 0;
				if(queue.search("hi")  == -1){tests_passed++;}
				queue.add("one",1);
				if(queue.search("two")  == -1){tests_passed++;}

				queue.add("two",2);
				if(queue.search("two")  == 1){tests_passed++;}
				if(queue.search("one")  == 2){tests_passed++;}
				System.out.println(tests_passed+"/4 tests passed for trivial search (T1)");

				queue.add("four",4);
				if(queue.search("four")  == 1){tests_passed++;}
				if(queue.search("two")  == 2){tests_passed++;}
				if(queue.search("one")  == 3){tests_passed++;}
				System.out.println(tests_passed+"/7 tests passed for search (T1)");
				
				if(queue.getFirst().equals("four")){tests_passed++;}
				if(queue.getFirst().equals("two")){tests_passed++;}
				if(queue.getFirst().equals("one")){tests_passed++;}
				if(queue.getFirst().equals("post")){tests_passed++;}
				System.out.println(tests_passed+"/11 tests passed in Test 1 with two threads (T1)");
			}
		});

		Thread testThread2 = new Thread(new Runnable(){
			public void run(){
				System.out.println("Thread2 Sleeping...");
				try {
					TimeUnit.SECONDS.sleep(2);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("Thread2 adding post");
				queue.add("post",8);			   
			}
		});
		testThread1.start();
		testThread2.start();
		testThread1.join();
		testThread2.join();

		System.out.println("\n\n");
		
		/*
		 * Testing #2: Use two threads to check busy_wait for add(), search for existing values
		 */
		
		Thread testThread20 = new Thread(new Runnable(){
			public void run(){
				int tests_passed = 0;
				queue.add("three",3);
				queue.add("eight",8);
				queue.add("eight0",8);
				if(queue.search("three") == 3){tests_passed++;}
				if(queue.search("eight")==1){tests_passed++;}
				if(queue.search("eight0")==2){tests_passed++;}
				if(queue.getFirst().equals("eight")){tests_passed++;}
				if(queue.getFirst().equals("eight0")){tests_passed++;}
				if(queue.getFirst().equals("three")){tests_passed++;}
				System.out.println(tests_passed+"/6 tests passed for trivial ordering and retrieval");

				queue.add("three",3);
				queue.add("eight",8);
				queue.add("seven",7);
				System.out.println("T2 Trying to add fourth element");
				queue.add("one",1);	// we should see a busy wait pause here

				if(queue.getFirst().equals("three")){tests_passed++;}
				if(queue.getFirst().equals("one")){tests_passed++;}
				if(queue.getFirst().equals("one2")){tests_passed++;}
				
				
				System.out.println(tests_passed+"/9 tests passed in Test 2 with two threads");
				
			}
		});

		Thread testThread21 = new Thread(new Runnable(){
			public void run(){
				System.out.println("Thread21 Sleeping...");
				int tests_passed = 0;
				try {
					TimeUnit.SECONDS.sleep(3);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("Thread21 retrieving two values");
				if(queue.search("three") == 3){tests_passed++;}
				if(queue.search("eight")==1){tests_passed++;}
				if(queue.search("seven")==2){tests_passed++;}
				if(queue.getFirst().equals("eight")){tests_passed++;}
				if(queue.getFirst().equals("seven")){tests_passed++;}
				System.out.println(tests_passed+"/5 tests passed for concurrent retrieval, search");
				queue.add("one2",1);		
			}
		});

		System.out.println("Test 2 Start:");
		testThread20.start();
		testThread21.start();
		
		testThread20.join();
		testThread20.join();
		System.out.println("Test 2 Complete\n\n\n");

		int bigMaxSize = 15;
		final PriorityQueue bigqueue;
		bigqueue = new PriorityQueue(bigMaxSize);
		// /*
		//  * Testing #3: 
		//		Three threads add super quickly. Then, use a fourth thread to check that all three added in the correct order
		// 		This test validates that the name and priority hierarchy is held
		// 		Test validates that adding a node with a name that already exists will not work!
		//  */
		
		 Thread testThread30 = new Thread(new Runnable(){
			public void run(){
				int tests_passed = 0;
				bigqueue.add("three",3);
				bigqueue.add("one",1);
				bigqueue.add("eight",8);
				bigqueue.add("eight0",8);
			}
		});

		Thread testThread31 = new Thread(new Runnable(){
			public void run(){
				int tests_passed = 0;
				bigqueue.add("two",2);
				if(bigqueue.search("two")!=-1){System.out.println("1/1 test 3 check passed for search");
				}else{System.out.println("failed test 3 search check");}
				bigqueue.add("four",4);
				bigqueue.add("five",5);
				try {
					TimeUnit.SECONDS.sleep(2);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				bigqueue.add("one0",1);
				bigqueue.add("three0",3);

			}
		});

		
		Thread testThread32 = new Thread(new Runnable(){
			public void run(){
				int tests_passed = 0;
				bigqueue.add("seven",7);
				bigqueue.add("six",6);
				bigqueue.add("nine",9);
				bigqueue.add("ten",10);
				try {
					TimeUnit.SECONDS.sleep(6);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				bigqueue.add("one1",1);
				bigqueue.add("seven0",7);
			}
		});
		Thread testThread33 = new Thread(new Runnable(){
			int tests_passed = 0;
			public void run(){
				System.out.println(bigqueue.getSize()+" == 15");
				if(bigqueue.add("two",10)==-1){tests_passed++;}
				if(bigqueue.getFirst().equals("ten")){tests_passed++;}
				if(bigqueue.getFirst().equals("nine")){tests_passed++;}
				if(bigqueue.getFirst().equals("eight")){tests_passed++;}	
				if(bigqueue.getFirst().equals("eight0")){tests_passed++;}	
				if(bigqueue.getFirst().equals("seven")){tests_passed++;}
				if(bigqueue.getFirst().equals("seven0")){tests_passed++;}
				if(bigqueue.getFirst().equals("six")){tests_passed++;}
				if(bigqueue.getFirst().equals("five")){tests_passed++;}
				if(bigqueue.getFirst().equals("four")){tests_passed++;}	
				if(bigqueue.getFirst().equals("three")){tests_passed++;}	
				if(bigqueue.getFirst().equals("three0")){tests_passed++;}	
				if(bigqueue.getFirst().equals("two")){tests_passed++;}	
				if(bigqueue.getFirst().equals("one")){tests_passed++;}	
				if(bigqueue.getFirst().equals("one0")){tests_passed++;}	
				if(bigqueue.getFirst().equals("one1")){tests_passed++;}
				
				System.out.println(tests_passed+"/16 tests passed for test 3 check");

				// bigqueue.add("three",3);
				// bigqueue.add("eight",8);
				
				// System.out.println(tests_passed+"/9 tests passed in Test 2 with two threads");
			}
		});

		System.out.println("Start Test 3:");
		testThread30.start();
		testThread31.start();
		testThread32.start();
		testThread30.join();
		testThread31.join();
		testThread32.join();
		testThread33.start();
		
		/*
		 * 1. ACTIVE BARRIER
		 * Many threads run in parallel
		 * Each thread calls barrier.await() 'numRounds' number of times
		 * If the running threads block - there's an issue with the behavior of the barrier
		 */
		// reset();
		// for (int i = 0; i < numParties; ++i) {
		// 	t[i] = new Thread(new PriorityQueueTest(numRounds));
		// }
		// for (int i = 0; i < numParties; ++i) {
		// 	t[i].start();
		// }
		// for (int i = 0; i < numParties; ++i) {
		// 	t[i].join();
		// }
		// if(barrierBroken) {
		// 	System.out.println("Test 1 Failed - Barrier was broken during execution !!!!!!!!!!!!!!!!!!!!1");
		// }
		// else {
		// 	System.out.println("Test 1 Succeeded!");
		// }



	// 	/*
	// 	 * 2. DEACTIVATED BARRIER
	// 	 * The barrier is deactivated
	// 	 * Many threads run in parallel 
	// 	 * Each thread iterates and calls barrier.await() a distinct number of times. 
	// 	 * With an active barrier - the running threads will wait forever since not enough threads can reach the barrier in later rounds
	// 	 * With a deactivated barrier - this should work fine
	// 	 */
	// 	barrier.deactivate();
	// 	for (int i = 0; i < numParties; ++i) {
	// 		t[i] = new Thread(new CyclicBarrierSemaphoreTest(barrier, i));
	// 	}
	// 	for (int i = 0; i < numParties; ++i) {
	// 		t[i].start();
	// 	}
	// 	for (int i = 0; i < numParties; ++i) {
	// 		t[i].join();
	// 	}
	// 	System.out.println("Test 2 Succeeded!");



	// 	/* 
	// 	 * 3. MIDWAY BARRIER DEACTIVATION
	// 	 * The barrier is reactivated.
	// 	 * Many threads run in parallel 
	// 	 * Each thread iterates and calls barrier.await() a distinct number of times - with an active barrier the threads would block forever. 
	// 	 * Before waiting for the threads to complete - the barrier is deactivated
	// 	 * Deactivation should 
	// 	 * 		a. nullify the barrier, 
	// 	 * 		b. release any waiting threads
	// 	 * If the barrier is deactivated as expected - the threads should be able to execute to completion
	// 	 */
	// 	// System.out.println("Test 3.0 Started!");
	// 	barrier.activate();
	// 	// System.out.println("Test 3.0 Succeeded!");
	// 	for (int i = 0; i < numParties; ++i) {
	// 		t[i] = new Thread(new CyclicBarrierSemaphoreTest(barrier, i));
	// 	}
	// 	// System.out.println("Test 3.1 Succeeded!");
	// 	for (int i = 0; i < numParties; ++i) {
	// 		t[i].start();
	// 	}
	// 	barrier.deactivate();
	// 	for (int i = 0; i < numParties; ++i) {
	// 		t[i].join();
	// 	}
	// 	System.out.println("Test 3 Succeeded!");




	// 	/*
	// 	 * 4. ACTIVE BARRIER 2.0
	// 	 * This is identical to the first test case above
	// 	 * This serves as a check to ensure that deactivating the barrier in the middle of its execution in case 3 did not break it in any way
	// 	 */
	// 	reset();
	// 	barrier.activate();
	// 	for (int i = 0; i < numParties; ++i) {
	// 		t[i] = new Thread(new CyclicBarrierSemaphoreTest(barrier, numRounds));
	// 	}
	// 	for (int i = 0; i < numParties; ++i) {
	// 		t[i].start();
	// 	}
	// 	for (int i = 0; i < numParties; ++i) {
	// 		t[i].join();
	// 	}
	// 	if(barrierBroken) {
	// 		System.out.println("Test 4 Failed - Barrier was broken during execution");
	// 	}
	// 	else {
	// 		System.out.println("Test 4 Succeeded!");
	// 	}
	}
}