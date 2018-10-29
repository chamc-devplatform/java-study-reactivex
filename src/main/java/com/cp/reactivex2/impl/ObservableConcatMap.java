package com.cp.reactivex2.impl;

public class ObservableConcatMap<T, R> extends Observable<R> {

	private Observable<T> source;
	private Function<T, Observable<R>> mapper;
	
	ObservableConcatMap(Observable<T> source, Function<T, Observable<R>> mapper) {
		this.source = source;
		this.mapper = mapper;
	}
	
	@Override
	void subscribe(Observer<R> observer) {
		source.subscribe(new SourceObserver<>(observer, mapper));
	}
	
	static class SourceObserver<T, R> implements Observer<T> {
		
		private Observer<R> observer;
		private Function<T, Observable<R>> mapper;
		
		SourceObserver(Observer<R> observer, Function<T, Observable<R>> mapper) {
			this.observer = observer;
			this.mapper = mapper;
		}

		@Override
		public void onNext(T t) {
			Observable<R> r = mapper.apply(t);
			r.subscribe(observer);
		}

		@Override
		public void onError(Throwable e) {
			
		}

		@Override
		public void onComplete() {
			
		}
	}
}
