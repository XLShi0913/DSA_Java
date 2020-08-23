package chapter9.dictionary;
/*
随机整数发生器，适用于小范围的随机
 */
public class randomNum {
    long seed;

    public randomNum(long seed) { this.seed = seed; }
    public randomNum() { seed = System.currentTimeMillis(); }

    protected long next(long former){
        return (16807L * former) % 2147483647L;
        //这两个数分别是7^5和2^31-1
    }

    //返回一个随机整数，下限为0，上限给定
    public long getRandNum(long M){
        long next = seed;
        for (int i = 0; i < 7; i++) {
            next = next(next);
        }
        return next % M;
    }

    //返回一组随机整数，给定上限和返回随机数的个数
    public long[] getRandNum(long M, int num){
        long[] res = new long[num];
        res[0] = next(seed);
        for (int i = 1; i < num; i++) {
            res[i] = next(res[i - 1]);
        }
        for (int i = 0; i < num; i++) {
            res[i] %= M;
        }
        return res;
    }

}
