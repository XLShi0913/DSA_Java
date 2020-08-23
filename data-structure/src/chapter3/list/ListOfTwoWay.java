package chapter3.list;

import exception.BoundaryViolationException;
import exception.IncomparableException;
import exception.ListEmptyException;
import exception.PositionInvalidException;
import interfaceUsedInDS.List;
import interfaceUsedInDS.Position;
import itertor.IteratorElement;
import itertor.IteratorPosition;
import java.util.Iterator;
/*=================================================================================================
1、列表实质上是给每个元素搭建一个联系；
2、这一章介绍的列表是前后单链表（聚乙烯）；
3、从封装的角度看，有两层封装关系：node封装了position position封装了element，其中节点可以理解成多聚物的一个链节，
    不仅仅有一个链节里的物质，还包括了与前驱/后继链节的链接方法；
4、真正需要的是element，对外的使用（读取与返回）用position，因为position仅有读取和设定element的方法,
    而程序中的操作需要借助node，因为node操作方法更多（返回简单的，使用复杂的）；
5、查找、排序的方法需要类型T重写equals方法、实现comparable接口。
 ================================================================================================*/
public class ListOfTwoWay<T> implements List{

    private int size;
    private final ListNode<T> header;
    private final ListNode<T> trailer;
//==================================================================================================
//    一些必须的前置方法
    //    默认构造函数
    public ListOfTwoWay() {
        header = new ListNode<>();
        trailer = new ListNode<>(null, header, null);
        header.setNext(trailer);
        this.size = 0;
    }
    //    基于复制的构造，p及p的n-1个后继
    public ListOfTwoWay(Position p, int n){
        this.size = n;
        header = new ListNode<>();
        trailer = new ListNode<>();
        int rank = 0;
        ListNode<T> node = turnToNode(p);//开始时的节点
        while (rank < n){
            insertBefore(trailer, node.getEle());
            //仅仅是调取node中的元素，并没有对node的连接关系进行改动
            node = node.getPrev();
            rank++;
        }
    }
    //将位置转化为一个节点，对外的读入读出时用Position，内部的操作使用Node（Node转型成Position可以自动转）
    public ListNode<T> turnToNode(Position p){
        if (p == null){ throw new PositionInvalidException("异常：传递给节点的位置为空"); }
        //if (p == header){ throw new PositionInvalidException("异常：头节点哨兵位置非法"); }
        //if (p == trailer){ throw new PositionInvalidException("异常：尾节点哨兵位置非法"); }
        if (p instanceof ListNode){
            return (ListNode<T>) p;
        }else {
            throw new ClassCastException("类型转换错误");
        }
    }
//==================================================================================================
//    来自List通用接口的方法
    //    返回列表的元素个数
    public int getSize() { return this.size; }
    //    判断列表是否为空
    public boolean isEmpty() { return this.size == 0; }
    //    返回首元素的位置
    public Position first() throws ListEmptyException {
        if (isEmpty()){ throw new ListEmptyException("异常：无法获取首元素，因为列表为空"); }
        return header.getNext();/*向上转型*/
    }
    //    返回末元素的位置
    public Position last() throws ListEmptyException{
        if (isEmpty()){ throw new ListEmptyException("异常：无法获取末元素，因为列表为空"); }
        return trailer.getPrev();
    }
    //    返回p处元素的后继
    public Position getNext(Position p)
            throws PositionInvalidException, BoundaryViolationException {
        ListNode<T> nextNode = turnToNode(p).getNext();
        if (nextNode == trailer){
            throw new BoundaryViolationException("异常：已经到达末元素，无法获取后继");
        }
        return nextNode;
    }
    //    返回p处元素的前驱
    public Position getPrev(Position p)
            throws PositionInvalidException, BoundaryViolationException {
        ListNode<T> prevNode = turnToNode(p).getPrev();
        if (prevNode == header){
            throw new BoundaryViolationException("异常：已经到达首元素，无法获取前驱");
        }
        return prevNode;
    }
    //    在p处元素的后方插入一个元素，其内容为e
    public Position insertAfter(Position p, Object e)
            throws PositionInvalidException {
        ListNode<T> nodeOld = turnToNode(p);
        Position pNext = nodeOld.insertAsNext((T) e);
        this.size++;
        return pNext;
    }
    //    在p处元素的前方插入一个元素，其内容为e
    public Position insertBefore(Position p, Object e)
            throws PositionInvalidException {
        ListNode<T> nodeOld = turnToNode(p);
        Position pPrev = nodeOld.insertAsPrev((T) e);
        this.size++;
        return pPrev;
    }
    //在首位置插入一个元素
    public Position insertAtFirst(Object e){ return this.insertAfter(header, e); }
    //在末位置插入一个元素
    public Position insertAtLast(Object e){ return this.insertBefore(trailer, e); }
    //    将p处元素移出列表
    public Object remove(Position p)
            throws PositionInvalidException {
        ListNode<T> delNode = turnToNode(p);
        //修改联系，依然是4个，注意最后修改delNode的
        delNode.getPrev().setNext(delNode.getNext());//把前驱的后继设置成当前后继
        delNode.getNext().setPrev(delNode.getPrev());//把后继的前驱设置成当前前驱
        delNode.setNext(null);delNode.setPrev(null);//删除前驱、后继，delNode社会性死亡
        this.size--;
        return delNode.getEle();
    }
    //    将p处元素的内容替换成e
    public Object replace(Position p, Object e)
            throws PositionInvalidException {
        p.setEle(e);
        return e;
    }
    //    位置迭代器
    public Iterator positions() { return new IteratorPosition(this); }
    //    元素迭代器
    public Iterator elements() { return new IteratorElement(this); }
//==================================================================================================
//    给出一些列表的扩展操作
    //    用于获取首位置和末位置
    public Position getFirst(){ return header.getNext(); }
    public Position getLast(){ return trailer.getPrev(); }

    //    此方法用于循秩访问，效率低下，偶尔为之不可常用
    public T callByRank(int rank){
        int i = 0;
        ListNode<T> node = header;
        while (i <= rank){
            node = node.getNext();
            i++;
        }
        return node.getEle();
    }

    //    此方法用于无序列表的查找，以p为基准，查找前驱的n个元素，返回出现重复的position
    public Position find(T ele, int n, Position p){
        ListNode<T> node = turnToNode(p);
        int rank = 0;//计数器
        while (rank < n){
            if (node.getEle().equals(ele)){
                return node;
            }
            node = node.getPrev();
            rank++;
        }
        return null;//表示没找到
    }

    //此方法也用于无序列表的查找，以p为基准，在直至首元素的范围内进行查找，返回出现重复的position
    public Position find(T ele, Position p){
        ListNode<T> node = turnToNode(p);
        while (node != header){
            if (node.getEle().equals(ele)){
                return node;
            }
            node = node.getPrev();
        }
        return null;//表示没找到
    }

    //    此方法用于析构（清空列表），返回清空前列表长度
    public int clear(){
        int oldSize = this.size;
        if (header.getNext() != trailer){
            remove(header.getNext());
        }
        //this.size = 0;//也会自动减为0，可不写
        return oldSize;
    }

    //    此方法用于无序列表的唯一化，返回删除元素的总数
    public int deduplicate(){
        int oldSize = this.size;
        if (this.size <= 1){ return this.size; }
        ListNode<T> node = header.getNext().getNext();//从节点1开始检查
        while (node != trailer){
            Position pAnswer = find(node.getEle(), node.getPrev());
            //注意：要从node的前驱节点开始找，否则找到的永远是自己的位置
            if (pAnswer != null){ remove(pAnswer); }//表示在node的前驱中有重复元素
            node = node.getNext();//node后移一位
        }
        return oldSize - this.size;
    }

    //    此方法用于有序列表的唯一化，返回删除元素的总数
    public int uniquify(){
        int oldSize = this.size;
        if (this.size <= 1){ return 0; }
        ListNode<T> nodeP = header.getNext();//nodeP是第一个节点
        ListNode<T> nodeQ = nodeP.getNext();
        while (nodeQ != trailer){
            nodeQ = nodeP.getNext();
            if (!nodeQ.equals(nodeP)){
                nodeP = nodeQ;
            }else {
                remove(nodeQ);
            }
            //q不等于q的情况，将q的地址值传给p，若等于，删除节点q
        }
        return oldSize - this.size;
    }

    //    此方法用于有序向量的查找，在从p向前的n个前驱（共n个点，包括p），返回不大于ele的最后一个元素
    public Position search(T ele, int n, Position p){
        //进入方法前需要判断数据类型T是否继承了Comparable接口
        if ( ! (ele instanceof Comparable) ){
            throw new IncomparableException("异常：无法比较，列表的数据类型没有实现Comparable接口");
        }
        ListNode<T> node = turnToNode(p);//开始的node在p处
        int rank = 0;//计数器
        while (rank < n){
            if (((Comparable) node.getEle()).compareTo(ele) <= 0){
                //node的数据不大于ele，则将之返回
                return node;
            }
            node = node.getPrev();//node指向其前驱，赋值的是地址
            rank++;
        }
        return node;//对应于ele比该区间内所有元素都小的情况
    }

    //    此方法用于列表的排序，使用选择排序的方法，对起始于位置p的n个元素进行排序
    public void selectSort(Position p, int n){
        if (this.size <= 1){ return; }//仅1个元素和没元素时不需要排序
        ListNode<T> node = turnToNode(p);
        ListNode<T> head = node.getPrev();//待排序区间的首
        ListNode<T> tail = node;
        for (int i = 0; i < n; i++) { tail = tail.getNext(); }//待排序区间的尾
        while (1 < n){
            //交换最大元素和tail前驱的数据域
            Position pMax = selectMax(p, n);
            Object eleBeIn = tail.getPrev().getEle();
            tail.getPrev().setEle(pMax.getEle());
            pMax.setEle(eleBeIn);
            //修改待排序的范围、长度
            tail = tail.getPrev();
            n--;
        }
    }
    //    选择排序的子方法，在起始于位置p的n个元素中选择出值最大的，返回位置
    public Position selectMax(Position p, int n){
        //进入方法前需要判断数据类型T是否继承了Comparable接口
        if ( ! (p.getEle() instanceof Comparable) ){
            throw new IncomparableException("异常：无法比较，因为列表的数据类型没有实现Comparable接口");
        }
        ListNode<T> node = turnToNode(p);
        ListNode<T> nodeMax = node;//用于存储最大元素的节点（地址值）
        int rank = 0;
        while (rank < n - 1){
            if ( ((Comparable<T>)node.getEle()).compareTo(node.getNext().getEle()) <= 0 ) {
                //node的数据不大于其后继，则将nodeMax移动到其后继上
                nodeMax = node.getNext();
            }
            node = node.getNext();//node指向其后继，赋值的是地址
            rank++;
        }
        return nodeMax;
    }

    //    此方法用于列表的排序，使用插入排序的方法，对起始于位置p的n个元素进行排序
    public void insertionSort(Position p, int n){
        if (this.size <= 1){ return; }//仅1个元素和没元素时不需要排序
        ListNode<T> node = turnToNode(p).getNext();//指针，代表无序后缀的首元素
        ListNode<T> nodeOld;
        int r = 1;//代表有序前缀的长度
        while (r < n){
            //寻找到的在有序前缀中不大于无序后缀首元素的位置：
            Position search = search(node.getEle(), r, node.getPrev());
            insertAfter(search, node.getEle());
            nodeOld = node;//寄存node的地址
            node = nodeOld.getNext();//node后移一位
            remove(nodeOld);
            r++;
        }
    }
//==================================================================================================
}