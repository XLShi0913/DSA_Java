package interfaceUsedInDS;

import exception.BoundaryViolationException;
import exception.PositionInvalidException;
import javafx.geometry.Pos;

import java.util.Iterator;

/*
列表的通用的接口，给出一个列表的模板（通用的方法）
 */
public interface List {
    int getSize();
    boolean isEmpty();
    Position first();
    Position last();
    Position getNext(Position p)
            throws PositionInvalidException, BoundaryViolationException;
    Position getPrev(Position p)
            throws PositionInvalidException, BoundaryViolationException;
    Position insertAfter(Position p, Object e)
            throws PositionInvalidException;
    Position insertBefore(Position p, Object e)
            throws PositionInvalidException;
    Position insertAtFirst(Object e)
            throws PositionInvalidException;
    Position insertAtLast(Object e)
            throws PositionInvalidException;
    Object remove(Position p)
            throws PositionInvalidException;
    Object replace(Position p, Object e)
            throws PositionInvalidException;
    Iterator positions();
    Iterator elements();
}
