package com.cp.reactivex2.functional;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * 响应式
 */
public class ReactiveTest {

	public static void main(String[] args) throws InterruptedException {
		test1();
//		test2();
		
		Thread.sleep(2000);
	}
	
	/**
	 * 命令式
	 */
	public static void nomalSet() {
		int a = 1;
		int b = a + 1;
		System.out.println(b); // 2;
		
		a = 10;
		b = a + 1;
		System.out.println(b); // 2;
	}
	
	/**
	 * 响应式
	 */
	public static void reactiveSet() {
		int a = 1;
		System.out.println(add(a)); // 2;
		
		a = 10;
		System.out.println(add(a)); // 11;
	}
	
	public static int add(int a) { // 构建了某种关系
		return a + 1;
	}
	
	
	
	/**
	 * 按调用方法的顺序输出
	 */
	public static void test1() {
		Observable.just(1, 2, 3)
				.map(i -> {return "输出：" + i;})
				.map(t -> {return t + "次";})
				.subscribeOn(Schedulers.newThread())
				.subscribe(str -> System.out.println(str));
				
	}
	
	/**
	 *  随机输出,merge()是无序的
	 */
	public static void test2() {
		Observable<Integer> initData = Observable.just(1, 2, 3).subscribeOn(Schedulers.newThread());
		Observable<String> dataOP1 = Observable.just(1, 2, 3).map(t -> {return "hello:" + t;}).subscribeOn(Schedulers.newThread());
		Observable<String> dataOP2 = Observable.just(1, 2, 3).map(t -> {return "输出:" + t;}).subscribeOn(Schedulers.newThread());
		Observable.merge(initData, dataOP1, dataOP2).subscribe(t -> System.out.println(t));
	}
}
