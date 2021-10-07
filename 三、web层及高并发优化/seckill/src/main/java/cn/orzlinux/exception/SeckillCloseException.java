package cn.orzlinux.exception;

// 秒杀关闭异常，如时间到了，库存没了
public class SeckillCloseException extends SeckillException {
    public SeckillCloseException(String message) {
        super(message);
    }

    public SeckillCloseException(String message, Throwable cause) {
        super(message, cause);
    }
}
