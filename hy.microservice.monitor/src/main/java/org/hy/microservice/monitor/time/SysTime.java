package org.hy.microservice.monitor.time;

import java.util.Calendar;
import java.util.List;

import org.hy.common.Date;
import org.hy.common.Help;
import org.hy.common.StringHelp;
import org.hy.common.xml.log.Logger;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.Structure;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinBase.SYSTEMTIME;





/**
 * 操作系统时间的工具类
 *
 * @author      ZhengWei(HY)
 * @createDate  2021-03-05
 * @version     v1.0
 */
public class SysTime
{
    private static Logger $Logger = Logger.getLogger(SysTime.class);
    
    
    
    /** Window操作类库（请使用管理员权限执行） */
    private Kernel32 kernel32;
    
    /** 操作系统的类型。1：Window   2：Linux */
    private int      osType;
    
    
    
    public SysTime()
    {
        String v_OSName = System.getProperty("os.name");
        
        if ( Help.isNull(v_OSName) )
        {
            v_OSName = "window";
            $Logger.warn("os.name is null.");
        }
        
        v_OSName = v_OSName.toLowerCase();
        
        if ( StringHelp.isContains(v_OSName ,"window") )
        {
            this.kernel32 = Kernel32.INSTANCE;
            this.osType   = 1;
        }
        else if ( StringHelp.isContains(v_OSName ,"linux") )
        {
            this.osType = 2;
        }
    }
    
    
    
    /**
     * 设置操作系统的时间
     * 
     * @author      ZhengWei(HY)
     * @createDate  2021-03-05
     * @version     v1.0
     *
     * @param i_TimeDelay  延时时长。本机与Ntp服务器时间的差值（单位：毫秒）。用本机时间“加”上差值即可校正时间
     * @return
     */
    public boolean setTime(long i_TimeDelay)
    {
        if ( i_TimeDelay == 0 )
        {
            return true;
        }
        
        if ( this.osType == 1 )
        {
            SYSTEMTIME v_SystemTime = new SYSTEMTIME();
            
            this.kernel32.GetLocalTime(v_SystemTime);
            
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(Date.getNowTime().getTime() + i_TimeDelay);
            
            v_SystemTime.wYear         = (short) c.get(Calendar.YEAR);
            v_SystemTime.wMonth        = (short)(c.get(Calendar.MONTH) + 1);
            v_SystemTime.wDay          = (short) c.get(Calendar.DAY_OF_MONTH );
            v_SystemTime.wDayOfWeek    = (short) c.get(Calendar.DAY_OF_WEEK);
            v_SystemTime.wHour         = (short) c.get(Calendar.HOUR_OF_DAY);
            v_SystemTime.wMinute       = (short) c.get(Calendar.MINUTE);
            v_SystemTime.wSecond       = (short) c.get(Calendar.SECOND);
            v_SystemTime.wMilliseconds = (short) c.get(Calendar.MILLISECOND);
            
            if ( Kernel32.INSTANCE.SetLocalTime(v_SystemTime) )
            {
                return true;
            }
            else
            {
                $Logger.error("校正系统时间异常：" + Kernel32.INSTANCE.GetLastError());
            }
        }
        else if ( this.osType == 2 )
        {
            Date v_NewTime = new Date(Date.getNowTime().getTime() + i_TimeDelay);
            List<String> v_Ret = Help.executeCommand(true ,"date" ,"-s" ,v_NewTime.getFullMilli());
            
            Help.print(v_Ret);
            return true;
            
//            LinuxTimeval.ByReference  tv = new LinuxTimeval.ByReference();
//            LinuxTimezone.ByReference tz = new LinuxTimezone.ByReference();
//            
//            int v_Ret = LinuxC.INSTANCE.gettimeofday(tv ,tz);
//            System.out.println(v_Ret);
//            
//            long v_NewTime = Date.getNowTime().getTime() + i_TimeDiff;
//            
//            tv.tv_sec  = new NativeLong(v_NewTime / 1000);
//            tv.tv_usec = new NativeLong((v_NewTime % 1000) * 1000);
//            
//            v_Ret = LinuxC.INSTANCE.settimeofday(tv ,tz);
//            System.out.println(v_Ret);
        }
        
        return false;
    }
    
    
    public static class LinuxTimeval extends Structure
    {
        public static class ByReference extends LinuxTimeval implements Structure.ByReference{}
        public static class ByValue     extends LinuxTimeval implements Structure.ByValue{}
        
        public NativeLong tv_sec; /* 秒数 */
        public NativeLong tv_usec; /* 微秒数 */
    }
    public static class LinuxTimezone extends Structure
    {
        public static class ByReference extends LinuxTimezone implements Structure.ByReference{}
        public static class ByValue     extends LinuxTimezone implements Structure.ByValue{}
        
        public int tz_minuteswest;
        public int tz_dsttime; 
    }

    
    public interface LinuxC extends Library {
        LinuxC INSTANCE = Native.load("c", LinuxC.class);
        
        int gettimeofday(LinuxTimeval.ByReference tv, LinuxTimezone.ByReference tz);
        int settimeofday(LinuxTimeval.ByReference tv, LinuxTimezone.ByReference tz);
        
    }

}
