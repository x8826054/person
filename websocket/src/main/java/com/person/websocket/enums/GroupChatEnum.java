package com.person.websocket.enums;

/**
 * 类说明
 * <p>
 *
 * @author 徐擂
 * @version 1.0.0
 * @date 2019-1-24
 */
public enum GroupChatEnum {

    // 公共群聊类型 默认id
    ALL_GROUP(0,"全部群聊");

    GroupChatEnum(int code, String desc){
        this.code = code;
        this.desc = desc;
    }

    public static String getDescByCode(int code) {
        GroupChatEnum[] types = GroupChatEnum.values();
        for (GroupChatEnum type : types) {
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
