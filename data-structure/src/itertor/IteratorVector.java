package itertor;

import chapter2.vector.Vector;
import java.util.Iterator;

/*
用于对向量元素的遍历
 */
public class IteratorVector<T> implements Iterator<T> {
    Vector<T> vector;
    int rank;

    public IteratorVector(Vector<T> vector) {
        this.vector = vector;
        rank = 0;//obj为向量的起始元素
    }

    public boolean hasNext() {
        return vector.getArray(rank) != null;
    }

    //返回当前位置向量元素并将指针后移
    public T next() {
        if (!hasNext()){
            throw new NullPointerException("异常：遍历已达向量尾");
        }
        rank++;
        return vector.getArray(rank - 1);//rank已经增加过了
    }
}
