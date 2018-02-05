package com.sannu.designpatterns;

public class Singleton {
	
	/**
	 * version 1 - eager initialization
	private static Singleton INSTANCE = new Singleton();
	
	public static Singleton getInstance() {
		return INSTANCE;
	}
	*/
	
	/**
	 * version 2 - lazy initialization without synchronization
	private static Singleton INSTANCE = null;
	
	public static Singleton getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new Singleton();
		}
		return INSTANCE;
	}
	*/
	
	/**
	 * version 3 - lazy initialization with method level synchronization
	private static Singleton INSTANCE = null;
	
	public static synchronized Singleton getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new Singleton();
		}
		return INSTANCE;
	}
	*/
	
	/**
	 * version 4 - lazy initialization with block level synchronization
	 * also called double checked locking
	private static Singleton INSTANCE = null;
	
	public static Singleton getInstance() {
		if (INSTANCE == null) {
			synchronized(Singleton.class) {
				INSTANCE = new Singleton();
			}			
		}
		return INSTANCE;
	}
	*/
	
	/**
	 * version 5 - lazy initialization with double checked locking with volatile
	 * 
	private static volatile Singleton INSTANCE = null;
	
	public static Singleton getInstance() {
		if (INSTANCE == null) {
			synchronized(Singleton.class) {
				INSTANCE = new Singleton();
			}			
		}
		return INSTANCE;
	}
	*/
	
	public static Singleton getInstance() {
		return SingletonHolder.INSTANCE;
	}
	
	static class SingletonHolder {
		private static Singleton INSTANCE = new Singleton();
	}
	
	public static void main(String args[]) {
		System.out.println(Singleton.getInstance());
		System.out.println(SingletonEnum.INSTANCE);
	}

}

enum SingletonEnum {
	INSTANCE;
}