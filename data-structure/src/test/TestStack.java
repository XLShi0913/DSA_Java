package test;

import chapter4.stack.Stack;

public class TestStack {
    public static void main(String[] args) {
        System.out.println("进制转换测试：");
        System.out.println(Stack.convert(10, 16));
        System.out.println("=================================");

        System.out.println("括号匹配测试：");
        String str1 = "()()(())";
        String str2 = "())()";
        System.out.println(Stack.paren(str1.toCharArray(), 0, 8));//true
        System.out.println(Stack.paren(str2.toCharArray(), 0, 8));//false
        System.out.println("=================================");

//        System.out.println("栈混洗甄别测试：");
//        Integer[] array1 = new Integer[]{1, 2, 3};
//        Integer[] array2 = new Integer[]{3, 1, 2};//不是栈混洗
//        Integer[] array3 = new Integer[]{3, 2, 1};//是栈混洗
//        System.out.println(Stack.judgePermutation(array1, array2));
//        System.out.println(Stack.judgePermutation(array1, array3));
//        System.out.println("=================================");

        System.out.println("中缀表达式求值测试：");
        double num = Stack.evaluate("(1+2^6-4)*(120-(6-(7-(89-1))))\0");
        System.out.println(num);
    }
}
