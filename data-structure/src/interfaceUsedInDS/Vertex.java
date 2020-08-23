package interfaceUsedInDS;

import java.util.Iterator;
//图节点的ADT接口

public interface Vertex {
    final static int UNDISCOVERED = 0;
    final static int DISCOVERED = 1;
    final static int VISITED = 2;

    //返回与更新当前节点信息
    public Object getInfo();
    public void setInfo(Object info);

    //返回当前节点的出入度
    public int getOutDeg();
    public int getInDeg();

    //返回当前顶点所有关联边、关联边位置的迭代器
    public Iterator inEdges();
    public Iterator inEdgesPosition();
    public Iterator outEdges();
    public Iterator outEdgesPosition();

    //当前顶点在图的顶点集中的位置
    Position getVPosInV();

    //读取、设置顶点状态（DFS + BFS）
    int getStatus();
    void setStatus(int s);

    //读取、设置顶点在不同意义下的父亲
    Vertex getParent();
    void setParent(Vertex v);
}
