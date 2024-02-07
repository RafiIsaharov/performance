package victor.training.performance.parallelStream;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static victor.training.performance.util.PerformanceUtil.sleepMillis;

@Slf4j
public class ParallelStreams {
   public static void main(String[] args) {
      OnAServer.otherParallelRequestsAreRunning(); // starve the shared commonPool

      long t0 = System.currentTimeMillis();

      List<Integer> list = IntStream.range(1,100).boxed().collect(toList());

      List<Integer> result = list.parallelStream()
          .filter(i -> i % 2 == 0)
          .map(i -> {
             log.debug("working on " + i);
             sleepMillis(100); // time-consuming work (DB, REST)
             return i * 2;
          })
          .collect(toList());
      log.debug("Got result: " + result);

      long t1 = System.currentTimeMillis();
      log.debug("Took {} ms", t1 - t0);
      // what kind of work should you do in a parallelStream, knowing that
     // hyou will execute on exactly N-1+you =N CPUs?
     // Answer: CPU-bound work, not IO-bound work
   }
}

