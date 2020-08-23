package chapter6.graph;

/*
基于邻接矩阵的有向图实现————边
 */

public class Edge_Matrix {
    final static int UNDETERMINED = 0;
    final static int TREE = 1;
    final static int CROSS = 2;
    final static int FORWARD = 3;
    final static int BACKWARD = 4;

    protected Object info;  //边的内容
    protected int status;   //边的状态
    //protected int ePosInE;  //边在图边集中的秩

    //定义边的在图中的tail和head、内容，默认状态为UNDETERMINED
    public Edge_Matrix
            (Graph_Matrix g, int u, int v, Object info) {
        this.info = info;
        status = UNDETERMINED;
        //ePosInE = g.insert(this, u, v);
    }

    public Object getInfo() { return info; }
    public void setInfo(Object x) { info = x; }

    public int getType() { return status; }
    public void setType(int t) { status = t; }

    //public int getEPosInE() { return ePosInE; }
}
