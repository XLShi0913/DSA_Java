package chapter7.binsearchtree;

import chapter5.bintree.BinTreeNode;

/*
二叉搜索树的节点类型：
1、继承了二叉树的节点类型；
2、与二叉树节点类型唯一的不同之处在于BST节点的内容是Entry类型，可以实现循关键码访问；
3、要注意的是，BinTre节点给出了插入方法，直接插入作为左子/右子，BST的实际插入需要判断key，不可直接插入；
4、需要对更新高度方法进行一点扩展；
 */

public class BSTreeNode extends BinTreeNode {
//====================================================================================
//构造方法
    public BSTreeNode() { super(); }

    public BSTreeNode(Entry ele, BinTreeNode parent, boolean asLChild,
                      BinTreeNode lChild, BinTreeNode rChild) {
        super(ele, parent, asLChild, lChild, rChild);
    }
//====================================================================================
//词条相关的方法
    public Object getKey(){
        return ((Entry)getEle()).getKey();
    }
    public void setKey(Comparable k){
        ((Entry)getEle()).setKey(k);
    }
    public Object getValue(){
        return ((Entry)getEle()).getValue();
    }
    public void setValue(Object value){
        ((Entry)getEle()).setValue(value);
    }
//====================================================================================
//父类方法的语义调整
    //更新高度的方法
    public void updateHeightSingle(){
        //仅更新该节点的高度
        height = 0;
        if (hasLChild()) height = Math.max(height, 1 + lChild.getHeight());
        if (hasRChild()) height = Math.max(height, 1 + rChild.getHeight());
    }
//====================================================================================
}