package interfaceUsedInDS;

import java.util.Iterator;

public interface Graph {
    int vNumber();//顶点数目
    int eNumber();//边的数目

    Iterator vertices();//顶点内容的迭代器
    Iterator vPositions();//顶点位置迭代器
    Iterator edges();//边内容迭代器
    Iterator ePositions();//边位置迭代器

    //判定是否存在/返回 由顶点u指向顶点v的边（在图定义的意义下u为tail，v为head）
    boolean areAdjacent(Vertex u, Vertex v);
    Edge edgeFromTo(Vertex u, Vertex v);

    //将一个顶点/边在图中移除，并将之返回
    Vertex remove(Vertex v);
    Edge remove(Edge e);

    //插入一个顶点/边，返回其位置
    Position insert(Vertex v);
    Position insert(Edge e);
}
