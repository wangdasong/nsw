package nsw.base.core.utils.exception;

public final class BusinessException extends RuntimeException{
  private static final long serialVersionUID = -578607331764730641L;
  private final String code;
  private final String msg;

  public BusinessException(String code, String msg){
    this(code, msg, null);
  }

  public BusinessException(String code, String msg, Throwable cause){
    super(msg, cause);
    this.msg = msg;
    this.code = code;
  }

  public String getCode() {
    return this.code;
  }

  public String getMsg() {
    return this.msg;
  }
}