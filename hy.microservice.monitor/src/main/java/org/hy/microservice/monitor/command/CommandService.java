package org.hy.microservice.monitor.command;

import java.util.List;

import org.hy.common.Help;
import org.hy.common.xml.XJava;
import org.hy.common.xml.log.Logger;





/**
 * 执行操作系统命令
 *
 * @author      ZhengWei(HY)
 * @createDate  2021-05-12
 * @version     v1.0
 */
public class CommandService
{
    private static final Logger $Logger = new Logger(CommandService.class);
    
    /** 命令 */
    private String cmd;
    
    
    
    public CommandService(String i_Cmd)
    {
        this.cmd = i_Cmd;
    }
    
    
    
    public void execute()
    {
        List<String> v_Ret = Help.executeCommand("UTF-8", true, true, XJava.getParam("MS_Monitor_Command_Timeout").getValueInt(), this.cmd);
        
        if ( !Help.isNull(v_Ret) )
        {
            StringBuilder v_Buffer = new StringBuilder();
            for (String v_Log : v_Ret)
            {
                v_Buffer.append(v_Log).append("\n");
            }
            
            $Logger.info(v_Buffer.toString());
        }
    }
    
}
