package com;

import java.util.Random;
import java.util.Scanner;

public class DiceRoller {
	private static final String ENTER_10_TO_ROLL_THE_DICE = "enter 10 to roll the dice";
	private static final String DICE_VALUE_IS = "dice value is ";
	static int sum = 0;
	public static void roll(int a,Scanner sc) {
		Random r = new Random();
		if(a==10) {
			int x=r.nextInt(1,7);
			if(x!=1) {
				System.out.println(DICE_VALUE_IS+ x);
				sum +=x;
				System.out.println(ENTER_10_TO_ROLL_THE_DICE);
				getNum(sc);
			}else{
				System.out.println(DICE_VALUE_IS+ x);
				System.out.println("you are out of the game \nfinal score is "+ sum);	
			}
		} else {
			System.out.println("enetered wrong number, please "+ ENTER_10_TO_ROLL_THE_DICE);
			getNum(sc);
		}
			
	}
	private static void getNum(Scanner sc) {
		int num=sc.nextInt();
		roll(num,sc);
	}
	public static void main(String[] args) {
		System.out.println("enter 10 to start the game and roll the dice");
		System.out.println("your inital score is 0");
		try(Scanner sc = new Scanner(System.in)){
			getNum(sc);
		}catch (RuntimeException e) {
			System.out.println("enter number 10 to start, please restart the dice game");
		}		
	}

}
