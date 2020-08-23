package chapter7.binsearchtree;

import interfaceUsedInDS.BinTreePosition;
/*
二叉搜索树——AVL树：
1、初衷是通过定义一种平衡，控制树的高度，从而降低插入、删除操作的时间复杂度（更新高度）；
2、节点类型是BSTreeNode；
3、重点定义了平衡判别标准和重平衡方法，并在插入、删除后添加重平衡方法；
 */
public class AVLTree extends BSTree{
//======================================================================================================================
//  构造函数
    public AVLTree() {
    }
    public AVLTree(BSTreeNode hot) {
        super(hot);
    }
//======================================================================================================================
//  平衡判别方法，是否绝对平衡、计算平衡因子、是否AVL意义平衡
    public boolean isBalanced(BinTreePosition x){
        return x.getLChild().getHeight() == x.getRChild().getHeight();
    }
    public int balFac(BinTreePosition x){
        return x.getLChild().getHeight() - x.getRChild().getHeight();
    }
    public boolean isAVLBalanced(BinTreePosition x){
        return -2 < balFac(x) && balFac(x) < 2;
    }
//======================================================================================================================
//  需要重写插入、删除方法
    @Override
    public BinTreePosition insert(Comparable key, Object value) {
        //下面是常规的插入操作
        BinTreePosition x = search(key);
        if (x != null) return x;
        Entry entry = new Entry(key, value);
        boolean isLeftChild = key.compareTo( ((Entry)hot.getEle()).getKey() ) < 0 ;
        x = new BSTreeNode(entry, hot, isLeftChild, null, null);
        size++;
        //第一个可能失衡的节点为hot
        BinTreePosition g = hot;
        while (g != null){
            //一旦发现g失衡了，随即进行旋转，并退出循环，旋转后的高度不变，故不需要更新高度
            if ( !isAVLBalanced(g) ){
                rotateAt(tallerChild( tallerChild(g) ));
                break;
            }
            ( (BSTreeNode)g ).updateHeightSingle();//仅更新g一个点的高度
            g = g.getParent();
        }
        return x;
    }
    @Override
    public boolean remove(Comparable key) {
        BinTreePosition x = search(key);
        if (x == null) return false;
        removeAt(x);
        size--;
        //hot及其祖先可能会失衡，需要依次检查并调整
        BinTreePosition g = hot;
        while ( g != null ){
            if ( !isAVLBalanced(g) ){
                rotateAt( tallerChild(tallerChild(g)) );
            }
            ( (BSTreeNode)g ).updateHeightSingle();//仅更新g一个点的高度
            g = g.getParent();
        }
        return true;
    }
//======================================================================================================================
//  辅助方法
    //返回高度更大的孩子
    protected BinTreePosition tallerChild(BinTreePosition g){
        //特殊情况的处理
        if (g == null) return null;
        if ( !g.hasLChild() ) return g.getRChild();
        if ( !g.hasRChild() ) return g.getLChild();
        //非特殊情况：g的两颗子树都在
        return (g.getLChild().getHeight() < g.getRChild().getHeight()) ?
                g.getRChild() : g.getLChild();
    }

    //进行旋转操作
    protected void rotateAt(BinTreePosition v) {
        BinTreePosition p = v.getParent();
        BinTreePosition g = p.getParent();
        if (p.isLChild()){
            if (v.isLChild())
                connect34('c', v, p, g, v.getLChild(), v.getRChild(), p.getRChild(), g.getRChild());
            else
                connect34('c', p, v, g, p.getLChild(), v.getLChild(), v.getRChild(), g.getRChild());
        }else {
            if (v.isLChild())
                connect34('a', g, v, p, g.getLChild(), v.getLChild(), v.getRChild(), p.getRChild());
            else
                connect34('a', g, p, v, g.getLChild(), p.getLChild(), v.getLChild(), v.getRChild());
        }
    }

    //进行3+4重构
    protected BinTreePosition connect34(
            char whichIsG, BinTreePosition a, BinTreePosition b, BinTreePosition c,
            BinTreePosition T0, BinTreePosition T1, BinTreePosition T2, BinTreePosition T3){
        //先建立向上的联系
        BinTreePosition parentOfAll;//一个指针，表示3+4的共同父亲
        switch (whichIsG){
            case 'a':
                parentOfAll = a.getParent();
                b.setParent( parentOfAll );
                if (parentOfAll != null) reConnectWithParent(parentOfAll, a, b);
                break;
            case 'c':
                parentOfAll = c.getParent();
                b.setParent( parentOfAll );
                if (parentOfAll != null) reConnectWithParent(parentOfAll, c, b);
                break;
        }
        //再建立3+4内部的联系
        a.setLChild(T0); if (T0 != null) T0.setParent(a);
        a.setRChild(T1); if (T1 != null) T1.setParent(a);
        c.setLChild(T2); if (T2 != null) T2.setParent(c);
        c.setRChild(T3); if (T3 != null) T3.setParent(c);
        b.setLChild(a); a.setParent(b);
        b.setRChild(c); c.setParent(b);
        //更新高度
        ((BSTreeNode) a).updateHeightSingle();
        ((BSTreeNode) b).updateHeightSingle();
        ((BSTreeNode) c).updateHeightSingle();
        return b;
    }
}