package victor.training.performance.concurrency;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.*;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;


@Slf4j
public class RaceBugsIntro {

   private static Integer total = 0;

   // 2 parallel threads run this:
   private static void countEven(List<Integer> numbers) {
      for (Integer n : numbers) { // .size() = 10k
         if (n % 2 == 0) {
            total++;
         }
      }
   }

   public static void main(String[] args) throws ExecutionException, InterruptedException {
      List<Integer> ids = IntStream.range(0, 20_000).boxed().collect(toList());

      // split the work in two
      List<Integer> firstHalf = ids.subList(0, ids.size() / 2);
      List<Integer> secondHalf = ids.subList(ids.size() / 2, ids.size());

      // submit the 2 tasks
      ExecutorService pool = Executors.newCachedThreadPool();
      Future<?> future1 = pool.submit(() -> countEven(firstHalf));
      Future<?> future2 = pool.submit(() -> countEven(secondHalf));
      log.debug("Tasks launched...");

      // wait for the tasks to complete
      future1.get();
      future2.get();

      log.debug("Counted: " + total);
   }


}