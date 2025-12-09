package dsapractice;

public class Fibonacci {

    public static void main(String[] args) {

        for(int i=0;i<=15;i++){
            System.out.print(fibonacci(i)+" ");
        }
    }

    public static int fibonacci(int n){
        if(n==1||n==0){
            return n;
        }
        return  fibonacci(n-1)+fibonacci(n-2);

    }
}
