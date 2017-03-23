package com.heyi.utils;

import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;

/**
 * 数字类操作工具类
 * @author TangGang   2015.4.24
 * 
 */
public class NumberUtils {
    /** Reusable Long constant for zero. */
    public static final Long LONG_ZERO = new Long(0L);
    /** Reusable Long constant for one. */
    public static final Long LONG_ONE = new Long(1L);
    /** Reusable Long constant for minus one. */
    public static final Long LONG_MINUS_ONE = new Long(-1L);
    /** Reusable Integer constant for zero. */
    public static final Integer INTEGER_ZERO = new Integer(0);
    /** Reusable Integer constant for one. */
    public static final Integer INTEGER_ONE = new Integer(1);
    /** Reusable Integer constant for minus one. */
    public static final Integer INTEGER_MINUS_ONE = new Integer(-1);
    /** Reusable Short constant for zero. */
    public static final Short SHORT_ZERO = new Short((short) 0);
    /** Reusable Short constant for one. */
    public static final Short SHORT_ONE = new Short((short) 1);
    /** Reusable Short constant for minus one. */
    public static final Short SHORT_MINUS_ONE = new Short((short) -1);
    /** Reusable Byte constant for zero. */
    public static final Byte BYTE_ZERO = new Byte((byte) 0);
    /** Reusable Byte constant for one. */
    public static final Byte BYTE_ONE = new Byte((byte) 1);
    /** Reusable Byte constant for minus one. */
    public static final Byte BYTE_MINUS_ONE = new Byte((byte) -1);
    /** Reusable Double constant for zero. */
    public static final Double DOUBLE_ZERO = new Double(0.0d);
    /** Reusable Double constant for one. */
    public static final Double DOUBLE_ONE = new Double(1.0d);
    /** Reusable Double constant for minus one. */
    public static final Double DOUBLE_MINUS_ONE = new Double(-1.0d);
    /** Reusable Float constant for zero. */
    public static final Float FLOAT_ZERO = new Float(0.0f);
    /** Reusable Float constant for one. */
    public static final Float FLOAT_ONE = new Float(1.0f);
    /** Reusable Float constant for minus one. */
    public static final Float FLOAT_MINUS_ONE = new Float(-1.0f);
    
	/**
	 * @param n  需要判断的数字
	 * @return 如果指定的数字为null则返回true
	 */
	public static boolean isNull(Number n) {		
		return n == null;
	}
	
	/**
	 * @param n  需要判断的数字
	 * @return   如果指定的数字为null或为0则返回true
	 */
	public static boolean isNullOrZero(Number n) {
		boolean result = isNull(n);
		
		return result || n.equals(0);
	}
	
	/**
	 * 将指定数四舍五入到指定小数位数
	 * @param data   原始数据
	 * @param digits 需要保留的小数据位数
	 * @return 四舍五入到指定小数位数的数字
	 */
	public static double round(double data, int digits) {
		BigDecimal bd = new BigDecimal(data);

		return bd.setScale(digits, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	/**
	 * 判断两个数字是否相等
	 * @param n1   
	 * @param n2
	 * @return 相等返回true,否则false
	 * @author TangGang  2015-6-9
	 */
	public static boolean equals(Number n1, Number n2) {
		if (n1 == null && n2 == null) {
			return true;
		} else if (n1 != null && n2 == null) {
			return false;
		} else if (n1 == null && n2 != null) {
			return false;
		} else {
			return n1.equals(n2);
		}		
	}
	
	public static boolean equals(Short n1, int n2) {
		if (n1 == null) {
			return false;
		}
		
		return n1.intValue() == n2;
	}
	
	public static boolean isNumeric(String str) {
		return org.apache.commons.lang.math.NumberUtils.isNumber(str);
	}
	
	public static String formatNumber(Number number , Integer length) {
		String str = String.format("%0".concat(String.valueOf(length)).concat("d"), number); 
		return str;
	}
	
	/**
	 * 将元转换成分
	 * @param fee
	 * @return
	 */
	public static int formatFee2Fen(double fee) {
		return formatFee2Fen(String.valueOf(fee));
	}
	
	/**
	 * 将元转换成分
	 * @param fee
	 * @return
	 */
	public static int formatFee2Fen(String fee) {
		if(StringUtils.isBlank(fee) || !isNumeric(fee))
			return 0;
		
		BigDecimal bd = new BigDecimal(fee);		
		return bd.multiply(new BigDecimal("100")).intValue();
	}
	
	/**
	 * 将分转换成元
	 * @param fen
	 * @return
	 */
	public static double formatFen2Yuan(int fen) {
		return fen / 100.00;
	}

	/**
	 * 将分转换成元
	 * @param fen
	 * @return
	 */
	public static double formatFen2Yuan(String fen) {
		if(StringUtils.isBlank(fen) || !isNumeric(fen))
			return 0.0;
		
		return Integer.valueOf(fen) / 100.00;
	}
}