package com.project.learnreactiveprogramming;

import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SignalType;
import reactor.util.function.Tuple3;

import java.time.Duration;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ReactiveTutorial {

    //MONO -> single data
    private Mono<String> testMono() {
        return Mono.just("Android").log();
//      return Mono.justOrEmpty(null); //to pass null value use justOrEmpty()
//      return Mono.empty();    // to pass empty value use empty()
    }


    //FLUX -> a stream of data
    private Flux<String> testFlux() {
//        return Flux.just("Android", "Java", "Kotlin", "C++");
//                .log();

        List<String> languages = List.of("Java", "Android", "Kotlin", "C++");
        return Flux.fromIterable(languages);
    }


    //map() transforms each element into another object synchronously
    //Use map() if your function returns a simple object.
    private Flux<String> testMap() {
        Flux<String> flux = Flux.just("Java", "Kotlin", "C++", "Android");
        return flux.map(String::toUpperCase);
    }


    //flatMap() transforms each element into another Publisher (Mono/Flux) and then flattens/merges those publishers into a single stream.
    //When each element needs an async call (e.g., DB, API call).
    private Flux<String> testFlatMap() {
        Flux<String> flux = Flux.just("Java", "Kotlin", "C++", "Android");
        return flux.flatMap(s -> Mono.just(s.toLowerCase(Locale.ROOT)));
    }


    private Flux<String> testSkip() {
        Flux<String> flux = Flux.just("Android", " Kotlin", "C++", "Java", "Flutter", "React").delayElements(Duration.ofSeconds(1));
//        return flux.skip(2).map(String::toUpperCase);
        return flux.skip(Duration.ofMillis(2050));
    }


    private Flux<Integer> testConcatFlux() {
        Flux<Integer> flux1 = Flux.range(1, 10);
        Flux<Integer> flux2 = Flux.range(101, 10);
        Flux<Integer> flux3 = Flux.range(1001, 10);
        return Flux.concat(flux1, flux2, flux3);
    }


    private Flux<Integer> testMergeFlux() {
        Flux<Integer> flux1 = Flux.range(1, 10).delayElements(Duration.ofSeconds(1));
        Flux<Integer> flux2 = Flux.range(101, 10).delayElements(Duration.ofSeconds(1));
        return Flux.merge(flux1, flux2);
    }


    private Flux<Tuple3<Integer, Integer, Integer>> testZipFlux() {
        Flux<Integer> flux1 = Flux.range(1, 10);
        Flux<Integer> flux2 = Flux.range(101, 20);
        Flux<Integer> flux3 = Flux.range(1001, 20);
//        return Flux.zip(flux1, flux2);  //tuple will be created for min number of elements,[10 elements->flux1]
        return Flux.zip(flux1, flux2, flux3);
    }


    private Mono<List<Integer>> testCollectFlux() {
        Flux<Integer> flux1 = Flux.range(1, 10);
        return flux1.collectList();
    }


    private Flux<List<Integer>> testBufferFlux() {
//        Flux<Integer> flux1 = Flux.range(1, 10);
//        return flux1.buffer();
//        return flux1.buffer(3);   //size of elements emitted in list
        Flux<Integer> flux1 = Flux.range(1, 10).delayElements(Duration.ofMillis(1000));
        return flux1.buffer(Duration.ofMillis(3_100));
    }


    private Mono<Map<Integer, Integer>> testMapCollection() {
        //map (key, value) = (n, n*n)
        //(2, 4), (3, 9), (4, 16), (5, 25)
        Flux<Integer> flux1 = Flux.range(1, 10);
        return flux1.collectMap(integer -> integer, i -> i * i);
    }


    private Flux<Integer> testDoFunctions() {
        Flux<Integer> flux1 = Flux.range(1, 10);
//        return flux1.doOnEach(integerSignal -> System.out.println(integerSignal));
        return flux1.doOnEach(signal -> {
            if (signal.getType() == SignalType.ON_COMPLETE) {
                System.out.println("I am done!");
            } else {
                System.out.println(signal.get());
            }
        });
    }


    private Flux<Integer> testDoOnComplete() {
        Flux<Integer> flux1 = Flux.range(1, 10);
        return flux1.doOnComplete(() -> System.out.println("I am complete!"));
    }


    private Flux<Integer> testDoOnSubscribe() {
        Flux<Integer> flux1 = Flux.range(1, 10);
        return flux1.doOnSubscribe(subscribe -> System.out.println("I am subscribed!"));
    }


    private Flux<Integer> testDoOnCancel() {
        Flux<Integer> flux1 = Flux.range(1, 10).delayElements(Duration.ofSeconds(1));
        return flux1.doOnCancel(() -> System.out.println("I am Cancelled!"));
    }


    private Flux<Integer> testDoOnError() {
        Flux<Integer> flux = Flux.range(1, 10).delayElements(Duration.ofMillis(500))
                .map(integer -> {
                    if (integer == 5) {
                        throw new RuntimeException("Unexpected Number");
                    }
                    return integer;
                });
//        return flux;  //this will print the exception statement and abort the program

        //to continue the program even after the exception occurred
        /*return flux.onErrorContinue((error, data) -> {
            System.out.println("Don't worry about " + data + " and continue");
        }); */

        //to return a value on error
//        return flux.onErrorReturn(-1);

        //used to resume operation with given code after the exception occurs
        return flux.onErrorResume(throwable -> Flux.just(-1));
    }


    private Flux<Integer> testDoOnErrorMap() {
        Flux<Integer> flux = Flux.range(1, 10)
                .delayElements(Duration.ofMillis(500))
                .map(integer -> {
                    if (integer == 5) {
                        throw new RuntimeException("Unexpected Number!!");
                    }
                    return integer;
                });
        return flux.onErrorMap(throwable -> new UnsupportedOperationException(throwable.getMessage()));
    }


    public static void main(String[] args) throws InterruptedException {
        ReactiveTutorial reactiveTutorial = new ReactiveTutorial();
//        reactiveTutorial.testMono().subscribe(System.out::println);
//        reactiveTutorial.testFlux().subscribe(System.out::println);
//        reactiveTutorial.testMap().subscribe(System.out::println);
//        reactiveTutorial.testFlatMap().subscribe(System.out::println);
//        reactiveTutorial.testSkip().subscribe(System.out::println);
//        reactiveTutorial.testConcatFlux().subscribe(System.out::println);
//        reactiveTutorial.testMergeFlux().subscribe(System.out::println);
//        reactiveTutorial.testZipFlux().subscribe(System.out::println);
//        reactiveTutorial.testCollectFlux().subscribe(System.out::println);
//        reactiveTutorial.testBufferFlux().subscribe(System.out::println);
//        reactiveTutorial.testMapCollection().subscribe(System.out::println);
//        reactiveTutorial.testDoFunctions().subscribe() ;
//        reactiveTutorial.testDoFunctions2().subscribe(System.out::println);

//        reactiveTutorial.testDoOnSubscribe().subscribe(System.out::println); // subscription 1 for testDoFunctions3
//        reactiveTutorial.testDoOnSubscribe().subscribe(System.out::println); // subscription 2 for testDoFunctions3

//        Disposable disposable = reactiveTutorial.testDoOnCancel().subscribe(System.out::println) ;
//        Thread.sleep(3_500);
//        disposable.dispose();

//        reactiveTutorial.testDoOnError().subscribe(System.out::println);
        reactiveTutorial.testDoOnErrorMap().subscribe(System.out::println);

        Thread.sleep(3_000);
    }

}
