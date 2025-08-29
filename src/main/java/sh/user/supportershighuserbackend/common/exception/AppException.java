package sh.user.supportershighuserbackend.common.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import sh.user.supportershighuserbackend.common.constant.AppErrorCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AppException extends RuntimeException {
	private static final long serialVersionUID = 2063143507320417047L;

    private AppErrorCode enumError = AppErrorCode.SUCCESS;

    public AppException() {
        super();
    }

    public AppException(String message) {
        super(message);
    }

    public AppException(AppErrorCode errCd) {
        super(errCd.getErrMsg());
        this.setEnumError(errCd);
    }

    public String getErrCode() {
        return enumError.getErrCode();
    }
}
