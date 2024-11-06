package victor.training.performance;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import victor.training.performance.drinks.Beer;
import victor.training.performance.drinks.DillyDilly;
import victor.training.performance.drinks.Vodka;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static java.lang.System.currentTimeMillis;

@Slf4j
@RestController
public class Barman {
  //1. start wiremock server on 9999
  //2. start this app ConcurrencyApp
  @Autowired
  private RestTemplate rest;

  @GetMapping("/drink")
  public DillyDilly drink() throws ExecutionException, InterruptedException {
    long t0 = currentTimeMillis();
    //how to optimize this?
    //executor.submit(() -> rest.getForObject("http://localhost:9999/beer", Beer.class));
//    Beer beer = rest.getForObject("http://localhost:9999/beer", Beer.class); //1s
//    Vodka vodka = rest.getForObject("http://localhost:9999/vodka", Vodka.class);//1s
//    DillyDilly dilly = new DillyDilly(beer, vodka);

//    Future<Beer> beerFuture = CompletableFuture.supplyAsync(() -> rest.getForObject("http://localhost:9999/beer", Beer.class));
//    Future<Vodka> vodkaFuture = CompletableFuture.supplyAsync(() -> rest.getForObject("http://localhost:9999/vodka", Vodka.class));
//    DillyDilly dilly = new DillyDilly(beerFuture.get(), vodkaFuture.get());
    DillyDilly dilly = CompletableFuture.supplyAsync(() -> rest.getForObject("http://localhost:9999/beer", Beer.class))
            .thenCombineAsync(CompletableFuture.supplyAsync(() -> rest.getForObject("http://localhost:9999/vodka", Vodka.class)),
                    DillyDilly::new)
            .join();

    log.info("HTTP thread blocked for {} durationMillis", currentTimeMillis() - t0);
    return dilly;
  }
}
