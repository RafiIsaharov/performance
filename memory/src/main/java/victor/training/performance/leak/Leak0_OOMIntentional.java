package victor.training.performance.leak;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import victor.training.performance.leak.obj.BigObject20MB;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("leak0")
public class Leak0_OOMIntentional {

  @GetMapping
  public String endpoint() {
    List<int[]> boom = new ArrayList<>();
    while (true) boom.add(new int[1000_000]);
    // throw new OutOfMemoryError("Does not generate heapdump despite the flags");
  }
}

/**
 * After an OOME a .hprof file is generated by JVM by startup flags:
 * -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/Users/victorrentea/workspace
 */
