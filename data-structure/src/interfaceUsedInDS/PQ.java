package interfaceUsedInDS;
/*
优先级队列的对外接口，继续使用泛型；
包括插入、获取最大元和删除最大元三部分；
 */
public interface PQ<T> {
    void insert(T ele);
    T getMax();
    T delMax();
}
