package org.hy.microservice.monitor.time;

import java.net.InetAddress;
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
public class NtpTime
{
    
    private static Logger $Logger = Logger.getLogger(NtpTime.class);
    
    
    
    /** Ntp服务器的IP地址 */
    private InetAddress ntpServerHost;
    
    /** Ntp服务器的端口 */
    private int         ntpServerPort;
    
    /** 通讯超时时长(单位：毫秒) */
    private int         timeout;
    
    
    
    public NtpTime(String i_Host) throws UnknownHostException
    {
        this(i_Host ,NTPUDPClient.DEFAULT_PORT);
    }
    
    
    
    public NtpTime(String i_Host ,int i_Port) throws UnknownHostException
    {
        this.ntpServerHost = InetAddress.getByName(i_Host);
        this.ntpServerPort = i_Port;
        this.timeout       = 10000;
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
        return this.getTime(this.ntpServerHost ,this.ntpServerPort);
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
     * @param i_Host  Ntp服务器的IP地址
     * @param i_Port  Ntp服务器的端口
     * 
     * @return        异常时返回NULL
     */
    public Time getTime(InetAddress i_Host ,int i_Port)
    {
        NTPUDPClient v_NtpClient = new NTPUDPClient();
        
        v_NtpClient.setDefaultTimeout(this.timeout);
                
        try
        {
            TimeInfo v_NtpTime  = v_NtpClient.getTime(i_Host ,i_Port);
            Time     v_RetTime  = new Time();
            long     v_OldValue = Date.getNowTime().getTime();
            
            v_RetTime.setNtpTime(v_NtpTime.getMessage().getTransmitTimeStamp().getTime());
            v_RetTime.setDelay(v_RetTime.getNtpTime() - v_OldValue);
            
            System.out.println("本机的时间：" + new Date(v_OldValue)            .getFullMilli()); 
            System.out.println("校准的时间：" + new Date(v_RetTime.getNtpTime()).getFullMilli()); 
            System.out.println("校准时间的差值：" + v_RetTime.getDelay()); 
            
            return v_RetTime;
        }
        catch (Exception exce)
        {
            $Logger.error(exce);
        }
        
        return null;
    }
    
}
