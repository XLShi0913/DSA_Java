package chapter6.graph;

import chapter3.list.ListNode;
import interfaceUsedInDS.Edge;
import interfaceUsedInDS.Graph;
import interfaceUsedInDS.Position;

/*
基于邻接表法的有向图构造方法————边的构造
 */

public class Edge_List implements Edge {
    protected Object info;          //当前边的内容
    protected Position ePosInE;     //当前边在图的边集中的位置
    protected Position[] vPosInV;   //当前边的tail和head在图的点集中的位置
    protected Position[] ePosInI;   //当前边的tail和head在图的点集中的位置
    //这两个是长度为2的数组，0元素与tail相关，1元素与head相关
    protected double weight;        //边的权重
    protected int type;             //当前边的状态

    //构造方法，给出边所在的图g，tail顶点u，head顶点v和内容info
    public Edge_List(Graph g, Vertex_List u, Vertex_List v, Object info) {
        this.info = info;
        ePosInE = g.insert(this);
        //当前边的tail和head在图的点集中的位置：调用顶点中的方法
        vPosInV = new ListNode[2];
        vPosInV[0] = u.getVPosInV();
        vPosInV[1] = v.getVPosInV();
        //当前边的tail和head在图的点集中的位置：插入关联边列表
        ePosInI = new ListNode[2];
        ePosInI[0] = u.outEdges.insertAtLast(this);
        ePosInI[1] = v.inEdges.insertAtLast(this);
        type = UNDETERMINED;
    }

    //构造方法，给出边所在的图g，图中已存在的tail顶点u，head顶点v，内容info和权重weight，在图中构造以u为tail，v为head的边
    public Edge_List(Graph g, Vertex_List u, Vertex_List v, Object info, double weight) {
        this.info = info;
        this.weight = weight;
        ePosInE = g.insert(this);
        //当前边的tail和head在图的点集中的位置：调用顶点中的方法
        vPosInV = new ListNode[2];
        vPosInV[0] = u.getVPosInV();
        vPosInV[1] = v.getVPosInV();
        //当前边的tail和head在图的点集中的位置：插入关联边列表
        ePosInI = new ListNode[2];
        ePosInI[0] = u.outEdges.insertAtLast(this);
        ePosInI[1] = v.inEdges.insertAtLast(this);
        type = UNDETERMINED;
    }
//===============================================================================================================
//    来自接口Edge中的方法
    public Object getInfo() { return info; }
    public void setInfo(Object x) { this.info = x; }

    public Position getEPosInE() { return ePosInE; }
    public Position getVPosInV(int i) { return vPosInV[i]; }
    public Position getEPosInT(int i) { return ePosInI[i]; }

    public int getType() { return type; }
    public void setType(int t) { type = t; }
}
