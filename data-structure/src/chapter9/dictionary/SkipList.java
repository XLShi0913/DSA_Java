package chapter9.dictionary;

import chapter7.binsearchtree.Entry;

/*
跳表：
1、跳表是一种可以实现O(logn)复杂度查找、插入和删除的数据结构，效率可以媲美BBST家族，同时又实现简单；
2、借助于随机的塔高度，每一层都有1/2的概率向上长一层，但总高度的期望值仅为2；
3、跳表的节点排列是有序的；
 */

public class SkipList {
    private int maxNodeLevel;       //节点的最大高度，这个值不超过64
    private final int MAXLevel = 64;//节点高度上限
    private int size;               //节点数目
    SkipListNode header;
    SkipListNode tailor;
//======================================================================================================================
//构造方法
    public SkipList() {
        maxNodeLevel = 0;
        size = 0;
        header = new SkipListNode(null, MAXLevel);
        tailor = new SkipListNode(null, MAXLevel);
    }
//======================================================================================================================
//对外接口
    //查找操作
    public SkipListNode search(Comparable key){
        int hotLevel = maxNodeLevel - 1;                //是前驱/后继数组的索引值
        SkipListNode hotNode = header.succ[hotLevel];   //当前访问到的节点
        //用两层循环完成二维的查找
        while ( 0 <= hotLevel ){
            //说明这一层还可判断
            hotNode = hotNode.succ[hotLevel];           //在新的一层中后移一个身位
            while ( hotNode.entry != null && key.compareTo(hotNode.entry.getKey()) < 0)
                //未抵达tailor且比key小的情况，沿当前链下行
                hotNode = hotNode.succ[hotLevel];
            if (hotNode.entry != null && key.equals(hotNode.entry.getKey()))
                return hotNode;
            //若不相等，则需要下行一层
            hotNode = hotNode.prev[hotLevel];
            hotLevel--;
        }
        //查找失败，返回不大于给定key的最大的词条；注意可能会出现返回header的情况，说明列表中所有的词条都比给定词条大
        return hotNode;
    }

    //插入操作，不允许关键码发生重复
    public boolean insert(Entry entry){
        //建立节点
        SkipListNode newNode = new SkipListNode(entry);
        SkipListNode prevNode;//重构关系的时候用到
        int nodeLevel = newNode.level;
        //更新跳表信息，以及必要的前置重构
        int listLevel = maxNodeLevel;
        if (maxNodeLevel < nodeLevel) {
            for (int i = listLevel; i < nodeLevel; i++){
                header.succ[i] = newNode;
                tailor.prev[i] = newNode;
            }
            maxNodeLevel = nodeLevel;
        }
        size++;
        //逐层查找，逐层设置前驱后继
        int hotLevel = listLevel - 1;
        SkipListNode hotNode = header.succ[hotLevel];
        while (0 <= hotLevel){
            hotNode = hotNode.succ[hotLevel];
            while (hotNode.entry != null && entry.getKey().compareTo(hotNode.entry.getKey()) < 0)
                hotNode = hotNode.succ[hotLevel];
            if (hotNode.entry != null && entry.getKey().equals(hotNode.entry.getKey()))
                return false;//发现了重复的情况
            //剩下的是未发现重复的情况，需要在hotLevel层次上进行部分重构
            prevNode = hotNode.prev[hotLevel];
            newNode.succ[hotLevel] = hotNode;
            newNode.prev[hotLevel] = prevNode;
            hotNode.prev[hotLevel] = newNode;
            prevNode.succ[hotLevel] = newNode;
            hotLevel--;
        }
        return true;//插入成功
    }

    //删除操作，与插入操作类似
    public boolean remove(Entry entry){
        return true;
    }
//======================================================================================================================
}
