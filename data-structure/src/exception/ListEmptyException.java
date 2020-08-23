package exception;
/*
异常：列表为空
 */
public class ListEmptyException extends RuntimeException{
    public ListEmptyException(String message) {
        super(message);
    }
}
