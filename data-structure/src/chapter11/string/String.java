package chapter11.string;
/*
串类型的数据结构
1、讨论的重点在于串匹配算法，返回第一次匹配的位置，并可根据这一位置判断查找是否成功；
2、蛮力算法，对于文本串的每一个位置，都拿模式串的每个元素与之比较；
3、KMP算法，分析并记忆模式串的自匹配特性，制成next[]表，大大减少了匹配过程中的比较时间；
 */
public class String {

//======================================================================================================================
    //蛮力算法
    public static int matchFB1(char[] P, char[] T){
        int m = P.length; int j = 0;//模式串长度与模式串当前考察指针
        int n = T.length; int i = 0;//文本串长度与文本串当前考察指针
        while (i < n && j < m){
            if (P[j] == T[i]) { i++; j++; }//若匹配，则携手后移一位
            else { i -= j - 1; j = 0; }//否则i回退，j复位
        }
        return i - j;
    }

    public static int matchFB2(char[] P, char[] T){
        int m = P.length; int j;//模式串长度与模式串当前考察指针
        int n = T.length; int i;//文本串长度与文本串当前考察段落首位置指针
        for (i = 0; i < n - m + 1; i++){
            for (j = 0; j < m; j++){
                if (T[i + j] != P[j]) break;
            }
            if (m <= j) break;
        }
        return i;
    }
//======================================================================================================================
    //KMP算法
    public static int matchKMP(char[] P, char[] T){
        //建立next表，当不匹配时，找出下一个与文本串比较的模式串元素
        int[] next = buildNext(P);
        int m = P.length; int j = 0;//模式串长度与模式串当前考察指针
        int n = T.length; int i = 0;//文本串长度与文本串当前考察指针
        while (i < n && j < m){
            if (j < 0 || P[j] == T[i]) { j++; i++; }//匹配的情况
            else { j = next[j]; }//不匹配的情况，i不变
        }
        return i - j;
    }

    protected static int[] buildNext(char[] P){
        //对模式串建立next表，相当于模式串自己与自己匹配
        //第j个元素相当于子串P[0,j)中最大自匹配的前缀/后缀的长度
        int m = P.length;
        int[] n = new int[m];//存放结果
        n[0] = -1;//相当于在-1位置上放置了一个假想的通配字符
        int j = 0;//主串指针
        int i = n[j];//辅串指针
        while (j < m - 1){
            if ( i < 0 || P[j] == P[i] ) {//匹配的情况

                /*n[++j] = ++i;//原版*/
                j++; i++;
                n[j] = (P[j] != P[i]) ? i : n[i];//改进版
            }
            else i = n[i];//不匹配的情况
        }
        return n;
    }
//======================================================================================================================
    //BM算法，BC策略+GS策略
    public static int matchBM(char[] P, char[] T){
        // 需要建立两个表，bc[]表和gs[]表
        int[] bc = buildBC(P);
        int[] gs = buildGS(P);
        int i = 0;//字符串指针，初始化为0表示模式串起始位置与字符串左对齐
        while (i + P.length <= T.length){
            int j = P.length - 1;//模式串指针，从右向左逆向比对
            while (P[j] == T[i + j])
                if (--j < 0) break;//逆向逐次比对至完全匹配而退出
            if (0 > j) break;//至T完全比对完毕而退出
            else i += Math.max(gs[j], j - bc[ T[i + j] ]);//位移量取bc表和gs表中的最大者
        }
        return i;
    }


    protected static int[] buildBC(char[] P){
        // bc表的建立，长度与字符表长度一致，对应元素为模式串中对应字符最后一次出现的位置
        int[] bc = new int[256];
        for (int i = 0; i < 256; i++) { bc[i] = -1; }
        for (int i = 0; i < P.length; i++) { bc[ P[i] ] = i; }
        return bc;
    }

    protected static int[] buildSS(char[] P){
        // gs[]表的建立，需要先建立ss[]表；
        // ss[j]为终止于P[j]，同时于全串后缀实现最长匹配的字串的长度，记此子串为MS[j]
        int m = P.length;
        int[] ss = new int[m];
        ss[m - 1] = m;//最后一项，应该是m
        // 以下从倒数第二项开始，自右向左扫描P并建立ss[]
        int lo = m - 1; int hi = m - 1;//分别对应于MS[j]的上/下限
        for (int j = m - 2; 0 <= j; j--){
            if ( (lo < j) && ( ss[m - hi + j - 1] < j - lo ) )//情况1
                ss[j] = ss[m - hi + j - 1];//直接利用已经计算好的ss[]
            else {//情况2，无法使用已计算值，需要蛮力比对
                hi = j; lo = Math.min( lo, hi );
                while ( (0 <= lo) && ( P[lo] == P[m - hi + j - 1] ) )
                    lo--;
                ss[j] = hi - lo;
            }
        }
        return ss;
        /*对于情况1的理解，为什么不需要重新计算
        1、hi和lo仅在重新计算的时候更新，在第一次进入情况2之后(lo, hi]始终与全串的一个后缀[m-s, m)相对应；
        2、情况1的条件保证了j在lo和hi之间，且子串[m-s, m)中与j相同位置的元素，其MS长度未超过m-s的范围；
        3、两点限制保证了：j与m - hi + j - 1两个位置的“处境相同”，因而不必重新计算；*/
    }

    protected static int[] buildGS(char[] P){
        //gs[]表的建立，gs[j]即在模式串中另寻一子串，使之与P(j,m)匹配，返回这一子串之前的那个秩
        int[] ss = buildSS(P);
        int m = P.length;
        int[] gs = new int[m];
        for (int j = 0; j < m; j++) gs[j] = m;//初始化，默认没有好后缀
        for (int i = 0, j = m - 1; 0 < j; j--){//逆向逐一扫描各字符P[j]，找一个特殊的j
            if (ss[j] == j + 1)
                while (i < m - j - 1) gs[i++] = m - j - 1;//情况a
        }
        for (int j = 0; j < m - 1; j++)//正向扫描，gs[j]不断递减直至最小
            gs[m - ss[j] - 1] = m - j - 1;//情况b
        return gs;
        /*对于gs构造的理解
        1、算法使用了画家策略，画三层，前两层是特殊情况，最后一层是一般情况；
        2、gs[i]为从后至前失配于P[i]时，需要向右移动多远方可再找到一个好的后缀；
        3、假想在P[0]左端有足够多的通配符哨兵，那么对于任意一个i，右移m必然有一个全部由通配符哨兵组成的好后缀；
        4、特殊情况：若存在这样一个j，使得ss[j] = j+1，即P[0,j] == P[m-j-1,m-1]，则不需要移动m，仅需要移动m-j-1，
            因为有j+1通配符哨兵可由P[0,j]代替；
        5、一般情况：对于P[m-ss[j]-1]，前移m-j-1到达的P[j]满足P{j-ss[j], j] == P(m-ss[j]-1, m}，恰好是好的；*/
    }
}