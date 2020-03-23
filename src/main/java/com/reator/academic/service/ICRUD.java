package com.reator.academic.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ICRUD<T, V> {

	Mono<T> register(T t);
	Mono<T> modify(T t);
	Flux<T> list();
	Mono<T> listById(V v);
	Mono<Void> delete(V v);
}
