package chapter5.bintree;

import interfaceUsedInDS.BinTree;
import interfaceUsedInDS.BinTreePosition;
import java.util.Iterator;

public class BinTree_LinkedList implements BinTree {
//===============================================================================
//  内容
    protected BinTreeNode root;   //根节点
    protected int size;           //root树的规模
    protected int height;         //root的高度
//===============================================================================
//  构造函数
    //空参构造
    public BinTree_LinkedList() {
        root = new BinTreeNode();
        size = 1;
        height = 0;
    }
    //以一个节点为根节点的构造方法
    public BinTree_LinkedList(BinTreeNode root){
        this.root = root;
        size = 1;
        height = 0;
    }
//===============================================================================
//  接口BinTree中的方法
    public BinTreePosition getRoot() { return root; }
    public boolean isEmpty() { return size <= 1; }
    public int getSize() { return size; }
    public int getHeight() { return height; }
    public Iterator elementPreorder() {
        return root.elementsInorder();
    }
    public Iterator elementInorder() {
        return root.elementsInorder();
    }
    public Iterator elementPostorder() {
        return root.elementsPostorder();
    }
    public Iterator elementLevelorder() {
        return root.elementsLevelorder();
    }
}