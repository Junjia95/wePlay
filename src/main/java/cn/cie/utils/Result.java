package cn.cie.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;


@Getter
public class Result<T> {
    private boolean success;
    private String msg;
    private T data;

    private Result(boolean success, String msg) {
        this.success = success;
        this.msg = msg;
    }

    private Result(boolean success, String msg, T data) {
        this.success = success;
        this.msg = msg;
        this.data = data;
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
