package victor.training.concurrency.pools;

import victor.training.concurrency.ConcurrencyUtil;
import victor.training.concurrency.pools.tasks.FragileEndpointTask;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Throttling {
    private static final FragileEndpointTask fragile = new FragileEndpointTask();

    public static void main(String[] args) {
        ExecutorService httpPool = Executors.newFixedThreadPool(20);
        for (int i = 0; i < 100; i++) {
            ConcurrencyUtil.log("Requesting " + i);
            httpPool.submit(fragile);
        }
        httpPool.shutdown();
    }

}

