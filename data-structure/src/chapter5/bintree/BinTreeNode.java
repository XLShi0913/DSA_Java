package chapter5.bintree;

import chapter3.list.ListOfTwoWay;
import chapter4.queue.Queue;
import chapter4.stack.Stack;
import interfaceUsedInDS.BinTreePosition;
import interfaceUsedInDS.List;
import java.util.Iterator;

/*
1、这是一个一般意义上的二叉树数据结构；
2、算法的核心在于遍历操作，遍历操作中的访问，就是将当前元素置于列表的尾部（insertAsLast）；
3、遍历返回的是一个Iterator类型的变量，默认是从root节点开始遍历，this.next即可返回下一元素；
4、访问操作使用列表而非向量，是因为列表元素的动态操作更高效；
 */

public class BinTreeNode implements BinTreePosition {
//================================================================================================
//  内容
    Object ele;//数据域
    //引用域
    protected BinTreePosition parent;
    protected BinTreePosition lChild;
    protected BinTreePosition rChild;
    //重要指标
    protected int size;//子树规模
    protected int depth;//深度
    protected int height;//子树高度
    protected byte color;//颜色，用于红黑树
//================================================================================================
//  构造函数
    //常规的构造方法
    public BinTreeNode
            (Object ele, BinTreeNode parent, boolean asLChild,
             BinTreeNode lChild, BinTreeNode rChild) {
        this.ele = ele;
        this.parent = null; this.lChild = null; this.rChild = null;
        this.size = 1; this.depth = 0; this.height = 0;
        //建立联系的过程由attach方法实现
        if (parent != null){
            if ((asLChild)) { parent.attachL(this); }
            else { parent.attachR(this); }
        }
        if (lChild != null){ this.attachL(lChild); }
        if (rChild != null){ this.attachR(rChild); }
        //定义红黑树的颜色，新建的红黑树节点颜色默认都是红色，所以实际上这句可以省略，为理解方便，将此句留下
        this.color = RED;
    }
    //空的构造方法，不会建立联系
    public BinTreeNode() {
        this(null, null, true, null, null);
    }
//================================================================================================
//  Position接口的方法
    public Object getEle() { return this.ele; }
    public void setEle(Object ele) { this.ele = ele; }
//================================================================================================
//  BinTreePosition接口的方法
    //判断是否有父节点、获取父节点、将一个节点设置成父节点
    public boolean hasParent() { return parent != null; }
    public BinTreePosition getParent() { return parent; }
    public void setParent(BinTreePosition p) { this.parent = p; }
    //判断是否是叶节点
    public boolean isLeaf() { return (!hasLChild()) && (!hasRChild()); }
    //判断是否是父节点的左子节点、判断是否有左子节点、获取左子节点、将一个节点设置成左子节点
    public boolean isLChild() { return hasParent() && (parent.getLChild() == this); }
    public boolean hasLChild() { return lChild != null; }
    public BinTreePosition getLChild() { return lChild; }
    public void setLChild(BinTreePosition p) { lChild = p; }
    //判断是否是父节点的右子节点、判断是否有右子节点、获取右子节点、将一个节点设置成右子节点
    public boolean isRChild() { return hasParent() && (parent.getLChild() == this); }
    public boolean hasRChild() { return rChild != null; }
    public BinTreePosition getRChild() { return rChild; }
    public void setRChild(BinTreePosition p) { rChild = p; }
    //获取和更新子树规模，向上更新
    public int getSize() { return size; }
    public void updateSize() {
        size = 1;
        if (hasLChild()) size += lChild.getSize();
        if (hasRChild()) size += rChild.getSize();
        if (hasParent()) parent.updateSize();
    }
    //获取和更新高度，向上更新
    public int getHeight() { return height; }
    public void setHeight(int height){ this.height = height; }
    public void updateHeight() {
        height = 0;
        if (hasLChild()) height = Math.max(height, 1 + lChild.getHeight());
        if (hasRChild()) height = Math.max(height, 1 + rChild.getHeight());
        if (hasParent()) parent.updateHeight();
    }
    //获取和更新深度，向下更新
    public int getDepth() { return depth;}
    public void updateDepth() {
        depth = 0;
        if (hasParent()) depth = parent.getDepth() + 1;
        if (hasLChild()) lChild.updateDepth();
        if (hasRChild()) rChild.updateDepth();
    }
    //获取中序遍历意义下的直接前驱和直接后继
    public BinTreePosition getPrev() {
        //有左孩子，当且仅当左子遍历完毕，才会访问到当前节点,否则直接前驱只可能是其祖先
        if(hasLChild()) return findMaxDescendant(this.getLChild());
        //无左孩子，是右孩子，说明parent访问完毕后直接访问到了当前节点
        if (isRChild()) return this.getParent();
        //无左孩子，且是左孩子
        BinTreePosition v= this;//指针
        while (v.isLChild())  v = v.getParent();
        return v.getParent();
    }
    public BinTreePosition getSucc() {
        //有右孩子，下一个是右子树的第一个被访问到的元素，否则直接后继只可能是其祖先
        if (hasRChild()) return findMinDescendant(this.getRChild());
        //无右子树，是左孩子，下一个是parent
        if (isLChild()) return this.getParent();
        //无右子树，是右孩子
        BinTreePosition v = this;
        while (v.isRChild()) v = v.getParent();
        return v.getParent();
    }

    //断绝与当前父节点的父子关系，返回当前节点（注意指标更新的操作次序）
    public BinTreePosition secede() {
        if (isLChild()) parent.setLChild(null);
        else            parent.setRChild(null);
        parent.updateHeight();
        parent.updateSize();
        setParent(null);
        updateDepth();
        return this;
    }
    //将节点l/r作为左子节点/右子节点
    public BinTreePosition attachL(BinTreePosition l) {
        if (hasLChild()) getLChild().secede();//摘除当前节点可能存在的左子节点
        if (l != null){
            l.secede();//断绝l和其父节点的父子关系
            lChild = l; l.setParent(this);
            updateSize();updateHeight();
            l.updateDepth();
        }
        return this;
    }
    public BinTreePosition attachR(BinTreePosition r) {
        if (hasRChild()) getRChild().secede();
        if (r != null){
            r.secede();
            rChild = r; r.setParent(this);
            updateHeight(); updateSize();
            r.updateDepth();
        }
        return this;
    }
    //先序遍历/中序遍历/后序遍历/层次遍历意义下依次访问各个元素
    public Iterator elementsPreorder() {
        List list = new ListOfTwoWay<BinTreePosition>();
        preOrder(list, this);
        return list.elements();
    }
    public Iterator elementsInorder() {
        List list = new ListOfTwoWay<BinTreePosition>();
        inOrder(list, this);
        return list.elements();
    }
    public Iterator elementsPostorder() {
        List list = new ListOfTwoWay<BinTreePosition>();
        postOrder(list, this);
        return list.elements();
    }
    //红黑树判断与修改颜色使用
    public boolean isBlack(){ return color == BLACK; }
    public void setColor(byte color){ this.color = color; }
//================================================================================================
//  接口中未包含的通用方法
    //在v的所有后代中，中序遍历下第一个/最后一个被访问到的节点
    protected static BinTreePosition findMinDescendant(BinTreePosition v){
        if (v != null){
            while (v.hasLChild()) v = v.getLChild();
        }
        return v;
    }
    protected static BinTreePosition findMaxDescendant(BinTreePosition v){
        if (v != null){
            while (v.hasRChild()) v = v.getRChild();
        }
        return v;
    }
    //作为left child插入，前提：this没有left child
    public BinTreePosition insertAsLChild(Object ele){
        return lChild = new
                BinTreeNode(ele, this, true, null, null);
    }//等号前建立了了this里的联系，等号后的构造方法建立了新节点内部的联系

    //作为right child插入，前提：this没有right child
    public BinTreePosition insertAsRChild(Object ele) {
        return rChild = new
                BinTreeNode(ele, this, false, null, null);
    }
//================================================================================================
//  遍历方法
    //先序遍历，返回值为空，对象list在经此方法后依次存储有先序遍历顺序下以p为根子树的所有非null元素
    public void preOrder(List list, BinTreePosition p){
        Stack stack = new Stack();
        while (true){
            visitAlongLeftBranch(p, list, stack);
            if (stack.isEmpty()) break;//pop之后的一次访问左侧链没有向stack中加入新元素，则退出
            p = (BinTreePosition) stack.pop();
        }
    }
    //先序遍历子方法，沿左侧链访问二叉树中元素
    private void visitAlongLeftBranch(BinTreePosition p, List list, Stack stack){
        while (p != null){
            stack.push(p.getRChild());
            list.insertAtLast(p);
            p = p.getLChild();
        }
    }
    //中序遍历，返回值为空，对象list在经此方法后依次存储有先序遍历顺序下以p为根子树的所有非null元素
    public void inOrder(List list, BinTreePosition p){
        Stack stack = new Stack();
        while (true){
            goAlongLeftBranch(p, stack);
            if (stack.isEmpty()) break;
            p = (BinTreePosition) stack.pop();
            list.insertAtLast(p);
            p = p.getRChild();
        }
    }
    //中/后序遍历子方法，沿二叉树左侧链下行
    private void goAlongLeftBranch(BinTreePosition p, Stack stack){
        while (p != null){
            stack.push(p);
            p = p.getLChild();
        }
    }
    //后序遍历，返回值为空，对象list在经此方法后依次存储有先序遍历顺序下以p为根子树的所有非null元素
    public void postOrder(List list, BinTreePosition p){
        BinTreePosition pOri = p;
        Stack stack = new Stack();
        while (true){
            goAlongLeftBranch(p, stack);
            if (stack.isEmpty()) break;
            p = ((BinTreePosition) stack.top()).getRChild();
            if (p == null){
                //访问栈顶元素的时机已到，同时访问右侧链
                visitAlongRightBranch(pOri, list, stack);
                p = ((BinTreePosition) stack.top()).getRChild();
            }
        }
    }
    //后序遍历子方法，沿二叉树右侧链访问
    public void visitAlongRightBranch(BinTreePosition pOri, List list, Stack stack){
        BinTreePosition position = (BinTreePosition)stack.top();
        if ( position.isRChild() && position != pOri){//或者直接将pOri定义成左子?
            list.insertAtLast(stack.pop());
        }
        list.insertAtLast(stack.pop());
    }
    //层次遍历
    public void levelOrder(List list, BinTreePosition p){
        Queue<BinTreePosition> q = new Queue<>();
        q.enqueue(p);
        while (! q.empty()){
            BinTreePosition x = q.dequeue();
            if (x.hasLChild()) q.enqueue(x.getLChild());
            if (x.hasRChild()) q.enqueue(x.getRChild());
        }
    }
    public Iterator elementsLevelorder() {
        List list = new ListOfTwoWay<BinTreePosition>();
        levelOrder(list, this);
        return list.elements();
    }
//================================================================================================
}