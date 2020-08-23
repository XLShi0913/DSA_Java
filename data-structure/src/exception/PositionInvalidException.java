package exception;
/*
异常：位置无效
 */
public class PositionInvalidException extends RuntimeException{
    public PositionInvalidException(String message) {
        super(message);
    }
}
