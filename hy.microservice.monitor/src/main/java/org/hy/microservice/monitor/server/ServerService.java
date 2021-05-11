package org.hy.microservice.monitor.server;

import org.hy.common.Date;
import org.hy.common.Help;
import org.hy.common.StringHelp;
import org.hy.common.xml.annotation.Xjava;
import org.hy.common.xml.plugins.analyse.AnalyseBase;
import org.hy.common.xml.plugins.analyse.data.ClusterReport;





/**
 * 集群的服务器信息
 *
 * @author      ZhengWei(HY)
 * @createDate  2021-04-30
 * @version     v1.0
 */
@Xjava
public class ServerService
{
    
    @Xjava
    private IServerDAO  serverDAO;
    
    @Xjava
    private AnalyseBase analyseBase;
    
    
    
    /**
     * 记录日志
     * 
     * @author      ZhengWei(HY)
     * @createDate  2021-04-30
     * @version     v1.0
     *
     * @return
     */
    public boolean addLog()
    {
        ClusterReport v_CReport = this.analyseBase.analyseCluster_Info();
        ServerInfo    v_Server  = new ServerInfo();
        
        v_Server.setId(StringHelp.getUUID());
        v_Server.setHostName(      Help.getIPs());
        v_Server.setStartTime( new Date(v_CReport.getStartTime()));
        v_Server.setSystemTime(new Date(v_CReport.getSystemTime()));
        v_Server.setOsCPURate(          v_CReport.getOsCPURate());
        v_Server.setOsDiskMaxRate(      v_CReport.getLinuxDiskMaxRate());
        v_Server.setJvmMaxMemory(       v_CReport.getMaxMemory());
        v_Server.setJvmTotalMemory(     v_CReport.getTotalMemory());
        v_Server.setJvmFreeMemory(      v_CReport.getFreeMemory());
        v_Server.setThreadCount(        v_CReport.getThreadCount());
        v_Server.setQueueCount(         v_CReport.getQueueCount());
        v_Server.setJavaVersion(        v_CReport.getJavaVersion());
        
        if ( v_CReport.getOsType() == 1 )
        {
            v_Server.setOsType("Linux");
        }
        else if ( v_CReport.getOsType() == 2 )
        {
            v_Server.setOsType("Windows");
        }
        else
        {
            v_Server.setOsType("-");
        }
        
        if ( v_CReport.getLinuxMemoryRate() >= 0 )
        {
            v_Server.setOsMemoryRate(v_CReport.getLinuxMemoryRate());
        }
        else
        {
            v_Server.setOsMemoryRate(v_CReport.getOsMemoryRate());
        }
        
        return this.serverDAO.addLog(v_Server) >= 1;
    }
    
    
    
    /**
     * 实时监控
     * 
     * @author      ZhengWei(HY)
     * @createDate  2021-04-30
     * @version     v1.0
     *
     * @return
     */
    public void monitor()
    {
        this.addLog();
        this.serverDAO.QueryWarning();
    }
    
    
    
    /**
     * 更新监控日志（发送警告）
     * 
     * @author      ZhengWei(HY)
     * @createDate  2021-04-30
     * @version     v1.0
     * 
     * @param i_LogID    日志ID
     * @param i_Phone    告警手机号
     * @param i_Content  告警内容
     */
    public boolean logWarn(String i_LogID ,String i_Phone ,String i_Content)
    {
        ServerInfo v_Log = new ServerInfo();
        
        v_Log.setId(        i_LogID);
        v_Log.setWarnPhone( i_Phone);
        v_Log.setWarnCotent(i_Content);
        
        return this.serverDAO.updateLogWarn(v_Log) >= 1;
    }
    
}
