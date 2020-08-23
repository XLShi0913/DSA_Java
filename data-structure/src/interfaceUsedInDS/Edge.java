package interfaceUsedInDS;

import javafx.geometry.Pos;

public interface Edge {
    int UNDETERMINED = 0;
    int TREE = 1;
    int CROSS = 2;
    int FORWARD = 3;
    int BACKWARD = 4;

    Object getInfo();//获得当前边的内容
    void setInfo(Object x);//改变当前边的内容

    Position getEPosInE();//当前边在图的边集中的位置
    Position getVPosInV(int i);//当前边的起点和终点在点集中的位置（i=0代表tail，i=1代表head）
    Position getEPosInT(int i);//当前边其在起点/终点的关联边集中的位置（i=0代表tail，i=1代表head）

    int getType();//获得当前边的状态
    void setType(int t);//改变当前边的状态
}
