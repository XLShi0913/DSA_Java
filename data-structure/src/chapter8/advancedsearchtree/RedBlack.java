package chapter8.advancedsearchtree;

import chapter7.binsearchtree.BSTree;
import chapter7.binsearchtree.BSTreeNode;
import interfaceUsedInDS.BinTreePosition;

/*
红黑树：
1、红黑树可以实现在插入和删除操作中拓扑结构变化量都为O(1)；
2、其逻辑结构上，与4阶B树等价，常常需要借助4阶B树进行重构原理上的理解；
3、其在数据结构上的特点，在于可以通过染色替代一部分拓扑结构的变化；
4、其核心算法为双红缺陷和双黑缺陷的处理；
5、其查找操作与BST完全一致，需要重写的是插入操作、删除操作；
6、其更新高度的操作也需要重写，因为红黑树的高度指的是黑高度；
 */

public class RedBlack extends BSTree {
    //空参构造
    public RedBlack() { }

//==================================================================================================================
//核心算法：双红缺陷和双黑缺陷的修正
    protected void solveDoubleRed(BinTreePosition x) {
        //双红缺陷的修正
        BinTreePosition p = x.getParent();
        if (p == null){
            //x父节点为空，说明已经到达根节点，需要强行把x染黑，然后退出
            x.setColor(BLACK);
            return;
        }
        if (p.isBlack()) return;//p为黑，双红缺陷解除，退出
        BinTreePosition g = p.getParent();//x的祖父，由于p为红，故必存在且为黑
        BinTreePosition u = (p.isLChild()) ? g.getRChild() : g.getLChild();//x的叔父
        //以下分两种情况进行修复
        if (u == null || u.isBlack()) {
            //RR-1：u为黑，需要进行局部的3+4重构及染色，发生拓扑结构变换，完成后退出
            rotateAt(x);
        }else {
            //RR-2：u为红，对应于B树发生上溢的情况，不进行拓扑结构变换，但是可能产生双红缺陷的向上传播
            p.setColor(BLACK);//p染黑
            g.setColor(RED);//g染红
            solveDoubleRed(g);
        }
    }

    protected void solveDoubleBlack(BinTreePosition r){
        //对双黑缺陷的修正，待删除节点和替代节点均为黑的情况，发生下溢
        BinTreePosition p = r.getParent();
        if (p == null) return;//r已经到根节点了，不需要进行操作（相当于B树变矮）
        BinTreePosition s = (r.isLChild()) ? p.getRChild() : p.getLChild();//注意：s必然存在，否则删除前的规则4不满足
        if (s.isBlack()){
            BinTreePosition t = null;
            if ( (s.getLChild() != null && !s.getLChild().isBlack()) ) t = s.getLChild();
            else if ( (s.getRChild() != null && !s.getRChild().isBlack()) ) t = s.getRChild();
            if (t != null){
                //BB-1：s为黑且至少有一个红孩子t
                byte oldColor = (p.isBlack()) ? BLACK : RED;
                BinTreePosition b = rotateAt(t);
                if (b.hasLChild()) {
                    b.getLChild().setColor(BLACK);
                    updateHeight(b.getLChild());
                }
                if (b.hasRChild()) {
                    b.getRChild().setColor(BLACK);
                    updateHeight(b.getRChild());
                }
                b.setColor(oldColor); updateHeight(b);
            }else {
                //BB-2：s为黑且两子皆黑
                s.setColor(RED);
                s.setHeight(s.getHeight() - 1);
                if ( !p.isBlack() ){
                    //BB-2B：s为黑且有两个黑子，同时p为红，不会下溢传播，因为p所在B树节点必然还有另一个黑节点
                    p.setColor(BLACK);
                }else {
                    //BB-2B：s为黑且有两个黑子，同时p为黑，发生向上传播的情况
                    p.setHeight(p.getHeight() - 1);//局部的黑高度-1
                    solveDoubleBlack(p);
                }
            }
        }else {
            //BB-3：s为红，作以染色+单旋，（大家黑高度不变）化归为BB-1或BB-2R
            s.setColor(BLACK); p.setColor(RED);
            s.setParent(p.getParent());
            p.setParent(s);
            if (s.isLChild()){
                BinTreePosition t = s.getRChild();
                p.setLChild(t);
                if (t != null) t.setParent(p);
                s.setRChild(p);
            }else {
                BinTreePosition t = s.getLChild();
                p.setRChild(t);
                if (t != null) t.setParent(p);
                s.setLChild(p);
            }
            solveDoubleBlack(r);
        }
    }
//==================================================================================================================
//必要的辅助方法
    //进行3+4重构
    protected BinTreePosition connect34(
            char whichIsG, BinTreePosition a, BinTreePosition b, BinTreePosition c,
            BinTreePosition T0, BinTreePosition T1, BinTreePosition T2, BinTreePosition T3) {
        //染色
        a.setColor((byte) 0);b.setColor((byte) 1);c.setColor((byte) 0);
        //先建立向上的联系
        BinTreePosition parentOfAll;//一个指针，表示3+4的共同父亲
        switch (whichIsG) {
            case 'a':
                parentOfAll = a.getParent();
                b.setParent(parentOfAll);
                if (parentOfAll != null) reConnectWithParent(parentOfAll, a, b);
                break;
            case 'c':
                parentOfAll = c.getParent();
                b.setParent(parentOfAll);
                if (parentOfAll != null) reConnectWithParent(parentOfAll, c, b);
                break;
        }
        //再建立3+4内部的联系
        a.setLChild(T0);
        if (T0 != null) T0.setParent(a);a.setRChild(T1);
        if (T1 != null) T1.setParent(a);c.setLChild(T2);
        if (T2 != null) T2.setParent(c);
        c.setRChild(T3);if (T3 != null) T3.setParent(c);
        b.setLChild(a);a.setParent(b);
        b.setRChild(c);c.setParent(b);
        //更新高度
        ((BSTreeNode) a).updateHeightSingle();
        ((BSTreeNode) b).updateHeightSingle();
        ((BSTreeNode) c).updateHeightSingle();
        return b;
    }

    //进行旋转操作
    protected BinTreePosition rotateAt(BinTreePosition v) {
        BinTreePosition p = v.getParent();
        BinTreePosition g = p.getParent();
        if (p.isLChild()){
            if (v.isLChild())
                return connect34('c', v, p, g, v.getLChild(), v.getRChild(), p.getRChild(), g.getRChild());
            else
                return connect34('c', p, v, g, p.getLChild(), v.getLChild(), v.getRChild(), g.getRChild());
        }else {
            if (v.isLChild())
                return connect34('a', g, v, p, g.getLChild(), v.getLChild(), v.getRChild(), p.getRChild());
            else
                return connect34('a', g, p, v, g.getLChild(), p.getLChild(), v.getLChild(), v.getRChild());
        }
    }

    public void updateHeight(BinTreePosition b){
        int height = Math.max(b.getLChild().getHeight(), b.getRChild().getHeight());
        if (b.isBlack()) b.setHeight(height + 1);
        else b.setHeight(height);
    }
}
