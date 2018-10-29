package com.cp.reactivex2;

import java.util.ArrayList;
import java.util.List;

public class GenericTest {
	
	public static void main(String[] args) {
		
		List<? extends Parent> list = new ArrayList<>(2);
//		list.add(new Parent());	// 编译失败
//		list.add(new Son()); // 编译失败
//		list.add(new People());
		People peple = list.get(0);
		Parent parent = list.get(0);
		Son son = (Son) list.get(0);
		
		List<? super Parent> list1 = new ArrayList<>(2);
//		list1.add(new People()); // 编译失败
		list1.add(new Parent());
		list1.add(new Son());
		Object o = list1.get(0);
	}
	
	static class People {}
	
	static class Parent extends People {}
	
	static class Son extends Parent {}
}
