package chapter4.stack;
import exception.IllegalFormatOfOperatorException;
/*
1、基于数组实现的栈；
2、以静态方法的型式给出了栈的几个典型应用：
    进制转换（逆序输出，conversion）
    括号匹配（递归嵌套，stack permutation + parenthesis）
    栈的混洗（递归嵌套，stack permutation + parenthesis）
    算式求值（延迟缓冲，evaluation）
 */
public class Stack {
//==================================================================================================
//    栈框架的构造
    private Object[] array;
    private int size;
    private int capacity;
    private final int defaultCapacity = 16;
    //    空参构造
    public Stack() {
        this.size = 0;
        this.capacity = defaultCapacity;
        this.array = new Object[defaultCapacity];
    }
    //    复制数组的一段构造
    public Stack(Object[] array, int lo, int hi) {
        this.size = hi - lo;
        this.capacity = this.size * 2;
        this.array = new Object[capacity];
        for (int i = 0; i < this.size; i++) { this.array[i] = array[i + lo]; }
    }
    //    栈的扩容
    private void expand() {
        if (this.size < this.capacity) { return; }
        Object[] arrayNew = new Object[2 * this.capacity];
        for (int i = 0; i < this.size; i++) { arrayNew[i] = this.array[i]; }
        this.array = arrayNew;
    }
    //    将一个元素添加到栈的顶端
    public void push(Object ele){ expand();this.array[this.size++] = ele; }
    //    读取栈顶端的元素
    public Object top(){ return this.array[this.size - 1]; }
    //    弹出栈的首元素
    public Object pop(){ return this.array[--this.size]; }
    //    检查栈是否为空
    public boolean isEmpty(){ return this.size == 0; }
    //    返回栈中元素个数
    public int getSize() { return size; }
//==================================================================================================
//    进制转换，十进制转换为其他进制，目前仅支持十六进制以下
    public static String convert(int n, int base){
        char[] digit = new char[]
                {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        Stack stack = new Stack();
        while (n > 0){
            stack.push(digit[n % base]);
            n /= base;
        }
        StringBuilder result = new StringBuilder();
        while ( !stack.isEmpty() ){
            result.append(stack.pop());
        }
        return result.toString();
    }
//==================================================================================================
//    括号匹配，目前只支持一对括号'(' ')'的匹配
    public static boolean paren(char[] exp, int lo, int hi){
        Stack stack = new Stack();//用于存放括号
        while (lo < hi){
            if (exp[lo] == '(') { stack.push(exp[lo]); }
            else if (exp[lo] == ')' && !stack.isEmpty()) { stack.pop(); }
            else if (exp[lo] == ')' && stack.isEmpty()){ return false; }
            lo++;
        }
        return stack.isEmpty();
    }
//==================================================================================================
/*    栈混洗，判断序列B是否为序列A的栈混洗
        栈A：<a1, a2, a3, ... an]
        栈B：[b1, b2, b3, ... bn>
 */
    public static boolean judgePermutation(Object[] orderA, Object[] orderB){
        Stack stackA = new Stack();
        Stack stackB = new Stack();
        Stack stackS = new Stack();
        for (int i = orderA.length - 1; i >= 0; i--) { stackA.push(orderA[i]); }
        int point = 0;//orderB的指针
        while ( !stackA.isEmpty() ){
            stackS.push(stackA.pop());
            while ( !stackS.isEmpty() ){
                if (stackS.top() == orderB[point]){
                    stackB.push(stackS.pop());
                    point++;
                }
            }
        }
        return !stackS.isEmpty();
    }
//==================================================================================================
//    中缀表达式求值，暂时支持+ - * / ^ () !，不支持输入小数点，不支持小数的阶乘
    public static double evaluate(String formula){
        char[] chars = formula.toCharArray();
        Stack opnd = new Stack();//存储数字
        Stack optr = new Stack();//存储操作符
        optr.push('\0');//栈底哨兵
        int point = 0;//字符串数组的指针
        while ( !optr.isEmpty() ){
            if ( isDigit(chars[point]) ){//字符数组当前位置是数字的情况
                point = opnd.readNumber(chars, point);//操作 + 返回移动后的指针值
            }else {//字符数组当前位置是操作符的情况
                switch (orderBetween(optr.top(), chars[point])){
                    case '<'://栈顶操作符优先级更低，无法判断可执行与否，操作符进栈并接收下一操作符
                        optr.push(chars[point]);
                        point++;
                        break;
                    case '>'://栈顶操作符优先级更高，执行栈顶运算符，point位置不变
                        char op = (char)optr.pop();
                        if (op == '!'){//一元运算符的情况
                            double pop = (double)opnd.pop();
                            opnd.push(calcu(op, pop));
                        }else {//二元运算符的情况
                            double pop1 = (double)opnd.pop();
                            double pop2 = (double)opnd.pop();
                            opnd.push(calcu(op, pop1, pop2));
    //***********************************************************************************
                            //System.out.println("本步计算的结果：" + opnd.top());//调试用
    //***********************************************************************************
                        }
                        break;
                    case '='://脱括号并接收下一操作符
                        optr.pop();
                        point++;
                        break;
                }
            }
        }
        return (double)opnd.pop();
    }
    //    子方法：判断一个字符是否为数字
    private static boolean isDigit(char aChar) { return (aChar <= '9' && '0' <= aChar); }
    //    子方法：将一个字符数组的数字读入栈中，进栈的是double类型（注意java是值传递，需要返回移动后的指针）
    private int readNumber(char[] chars, int point) {
        double result = 0;
        while (isDigit(chars[point])){
            String s = chars[point] + "";//先变成字符串，再变成数字
            result = result * 10 + Integer.parseInt(s);
            point++;
        }
        this.push(result);
        return point;
    }
    //    子方法：用于判断运算符的优先级
    private static char orderBetween(/*栈顶*/Object top, /*当前*/char charNow) {
        char charTop = (char) top;//先强转成char类型
        char[] chars = new char[]{'+', '-', '*', '/', '^', '!', '(', ')', '\0'};
        int nTop = 0;//栈顶操作符编号，0~8
        int nNow = 0;//当前操作符编号，0~8
        while (chars[nTop] != charTop){ nTop++; }
        while (chars[nNow] != charNow){ nNow++; }
        char[] priority = new char[]//定义优先级次序，9行9列
                {   //---------纵排为栈顶操作符，横排为当前操作符----------
                    //       +    -    *    /    ^    !    (    )    \0
                    /* + */ '>', '>', '<', '<', '<', '<', '<', '>', '>',
                    /* - */ '>', '>', '<', '<', '<', '<', '<', '>', '>',
                    /* * */ '>', '>', '>', '>', '<', '<', '<', '>', '>',
                    /* / */ '>', '>', '>', '>', '<', '<', '<', '>', '>',
                    /* ^ */ '>', '>', '>', '>', '>', '<', '<', '>', '>',
                    /* ! */ '>', '>', '>', '>', '>', '>', ' ', '>', '>',
                    /* ( */ '<', '<', '<', '<', '<', '<', '<', '=', ' ',
                    /* ) */ ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ',
                    /*\0 */ '<', '<', '<', '<', '<', '<', '<', ' ', '='};
        return priority[nTop * 9 + nNow];
    }
    //    子方法：用于执行运算符，重载型式：一元运算符，仅支持阶乘运算，输出为double类型
    private static double calcu(char op, double pop) {
        int num = (int)pop;//向下转型，将double强转成int
        int result = 1;
        if (num <= 1){ return 1; }
        while (num > 1){ result *= num--; }
        return result;//向上转型成double
    }
    //    子方法：用于执行运算符，重载型式：二元运算符，仅支持+ - * / ^运算，输出为double类型
    private static double calcu(char op, double pop1, double pop2){
        switch (op){
            //注意：pop1对应的是算式后边的操作数
            case '+': return pop2 + pop1;
            case '-': return pop2 - pop1;//注意减数与被减数的位置
            case '*': return pop2 * pop1;
            case '/': return pop2 / pop1;//注意除数与被除数的位置
            case '^': return Math.pow(pop2, pop1);
            default: throw new IllegalFormatOfOperatorException("异常：暂不支持的操作符");
        }
    }
//==================================================================================================
}