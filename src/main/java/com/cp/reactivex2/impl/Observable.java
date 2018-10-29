package com.cp.reactivex2.impl;

public abstract class Observable<T> {

	@SuppressWarnings("unchecked")
	public static <T> Observable<T> just(T... items) {
		return new ObservableJust<T>(items);
	}
	
	public <R> Observable<R> map(Function<T, R> mapper) {
		return new ObservableMap<>(this, mapper);
	}
	
	public <R> Observable<R> concatMap(Function<T, Observable<R>> mapper) {
		return new ObservableConcatMap<>(this, mapper);
	}
	
	abstract void subscribe(Observer<T> observer);
}
