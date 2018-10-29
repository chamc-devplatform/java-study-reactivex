package com.cp.reactivex2.observerpattern;

public interface Observable {
	
	public void registerObserver(Observer o); // 关注
	
	public void removeObserver(Observer o); // 取关
	
	public void notifyObserver(); // 消息通知
}
