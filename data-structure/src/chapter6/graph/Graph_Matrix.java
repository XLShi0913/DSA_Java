package chapter6.graph;

import chapter2.vector.Vector;
import chapter4.queue.Queue;
import java.util.Iterator;

/*
基于邻接矩阵的有向图实现
 */

public class Graph_Matrix {
//===================================================================================
//    基本内容
    Vector V;   //点集
    Vector E;   //边集

    //空参构造
    public Graph_Matrix() {
        V = new Vector();
        E = new Vector();
    }
//===================================================================================
//    基本接口
    //点集/边集的元素个数，这样设置可以在调出时更新
    public int vNumber() { return V.getSize(); }
    public int eNumber() { return E.getSize(); }

    //点集/边集的元素迭代器
    public Iterator vertices() { return V.traverse(); }
    public Iterator edges() { return E.traverse(); }

    //判断一条边是否存在/返回这条边
    public boolean areAdjacent(int u, int v) {
        return edgeFromTo(u, v) != null;
    }
    public Edge_Matrix edgeFromTo(int u, int v) {
        return (Edge_Matrix) E.getArray(u * vNumber() + v);
    }
    public Edge_Matrix edgeCallByRank(int rank){
        return (Edge_Matrix) E.getArray(rank);
    }

    //顶点和边的删除
    public Vertex_Matrix remove(int u) {
        //记录删除前的顶点，并重新做初始化设置（位置在下次插入时更新）
        Vertex_Matrix vertexOld = (Vertex_Matrix) V.getArray(u);
        vertexOld.setInDeg(0);
        vertexOld.setOutDeg(0);
        vertexOld.setStatus(Vertex_Matrix.UNDISCOVERED);
        vertexOld.setDTime(Integer.MAX_VALUE);
        vertexOld.setFTime(Integer.MAX_VALUE);
        vertexOld.setDistance(Integer.MAX_VALUE);
        vertexOld.setParent(null);
        //删除点集中的u
        int vNumOld = vNumber();//原来的顶点数
        V.remove(u);//删除点集中的对应元素
        //删除入边（边集的列元素）
        for (int i = u; i < vNumOld*vNumOld; i += vNumOld){ E.remove(i); }
        //删除出边（边集的行元素）
        for (int i = u * (vNumOld - 1); i < (u + 1) * (vNumOld - 1); i++){ E.remove(i); }
        //返回重置后的顶点
        return vertexOld;
    }
    public Edge_Matrix remove(int u, int v) {
        //记录要删除的边，并作初始化设置（位置在下次插入时更新）
        Edge_Matrix edgeOld = edgeFromTo(u, v);
        edgeOld.setType(Edge_Matrix.UNDETERMINED);
        //删除边集中对应位置的地址值
        E.remove(u * vNumber() + v);
        //更新顶点的出入度
        int uOutDeg = ( (Vertex_Matrix) V.getArray(u) ).getOutDeg();
        int vInDeg = ( (Vertex_Matrix) V.getArray(v) ).getInDeg();
        ( (Vertex_Matrix) V.getArray(u) ).setOutDeg(uOutDeg - 1);
        ( (Vertex_Matrix) V.getArray(v) ).setInDeg(vInDeg - 1);
        //返回重置后的边
        return edgeOld;
    }

    //顶点和边的插入
    public int insert(Vertex_Matrix v) {
        //将顶点插入向量尾
        int vNumOld = V.getSize();
        V.insert(v, vNumOld - 1);
        //插入入边，仅留出位置，插入元素为空
        for (int i = vNumOld; i < vNumOld*(vNumOld+1); i += (vNumOld+1))
        { E.insert(null, i); }
        //插入出边，仅留出位置，插入元素为空
        for (int i = vNumOld*(vNumOld+1); i < (vNumOld+1)*(vNumOld+1); i ++)
        { E.insert(null, i); }
        //返回新插入顶点的秩
        v.vPosInV = vNumOld - 1;
        return vNumOld - 1;
    }
    public void insert(Edge_Matrix e, int u, int v) {
        if (areAdjacent(u, v)){ return; }
        //插入以u为tail，以v为head的边
        int rankOfNew = u * vNumber() + v;
        E.insert(e, rankOfNew);
        //更新顶点的出入度
        int uOutDeg = ( (Vertex_Matrix) V.getArray(u) ).getOutDeg();
        int vInDeg = ( (Vertex_Matrix) V.getArray(v) ).getInDeg();
        ( (Vertex_Matrix) V.getArray(u) ).setOutDeg(uOutDeg + 1);
        ( (Vertex_Matrix) V.getArray(v) ).setInDeg(vInDeg + 1);
        //返回新插入边的秩
        //e.ePosInE = rankOfNew;
        //return rankOfNew;
    }

    //对于任意顶点i，枚举其所有邻接顶点
    public int nextNbr(int i, int j){
        //i：对象顶点，j：已知的一个邻居，返回下一个邻居
        j--;
        while ( -1 < j && !areAdjacent(i, j) ){/*逆向查找*/j--; }
        return j;//允许存在-1
    }
    public int firstNbr(int i){/*顶点i的第一个邻居*/return nextNbr(i, vNumber()); }
//===================================================================================
//    广度优先遍历BFS
    public void BFS(int v, int clock){
        Queue<Integer> q  = new Queue<>();//队列存放的是顶点在顶点集中的秩
        q.enqueue(v);
        ( (Vertex_Matrix) V.getArray(v) ).setStatus(Vertex_Matrix.DISCOVERED);
        while ( !q.empty() ){
            v = q.dequeue();
            ( (Vertex_Matrix) V.getArray(v) ).setDTime(clock++);
            for (int u = firstNbr(v); -1 < u; u = nextNbr(v, u)){
                //考察v的每一个邻居顶点
                if ( ( (Vertex_Matrix) V.getArray(u) ).getStatus() == Vertex_Matrix.UNDISCOVERED ) {
                    ( (Vertex_Matrix) V.getArray(u) ).setStatus(Vertex_Matrix.DISCOVERED);
                    ( (Vertex_Matrix) V.getArray(u) ).setParent(( (Vertex_Matrix) V.getArray(v) ));
                    q.enqueue(u);
                    edgeFromTo(v, u).setType(Edge_Matrix.TREE);
                }else { edgeFromTo(v, u).setType(Edge_Matrix.CROSS); }
            }
            ( (Vertex_Matrix) V.getArray(v) ).setStatus(Vertex_Matrix.VISITED);
        }
    }
    public void bfs(){
        int clock = 0;
        for (int i = 0; i < vNumber(); i++){
            if ( ((Vertex_Matrix) V.getArray(i) ).getStatus() == Vertex_Matrix.UNDISCOVERED )
            { BFS(i, clock); }
        }
    }
//===================================================================================
//    深度优先遍历DFS
    public void DFS(int v, int clock){
        ( (Vertex_Matrix) V.getArray(v) ).setStatus(Vertex_Matrix.DISCOVERED);
        ( (Vertex_Matrix) V.getArray(v) ).setDTime(++clock);
        int status;
        for (int u = firstNbr(v); -1 < u; u = nextNbr(v, u)){
            switch ( ( (Vertex_Matrix) V.getArray(u) ).getStatus() ){
                case Vertex_Matrix.UNDISCOVERED:
                    edgeFromTo(v, u).setType(Edge_Matrix.TREE);
                    ( (Vertex_Matrix) V.getArray(u) ).setParent(( (Vertex_Matrix) V.getArray(v) ));
                    DFS(u, clock);
                    break;
                case Vertex_Matrix.DISCOVERED:
                    edgeFromTo(v, u).setType(Edge_Matrix.BACKWARD);
                    break;
                default:
                    status =
                            ( ((Vertex_Matrix)V.getArray(v) ).getDTime() < ( (Vertex_Matrix) V.getArray(u) ).getDTime() ) ?
                                    Edge_Matrix.FORWARD : Edge_Matrix.CROSS;
                    edgeFromTo(v, u).setType(status);
                    break;
            }
        }
        ( (Vertex_Matrix) V.getArray(v) ).setStatus(Vertex_Matrix.VISITED);
        ((Vertex_Matrix)V.getArray(v) ).setFTime(++clock);
    }

    public void dfs(){
        int clock = 0;
        for (int i = 0; i < vNumber(); i++){
            if ( ((Vertex_Matrix) V.getArray(i) ).getStatus() == Vertex_Matrix.UNDISCOVERED )
            { DFS(i, clock); }
        }
    }
//===================================================================================
}
