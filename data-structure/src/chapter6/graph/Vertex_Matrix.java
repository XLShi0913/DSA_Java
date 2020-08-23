package chapter6.graph;

import chapter2.vector.Vector;
import java.util.Iterator;

/*
基于邻接矩阵的有向图实现————顶点
 */

public class Vertex_Matrix{
    final static int UNDISCOVERED = 0;
    final static int DISCOVERED = 1;
    final static int VISITED = 2;

    protected Object info;          //顶点的内容
    protected int vPosInV;          //顶点在图的点集中的秩
//    protected Vector outEdges;      //顶点的出边集
//    protected Vector inEdges;       //顶点的入边集
    protected int outDeg;           //顶点的出度
    protected int inDeg;            //顶点的入度
    protected int status;           //顶点的状态
    protected int dTime;            //顶点被发现的时间
    protected int fTime;            //顶点被访问完毕的时间
    protected int distance;         //最短路径
    protected Vertex_Matrix parent; //父节点（BFS意义或DFS意义）

    //构造方法，在图中插入顶点，并作初始化设置
    public Vertex_Matrix(Object info, Graph_Matrix g) {
        this.info = info;
        vPosInV = g.insert(this);
//        outEdges = new Vector();
//        inEdges = new Vector();
        outDeg = 0;
        inDeg = 0;
        status = UNDISCOVERED;
        dTime = Integer.MAX_VALUE;
        fTime = Integer.MAX_VALUE;
        distance = Integer.MAX_VALUE;
        parent = null;
    }

    public Object getInfo() { return info; }
    public void setInfo(Object info) { this.info = info; }

//    public int getOutDeg() { return outEdges.getSize(); }
//    public int getInDeg() { return inEdges.getSize(); }
//    public Iterator inEdges() { return inEdges.traverse(); }
//    public Iterator outEdges() { return outEdges.traverse(); }


    public int getOutDeg() { return outDeg; }
    public void setOutDeg(int outDeg) { this.outDeg = outDeg; }
    public int getInDeg() { return inDeg; }
    public void setInDeg(int inDeg) { this.inDeg = inDeg; }

    public int getVPosInV() { return vPosInV; }

    public int getStatus() { return status; }
    public void setStatus(int s) { status = s; }

    public Vertex_Matrix getParent() { return parent; }
    public void setParent(Vertex_Matrix v) { parent = v; }

    public int getDTime() { return dTime; }
    public void setDTime(int dTime) { this.dTime = dTime; }
    public int getFTime() { return fTime; }
    public void setFTime(int fTime) { this.fTime = fTime; }

    public int getDistance() { return distance; }
    public void setDistance(int distance) { this.distance = distance; }
}
