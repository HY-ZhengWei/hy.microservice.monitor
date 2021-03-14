package org.hy.microservice.monitor.time;





/**
 * 时间 
 *
 * @author      ZhengWei(HY)
 * @createDate  2021-03-05
 * @version     v1.0
 */
public class Time
{
    
    /** Ntp服务器的名称或地址 */
    private String ntpServer;
    
    /** Ntp服务器的时间戳 */
    private long ntpTime;
    
    /** 延时时长。本机与Ntp服务器时间的差值（单位：毫秒）。用本机时间“加”上差值即可校正时间 */
    private long delay;

    
    
    /**
     * 获取：Ntp服务器的时间戳
     */
    public long getNtpTime()
    {
        return ntpTime;
    }

    
    /**
     * 获取：延时时长。本机与Ntp服务器时间的差值（单位：毫秒）。用本机时间“加”上差值即可校正时间。
     */
    public long getDelay()
    {
        return delay;
    }

    
    /**
     * 设置：Ntp服务器的时间戳
     * 
     * @param ntpTime 
     */
    public Time setNtpTime(long ntpTime)
    {
        this.ntpTime = ntpTime;
        return this;
    }

    
    /**
     * 设置：延时时长。本机与Ntp服务器时间的差值（单位：毫秒）。用本机时间“加”上差值即可校正时间。
     * 
     * @param delay 
     */
    public Time setDelay(long delay)
    {
        this.delay = delay;
        return this;
    }


    /**
     * 获取：Ntp服务器的名称或地址
     */
    public String getNtpServer()
    {
        return ntpServer;
    }


    /**
     * 设置：Ntp服务器的名称或地址
     * 
     * @param ntpServer 
     */
    public String setNtpServer(String ntpServer)
    {
        this.ntpServer = ntpServer;
        return this.ntpServer;
    }
    
}
