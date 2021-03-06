package org.hy.microservice.monitor.time;

import java.net.UnknownHostException;

public class JU_NtpTime
{
    
    /**
     * Window下请使用《管理员权限》的命令行窗口执行
     * 
     *      d:
     * 
     *      cd D:\apache-tomcat-9.0.31\webapps\hy.microservice.monitor\WEB-INF\classes
     * 
     *      java -cp .;..\lib\*;D:\apache-tomcat-9.0.31\lib\* org.hy.microservice.monitor.time.JU_NtpTime
     *      
     *      
     * Linux的测试命令
     * 
     *      cd /opt/apache-tomcat-9.0.43/webapps/msMonitor/WEB-INF/classes
     *      
     *      java -cp .:../lib/*:/opt/apache-tomcat-9.0.43/lib/* org.hy.microservice.monitor.time.JU_NtpTime
     * 
     * @author      ZhengWei(HY)
     * @createDate  2021-04-05
     * @version     v1.0
     *
     * @param i_Args
     * @throws UnknownHostException
     */
    public static void main(String [] i_Args) throws UnknownHostException
    {
        // NtpTime v_Ntp      = new NtpTime("time.windows.com");
        NtpTime v_Ntp  = new NtpTime("10.1.50.21");
        Time    v_Time = v_Ntp.getTime();
        
        new SysTime().setTime(v_Time.getDelay());
    }
    
}
