package chapter3.list;

import interfaceUsedInDS.Position;

import java.util.Comparator;

/*
考虑化学中的链式多聚物，这个Node不仅仅是一个元素，而是多聚物的一个链节
 */

public class ListNode<T> implements Position {

    private T ele;
    private ListNode<T> prev;
    private ListNode<T> next;
//===============================================================================
//    Position接口继承下来的方法，存取元素
    @Override
    public T getEle() { return this.ele; }
    @Override
    public void setEle(Object ele) { this.ele = (T) ele; }
//==============================================================================
//    构造方法
    //    这是一个无参构造，用于对头/尾节点进行构造
    public ListNode() {
        this.ele = null;
        this.prev = null;
        this.next = null;
    }
    //    这是一个标准的构造，给定存储数据、前后节点位置
    public ListNode(T ele, ListNode<T> prev, ListNode<T> next) {
        this.ele = ele;
        this.prev = prev;
        this.next = next;
    }
//==============================================================================
    //    返回和修改前驱节点
    public ListNode<T> getPrev(){ return prev; }
    public void setPrev(ListNode<T> prev) { this.prev = prev; }

    //    返回和修改后继节点
    public ListNode<T> getNext() { return next; }
    public void setNext(ListNode<T> next) { this.next = next; }

    //     将一个指定的内容插入作为当前节点的前驱
    public Position insertAsPrev(T ele){
        ListNode<T> newNode = new ListNode<>(ele, this.getPrev(), this);
        this.prev.setNext(newNode);
        this.setPrev(newNode);//这两句不可颠倒
        //注意：一共需要新建/修改4个关系
        return newNode;
    }
    //     将一个指定的内容插入作为当前节点的后继
    public Position insertAsNext(T ele){
        ListNode<T> newNode = new ListNode<>(ele, this, this.getNext());
        this.next.setPrev(newNode);
        this.setNext(newNode);//这两句不可颠倒
        //注意：一共需要新建/修改4个关系
        return newNode;
    }
}