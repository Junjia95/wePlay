package cn.cie.event;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wjj on 2023/6/25.
 * 事件实体
 */
@Data
@RequiredArgsConstructor
public class EventModel {
    /**
     * 在缓存中事件队列的key
     */
    public static final String EVENT_KEY = "event";

    /**
     * 事件类型
     */
    @NonNull
    private EventType eventType;
    /**
     * 事件发出者id
     */
    private int fromId;
    /**
     * 事件接受者id
     */
    private int toId;
    /**
     * 触发事件的实体，比如评论点赞
     */
    private int entityId;
    /**
     * 实体拥有者
     */
    private int entityOwnerId;
    /**
     * 可能会有的额外信息
     */
    private Map<String ,String> exts = new HashMap<String, String>();

    /**
     * 每次调用，都往本对象的exts中存入一对键值对
     * @param key
     * @param value
     * @return
     */
    public EventModel setExts(String key, String value) {
        this.exts.put(key, value);
        return this;
    }
}
