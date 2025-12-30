/*
Your task is simple. You have to call counter.count(int) with the numbers 1 to 100 inclusive. So a simple solution might look like this:

for (int i = 1; i <= 100; i++) {
  counter.count(i);
}
But there's a catch. Your solution also has to satisfy the following conditions:

Three different threads must be used
Numbers of the form 3n (multiples of 3) must be called in one thread
Numbers of the form 3n + 1 must be called in a second
Numbers of the form 3n + 2 must be called in a third
The numbers have to be called in sequence 1 to 100
Also, make sure your method doesn't return until all three threads have completed. Otherwise the tests may not work even if your solution is correct.

https://www.codewars.com/kata/549e70e994e517ed8b00043e
*/

import java.util.concurrent.*;

public class ThreadedCounting {
  
  private static final BlockingQueue<Boolean> done = new LinkedBlockingQueue<>();

  
  public static void countInThreads(Counter counter) {
    
    ExecutorService threads[] = new ExecutorService[3];
    threads[0] = Executors.newSingleThreadExecutor();
    threads[1] = Executors.newSingleThreadExecutor();
    threads[2] = Executors.newSingleThreadExecutor();
    
    threads[0].submit(() -> threadedCounter(counter, 1, threads));
    
    try {
      done.take();
    } catch ( InterruptedException e ) { throw new RuntimeException( e ); }
      
    threads[0].shutdown();
    threads[1].shutdown();
    threads[2].shutdown();
    

  }
  
  static void threadedCounter(Counter counter, int c, ExecutorService[] es) {
    counter.count(c);
    if (c > 99) {
      done.offer(true);
      return;
    }
    if (c < 100) {
      es[c%3].submit(() -> threadedCounter(counter, c+1, es));
    }
  }
}
