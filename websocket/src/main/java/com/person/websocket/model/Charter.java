package com.person.websocket.model;

/**
 * 聊天人
 * <p>
 *
 * @author 徐擂
 * @version 1.0.0
 * @date 2019-1-11
 */
public class Charter {

    /**
     * 网名
     */
    private String nickName;

    /**
     * 性别 1.男 2.女
     */
    private String gentle;

    /**
     * 头像
     */
    private String image;

    /**
     * 座右铭
     */
    private String motto;

    public String getNickName() { return nickName; }

    public void setNickName(String nickName) { this.nickName = nickName; }

    public String getGentle() { return gentle; }

    public void setGentle(String gentle) { this.gentle = gentle; }

    public String getImage() { return image; }

    public void setImage(String image) { this.image = image; }

    public String getMotto() { return motto; }

    public void setMotto(String motto) { this.motto = motto; }
}
