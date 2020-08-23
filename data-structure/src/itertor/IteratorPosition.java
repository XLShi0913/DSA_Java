package itertor;

import exception.NoSuchElementException;
import interfaceUsedInDS.List;
import interfaceUsedInDS.Position;
import java.util.Iterator;

/*
1、这是一个基于列表接口的位置迭代器，返回位置，并将指针后移;
2、这是一个Iterator接口的实现类，其对象可以实现下一位置判空、返回下一位置的功能；
3、构造方法中需要给出一个已创建完毕的列表list对象
 */

public class IteratorPosition implements Iterator{

    private List list;
    private Position p;
//========================================================================
//构造方法
    public IteratorPosition() { }
    public IteratorPosition(List list) {
        //从首元素开始，对列表list的迭代
        if(list.isEmpty()){ this.p = null; }
        this.list = list;
        this.p = list.first();
    }
    public IteratorPosition(List list, Position p) {
        //从元素p开始，对列表list的迭代
        this.list = list;
        this.p = p;
    }
//========================================================================
//接口iterator中的方法
    //    判断是否存在下个元素的方法
    @Override
    public boolean hasNext() {
        return !(p == null);
    }
    //    将指针移动到下一个位置，返回本位置中内容
    @Override
    public Object next()
            throws NoSuchElementException/*运行期异常，也可以不抛出，JVM会自动处理*/{
        if (!hasNext()){ throw new NoSuchElementException("异常：已经到达列表末尾"); }
        Position pOld = p;
        if (p == list.last()){/*p就是最后一个元素*/
            p = null;//赋值为空表示没有下个元素了
        }else {
            p = list.getNext(p);//p移动到了下一个位置
        }
        return pOld;
    }
}
