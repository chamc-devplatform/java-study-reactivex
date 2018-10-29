package com.cp.reactivex2.impl;

@FunctionalInterface
public interface Function<T, R> {

	R apply(T t);
}
