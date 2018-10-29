package com.cp.reactivex2.functional;

import java.util.ArrayList;
import java.util.List;

/**
 * 命令式
 */
public class CommandTest {

	public static void main(String[] args) {
		
		List<Integer> results = new ArrayList<Integer>(3);
		results.add(1);
		results.add(2);
		results.add(3);
		
		int count = 0;	// 第一步,创建一个变量并初始化
		for(Integer i : results) { // 第二步，遍历集合
			if (i > 1) {
				count += i;  // 第三步，如果数字>1就加到count里
			}
		}
		System.out.println(count);
	}
}
