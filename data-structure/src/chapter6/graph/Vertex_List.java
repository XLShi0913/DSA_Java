package chapter6.graph;

import chapter3.list.ListOfTwoWay;
import interfaceUsedInDS.Graph;
import interfaceUsedInDS.List;
import interfaceUsedInDS.Position;
import interfaceUsedInDS.Vertex;
import java.util.Iterator;

/*
基于邻接表法的有向图构造方法————顶点的构造
 */

public class Vertex_List implements Vertex {
    protected Object info;      //顶点内容
    protected Position vPosInV; //当前顶点在所属图顶点表中的位置
    protected List outEdges;    //存放以当前顶点为尾的各边内容
    protected List inEdges;     //存放以当前顶点为首的各边内容
    protected int status;       //当前顶点的状态
    protected int dTime;        //时间标签，当前顶点被发现的时刻
    protected int fTime;        //时间标签，当前顶点被访问完毕的时刻
    protected int distance;     //到指定顶点的距离
    protected Vertex parent;    //遍历树（支撑树）中的父顶点

    //在图中插入当前顶点
    public Vertex_List(Graph g, Object info) {
        this.info= info;
        vPosInV = g.insert(this);
        outEdges = new ListOfTwoWay<>();
        inEdges = new ListOfTwoWay<>();
        status = UNDISCOVERED;
        dTime = fTime = Integer.MAX_VALUE;
        distance = Integer.MAX_VALUE;
        parent = null;
    }
//==============================================================================
//    vertex接口中的方法
    public Object getInfo() { return info; }
    public void setInfo(Object info) { this.info = info; }

    public int getOutDeg() { return outEdges.getSize(); }
    public int getInDeg() { return inEdges.getSize(); }

    public Iterator inEdges() { return inEdges.elements(); }
    public Iterator inEdgesPosition() { return inEdges.positions(); }
    public Iterator outEdges() { return outEdges.elements(); }
    public Iterator outEdgesPosition() { return outEdges.positions(); }

    public Position getVPosInV() { return vPosInV; }

    public int getStatus() { return status; }
    public void setStatus(int s) { status = s; }

    public Vertex getParent() { return parent; }
    public void setParent(Vertex v) { parent = v; }
//==============================================================================
//    其他getter/setter方法
    public int getDTime() { return dTime; }
    public void setDTime(int dTime) { this.dTime = dTime; }
    public int getFTime() { return fTime; }
    public void setFTime(int fTime) { this.fTime = fTime; }

    public int getDistance() { return distance; }
    public void setDistance(int distance) { this.distance = distance; }
}
