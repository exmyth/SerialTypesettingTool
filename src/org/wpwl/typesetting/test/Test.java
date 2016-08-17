package org.wpwl.typesetting.test;

public class Test {
	public static void main(String[] args) {
		String args1 = "    a   b,c d ";
		String[] split = args1.trim().split("\\s+");
		System.out.println(split);
	}
}
