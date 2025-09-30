package com.project.learnreactiveprogramming;

import reactor.core.publisher.BufferOverflowStrategy;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

public class BackPressureTutorial {

    public static void main(String[] args) {
        BackPressureTutorial backPressureTutorial = new BackPressureTutorial();
        backPressureTutorial
//                .createNoOverflowFlux()
//                .createOverflowFlux()
//                .createDropBackPressureFlux()
                .createBufferBackPressureFlux()
                .blockLast();       // it will block until the flux is complete
    }

    private Flux<Long> createNoOverflowFlux() {
        return Flux.range(1, Integer.MAX_VALUE)
                .log()
                .concatMap(x -> Mono.delay(Duration.ofMillis(100)));
    }

    private Flux<Long> createOverflowFlux() {
        return Flux.interval(Duration.ofMillis(1))
                .log()
                .concatMap(x -> Mono.delay(Duration.ofMillis(100)));
    }

    private Flux<Long> createDropBackPressureFlux() {
        return Flux.interval(Duration.ofMillis(1))
                .onBackpressureDrop()
                .concatMap(x -> Mono.delay(Duration.ofMillis(100)).thenReturn(x))
                .doOnNext(x -> System.out.println("Element kept by consumer:" + x));
    }

    private Flux<Long> createBufferBackPressureFlux(){
        return Flux.interval(Duration.ofMillis(1))
                .onBackpressureBuffer(50, BufferOverflowStrategy.DROP_LATEST)
                .concatMap(x -> Mono.delay(Duration.ofMillis(100)).thenReturn(x))
                .doOnNext(x -> System.out.println("Element kept by consumer:" + x));
    }

}
