package org.wpwl.typesetting.util;

public class StringUtil {
	public static String format(String str, int length, Long num) {
		String n = String.valueOf(num);
		if (n.length() >= length) {
			return n;
		} else {
			return String.format("%1$s%2$0" + (length - str.length()) + "d", str, num);
		}
	}

	public static String formatString(String str, int length, String num) {
		if (num.length() >= length) {
			return num;
		} else {
			int len = length - num.length();
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < len; i++) {
				sb.append(str);
			}
			sb.append(num);
			return sb.toString();
		}
	}

	final static char[] digits = { 
			'0', '1', '2', '3', '4',
			'5', '6', '7', '8', '9', 
			'A', 'B', 'C', 'D', 'E', 'F', 'G', 
			'H', 'I', 'J', 'K', 'L', 'M', 'N',
			'O', 'P', 'Q', 		'R', 'S', 'T', 
			'U', 'V', 'W',		'X', 'Y', 'Z',
			'a', 'b', 'c', 'd', 'e', 'f', 'g',
			'h', 'i', 'j', 'k', 'l', 'm', 'n',
			'o', 'p', 'q', 		'r', 's', 't',
			'u', 'v', 'w', 		'x', 'y', 'z',
			'+', '='};

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		System.out.println(compressNumber(999L, 5));
//		System.out.println(unCompressNumber("AVV987", 5));
//		System.out.println(getCharIndexNum('a'));
		System.out.println(isNumeric("0000A"));
	}

	/**
	 * 把10进制的数字转换成2^shift进制
	 * 
	 * @param number 十进制数字
	 * @param shift	2^shift=进制,二进制(shift=1),八进制(shift=3),十六进制(shift=4),
	 * @return
	 */
	public static String compressNumber(long number, int shift) {
		char[] buf = new char[64];
		int charPos = 64;
		int radix = 1 << shift;
		long mask = radix - 1;
		do {
			buf[--charPos] = digits[(int) (number & mask)];
			number >>>= shift;
		} while (number != 0);
		return new String(buf, charPos, (64 - charPos));
	}

	/**
	 * 把2^shift进制的字符串转换成10进制
	 * @param decompStr	字符串数字
	 * @param shift		2^shift=进制,二进制(shift=1),八进制(shift=3),十六进制(shift=4)
	 * @return
	 */
	public static long unCompressNumber(String decompStr, int shift) {
		long result = 0;
		for (int i = decompStr.length() - 1; i >= 0; i--) {
			if (i == decompStr.length() - 1) {
				result += getCharIndexNum(decompStr.charAt(i));
				continue;
			}
			for (int j = 0; j < digits.length; j++) {
				if (decompStr.charAt(i) == digits[j]) {
					result += ((long) j) << shift * (decompStr.length() - 1 - i);
				}
			}
		}
		return result;
	}

	/**
	 * 
	 * @param ch
	 * @return
	 */
	private static long getCharIndexNum(char ch) {
		int num = ((int) ch);
		if (num >= 48 && num <= 57) {
			return num - 48;
		} else if (num >= 97 && num <= 122) {
			return num - 61;
		} else if (num >= 65 && num <= 90) {
			return num - 55;
		} else if (num == 43) {
			return 62;
		} else if (num == 61) {
			return 63;
		}
		return 0;
	}

	public static boolean isNumeric(String serial) {
		if(serial == null || serial.length() == 0){
			return false;
		}
		else{
			for(int i = 0; i < serial.length(); i++){
				char c = serial.charAt(i);
				if(c<48||c>57){
					return false;
				}
			}
		}
		return true;
	}
}
