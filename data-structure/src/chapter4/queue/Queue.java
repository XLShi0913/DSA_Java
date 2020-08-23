package chapter4.queue;

import chapter3.list.ListOfTwoWay;

public class Queue<T> {
    ListOfTwoWay<T> list;

    public Queue() {
        this.list = new ListOfTwoWay<>();
    }
    public int getSize() { return list.getSize(); }
    public boolean empty(){ return list.isEmpty(); }

    //    此方法用于在队列末位置插入一个元素
    public void enqueue(T e){
        list.insertAtLast(e);
    }

    //    此方法用于弹出队列首位置元素
    public T dequeue(){
        T ele = (T)list.getFirst().getEle();
        list.remove(list.getFirst());
        return ele;
    }

    //    此方法用于读取队列的末元素
    public T rear(){
        return (T)list.getLast().getEle();
    }

    //    此方法用于读取队列的首元素
    public T front(){
        return (T)list.getFirst().getEle();
    }

}
