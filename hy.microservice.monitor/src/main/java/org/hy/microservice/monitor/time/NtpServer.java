package org.hy.microservice.monitor.time;

import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;
import org.hy.common.Date;
import org.hy.common.xml.log.Logger;





/**
 * NTP时间同步
 *
 * @author      ZhengWei(HY)
 * @createDate  2021-03-04
 * @version     v1.0
 */
public class NtpServer
{
    
    private static Logger $Logger = Logger.getLogger(NtpServer.class);
    
    
    
    /** Ntp服务器的名称 */
    private String      ntpServerName;
    
    /** Ntp服务器的IP地址 */
    private InetAddress ntpServerHost;
    
    /** Ntp服务器的端口 */
    private int         ntpServerPort;
    
    /** 通讯超时时长(单位：毫秒) */
    private int         timeout;
    
    
    
    public NtpServer(String i_Host) throws UnknownHostException
    {
        this(i_Host ,NTPUDPClient.DEFAULT_PORT);
    }
    
    
    
    public NtpServer(String i_Host ,int i_Port) throws UnknownHostException
    {
        this.ntpServerName = i_Host;
        this.ntpServerHost = InetAddress.getByName(i_Host);
        this.ntpServerPort = i_Port;
        this.timeout       = 10000;
        
        if ( this.ntpServerName.equals(this.ntpServerHost.getHostName()) )
        {
            $Logger.info("注册NTP服务：" + this.ntpServerHost.getHostName() + ":" + this.ntpServerPort);
        }
        else
        {
            $Logger.info("注册NTP服务：" + this.ntpServerName + "(" +this.ntpServerHost.getHostName() + "):" + this.ntpServerPort);
        }
    }
    
    
    
    /**
     * 获取：通讯超时时长(单位：毫秒)
     */
    public int getTimeout()
    {
        return timeout;
    }


    
    /**
     * 设置：通讯超时时长(单位：毫秒)
     * 
     * @param timeout 
     */
    public void setTimeout(int timeout)
    {
        this.timeout = timeout;
    }


    
    /**
     * 获取：Ntp服务器的IP地址
     */
    public InetAddress getNtpServerHost()
    {
        return ntpServerHost;
    }


    
    /**
     * 获取：Ntp服务器的端口
     */
    public int getNtpServerPort()
    {
        return ntpServerPort;
    }
    
    
    
    /**
     * 与Ntp服务器时间的差值（单位：毫秒）。
     * 
     * 用本系统时间“加”上差值即可校正时间。
     * 
     * @author      ZhengWei(HY)
     * @createDate  2021-03-05
     * @version     v1.0
     *
     * @return      异常时返回NULL
     */
    public Time getTime()
    {
        NTPUDPClient v_NtpClient = new NTPUDPClient();
        
        v_NtpClient.setDefaultTimeout(this.timeout);
                
        try
        {
            TimeInfo v_NtpTime  = v_NtpClient.getTime(this.ntpServerHost ,this.ntpServerPort);
            Time     v_RetTime  = new Time();
            long     v_OldValue = Date.getNowTime().getTime();
            
            v_RetTime.setNtpTime(v_NtpTime.getMessage().getTransmitTimeStamp().getTime());
            v_RetTime.setDelay(v_RetTime.getNtpTime() - v_OldValue);
            v_RetTime.setNtpServer(this.ntpServerName);
            
            $Logger.info("时间的校准：" + v_RetTime.getNtpServer());
            $Logger.info("本机的时间：" + new Date(v_OldValue)            .getFullMilli()); 
            $Logger.info("校准的时间：" + new Date(v_RetTime.getNtpTime()).getFullMilli()); 
            $Logger.info("校准的时差：" + v_RetTime.getDelay() + "\n"); 
            
            return v_RetTime;
        }
        catch (SocketTimeoutException exce)
        {
            $Logger.error("时间的校准：" + this.ntpServerName + " 连接超时。");
        }
        catch (Exception exce)
        {
            $Logger.error(exce);
        }
        
        return null;
    }
    
}
