package cn.cie.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
@AllArgsConstructor
@Data
public class Result<T> {
    private boolean success;
    private String msg;
    private T data;

    public Result(boolean success, String msg) {
        this.success = success;
        this.msg = msg;
    }

    public static Result success() {
        return new Result(true, MsgCenter.OK);
    }

    public static Result success(Object data) {
        return new Result(true, MsgCenter.OK, data);
    }

    public static Result fail(String msg) {
        return new Result(false, msg);
    }

    public static Result fail(String msg, Object data) {
        return new Result(false, msg, data);
    }
}
