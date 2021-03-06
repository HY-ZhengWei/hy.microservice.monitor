package org.hy.microservice.monitor.time;

import java.util.ArrayList;
import java.util.List;

import org.hy.common.Busway;
import org.hy.common.Help;





public class SyncTime
{
    private List<NtpTime>      ntpServers;
    
    private List<Busway<Time>> historyTimes;
    
    private SysTime            sysTime;
    
    
    
    public SyncTime()
    {
        this.ntpServers   = new ArrayList<NtpTime>();
        this.historyTimes = new ArrayList<Busway<Time>>();
        this.sysTime      = new SysTime();
    }
    
    
    public synchronized void syncTime()
    {
        if ( Help.isNull(this.ntpServers) )
        {
            return;
        }
        
        // 先获取所有Ntp服务的时间
        Time [] v_NtpTimes = new Time[this.ntpServers.size()];
        Time    v_MaxDelay = new Time().setDelay(Long.MIN_VALUE);
        Time    v_MinDelay = new Time().setDelay(Long.MAX_VALUE);
        
        for (int x=0; x<this.ntpServers.size(); x++)
        {
            NtpTime v_NtpServer = this.ntpServers.get(x);
            Time    v_NtpTime   = v_NtpServer.getTime();
            
            v_NtpTimes[x] = v_NtpTime;
            
            if ( v_NtpTime != null )
            {
                if ( v_NtpTime.getDelay() > v_MaxDelay.getDelay() )
                {
                    v_MaxDelay = v_NtpTime;
                }
                
                if ( v_NtpTime.getDelay() < v_MinDelay.getDelay() )
                {
                    v_MinDelay = v_NtpTime;
                }
            }
        }
        
        // 通过所有NTP服务时间，比较最大延时与最小延时来判定NTP服务自身的时间是否有异常
        boolean v_NtpIsOk = v_MaxDelay.getDelay() - v_MinDelay.getDelay() <= 1000 * 60;
        
        for (int x=0; x<this.ntpServers.size(); x++)
        {
            Time         v_NtpTime = v_NtpTimes[x];
            Busway<Time> v_History = this.historyTimes.get(x);
            
            if ( v_History.size() <= 0 )
            {
                v_History.put(v_NtpTime);
            }
            else
            {
                Time [] v_HistoryTimes = (Time[]) v_History.getArray();
                
            }
        }
    }
    
    
    public synchronized void setAddNtpServer(NtpTime i_NtpTime)
    {
        this.ntpServers  .add(i_NtpTime);
        this.historyTimes.add(new Busway<Time>(10));
    }
    
    
    public void clear()
    {
        this.ntpServers  .clear();
        this.historyTimes.clear();
    }
    
}
