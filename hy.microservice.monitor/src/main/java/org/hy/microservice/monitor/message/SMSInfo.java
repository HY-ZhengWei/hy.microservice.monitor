package org.hy.microservice.monitor.message;

import org.hy.common.xml.SerializableDef;





/**
 * 短信信息
 *
 * @author      ZhengWei(HY)
 * @createDate  2021-02-08
 * @version     v1.0
 */
public class SMSInfo extends SerializableDef
{
    
    private static final long serialVersionUID = -4258611634934946387L;

    /** 手机号 */
    private String phone;
    
    /** 消息 */
    private String message;

    
    
    /**
     * 获取：手机号
     */
    public String getPhone()
    {
        return phone;
    }

    
    /**
     * 获取：消息
     */
    public String getMessage()
    {
        return message;
    }

    
    /**
     * 设置：手机号
     * 
     * @param phone 
     */
    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    
    /**
     * 设置：消息
     * 
     * @param message 
     */
    public void setMessage(String message)
    {
        this.message = message;
    }
    
}
