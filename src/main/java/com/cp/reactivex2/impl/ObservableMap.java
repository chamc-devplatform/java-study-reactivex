package com.cp.reactivex2.impl;

public class ObservableMap<T, R> extends Observable<R> {

	private Function<T, R> mapper;
	private Observable<T> source;
	
	ObservableMap(Observable<T> source, Function<T, R> mapper) {
		this.source = source;
		this.mapper = mapper;
	}
	
	@Override
	void subscribe(Observer<R> observer) {
		source.subscribe(new SourceObserver<>(observer, mapper));
	}
	
	static class SourceObserver<T, R> implements Observer<T> {

		private Function<T, R> mapper;
		private Observer<R> observer;
		
		SourceObserver(Observer<R> observer, Function<T, R> mapper) {
			this.observer = observer;
			this.mapper = mapper;
		}
		
		@Override
		public void onNext(T t) {
			R r = mapper.apply(t);
			observer.onNext(r);
		}

		@Override
		public void onError(Throwable e) {
			
		}

		@Override
		public void onComplete() {
			
		}
		
	}

}
