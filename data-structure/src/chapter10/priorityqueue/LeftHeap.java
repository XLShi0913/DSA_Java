package chapter10.priorityqueue;

import chapter5.bintree.BinTree_LinkedList;
import chapter7.binsearchtree.BSTree;
import interfaceUsedInDS.BinTreePosition;
import interfaceUsedInDS.PQ;

/*
借助左式堆对优先级队列的实现
1、借助指标npl（到空节点的最近距离）来保证堆结构整体的左倾；
2、由于失去了结构性，不可以再使用向量作为底层，需要使用二叉树BST；
3、其核心体现在堆合并的算法上，插入与删除都是合并的应用；
 */
public class LeftHeap<T> implements PQ<T> {
    BinTree_LinkedList heap;//左式堆的底层是一个二叉树

    public LeftHeap(BSTree heap) {
        this.heap = heap;
    }
//===================================================================================
//辅助方法
    protected int npl(BinTreePosition node){
        if (node == null) return 0;//递归基，空节点npl为0
        return Math.max(npl(node.getLChild()), npl(node.getRChild()));//返回最大者
    }

    protected BinTreePosition merge(BinTreePosition a, BinTreePosition b){
        BinTreePosition temp;
        if (a == null) return b;
        if (b == null) return a;//递归基
        if (( (Comparable)(a.getEle()) ).compareTo(b.getEle()) < 0) {
            temp = a; a = b; b = temp;//交换位置，确保大小关系
        }
        a.setRChild(merge(a.getRChild(), b));
        a.getRChild().setParent(a);
        if (a.getLChild() == null || npl(a.getLChild()) < npl(a.getRChild()) ){
            temp = a.getLChild();
            a.setLChild(a.getRChild());
            a.setRChild(temp);//交换位置，确保左倾性质
        }
        return a;
    }
//===================================================================================
//对外接口
    public void insert(T ele) {
    }

    public T getMax() {
        return null;
    }

    public T delMax() {
        return null;
    }
}
