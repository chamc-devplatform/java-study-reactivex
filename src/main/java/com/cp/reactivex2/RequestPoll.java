package com.cp.reactivex2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.chamc.pmcsdk.api.MessageSenders;
import com.chamc.pmcsdk.common.PmcInitializer;
import com.chamc.pmcsdk.http.exception.HttpClientException;
import com.chamc.pmcsdk.param.AppParam;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiPredicate;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

public class RequestPoll {
	
//	static {
//		PmcInitializer.init();
//		try {
//			Thread.sleep(2000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//	}

	public static void main(String[] args) throws InterruptedException {
//		request0();
//		request();
//		request2();
//		request3();
//		request4();
//		request5();
//		request6();
//		request9();
		request11();
		Thread.sleep(5000); // 阻止主线程立即结束
	}
	
	/**
	 *  请求重试retry(times, Exception)
	 */
	public static void request11() {
		final String[] strs = {"hello1, hello2, hello3"};
		final AtomicInteger index = new AtomicInteger(0);
		Observable.create(new ObservableOnSubscribe<String>() {

			@Override
			public void subscribe(ObservableEmitter<String> emitter) throws Exception {
				try {
					int i = index.getAndIncrement();
					emitter.onNext(strs[i]);
				} catch (Exception e) {
					emitter.onError(e);
				}
			}
		})
		.concatMap(new Function<String, ObservableSource<? extends String>>() {

			@Override
			public ObservableSource<? extends String> apply(String t) throws Exception {
				return Observable.just(t).concatMap(new Function<String, ObservableSource<? extends String>>() {

					@Override
					public ObservableSource<? extends String> apply(String t) throws Exception {
						
						return Observable.just(t);
					}
				});
			}
		})
		.retry(new BiPredicate<Integer, Throwable>() {
			@Override
			public boolean test(Integer t1, Throwable t2) throws Exception {
				return t1 <= 1;
			}
		})
		.subscribe(new Observer<String>() {

			@Override
			public void onSubscribe(Disposable d) {
				
			}

			@Override
			public void onNext(String t) {
				System.out.println(t);
			}

			@Override
			public void onError(Throwable e) {
				e.printStackTrace();
			}

			@Override
			public void onComplete() {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	
	/**
	 *  interval()/intervalRange()
	 *  间隔发送事件
	 */
	public static void request0() {
		Observable
//			.interval(2, TimeUnit.SECONDS)
//			.subscribeOn(Schedulers.io())
			.intervalRange(1, 10, 0, 2, TimeUnit.SECONDS) // 初始值/发送次数/延迟时间/发送间隔/时间单位
			.subscribeOn(Schedulers.io()) // 开启一个io线程
			.subscribe(s -> System.out.println(s))
			.dispose();
		
	}

	/**
	 *  BehaviorSubject可以给订阅者发送订阅前最近的事件和订阅后发送的事件
	 */
	public static void request() {
		BehaviorSubject<Integer> subject = BehaviorSubject.create();
		subject.onNext(1);
		subject.onNext(2);
		subject.onNext(3);
		subject.subscribe(s -> System.out.println(s));
		subject.onNext(4);
		subject.onNext(5);
	}
	
	/**
	 *  takeUntil()
	 *  AObservable.takeUntil(BObsevable),当B事件源中发送数据时，A就停止发送数据（当前事件仍会被发送）
	 *  
	 *  takeWhile()用法与其类似
	 *  区别：当前时间不会被发送
	 */
	public static void request2() {
		Observable.just(1, 2, 3, 4)
			.takeUntil(new Predicate<Integer>() {
				@Override
				public boolean test(Integer t) throws Exception {
					return (t == 3); // takeUntil()使用这个
//					return (t != 3); // takeWhile()使用这个
				}
			})
			.subscribe(new Consumer<Integer>() {
				@Override
				public void accept(Integer t) throws Exception {
					System.out.println(t);
				}
			});
		
		// lambda版本
		Observable.just(1, 2, 3, 4)
			.takeUntil(t -> {
				return t == 3; // takeUntil()方法
			})
			.subscribe(t -> System.out.println(t));
	}
	
	/**
	 *  filter()过滤
	 */
	public static void request3() {
		Observable.just(1, 2, 3, 4)
        .filter(t -> {
                return t % 2 == 0; 
        })
        .subscribe(t -> System.out.println(t));
	}
	
	/**
	 *  compose()
	 */
	public static void request4() {
		Observable.interval(2, TimeUnit.SECONDS)
        .compose(bindUntil(10L)) 
        .takeUntil(Observable.timer(5, TimeUnit.SECONDS))
        .subscribe(t -> System.out.println(t));
	}
	
	public static ObservableTransformer<Long, Long> bindUntil(final Long deplayTime) {
		return new ObservableTransformer<Long, Long>() {
			@Override
			public ObservableSource<Long> apply(Observable<Long> upstream) {
				return upstream.subscribeOn(Schedulers.io()).takeUntil(Observable.timer(deplayTime, TimeUnit.SECONDS));
			}
		};
	}
	
	/**
	 * 
	 */
	public static void request5() {
		Observable.just(1, 2, 3)
			.map(t -> {
				System.out.println("map:" + Thread.currentThread().getName());
				return t;
			})
			.filter(t -> {
				System.out.println("filter:" + Thread.currentThread().getName());
				return true;
			})
			.doOnNext(t -> {
				System.out.println("doOnNext:" + Thread.currentThread().getName());
			})
			.subscribe(t -> System.out.println(t));
	}
	
	/**
	 *  轮询请求
	 */
	public static void request6() {
		Observable.interval(2, TimeUnit.SECONDS)
			.take(10)
//			.doOnNext(new Consumer<Long>() {
//				@Override
//				public void accept(Long t) throws Exception {
//					System.out.println("我在进行第" + t + "次请求");
//					try {
//						send();
//					} catch (HttpClientException e) {
//						return;
//					}
//				}
//			})
			.takeUntil(t -> {
				try {
					System.out.println("我在进行第" + t + "次请求");
					send();
					return true;
				} catch (HttpClientException e) {
					return false;
				}
			})
			.subscribeOn(Schedulers.io())
			.subscribe(new Observer<Long>() {
				@Override
				public void onSubscribe(Disposable d) {
					System.out.println("输出onSubscribe:" + d);
				}

				@Override
				public void onNext(Long t) {
					System.out.println("输出onNext:" + t);
				}

				@Override
				public void onError(Throwable e) {
					System.out.println("输出onError:" + e);
				}

				@Override
				public void onComplete() {
					System.out.println("输出onComplete:结束");
				}
			});
	}
	
	public static void send() {
		String[] acs = {"123"};
		AppParam param = AppParam.builder()
							.toAccounts(acs)
							.article("标题", "正文00")
							.bussinessType("aaa")
							.build();
		MessageSenders.send(param);
	}
	
	public static void request9() {
		Observable.create(new ObservableOnSubscribe<Integer>() {
			@Override
			public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
				for (int i = 0; i < 4; i++) {
					if (i == 2) {
						emitter.onComplete();
					}
					emitter.onNext(i);
				}
			}
		}).subscribe(t -> System.out.println(t));
	}
}























