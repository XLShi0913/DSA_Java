package chapter12.sort;
/*
排序器，排序算法的集合
已实现：快速排序、选取排序、希尔排序
 */
public class Sorter {
//=====================================================================================
    //快速排序
    public static void quickSort(int[] array, int lo, int hi){
        if (hi <= lo) return;//递归基
        int mi = partition(array, lo, hi);//轴点就位
        //分而治之，注意mi已经就位，不需要参与之后的过程
        quickSort(array, lo, mi);
        quickSort(array, mi + 1, hi);
    }
    protected static int partition(int[] array, int lo, int hi){
        //快速排序子方法，构造轴点
        //还可在此随机选一个节点与lo节点交换，以增加随机性，避免最坏情况的发生
        int pivot = array[lo];
        while (lo < hi){
            while (lo < hi && pivot <= array[hi]) hi--;//初始状态lo空闲
            array[lo] = array[hi];//说明后缀中出现了一个小的数，将之换到前缀，此时hi空闲
            while (lo < hi && array[lo] <= pivot) lo++;
            array[hi] = array[lo];//再次是lo空闲
        }//循环跳出时lo和hi已然相等
        array[lo] = pivot;
        return lo;//返回轴点位置
    }

}
