package com.person.websocket.model;


import com.person.websocket.util.DateUtils;

import java.io.Serializable;

/**
 * 消息实体
 * <p>
 *
 * @author 徐擂
 * @version 1.0.0
 * @date 2019-1-11
 */
public class Message implements Serializable{

    /**
     * 消息内容
     */
    private String content;

    /**
     * 消息时间
     */
    private String createTime;

    /**
     * 发送人
     */
    private String target;

    /**
     * 消息类型: 1.好友信息  2.群聊信息  3.系统提示  4.好友上线提示
     */
    private Integer type;

    /**
     * 备注
     */
    private Object remark;

    public Message(){
        this.createTime = DateUtils.getCurrentTime(DateUtils.FORMAT_NORMAL);
    }

    public Message(String content, Integer type){
        this();
        this.content = content;
        this.type = type;
    }

    public Message(String content, Integer type, Object remark){
        this();
        this.content = content;
        this.type = type;
        this.remark = remark;
    }


    public String getContent() { return content; }

    public void setContent(String content) { this.content = content; }

    public String getCreateTime() { return createTime; }

    public void setCreateTime(String createTime) { this.createTime = createTime; }

    public Integer getType() { return type; }

    public void setType(Integer type) { this.type = type; }

    public String getTarget() { return target; }

    public void setTarget(String target) { this.target = target; }

    public Object getRemark() { return remark; }

    public void setRemark(Object remark) { this.remark = remark; }
}
