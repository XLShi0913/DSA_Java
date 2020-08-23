package chapter8.advancedsearchtree;

import chapter2.vector.Vector;

/*
B树的实现：
1、B树的核心算法是对上溢和下溢的修复算法；
2、B树的核心功能是查找、插入与删除，其中要对插入可能造成的上溢和删除可能造成的下溢进行操作；
3、上溢修复的实现要靠节点的分裂，下溢修复要靠旋转与合并；
 */
public class BTree {
//==================================================================================================================
//基本内容与信息
    protected int size;     //关键码总数
    protected int order;    //阶次
    protected BTNode root;  //根节点
    protected BTNode hot;   //记忆热点，查找最后访问的非空节点位置
    //构造方法中要给定阶次
    public BTree(int order) {
        this.size = 0;
        this.order = order;
        root = new BTNode();
        hot = null;
    }
//==================================================================================================================
//上溢与下溢的处理
    protected void solveOverflow(BTNode point){
        //上溢的处理：分裂，需要对节点进行递归地处理
        int m = point.child.getSize();
        //特殊情况，作为递归基
        if (m <= order) return;
        //否则需要将此节点分裂
        BTNode rc = new BTNode();//key为空，child已有一人
        rc.key = new Vector(point.key, m/2 + 1, m - 2);
        rc.child = new Vector(point.child, m/2 + 1, m - 1);
        rc.parent = point.parent;
        //point即为左孩子，忽略掉一部分元素（改造前要寄存一下要用到的key）
        Object keyUpper = point.key.getArray(m/2);
        point.key.setSize(m/2);
        point.child.setSize(m/2 + 1);
        BTNode p = point.parent;
        if ( p == null ){
            //说明已经分裂到根了，需要重新创建一个节点，并建立必要的联系，使之与一般情况一致
            p = new BTNode();root = p;
            p.child.insert(point, 0);
            point.parent = p;
        }
        int r = 1 + p.key.search( (Comparable)point.key.getArray(0) );//找插入位置
        p.key.insert(keyUpper, r);
        p.child.insert(rc, r + 1);
        rc.parent = p;
        //对父节点递归
        solveOverflow(p);
    }

    protected void solveUnderflow(BTNode point){
        //下溢的处理，旋转 + 合并
        //一号递归基，本节点没有下溢
        int m = point.child.getSize();
        if (m <= order) return;
        //二号递归基，已经操作到根节点了
        BTNode p = point.parent;
        if (p == null) return;
        //递归实体
        int r = 0;
        while ( !p.child.getArray(r).equals(point) ) r++;//查找point在p孩子中的位置r
        if (0 < r){
            //有左兄弟的情况，跟左兄弟进行旋转和合并
            BTNode leftBro = (BTNode)p.child.getArray(r);
            int lBChildSize = leftBro.child.getSize();//左孩子的分支数目
            if ( (order+1)/2 < lBChildSize ){
                //若左兄弟可以分汤喝，则旋转
                point.key.insert(p.key.getArray(r - 1), 0);
                p.key.setArray(r - 1, leftBro.key.remove( lBChildSize - 2 ));
                //除了要改key，还要过继child（有两步：父认子，子认父）
                BTNode adopt = (BTNode) leftBro.child.remove(lBChildSize - 1);
                point.child.insert(adopt,0);
                if (adopt != null) adopt.parent = point;
            }else {
                //左兄弟没汤喝，要跟左兄弟合并成1个节点，并向上递归
                BTNode adopt = (BTNode) p.key.remove(r - 1);
                p.child.remove(r);//把自己在老家的房子拆了，住左兄弟家
                //接下来要复制自己的key和child到左兄弟，child还要改父亲
                leftBro.key.insert(adopt, lBChildSize - 1);
                int counter = 0;
                while (counter < m - 1){
                    leftBro.key.insert(point.key.getArray(counter), counter + lBChildSize);
                    counter++;
                }
                counter = 0;
                while (counter < m){
                    BTNode childOfPoint = (BTNode) point.child.getArray(counter);
                    leftBro.child.insert(childOfPoint, counter + lBChildSize);
                    if (childOfPoint != null) childOfPoint.parent = leftBro;
                    counter++;
                }
                //向上递归
                solveUnderflow(p);
            }
        }else {
            //没左兄弟，就一定有右兄弟
            BTNode rightBro = (BTNode)p.child.getArray(r + 1);
            int rBChildSize = rightBro.child.getSize();//右孩子的分支数目
            if ( (order+1)/2 < rBChildSize ){
                //右兄弟有汤喝，旋转
                point.key.insert(p.key.getArray(r), point.key.getSize());
                p.key.setArray(r, rightBro.key.remove(0));
                BTNode adopt = (BTNode) rightBro.child.remove(0);
                point.child.insert(adopt, point.child.getSize());
                if (adopt != null) adopt.parent = point;
            }
            else {
                //右兄弟没汤，合并，向上递归
                BTNode adopt = (BTNode) p.key.remove(r);
                p.child.remove(r + 1);//把右兄弟在老家的房子拆了，右兄弟住自己家
                //接下来要复制右兄弟的key和child到自己家，child还要改父亲
                point.key.insert(adopt, m - 1);
                int counter = 0;
                while (counter < rBChildSize - 1){
                    point.key.insert(rightBro.key.getArray(counter), counter + m);
                    counter++;
                }
                counter = 0;
                while (counter < rBChildSize){
                    BTNode childOfRB = (BTNode) rightBro.child.getArray(counter);
                    point.child.insert(childOfRB, counter + m);
                    if (childOfRB != null) childOfRB.parent = point;
                    counter++;
                }
                //向上递归
                solveUnderflow(p);
            }
        }
    }
//==================================================================================================================
//查找、插入与删除
    public BTNode search(Comparable key){
        hot = null;//查找之前要将hot赋空
        BTNode point = root;//从根节点开始
        while (point != null){
            int rank = point.key.search(key);
            //若找到了，则返回
            if (0 <= rank && key.equals(point.key.getArray(rank)))
                return point;
            //未找到，沿引用转至下层子树，载入根节点，继续查找
            hot = point;
            point = (BTNode) point.child.getArray(rank + 1);
        }
        return null;
    }

    public boolean insert(Comparable key){
        BTNode point = search(key);
        if (point != null) return false;//待插入节点已经存在，插入失败
        int r = hot.key.search(key);
        hot.key.insert(key, r+1);
        hot.child.insert(null, r+2);
        size++;
        solveOverflow(hot);
        return true;
    }

    public boolean remove(Comparable key){
        BTNode point = search(key);
        if (point == null) return false;//节点不存在，删除失败
        int r = point.key.search(key);
        BTNode adopt = (BTNode) point.child.getArray(r);
        if (adopt != null){
            //待删除节点不是叶节点
            hot = point;//再次调用记忆热点
            while (adopt.child.getArray(0) != null)
                adopt = (BTNode) adopt.child.getArray(0);
            point.key.setArray(r, adopt.key.getArray(0));//留下节点的关键码
            adopt.key.remove(0);//删除节点
            adopt.child.remove(adopt.child.getSize() - 1);
        }else {
            //待删除节点刚好是叶节点\
            point.key.remove(r);
            point.child.remove(point.child.getSize() - 1);
        }
        size--;
        solveUnderflow(hot);
        return true;
    }
}
