package org.hy.microservice.monitor.server;

import java.util.List;

import org.hy.common.xml.annotation.XType;
import org.hy.common.xml.annotation.Xjava;
import org.hy.common.xml.annotation.Xsql;





/**
 * 监控服务器的DAO
 *
 * @author      ZhengWei(HY)
 * @createDate  2021-04-30
 * @version     v1.0
 */
@Xjava(id="ServerDAO" ,value=XType.XSQL)
public interface IServerDAO
{
    
    /**
     * 查询监控日志
     * 
     * @author      ZhengWei(HY)
     * @createDate  2021-04-30
     * @version     v1.0
     *
     * @return
     */
    @Xsql("XSQL_MS_Monitor_Server_Query_Warning")
    public List<ServerInfo> QueryWarning();
    
    
    
    /**
     * 添加监控日志（日志开始）
     * 
     * @author      ZhengWei(HY)
     * @createDate  2021-04-30
     * @version     v1.0
     *
     * @return
     */
    @Xsql("XSQL_MS_Monitor_Server_Insert")
    public int addLog(ServerInfo i_Server);
    
    
    
    /**
     * 更新监控日志（发送警告）
     * 
     * @author      ZhengWei(HY)
     * @createDate  2021-04-30
     * @version     v1.0
     *
     * @return
     */
    @Xsql("XSQL_MS_Monitor_Server_Update_Warning")
    public int updateLogWarn(ServerInfo i_Server);
    
}
