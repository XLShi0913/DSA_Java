package chapter8.advancedsearchtree;

import chapter2.vector.Vector;
import interfaceUsedInDS.BinTreePosition;

/*
B树的节点
1、核心内容是两个向量，分别存储有关键码，以及孩子；
2、本节点中的key存储的是关键码，实质上关键码和词条是等价的（词条的比较就是关键码的比较，套娃）；
3、child向量中存储的类型是BTNode；
4、此外还有父节点(BTNode)的引用地址；
 */
public class BTNode {
    BTNode parent;
    Vector key;//用于存放关键码的向量
    Vector child;//用于存放孩子的向量，长度总比key多1
    //构造一个空的B树节点（空节点也有一个孩子）
    public BTNode() {
        parent = null;
        child.insert(null, 0);//要插入一个空孩子
    }
    //构造一个仅有一个关键码的节点（根节点），并指定两个孩子
    public BTNode(Comparable key, BTNode lc, BTNode rc){
        parent = null;
        this.key.insert(key, 0);
        child.insert(lc, 0);
        child.insert(rc, 1);
        if (lc != null) lc.parent = this;
        if (rc != null) rc.parent = this;
    }
}
