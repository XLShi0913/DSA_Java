package chapter8.advancedsearchtree;

import chapter5.bintree.BinTreeNode;
import chapter7.binsearchtree.BSTree;
import chapter7.binsearchtree.BSTreeNode;
import chapter7.binsearchtree.Entry;
import interfaceUsedInDS.BinTreePosition;

/*
伸展树：
1、基于二叉搜索树建立；
2、与二叉搜索树不同的，有自适应调整的功能；
3、需要构建伸展算法，并重写查找、插入、删除算法；
4、适用于局部性强、对效率敏感的场和；
5、分摊复杂度：O(logn)，但是不保证最坏情况不会出现；
 */

public class SplayTree extends BSTree {
//=====================================================================================
//构造方法
    public SplayTree() { }
    public SplayTree(BSTreeNode root) {
        super(root);
    }
//=====================================================================================
//辅助方法
    //方便起见，重写的attachL和attachR方法，因为伸展树不需要高度、深度等指标，将这部分去掉
    // （原方法在BinTreeNode里，严格说不能叫重写）
    protected void attachL(BinTreePosition parent, BinTreePosition lChild){
        //这里边的孩子是可能为空的，但是parent不可能为空，也不用断绝父子关系，因为自然有人去管
        //实际就是建立两个关系
        parent.setLChild(lChild);
        if (lChild != null) lChild.setParent(parent);
    }
    protected void attachR(BinTreePosition parent, BinTreePosition rChild){
        parent.setRChild(rChild);
        if (rChild != null) rChild.setParent(parent);
    }
//=====================================================================================
//伸展算法
    protected BinTreePosition splay(BinTreePosition v){
        if (v == null) return null;
        while (true){
            //v的父节点各祖父节点都非空时，执行双层伸展
            BinTreePosition p = v.getParent();
            if (p == null) break;
            BinTreePosition g = p.getParent();
            if (g == null) break;
            BinTreePosition gg = g.getParent();
            //v p g之间的联系，分了四种情况，有点像3+4重构，但形式有区别
            if (v.isLChild()){
                if (p.isLChild()){
                    BinTreePosition x = v.getRChild();
                    BinTreePosition y = p.getRChild();
                    attachR(v, p);attachL(p, x);attachR(p, g);attachR(g, y);
                }else {
                    BinTreePosition x = v.getLChild();
                    BinTreePosition y = v.getRChild();
                    attachL(v, g);attachR(v, p);attachR(g, x);attachL(p, y);
                }
            }else {
                if (p.isLChild()){
                    BinTreePosition x = v.getLChild();
                    BinTreePosition y = v.getRChild();
                    attachL(v, p);attachR(v, g);attachR(p, x);attachL(g, y);
                }else {
                    BinTreePosition x = p.getLChild();
                    BinTreePosition y = v.getLChild();
                    attachL(v, p);attachR(p, y);attachL(p, g);attachR(g, x);
                }
            }
            //v向上的联系
            if (gg != null) reConnectWithParent(gg, g, v);
            else v.setParent(null);//v最后会到达树根，所以不需要设置v的父亲
        }
        //双层伸展结束后，g必然为空，然而p却可能非空，此时需要做一次额外的单旋
        BinTreePosition p = v.getParent();
        if (p != null){
            //此时v调整后就是根节点了，其父节点必然为空，最后再设置
            if (v.isLChild()){
                BinTreePosition y = v.getRChild();
                attachR(v, p);attachL(p ,y);
            }else {
                BinTreePosition y = v.getLChild();
                attachL(v, p);attachR(p ,y);
            }
        }
        v.setParent(null);
        return v;
    }
//=====================================================================================
//重写查找、插入、删除
    @Override
    public BinTreePosition search(Comparable key) {
        //在查找之前将hot初始化
        hot = null;
        BinTreePosition p = searchIn(root, key);
        if (p == null) root = hot;
        else root = (BinTreeNode) p;
        return root;
    }

    @Override
    public BinTreePosition insert(Comparable key, Object value) {
        BinTreePosition x  = search(key);
        if (x == null){
            Entry entry = new Entry(key, value);
            //向下的联系由新建节点完成
            x = new BinTreeNode(entry, null, true,
                    hot, (BinTreeNode) hot.getRChild());
            //建立向上的联系
            hot.setParent(x);
            if (hot.getRChild() != null) hot.getRChild().setParent(x);
        }
        return x;
    }

    @Override
    public boolean remove(Comparable key) {
        BinTreePosition x = search(key);
        if (x == null) return false;//无节点可删，返回false
        BinTreePosition m = x.getSucc();
        if (m == null){
            m = x.getPrev();
            if (m == null) return false;//即将删除的节点是二叉树中唯一的节点
        }
        //接下来要把m推到x的位置上，直接将m的内容给x，并将m父亲处的引用设空即可
        x.setEle(m.getEle());
        if (m.isLChild()) m.getParent().setLChild(null);
        else            m.getParent().setRChild(null);
        return true;
    }
//=====================================================================================
}