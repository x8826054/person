package com.person.websocket;

/**
 * 消息类型
 * <p>
 *
 * @author 徐擂
 * @version 1.0.0
 * @date 2019-1-14
 */
public enum MessageType {

    // 消息类型: 1.好友信息  2.系统提示  3.好友上线提示
    CHART_MESSAGE(1,"好友信息"),
    SYSTEM_MESSAGE(2,"系统提示"),
    ONLINE_REMIND_MESSAGE(3,"好友上线提示"),
    OFF_LINE_REMIND_MESSAGE(4,"好友离线提示");

    MessageType(int code, String desc){
        this.code = code;
        this.desc = desc;
    }

    public static String getDescByCode(int code) {
        MessageType[] types = MessageType.values();
        for (MessageType type : types) {
            if (type.getCode() == code) {
                return type.getDesc();
            }
        }
        return null;
    }

    private int code;

    private String desc;

    public int getCode() { return code; }

    public String getDesc() { return desc; }
}
