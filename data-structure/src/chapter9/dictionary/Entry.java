package chapter9.dictionary;
/*
词典类型Entry的实现
1、作为哈希表中键值对的类型；
2、与红黑树中词条的不同在于其中的key仅需要判等，不需要比较；
3、尝试使用泛型定义，避免不必要的类型转换；
 */
public class Entry<K, V> {
    K key;//K类型需要支持判等操作
    V value;
    boolean empty;//用于懒惰删除的标记

    public Entry(K key, V value) {
        this.key = key;
        this.value = value;
        empty = false;//插入了就不为空
    }
}
