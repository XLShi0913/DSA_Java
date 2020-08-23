package chapter2.vector;

import itertor.IteratorVector;
import java.util.Iterator;

/*
1、基于数组的向量实现；
2、使用查找、排序的相关算法时，需要保证传入向量的数据类型重写了equal方法和compareTo方法
 */
public class Vector<T> {
//=============================================================================
//基本内容
    private final int defaultCapacity = 128;//向量的默认容量，固定为10
    private T[] array;
    private int capacity;
    private int size;

    //显示向量，需要向量元素有toString方法的支持
    public void show(){
        System.out.print("[");
        for (int i = 0; i < this.size - 1; i++) {
            System.out.print(this.array[i] + ",");
        }
        System.out.println(this.array[this.size - 1] + "]");
    }

    //    这是一个无参构造方法
    public Vector() {
        this.array = (T[]) new Object[defaultCapacity];
        this.capacity = defaultCapacity;//预给一个数组容量，其值为默认容量
        this.size = 0;//预给一个数组长度，其值为零
    }

    //    此构造方法用于将目标数组的一部分定义为一个向量
    public Vector(T[] Array, int from, int to) {
        this.size = to - from;
        this.capacity = 2 * size;
        this.array = (T[]) new Object[this.capacity];//根据输入重新分配空间
        for (int i = 0; i < this.size; i++) {
            this.array[i] =  Array[i + from];
        }
    }

    //    此构造方法用于将目标向量的一部分定义为一个向量
    public Vector(Vector<T> origin, int from, int to) {
        this.size = to - from;
        this.capacity = 2 * size;
        this.array = (T[]) new Object[this.capacity];//根据输入重新分配空间
        for (int i = 0; i < this.size; i++) {
            this.array[i] = (T) origin.getArray(i + from);
        }
    }

    //    此方法用于向量的扩容，采用加倍式扩容方法
    private void expand() {
        if (this.size < this.capacity) {
            return;
        }
        T[] arrayNew = (T[]) new Object[2 * this.capacity];
        for (int i = 0; i < this.size; i++) {
            arrayNew[i] = this.array[i];
        }
        this.array = arrayNew;
    }
//=============================================================================
//向量的扩展操作
    //    此方法用于循秩访问
    public T getArray(int n) {
        if (this.size <= n || n < 0) {
            throw new ArrayIndexOutOfBoundsException("循秩访问的索引值越界");
        }
        return this.array[n];
    }

    // 此方法用于修改元素
    public void setArray(int n, T ele){
        this.array[n] = ele;
    }

    //    此方法用于返回数组长度
    public int getSize() { return size; }

    //给出直接改变size的接口，便于高效算法的设计
    public void setSize(int size){
        this.size = size;
    }

    //    此方法用于向数组中插入一个元素，需要给出元素和插入位置，返回插入位置
    public int insert(T element, int rank) {
        if (element == null){/*不接受空内容的插入*/
            throw new NullPointerException("异常：插入的向量元素为空");
        }
        this.size++;
        expand();
        int r = this.size - 2;
        //r用于将向量元素后移一位，从后到前，因为size已经++了，所以最后一个元素的rank是size-2
        while (rank <= r) {
            this.array[r] = this.array[r + 1];
            r--;
        }
        this.array[rank] = element;
        return rank;
    }

    //    此方法用于移除向量中的一个指定位置的元素
    public T remove(int rank) {
        this.size--;
        T res = this.array[rank];
        while (rank < this.size) {//这个size已经减过1了
            this.array[rank] = this.array[rank + 1];
            rank++;
        }
        return res;
    }

    //    此方法用于移除向量中的一个指定区间的元素，给出上下限
    public void remove(int lo, int hi) {
        if (hi <= lo) {
            System.out.println("区间长度必须不小于1");
            return;
        }
        int len = hi - lo;//要减去区间的长度
        this.size -= len;
        int rank = lo;//用于标记循环中向量元素的秩
        while (rank < hi) {
            //这个size已经减过len了，直接前移后边的元素，覆盖待删除区间
            this.array[rank] = this.array[rank + len];
            rank++;
        }
    }

    //    此方法用于无序向量的元素查找，给出待查找元素和查找的上界下界，返回该元素第一次出现的秩
    public int find(Object ele, int lo, int hi) {
        while (lo < hi) {
            if (ele.equals(this.array[lo])) { return lo; }
            lo++;
        }
        return -1;//查找失败，返回-1
    }

    //    此方法用于无序向量的唯一化
    public void deduplicate() {
        int r = 0;//用于标记循环中向量元素的秩
        while (r < this.size) {
            for (int i = 0; i < r; i++) {
                if (this.array[i].equals(this.array[r])) { remove(r); }
            }
            r++;
        }
    }

    //    此方法用于向量各元素的遍历
    public Iterator<T> traverse() {
        return new IteratorVector<T>(this);
    }

    //    此方法用于有序向量的唯一化，注意观察算法的思想
    public void uniquify() {
        int rNew = 1;//用于标记唯一化后向量元素的秩
        for (int i = 1; i < this.size; i++) {
            if (!this.array[i].equals(this.array[i - 1])) {
                //本位置元素与前一个元素不相等，就将之放到结果中
                this.array[rNew] = this.array[i];
                rNew++;
            }
        }
        this.size = rNew;//应该用rNew，不是rNew++
    }

    //此方法是有序向量的顺序查找，给定待查找元素，在整个向量域中返回不小于其的最大元素的秩
    public int search(Comparable ele){
        int point = 0;
        while (point < size){
            if ( ((Comparable)array[point]).compareTo(ele) > 0 )
                break;
            point++;
        }
        return --point;
    }

    //    此方法用于有序向量的二分查找,版本A，给定待查找元素、查找上下界，返回元素位置，若失败，返回-1
    public int binSearchA(Comparable<Comparable> ele, int lo, int hi) {
        //先判断向量的数据有没有实现comparable接口
        if (! (array[lo] instanceof Comparable) ) {
            throw new ClassCastException("异常：向量元素的数据类型没有实现comparable接口");
        }
        int mi;
        while (lo < hi) {//数组长度为1或以上
            mi = (lo + hi) >> 1;
            if (ele.compareTo((Comparable) this.array[mi]) < 0) { hi = mi; }
            else if (ele.compareTo((Comparable) this.array[mi]) > 0) { lo = mi + 1; }
            else { return mi; }
        }
        return -1;
    }

    //    此方法用于有序向量的二分查找,版本C，给定待查找元素、查找上下界，返回不大于ele的最后一个元素
    public int binSearchC(Comparable ele, int lo, int hi) {
        //先判断向量的数据有没有实现comparable接口
        if (! (array[lo] instanceof Comparable) ) {
            throw new ClassCastException("异常：向量元素的数据类型没有实现comparable接口");
        }
        int mi;
        while (lo < hi) {//数组长度为1或以上
            mi = (lo + hi) >> 1;
            if (ele.compareTo((Comparable) this.array[mi]) < 0) { hi = mi; }
            else { lo = mi + 1; }
        }
        return --lo;
    }
    //    插值查找和fibonacci查找都只是在mi的选取上做改变，算法整体不变

    //    此方法用于将无序数组排序，使用起泡排序
    public void bubbleSort(){
        int lo = 0;
        int hi = this.size;
        while (lo < hi){
            hi = bubble(this.array, lo, hi);
        }
    }
    //    此方法是起泡排序的子方法，将向量进行一次起泡操作，返回最后一次起泡交换位置，只对指定区间内的元素进行操作
    public static int bubble(Object[] array, int lo, int hi){
        //先判断向量的数据有没有实现comparable接口
        if (! (array[lo] instanceof Comparable) ) {
            throw new ClassCastException("异常：向量元素的数据类型没有实现comparable接口");
        }
        Object temp;
        int iLast = lo;//用于记录最后一次的交换位置，返回后者的秩
        for (int i = lo + 1; i < hi; i++) {//与前一个元素相比，如果小，则交换位置
            Comparable eNext = (Comparable) array[i];
            Comparable eFro = (Comparable) array[i - 1];
            if (eNext.compareTo(eFro) < 0){
                temp = array[i];
                array[i] = array[i - 1];
                array[i - 1] = temp;
                iLast = i;
            }
        }
        return iLast;
    }

    //    此方法用于将无序数组排序，使用归并排序
    public void mergeSort(int lo, int hi){
        if (hi - lo < 2){ return; }//数组长度为1，直接返回
        int mi = (hi + lo) >> 1;
        mergeSort(lo, mi);
        mergeSort(mi, hi);
        merge(lo, mi, hi);

    }
    //    此方法是归并排序的子方法，用于将两个排序完毕的数组合成一个数组
    //    实际的排序工作是在merge的过程中完成的，mergeSort只是把元素分解到了单个元素的层次
    public void merge(int lo, int mi, int hi) {
        T[] b = (T[]) new Object[mi - lo];//向量b保存从lo到mi的元素（不包括mi）

        for (int i = lo; i < mi; i++) { b[i - lo] = this.array[i]; }
        //从mi到hi的数据仍然保存在array中，假想此部分元素存储在一个新向量c中
        int rank = lo;//归并后元素在array中的秩
        int rankB = 0;//归并前元素在b中的秩
        int rankC = mi;//归并前元素在c中的秩

        while (rankB < mi - lo || rankC < hi){//改进前的逻辑
            if (rankB < mi - lo && (hi <= rankC ||
                    ((Comparable) b[rankB]).compareTo((Comparable) this.array[rankC]) < 0))
            { this.array[rank++] = b[rankB++]; }
            //b没有结束，b中首元素小或c提前结束的情况
            if (rankC < hi && (mi - lo <= rankB ||
                    ((Comparable) this.array[rankC]).compareTo((Comparable) b[rankB]) < 0))
            { this.array[rank++] = this.array[rankC++]; }
            //c没有结束，c首元素小的或b提前结束的情况
        }
        /*while (rankB < mi - lo){//改进后的逻辑（有向量index越界的错误），悬而未决，初步判断是rankB的问题
            if (hi <= rankC || ((Comparable) b[rankB]).compareTo((Comparable) this.array[rankC]) <= 0)
                { this.array[rank++] = b[rankB++]; }
            if (rankC < hi && ((Comparable) this.array[rankC]).compareTo((Comparable) b[rankB]) < 0)
                { this.array[rank++] = this.array[rankC++]; }
        }*/
    }
}