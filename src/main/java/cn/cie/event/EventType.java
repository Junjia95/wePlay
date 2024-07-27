package cn.cie.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * Created by wjj on 2023/6/25.
 * 异步事件类型
 */

@Getter
@AllArgsConstructor
public enum EventType {
    SEND_VALIDATE_EMAIL(1),
    SEND_FIND_PWD_EMAIL(2);

    private int value;
}
