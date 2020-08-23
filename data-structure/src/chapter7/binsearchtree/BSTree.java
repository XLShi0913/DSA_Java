package chapter7.binsearchtree;

import chapter5.bintree.BinTree_LinkedList;
import interfaceUsedInDS.BinTreePosition;

/*
二叉搜索树：
1、初衷是同时实现高效的动态、静态操作；
2、已实现的子类：AVL树、伸展树、B树、红黑树；
3、实现了BST基本的查找、插入与删除操作，节点类型为支持循关键码访问的BSTreeNode；
4、与二叉树的不同：严格有序的中序遍历序列，这要求插入和删除操作需要有一定技巧；
 */
public class BSTree extends BinTree_LinkedList {
    protected BSTreeNode hot;//记忆热点，在每次查找后更新
//===============================================================================
//  构造函数
    public BSTree() { super(); }
    public BSTree(BSTreeNode root) { super(root); }
//===============================================================================
//辅助方法
    //重新认一个儿子的方法，只管建立一个关系
    protected void reConnectWithParent(
            BinTreePosition parent, BinTreePosition childOri, BinTreePosition childNew){
        if (childOri.isLChild()){
            parent.setLChild(childNew);
        }else {
            parent.setRChild(childNew);
        }
    }
//===============================================================================
//查找、插入与删除
    //依据关键码的查找，输出为null时表示查找失败，而无论查找成功或失败，hot始终指向最终节点的父亲
    public BinTreePosition searchIn(
            BinTreePosition v, Comparable key){
        //v直接命中结果的情况，此时hot无意义，默认为空
        if ( v == null || key.compareTo( ((Entry)v.getEle()).getKey() ) == 0 )
            return v;
        hot = (BSTreeNode) v;
        while (true){
            BinTreePosition c = ( key.compareTo( ((Entry)v.getEle()).getKey() ) < 0 ) ?
                    hot.getLChild() : hot.getRChild();
            if ( c == null || key.compareTo( ((Entry)c.getEle()).getKey() ) == 0 )
                return c;
            hot = (BSTreeNode) c;
        }
    }
    public BinTreePosition search(Comparable key){
        //返回查找的结果，并更新hot，hot在每次查找开始时赋空，在下次查找之前保持不变
        hot = null;
        return searchIn(getRoot(), key);
    }

    //基于关键码查找的节点插入
    public BinTreePosition insert(Comparable key, Object value){
        BinTreePosition x = search(key);
        if (x == null){
            //x不存在时，才进行实质上的插入
            Entry entry = new Entry(key, value);
            //判断下插在hot左边还是右边
            boolean isLeftChild = key.compareTo( ((Entry)hot.getEle()).getKey() ) < 0 ;
            x = new BSTreeNode(entry, hot, isLeftChild, null, null);
            size++;
            x.updateHeight();
        }
        return x;
    }
    //基于关键码查找的节点删除
    public boolean remove(Comparable key){
        BinTreePosition x = search(key);
        if (x == null) return false;
        removeAt(x);
        size--;
        hot.updateHeight();
        return true;
    }
    //节点删除的子方法，也是实际删除的方法，分两种情况
    protected BinTreePosition removeAt(BinTreePosition x) {
        BinTreePosition w = x;//实际被删除的节点
        BinTreePosition succ = null;//实际被删节点的接替者
        //第一种情况：x的左右子树至少有一个为空
        if ( !x.hasLChild() ){
            succ = x.getRChild();
        }else if ( !x.hasRChild() ){
            succ = x.getLChild();
        }else {/*第二种情况：左右子树并存*/
            w = w.getSucc();//中序遍历次序下的后继，由于有右子树，其后继必然在右子树中
            Object temp = x.getEle();
            x.setEle(w.getEle());
            w.setEle(temp);//交换两个节点的内容，此时删除w也就删除了要求的key
            succ = w.getRChild();//w的右孩子替代了w的位置
        }
        //建立联系
        hot = (BSTreeNode) w.getParent();
        reConnectWithParent(hot, w, succ);
        if (succ != null){
            succ.setParent(hot);
        }
        return succ;
    }
}