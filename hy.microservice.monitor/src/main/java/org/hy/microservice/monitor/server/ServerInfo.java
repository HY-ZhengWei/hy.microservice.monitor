package org.hy.microservice.monitor.server;

import org.hy.common.Date;





/**
 * 集群的服务器信息
 *
 * @author      ZhengWei(HY)
 * @createDate  2021-04-30
 * @version     v1.0
 */
public class ServerInfo
{
    /** 主键 */
    private String id;
    
    /** 操作类型（1:Linux，1:Unix，2:Windows） */
    private String osType;
    
    /** 主机名称 */
    private String hostName;
    
    /** 启动时间 */
    private Date   startTime;
    
    /** 操作系统的当前时间 */
    private Date   systemTime;
    
    /** 操作系统CPU使用率 */
    private double osCPURate;
    
    /** 操作系统CPU使用率（3分钟内的平均值） */
    private double osCPURate3Avg;
    
    /** 操作系统CPU使用率（4分钟内的平均值） */
    private double osCPURate4Avg;
    
    /** 操作系统CPU使用率（5分钟内的平均值） */
    private double osCPURate5Avg;
    
    /** 操作系统内存使用率 */
    private double osMemoryRate;
    
    /** 操作系统最大硬盘的使用率 */
    private double osDiskMaxRate;
    
    /** JVM最大内存：Java虚拟机（这个进程）能构从操作系统那里挖到的最大的内存。JVM参数为：-Xmx */
    private long   jvmMaxMemory;
    
    /** JVM内存总量：Java虚拟机现在已经从操作系统那里挖过来的内存大小。JVM参数为：-Xms */
    private long   jvmTotalMemory;
    
    /** JVM空闲内存 */
    private long   jvmFreeMemory;
    
    /** 线程总数 */
    private long   threadCount;
    
    /** 队列等待的任务数 */
    private long   queueCount;
    
    /** 服务器情况（正常、异常） */
    private String serverStatus;
    
    /** 运行时的JDK版本 */
    private String javaVersion;
    
    /** 是否已发送警告。1:是； 0:否 */
    private String isWarning;
    
    /** 警告时间 */
    private Date   warnTime;
    
    /** 警告手机 */
    private String warnPhone;
    
    /** 警告内容 */
    private String warnCotent;

    
    
    public String getId()
    {
        return id;
    }

    
    public void setId(String id)
    {
        this.id = id;
    }

    
    public String getOsType()
    {
        return osType;
    }

    
    public void setOsType(String osType)
    {
        this.osType = osType;
    }

    
    public String getHostName()
    {
        return hostName;
    }

    
    public void setHostName(String hostName)
    {
        this.hostName = hostName;
    }

    
    public Date getStartTime()
    {
        return startTime;
    }

    
    public void setStartTime(Date startTime)
    {
        this.startTime = startTime;
    }

    
    public Date getSystemTime()
    {
        return systemTime;
    }

    
    public void setSystemTime(Date systemTime)
    {
        this.systemTime = systemTime;
    }

    
    public double getOsCPURate()
    {
        return osCPURate;
    }

    
    public void setOsCPURate(double osCPURate)
    {
        this.osCPURate = osCPURate;
    }

    
    public double getOsMemoryRate()
    {
        return osMemoryRate;
    }

    
    public void setOsMemoryRate(double osMemoryRate)
    {
        this.osMemoryRate = osMemoryRate;
    }

    
    public long getJvmMaxMemory()
    {
        return jvmMaxMemory;
    }

    
    public void setJvmMaxMemory(long jvmMaxMemory)
    {
        this.jvmMaxMemory = jvmMaxMemory;
    }

    
    public long getJvmTotalMemory()
    {
        return jvmTotalMemory;
    }

    
    public void setJvmTotalMemory(long jvmTotalMemory)
    {
        this.jvmTotalMemory = jvmTotalMemory;
    }

    
    public long getJvmFreeMemory()
    {
        return jvmFreeMemory;
    }

    
    public void setJvmFreeMemory(long jvmFreeMemory)
    {
        this.jvmFreeMemory = jvmFreeMemory;
    }

    
    public long getThreadCount()
    {
        return threadCount;
    }

    
    public void setThreadCount(long threadCount)
    {
        this.threadCount = threadCount;
    }

    
    public long getQueueCount()
    {
        return queueCount;
    }

    
    public void setQueueCount(long queueCount)
    {
        this.queueCount = queueCount;
    }

    
    public String getServerStatus()
    {
        return serverStatus;
    }

    
    public void setServerStatus(String serverStatus)
    {
        this.serverStatus = serverStatus;
    }

    
    public String getJavaVersion()
    {
        return javaVersion;
    }

    
    public void setJavaVersion(String javaVersion)
    {
        this.javaVersion = javaVersion;
    }

    
    public String getIsWarning()
    {
        return isWarning;
    }

    
    public void setIsWarning(String isWarning)
    {
        this.isWarning = isWarning;
    }

    
    public Date getWarnTime()
    {
        return warnTime;
    }

    
    public void setWarnTime(Date warnTime)
    {
        this.warnTime = warnTime;
    }

    
    public String getWarnPhone()
    {
        return warnPhone;
    }

    
    public void setWarnPhone(String warnPhone)
    {
        this.warnPhone = warnPhone;
    }

    
    public String getWarnCotent()
    {
        return warnCotent;
    }

    
    public void setWarnCotent(String warnCotent)
    {
        this.warnCotent = warnCotent;
    }

    
    public double getOsDiskMaxRate()
    {
        return osDiskMaxRate;
    }

    
    public void setOsDiskMaxRate(double osDiskMaxRate)
    {
        this.osDiskMaxRate = osDiskMaxRate;
    }

    
    public double getOsCPURate4Avg()
    {
        return osCPURate4Avg;
    }

    
    public void setOsCPURate4Avg(double osCPURate4Avg)
    {
        this.osCPURate4Avg = osCPURate4Avg;
    }

    
    public double getOsCPURate3Avg()
    {
        return osCPURate3Avg;
    }

    
    public void setOsCPURate3Avg(double osCPURate3Avg)
    {
        this.osCPURate3Avg = osCPURate3Avg;
    }

    
    public double getOsCPURate5Avg()
    {
        return osCPURate5Avg;
    }


    public void setOsCPURate5Avg(double osCPURate5Avg)
    {
        this.osCPURate5Avg = osCPURate5Avg;
    }
    
}
