package com.cp.reactivex2.impl;

public interface Observer<T> {

	void onNext(T t);

	void onError(Throwable e);
	
	void onComplete();
}
