package chapter9.dictionary;

import chapter7.binsearchtree.Entry;
import java.util.Random;

/*
跳表的节点类型，给出在不同层次上的前驱和后继；
 */
public class SkipListNode {
    int level;                  //本节点的层次
    final int MAXLevel = 64;    //节点层次的上限
    SkipListNode[] prev;        //不同层次下的前驱节点集
    SkipListNode[] succ;        //不同层次下的后继节点集
    Entry entry;                //节点内容，用的是BBST家族的词条，因为支持比较
//=====================================================================================
//构造方法
    //默认的构造方法
    public SkipListNode(Entry entry) {
        this.level = randomLevel();
        prev = new SkipListNode[level];
        succ = new SkipListNode[level];
        this.entry = entry;
    }
    //给定层数的构造方法，用于构造头和尾节点
    public SkipListNode(Entry entry, int level) {
        this.level = level;
        prev = new SkipListNode[level];
        succ = new SkipListNode[level];
        this.entry = entry;
    }
//=====================================================================================
//辅助方法
    //用随机数方法获取高度
    protected int randomLevel(){
        Random r = new Random();
        int level = 1;
        while (level < MAXLevel){
            if (r.nextInt() % 2 == 0) level++;
            else break;
        }//随机数为奇数，则直接跳出循环；随机数为偶数，则进入下一循环
        return level;
    }
//=====================================================================================
}
