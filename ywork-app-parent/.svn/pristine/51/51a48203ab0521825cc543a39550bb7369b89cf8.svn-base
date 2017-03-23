package me.ywork.salary.util;

public class PersonalTaxUtils {
	public static double getPersonalTax(double shouldPaySal){
		 double attenPersonalSal=0;
		if(shouldPaySal>3500){
	       attenPersonalSal = shouldPaySal -3500; 
			if(attenPersonalSal<=1455){
				attenPersonalSal =	attenPersonalSal*0.03;
			}else if(attenPersonalSal>1455&&attenPersonalSal<=4155){
				attenPersonalSal =attenPersonalSal*0.1-105;
			}else if(attenPersonalSal>4155&&attenPersonalSal<=7755){
				attenPersonalSal =attenPersonalSal*0.2-555;
			}else if(attenPersonalSal>7755&&attenPersonalSal<=27255){
				attenPersonalSal =attenPersonalSal*0.25-1005;
			}else if(attenPersonalSal>27255&&attenPersonalSal<=41255){
				attenPersonalSal =attenPersonalSal*0.3-2755;
			}else if(attenPersonalSal>41255&&attenPersonalSal<=57505){
				attenPersonalSal =attenPersonalSal*0.35-5505;
			}else if(attenPersonalSal>57505){
				attenPersonalSal =attenPersonalSal*0.45-13505;
			}
		}
		return attenPersonalSal;
	}
	public static void main(String [] args){
		double actualSal =7724.14;
	//	Integer personPercent=2;
		//double replaceDeduct=0.0;
	//	replaceDeduct =(double)personPercent/100;
		//double sal=2.567*100;
	//	System.out.println(replaceDeduct);
		//double  sal =23000;
		System.out.println(getPersonalTax(actualSal));
	}

}
