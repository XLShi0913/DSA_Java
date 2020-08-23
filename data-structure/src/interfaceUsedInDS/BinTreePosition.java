package interfaceUsedInDS;

import java.util.Iterator;
/*
    二叉树节点的ADT接口
 */
public interface BinTreePosition extends Position {

    //红黑树的颜色，实际上不需要定义RED，因为color未定义状态的默认值是0
    final byte RED = 0;
    final byte BLACK = 1;

    boolean hasParent();
    BinTreePosition getParent();
    void setParent(BinTreePosition p);

    boolean isLeaf();

    boolean isLChild();
    boolean hasLChild();
    BinTreePosition getLChild();
    void setLChild(BinTreePosition p);

    boolean isRChild();
    boolean hasRChild();
    BinTreePosition getRChild();
    void setRChild(BinTreePosition p);

    int getSize();
    void updateSize();
    int getHeight();
    void setHeight(int height);
    void updateHeight();
    int getDepth();
    void updateDepth();

    //中序遍历意义下，获取直接前驱/后继
    BinTreePosition getPrev();
    BinTreePosition getSucc();

    //断绝当前节点与其父亲的父子关系，返回当前节点
    BinTreePosition secede();

    //将节点l/r作为当前节点的left/right child
    BinTreePosition attachL(BinTreePosition l);
    BinTreePosition attachR(BinTreePosition r);

    //前/中/后序遍历
    Iterator elementsPreorder();
    Iterator elementsInorder();
    Iterator elementsPostorder();

    //红黑树，判断与修改颜色
    boolean isBlack();
    void setColor(byte color);
}
