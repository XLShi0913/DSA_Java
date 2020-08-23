package chapter7.binsearchtree;
/*
二叉搜索树——词条类型：
是二叉搜索树节点的内容，包括关键码、值两部分，其中关键码应该可以比较；
也可以选择写成comparable接口的实现类，但是更重要的是有一个compareTo的方法，相信这也是弄这么一个comparable接口的初衷；
 */
public class Entry implements Comparable{
    protected Comparable key;
    protected Object value;

    public Entry(Comparable key, Object value) {
        this.key = key;
        this.value = value;
    }
    public Entry() { }

    public Comparable getKey() { return key; }
    public void setKey(Comparable key) { this.key = key; }
    public Object getValue() { return value; }
    public void setValue(Object value) { this.value = value; }

    @Override
    public int compareTo(Object anotherEntry) {
        return this.key.compareTo( ((Entry)anotherEntry).key );
    }
}
