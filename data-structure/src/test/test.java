package test;

import chapter9.dictionary.randomNum;

public class test {
    public static void main(String[] args) {
        randomNum r = new randomNum();
        System.out.println(r.getRandNum(1000));
        long[] res = r.getRandNum(100, 10);
        for (int i = 0; i < 10; i++) {
            System.out.println(res[i]);
        }
        int[] nums = new int[10];
    }
}
