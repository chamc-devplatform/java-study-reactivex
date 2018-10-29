package com.cp.reactivex2.impl;

public class ReactiveXTest {
	public static void main(String[] args) {
		Observable.just("hello1", "hello2", "hello3")
			.map(new Function<String, Integer>() {

				@Override
				public Integer apply(String t) {
					return 1;
				}
			})
			.concatMap(new Function<Integer, Observable<String>>() {

				@Override
				public Observable<String> apply(Integer t) {
					return Observable.just("world" + t);
				}
			})
			.subscribe(new Observer<String>() {
				
				@Override
				public void onNext(String t) {
					System.out.println(t);
				}
				
				@Override
				public void onError(Throwable e) {
					
				}
				
				@Override
				public void onComplete() {
					
				}
			});
		
	}
}
