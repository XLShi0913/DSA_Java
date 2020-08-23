package interfaceUsedInDS;

import java.util.Iterator;

public interface BinTree {
    final byte BLACK = 1;
    final byte RED = 0;

    BinTreePosition getRoot();
    boolean isEmpty();
    int getSize();
    int getHeight();
    Iterator elementPreorder();
    Iterator elementInorder();
    Iterator elementPostorder();
    Iterator elementLevelorder();
}
