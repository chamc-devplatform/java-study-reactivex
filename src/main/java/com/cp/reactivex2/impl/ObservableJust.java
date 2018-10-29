package com.cp.reactivex2.impl;

public class ObservableJust<T> extends Observable<T> {

	private T[] items;
	
	@SuppressWarnings("unchecked")
	ObservableJust(T... items) {
		this.items = items;
	}

	@Override
	void subscribe(Observer<T> observer) {
		try {
			for (T t : items) {
				observer.onNext(t);
			}
			observer.onComplete();
		} catch (Exception e) {
			observer.onError(e);
		}
	}
}
