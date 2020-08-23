package chapter10.priorityqueue;

import chapter2.vector.Vector;
import interfaceUsedInDS.PQ;

/*
使用完全二叉堆对优先级队列的实现
1、借助于完全二叉树的思想，保证堆有堆序性；
2、堆序性是优先级队列的本质要求，要求任意节点的优先级都不大于其父节点的优先级；
3、底层实际上是一个数组（向量），其与完全二叉树的层次遍历序列对应；
4、核心算法是恢复堆序性的上滤和下滤，以及批量建堆的方法；
 */

public class PQ_ComplHeap< T extends Comparable<T> > implements PQ<T> {
    protected Vector<T> heap;//完全二叉堆的底层

    //默认的空参构造
    public PQ_ComplHeap() { }

    //给定一个向量的构造，使用Floyd批量建堆方法
    public PQ_ComplHeap(Vector<T> heap) {
        this.heap = heap;
        heapify(heap);
    }

//======================================================================================================================
//对外接口
    public void insert(T ele) {
        heap.insert(ele, heap.getSize());//将新元素插入至向量末尾
        percolateUp(heap.getSize() - 1);
    }

    public T getMax() { return heap.getArray(0); }

    public T delMax() {
        T removedEle = heap.remove(0);//做备份
        percolateDown(0);
        return removedEle;
    }

    public int getSize(){ return heap.getSize(); }
    public boolean isEmpty(){ return heap.getSize() == 0; }
//======================================================================================================================
//辅助方法
    protected int parent(int rank) { return (rank - 1) >> 1; }
    protected int lChild(int rank) { return 1 + (rank << 1); }
    protected int rChild(int rank) { return (1 + rank) >> 1; }

    protected void percolateUp(int rank){//上滤，上行并以父代子
        T hotEle = heap.getArray(rank); //做备份，并且hotEle保持不变
        int hotRank = rank;             //当前抵达的节点的秩
        T hotParent;                    //当前抵达的节点的父节点
        while (0 < hotRank){            //hotRank大于0保证了有父节点
            hotParent = heap.getArray(parent(hotRank));
            if (hotEle.compareTo(hotParent) <= 0)
                break;                  //若父节点更大，堆序性满足，退出循环
            heap.setArray(hotRank, hotEle);//否则用父节点代替子节点
            hotRank = parent(hotRank);  //并继续循环
        }
        heap.setArray(hotRank, hotEle);
    }

    protected void percolateDown(int rank){//下滤，下行并以子代父
        int hotRank = rank;             //当前抵达的节点的秩
        T hotEle = heap.getArray(rank); //做备份，并且hotEle保持不变
        T lChild; T rChild;             //当前抵达的节点的左右子节点
        int maxChildRank;               //当前抵达的节点的左右子节点中的更大者的秩
        T maxChild;                     //当前抵达的节点的左右子节点中的更大者
        while (rChild(hotRank) < heap.getSize()){//保证有两个子节点
            lChild = heap.getArray(lChild(hotRank));
            rChild = heap.getArray(rChild(hotRank));
            maxChildRank = ( lChild.compareTo(rChild) < 0 ) ? rChild(hotRank) : lChild(hotRank);
            maxChild = heap.getArray(maxChildRank);
            if (maxChild.compareTo(hotEle) <= 0)
                break;                  //若父节点更大，堆序性满足，退出循环
            heap.setArray(hotRank, maxChild);//否则用子节点代替父节点
            hotRank = maxChildRank;     //并继续循环
        }
        if (rChild(hotRank) == heap.getSize()){
            //特殊情况：可能有一个节点仅有左节点而无右节点，此时左节点就是向量的末节点
            lChild = heap.getArray(lChild(hotRank));
            if (hotEle.compareTo(lChild) < 0)
                heap.setArray(hotRank, lChild);
            hotRank = lChild(hotRank);
        }
        heap.setArray(hotRank, hotEle);
    }

    protected void heapify(Vector<T> heap){//批量建堆，自下而上的下滤
        for (int i = heap.getSize()/2 - 1; 0 <= i; i--){
            percolateDown(i);
        }
    }
//======================================================================================================================
//堆排序，以选择排序为基础，将未排序部分组织成堆

//======================================================================================================================
}
