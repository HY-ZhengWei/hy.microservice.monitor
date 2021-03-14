package org.hy.microservice.monitor.time;

import java.util.ArrayList;
import java.util.List;

import org.hy.common.Busway;
import org.hy.common.Date;
import org.hy.common.Help;
import org.hy.common.Return;
import org.hy.common.xml.log.Logger;





/**
 * 同步校准时间
 *
 * @author      ZhengWei(HY)
 * @createDate  2021-03-12
 * @version     v1.0
 */
public class SyncTime
{
    private static Logger $Logger = Logger.getLogger(SyncTime.class);
    
    
    
    /** NTP服务器的列表 */
    private List<NtpTime> ntpServers;
    
    /** 历史校准时间的列表 */
    private Busway<Time>  historyTimes;
    
    /** 设定本机时间 */
    private SysTime       sysTime;
    
    
    
    public SyncTime()
    {
        this.ntpServers   = new ArrayList<NtpTime>();
        this.historyTimes = new Busway<Time>(10);
        this.sysTime      = new SysTime();
    }
    
    
    
    /**
     * 排除最差的Ntp服务（最大排除），同时判定其它Ntp服务是否一致。
     * 
     * @author      ZhengWei(HY)
     * @createDate  2021-03-08
     * @version     v1.0
     *
     * @param i_NtpTimes  所有Ntp服务的校准结果
     * @param i_Max       排除最大的值
     * @return            Return = 真，当其它服务一致时，返回最大的延时的
     *                    Return = 假，其它服务不一致时，也返回最大的延时，方便下一次判定   
    */ 
    private Return<Time> excludeWorstByMax(Time [] i_NtpTimes ,long i_Max)
    {
        Time v_MaxDelay = new Time().setDelay(Long.MIN_VALUE);
        Time v_MinDelay = new Time().setDelay(Long.MAX_VALUE);
        int  v_NtpCount = 0;
        
        for (int x=0; x<i_NtpTimes.length; x++)
        {
            Time v_NtpTime = i_NtpTimes[x];
            
            if ( v_NtpTime != null )
            {
                if ( v_NtpTime.getDelay() >= i_Max )
                {
                    continue;
                }
                
                v_NtpCount++;
                
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
        
        boolean v_NtpIsOk = v_MaxDelay.getDelay() - v_MinDelay.getDelay() <= 1000 * 30;
        if ( v_NtpCount >= 2 && v_NtpIsOk )
        {
            return new Return<Time>(true).setParamObj(v_MaxDelay).setParamInt(v_NtpCount);
        }
        else if ( v_NtpCount >= 1 ) 
        {
            return new Return<Time>(false).setParamObj(v_MaxDelay).setParamInt(v_NtpCount);
        }
        else
        {
            return null;
        }
    }
    
    
    
    
    /**
     * 排除最差的Ntp服务（最小排除），同时判定其它Ntp服务是否一致。
     * 
     * @author      ZhengWei(HY)
     * @createDate  2021-03-08
     * @version     v1.0
     *
     * @param i_NtpTimes  所有Ntp服务的校准结果
     * @param i_Max       排除最小的值
     * @return            Return = 真，当其它服务一致时，返回最小的延时的
     *                    Return = 假，其它服务不一致时，也返回最小的延时，方便下一次判定   
    */ 
    private Return<Time> excludeWorstByMin(Time [] i_NtpTimes ,long i_Min)
    {
        Time v_MaxDelay = new Time().setDelay(Long.MIN_VALUE);
        Time v_MinDelay = new Time().setDelay(Long.MAX_VALUE);
        int  v_NtpCount = 0;
        
        for (int x=0; x<i_NtpTimes.length; x++)
        {
            Time v_NtpTime = i_NtpTimes[x];
            
            if ( v_NtpTime != null )
            {
                if ( v_NtpTime.getDelay() <= i_Min )
                {
                    continue;
                }
                
                v_NtpCount++;
                
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
        
        boolean v_NtpIsOk = v_MaxDelay.getDelay() - v_MinDelay.getDelay() <= 1000 * 30;
        if ( v_NtpCount >= 2 && v_NtpIsOk )
        {
            return new Return<Time>(true).setParamObj(v_MinDelay).setParamInt(v_NtpCount);
        }
        else if ( v_NtpCount >= 1 ) 
        {
            return new Return<Time>(false).setParamObj(v_MinDelay).setParamInt(v_NtpCount);
        }
        else
        {
            return null;
        }
    }
    
    
    
    /**
     * 校准时间
     * 
     * @author      ZhengWei(HY)
     * @createDate  2021-03-10
     * @version     v1.0
     *
     */
    public synchronized void syncTime()
    {
        if ( Help.isNull(this.ntpServers) )
        {
            return;
        }
        
        // 先获取所有Ntp服务的时间
        // 预先找出单个Ntp服务与历史记录对比最好的Ntp服务时间
        Time [] v_NtpTimes  = new Time[this.ntpServers.size()];
        Time    v_BestTime  = null;
        int     v_BestLevel = 0;
        for (int x=0; x<this.ntpServers.size(); x++)
        {
            NtpTime v_NtpServer = this.ntpServers.get(x);
            Time    v_NtpTime   = v_NtpServer.getTime();
            
            v_NtpTimes[x] = v_NtpTime;
            
            if ( v_NtpTime == null )
            {
                continue;
            }
            
            if ( this.historyTimes.size() <= 0 )
            {
                v_BestTime  = v_NtpTime;
                v_BestLevel = 1;
                break;
            }
            
            Object [] v_HistoryTimes = this.historyTimes.getArray();
            Time      v_Last1        = (Time)v_HistoryTimes[v_HistoryTimes.length - 1];
            long      v_Diff0_1      = v_NtpTime.getNtpTime() - v_Last1.getNtpTime();
            
            if ( this.historyTimes.size() == 1 )
            {
                if ( v_Diff0_1 > 0 )
                {
                    if ( v_BestTime == null || v_BestLevel < 2 )
                    {
                        v_BestTime  = v_NtpTime;
                        v_BestLevel = 2;
                    }
                }
            }
            else
            {
                Time v_Last2   = (Time)v_HistoryTimes[v_HistoryTimes.length - 2];
                long v_Diff1_2 = v_Last1.getNtpTime() - v_Last2.getNtpTime();
                
                if ( Math.abs(v_Diff0_1 - v_Diff1_2) <= v_NtpServer.getTimeout() * 1000 )
                {
                    if ( v_BestTime == null || v_BestLevel < 3 )
                    {
                        v_BestTime  = v_NtpTime;
                        v_BestLevel = 3;
                        break;
                    }
                }
            }
        }
        
        
        // 排除最差的Ntp服务（最大排除）
        long         v_MaxDelay = Long.MAX_VALUE;
        Return<Time> v_EWMax    = null;
        do 
        {
            v_EWMax = this.excludeWorstByMax(v_NtpTimes ,v_MaxDelay);
            
            if ( v_EWMax != null && v_EWMax.booleanValue() )
            {
                String v_Info = "";
                if ( v_EWMax.getParamInt() == v_NtpTimes.length )
                {
                    v_Info = "所有";
                }
                else
                {
                    v_Info = "有" + v_EWMax.getParamInt() + "台";
                }
                
                this.setTime(v_EWMax.getParamObj() ,v_Info + "Ntp服务时间基本一致");
                return;
            }
            
            if ( v_EWMax != null && v_EWMax.paramObj != null )
            {
                v_MaxDelay = v_EWMax.getParamObj().getDelay();
            }
        }
        while ( v_EWMax != null );
        
        
        // 排除最差的Ntp服务（最小排除）
        long         v_MinDelay = Long.MIN_VALUE;
        Return<Time> v_EWMin    = null;
        do 
        {
            v_EWMin = this.excludeWorstByMin(v_NtpTimes ,v_MinDelay);
            
            if ( v_EWMin != null && v_EWMin.booleanValue() )
            {
                String v_Info = "";
                if ( v_EWMin.getParamInt() == v_NtpTimes.length )
                {
                    v_Info = "所有";
                }
                else
                {
                    v_Info = "有" + v_EWMin.getParamInt() + "台";
                }
                
                this.setTime(v_EWMin.getParamObj() ,v_Info + "Ntp服务时间基本一致");
                return;
            }
            
            if ( v_EWMin != null && v_EWMin.paramObj != null )
            {
                v_MinDelay = v_EWMin.getParamObj().getDelay();
            }
        }
        while ( v_EWMin != null );
        
        
        // 最后只好用单个Ntp服务来校准时间
        if ( v_BestTime != null )
        {
            this.setTime(v_BestTime ,"采用 " +  v_BestTime.getNtpServer() + " 校准时间");
        }
        else
        {
            $Logger.info("没有合适的Ntp校准时间服务。\n\n\n");
        }
    }
    
    
    
    private void setTime(Time i_Time ,String i_Hint)
    {
        this.historyTimes.put(i_Time);
        
        $Logger.info(i_Hint);
        
        if ( this.sysTime.setTime(i_Time.getDelay()) )
        {
            $Logger.info(i_Time.getNtpServer() + "校准时间完成：" + Date.getNowTime().getFullMilli() + "\n\n\n");
        }
        else
        {
            $Logger.info(i_Time.getNtpServer() + "校准时间异常。\n\n\n");
        }
    }
    
    
    
    public synchronized void setAddNtpServer(NtpTime i_NtpTime)
    {
        this.ntpServers.add(i_NtpTime);
    }
    
    
    
    public void clear()
    {
        this.ntpServers  .clear();
        this.historyTimes.clear();
    }
    
}
