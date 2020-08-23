package interfaceUsedInDS;

import chapter9.dictionary.Entry;
import java.util.Iterator;
/*
词典：
用于维护一组键值对，需要有基本的查找、插入和删除操作；
已有使用散列表的实现类，可实现快速查找；
尝试使用泛型；
 */
public interface Dictionary<K, V> {
    int getSize();                          //获取词典规模
    boolean isEmpty();                      //判断词典非空
    Entry<K, V> find(K key);                //词典中查找特定关键码的词条，若有多个则返回其中一个
    Iterator< Entry<K, V> > findAll(K key);      //查找所有的关键码词条
    Entry<K, V> insert(K key, V value);     //插入词条(key, value)并返回该词条
    Entry<K, V> remove(K key);              //删除关键码为key的一个词条，并将之返回
    Iterator< Entry<K, V> > entries();      //词典中所有词条的迭代器
}
