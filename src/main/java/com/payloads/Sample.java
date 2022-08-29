package com.payloads;

import java.util.Random;

public class Sample {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int max = 5000;
		int min = 1000;
		
	    int aisle = (int)(Math.random() * (max - min + 1) + min);
	    System.out.println(aisle);
	}

}
