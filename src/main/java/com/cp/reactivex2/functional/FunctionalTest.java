package com.cp.reactivex2.functional;

import java.io.IOException;

import com.chamc.pmcsdk.http.exception.HttpClientException;
import com.chamc.pmcsdk.http.fluent.Request;

/**
 * 函数式
 */
public class FunctionalTest {
	
	static int tries = 3;
	final static String url = "http://11.22.33.44:1234/test";
	
	public static void main(String[] args) {
		int result = tryIfError();
		System.out.println(result);
	}

	public static int tryIfError() {
	    if(tries > 0) { // 
	    	System.out.println("重试剩余" + tries + "次");
	        try {
	        	request(); // 发起请求
	        } catch (HttpClientException e) {
	        	tries--; // 重试次数减1
	        	tryIfError(); // 遇到错误就重试
	        }
	    }
	    return 0;
	}
	
	/**
	 *  JAVA5-SDK请求方式
	 *  使用说明：http://10.80.37.239:8090/pages/viewpage.action?pageId=2393909
	 */
	public static void request() {
		try {
			Request.get(url).connectTimeout(1000).execute();
			return;
		} catch (IOException e) {
			System.out.println("请求失败了");
			throw new HttpClientException(e.getMessage(), e);
		}
	}
}
