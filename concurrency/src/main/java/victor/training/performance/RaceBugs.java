package victor.training.performance;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.stream.Collectors.toList;


@SuppressWarnings("ALL")
@Slf4j
public class RaceBugs {
  private static List<Integer> evenNumbers = new ArrayList<>();

  private static Integer total = 0;

  // many parallel threads run this method:
  private static void countEven(List<Integer> numbers) {
    log.info("Start");
    for (Integer n : numbers) {
      if (n % 2 == 0) {
        synchronized (total) {
//          total++;
         // total = total +1;
          total = new Integer(total+1 );// imutable object that recreating itseld and the reference is
        }
//        System.out.println("in"); // or a log.debug they latency (spend time) outside of the risky line
        // race conditions = heisenbugs (Heisenberg's Uncertainty Principle)
      }
    }
    log.info("End");
  }

  public static void main(String[] args) throws Exception {
    List<Integer> fullList = IntStream.range(0, 1_0000).boxed().toList();
  // [[500elements],[500elements]]
    List<List<Integer>> parts = splitList(fullList, 2);

    ExecutorService pool = Executors.newCachedThreadPool();
    for (List<Integer> part : parts) {
      pool.submit(()-> countEven(part));
    }
    pool.shutdown();
    pool.awaitTermination(1, MINUTES);

    log.debug("Counted: " + total);
    log.debug("List.size: " + evenNumbers.size());
  }

  //<editor-fold desc="utility functions">
  private static List<List<Integer>> splitList(List<Integer> all, int numberOfParts) {
    List<Integer> shuffled = new ArrayList<>(all);
    Collections.shuffle(shuffled);
    List<List<Integer>> lists = new ArrayList<>();
    for (int i = 0; i < numberOfParts; i++) {
      lists.add(new ArrayList<>());
    }
    for (int i = 0; i < shuffled.size(); i++) {
      lists.get(i % numberOfParts).add(shuffled.get(i));
    }
    return lists;
  }
  //</editor-fold>
}