package victor.training.performance.leaks;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("leak5")
public class Leak5_LongStackFrame {
	@GetMapping
	public String test() {
		BigObject80MB big = new BigObject80MB();
		while (true) ; // or wait for a loong network call, or sleep 60 sec, or deadlock
		// Conclusion?...
	}
}