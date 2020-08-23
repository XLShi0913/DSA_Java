package chapter6.graph;

import chapter3.list.ListOfTwoWay;
import interfaceUsedInDS.*;
import java.util.Iterator;

/*
基于邻接表法的有向图构造
 */

public class Graph_List implements Graph {
    protected List V;
    protected List E;

    //构造函数
    public Graph_List() {
        V = new ListOfTwoWay<>();
        E = new ListOfTwoWay<>();
    }
//===============================================================================================================
//    来自接口Graph中的方法

    public int vNumber() { return V.getSize(); }
    public int eNumber() { return E.getSize(); }

    public Iterator vertices() { return V.elements(); }
    public Iterator vPositions() { return V.positions(); }
    public Iterator edges() { return E.elements(); }
    public Iterator ePositions() { return E.positions(); }

    public boolean areAdjacent(Vertex u, Vertex v)
    { return null != edgeFromTo(u, v); }
    public Edge edgeFromTo(Vertex u, Vertex v) {
        for (Iterator it = u.outEdges(); it.hasNext(); ){
            Edge e = (Edge) it.next();              //默认的起始元素为首元素
            if (v == e.getVPosInV(1).getEle())   //此处position的element就是vertex
            { return e; }
        }
        return null;
    }

    //顶点和边的删除
    public Vertex remove(Vertex v) {
        //删除所有出边、入边和顶点集中的顶点本身
        int i = 1;
        while (0 < v.getOutDeg()){
            remove( (Edge) ((Vertex_List) v).outEdges.first().getEle() );
        }
        while (0 < v.getInDeg()){
            remove( (Edge) ((Vertex_List) v).inEdges.first().getEle() );
        }
        return (Vertex) V.remove(v.getVPosInV());
    }
    public Edge remove(Edge e) {
        ( (Vertex_List) e.getEPosInT(0).getEle() ).outEdges.remove(e.getEPosInT(0));
        ( (Vertex_List) e.getEPosInT(1).getEle() ).outEdges.remove(e.getEPosInT(1));
        return null;
    }

    public Position insert(Vertex v) { return V.insertAtLast(v); }
    public Position insert(Edge e) { return E.insertAtLast(e); }

    public List getV() { return V; }
    public List getE() { return E; }
}
