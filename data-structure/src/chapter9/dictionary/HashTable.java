package chapter9.dictionary;

import chapter3.list.ListOfTwoWay;
import interfaceUsedInDS.Dictionary;
import java.util.Iterator;

/*
哈希表：
1、底层是一个数组，支持高效的循秩访问和高效缓存；
2、桶数组长度取4k+3型素数，而非2的整数，冲突的排解采用开放定址的双向平方试探；
3、hash函数借用Object类的toString方法得到的字符串，先使用近似多项式换成整数，再采用模余获取;
4、删除操作使用懒惰删除，默认装填因子上限为0.75；
5、哈希表的精髓在于循值访问，理想情况下可以通过值（key）直接找到词条，即O(1)时间复杂度；
6、然而冲突的不可避免性导致了在装填因子高的情况下进行多次试探，违背了hash的初衷，此时应当进行rehash操作；
 */

public class HashTable<K, V> implements Dictionary<K, V> {
    protected double loadFactor;                //装填因子
    protected final double defaultLoadFactor;   //装填因子上限
    protected int M;                            //散列表容量
    protected int size;                         //散列表中键值对（词条）的总数
    protected Entry<K, V>[] buckets;            //桶数组，哈希词典的底层结构，用于存储词条
//=====================================================================================================================
//构造方法，辅助实现词典的功能
    public HashTable(int n, double loadFactor) {
        //给定预期的键值对数目
        this.defaultLoadFactor = loadFactor;
        M = p(n);
        size = 0;
        buckets = new Entry[M];
    }

    public HashTable() { this(8, 0.75); }
//=====================================================================================================================
//辅助方法，辅助实现词典的功能
    //判断是否为素数
    private static boolean prime(int n){
        for (int i = 3; i < 1 + Math.sqrt(n); i++){
            if (n / i * i == 0) return false;
        }
        return true;
    }

    //给定一个数，获取下一个素数
    private static int p(int n){
        if (n < 3) return 3;
        n = n | 1;//奇数化
        while ( !prime(n) ) n += 2;
        return n;
    }

    //hash函数
    protected int hash(K key){
        char[] chars = key.toString().toCharArray();
        int h = 0;
        for (char aChar : chars) {
            h = h << 5 | h >> 27;
            h += (int) aChar;
        }
        return h % M;
    }

    //重散列方法
    protected void rehash(){
        if (loadFactor < defaultLoadFactor) return;
        int oldM = M;
        Entry<K, V>[] oldBuckets = buckets;
        M = p(2 * M);        //至少扩容两倍
        buckets = new Entry[M]; //搬到新的数组
        int rank = 0;           //旧桶数组秩的探针
        while (rank < oldM){
            //对旧桶中所有元素，逐次搬迁到新的桶数组
            if (oldBuckets[rank] != null)
                insert(oldBuckets[rank].key, oldBuckets[rank].value);
            rank++;
        }
    }
//=====================================================================================================================
//词典的对外接口
    public int getSize() { return this.size; }

    public boolean isEmpty() { return size == 0; }

    public Entry<K, V> find(K key) {
        int h = hash(key);
        int hot = h;
        while ( buckets[hot] != null ){
            //注意终止条件，empty的桶位是不占size的，因而装填因子小于1不代表必然有null的存在
            //故要附加一个终止条件
            if ( key.equals(buckets[hot].key) )
                return buckets[hot];
            hot = (hot + 1) % M;
            if (hot == h) break;
        }
        return null;//至循环结束都未返回，即说明查找失败
    }

    public Iterator<Entry<K, V>> findAll(K key){
        int h = hash(key);
        int hot = h;
        ListOfTwoWay<Entry<K, V>> list = new ListOfTwoWay<>();
        while (  buckets[hot] != null ){
            //注意终止条件，empty的桶位是不占size的，因而装填因子小于1不代表必然有null的存在
            //故要附加一个终止条件
            if (key.equals(buckets[hot].key)) list.insertAtLast(buckets[hot]);
            hot = (hot + 1) % M;
            if (hot == h) break;
        }
        if (list.isEmpty()) return null;
        return list.elements();
    }

    public Entry<K, V> insert(K key, V value) {
        Entry<K, V> entry = new Entry<>(key, value);
        int rank = hash(key);
        while ( !buckets[rank].empty ){
            //若位子被别人占了，则线性试探，装填因子保证了必然有空桶的出现
            rank = (rank + 1) % M;
        }
        buckets[rank] = entry;//向bucket[]中插入
        //插入完毕，要更新规模、加载因子，进行可能的rehash，返回插入词条
        size++;
        loadFactor = (double) size / M;
        rehash();
        return entry;
    }

    public Entry<K, V> remove(K key) {
        Entry<K, V> oldEntry = find(key);   //查找关键码
        if (oldEntry == null) return null;  //查无此人，也没法删除，删除失败
        oldEntry.empty = true;              //懒惰删除（仅删除了一个关键码符合要求的词条）
        return oldEntry;
    }

    public Iterator<Entry<K, V>> entries() {
        ListOfTwoWay<Entry<K, V>> list = new ListOfTwoWay<>();
        int hot = 0;
        while (hot < M){
            if (buckets[hot] != null && !buckets[hot].empty)
                list.insertAtLast(buckets[hot]);
            hot++;
        }
        if (list.isEmpty()) return null;
        return list.elements();
    }
//=====================================================================================================================
}
