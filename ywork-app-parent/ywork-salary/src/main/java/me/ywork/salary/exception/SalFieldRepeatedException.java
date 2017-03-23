package me.ywork.salary.exception;

public class SalFieldRepeatedException extends Exception {
	public SalFieldRepeatedException(String msg){
		super(msg);
	}
	
	public SalFieldRepeatedException(){
		super();
	}
	

}
