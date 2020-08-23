package exception;
/*
异常：未找到特定元素
在util包中有，此处又写了一遍
 */
public class NoSuchElementException extends RuntimeException{
    public NoSuchElementException(String message) {
        super(message);
    }
}
