package victor.training.performance.parallelStream;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.IntStream;

import static victor.training.performance.util.PerformanceUtil.sleepMillis;

@Slf4j
public class ParallelStreams {
  public static void main(String[] args) throws ExecutionException, InterruptedException {
    // OnAServer.otherParallelRequestsAreRunning(); // starve the shared commonPool din JVM

    List<Integer> list = IntStream.range(1, 100).boxed().toList();

    long t0 = System.currentTimeMillis();

      // both parallelStream and completableFuture run by default on the JVM commonPool
//      I take advantage of that internal common pool inside the JVM
//      and I paralyse my flow to take 10 threads(10 CPUs) to work on.
//      So instead of having 5000 milliseconds, I expect to see
//      all be doing thread sleep for 500 milliseconds.
      //starvation is no fair here all treads are sleeping for the same amount of time
//      And if I look at the CPU load 0%
      // Resource starvation: = Unbalanced usage of resources "Unfairness"
      //Other Tasks waiting in a queue for a long time to get CPU time because of other Task waiting for Network blocking a
//      share pooled resource (DB connection, REST - HTTP Connection, CPU - Thread)
      var result = list.parallelStream()
              .filter(i -> i % 2 == 0) //50% of the elements are filtered out
        .map(i -> {
          log.debug("Map " + i);
          sleepMillis(100); // network call (DB, REST, SOAP..) or CPU work
          return i * 2;
        }).toList();

    long t1 = System.currentTimeMillis();
    log.debug("Took {} ms to get: {}", t1 - t0, result);
  }
}
