package exception;
/*
异常：位置越界
 */
public class BoundaryViolationException extends RuntimeException{
    public BoundaryViolationException(String message) {
        super(message);
    }
}
