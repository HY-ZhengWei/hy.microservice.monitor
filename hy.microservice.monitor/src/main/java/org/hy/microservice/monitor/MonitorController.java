package org.hy.microservice.monitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;

import org.hy.common.Help;
import org.hy.common.StringHelp;
import org.hy.common.app.Param;
import org.hy.common.xml.XHttp;
import org.hy.common.xml.log.Logger;
import org.hy.microservice.common.BaseResponse;
import org.hy.microservice.monitor.user.UserService;
import org.hy.microservice.monitor.user.UserSSO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;





/**
 * 监控的控制层
 *
 * @author      ZhengWei(HY)
 * @createDate  2020-10-19
 * @version     v1.0
 */
@Controller
@RestController
@RequestMapping("monitor")
public class MonitorController
{
    
    private static final Logger $Logger = new Logger(MonitorController.class);
    
    @Autowired
    @Qualifier("UserService")
    private UserService userService;
    
    @Autowired
    @Qualifier("MS_Monitor_IsCheckToken")
    private Param isCheckToken;
    
    @Autowired
    @Qualifier("MS_Monitor_ClusterServers")
    private List<XHttp> serverList;
    
    
    
    /**
     * 注册服务
     * 
     * @author      ZhengWei(HY)
     * @createDate  2020-10-19
     * @version     v1.0
     *
     * @param i_PostInfo
     * @return
     */
    @RequestMapping(value="register" ,method={RequestMethod.POST} ,produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public BaseResponse<XHttp> register(@RequestParam("token") String i_Token ,@RequestBody XHttp i_Server)
    {
        this.getLocalServer();
        
        BaseResponse<XHttp> v_RetResp = new BaseResponse<XHttp>();
        
        if ( i_Server == null )
        {
            return v_RetResp.setCode("-1").setMessage("未收到任何参数");
        }
        
        if ( Help.isNull(i_Server.getIp()) ) 
        {
            return v_RetResp.setCode("-2").setMessage("服务IP地址为空");
        }
        
        
        if ( isCheckToken != null && Boolean.parseBoolean(isCheckToken.getValue()) )
        {
            // 验证票据及用户登录状态
            if ( Help.isNull(i_Token) ) 
            {
                return v_RetResp.setCode("-901").setMessage("非法访问");
            }
            
            UserSSO v_User = this.userService.getUser(i_Token);
            if ( v_User == null ) 
            {
                return v_RetResp.setCode("-901").setMessage("非法访问");
            }
        }
        
        
//        boolean v_AddRet = this.postService.addPost(i_PostInfo);
//        if ( v_AddRet )
//        {
//            $Logger.info("用户（" + i_PostInfo.getUserName() + i_PostInfo.getUserID() + "）发贴" + i_PostInfo.getTitle() + "，成功");
//            return this.queryPosts(i_PostInfo);
//        }
//        else
//        {
//            $Logger.error("用户（" + i_PostInfo.getUserName() + i_PostInfo.getUserID() + "）发贴" + i_PostInfo.getTitle() + "，异常");
//            return v_RetResp.setCode("-999").setMessage("系统异常");
//        }
        return null;
    }
    
    
    
    /**
     * 获取本机IP、服务端口。
     * 
     * @author      ZhengWei(HY)
     * @createDate  2021-02-20
     * @version     v1.0
     *
     * @return   当本机有多个IP地址时，返回多个
     */
    private List<XHttp> getLocalServer()
    {
        List<XHttp>       v_Ret          = new ArrayList<XHttp>();
        String            v_LocalID      = StringHelp.getUUID();
        List<MBeanServer> v_MBeanServers = MBeanServerFactory.findMBeanServer(null);
        
        if ( !Help.isNull(v_MBeanServers) ) 
        {
            for (MBeanServer v_MBeanServer : v_MBeanServers)
            {
                try 
                {
                    Set<ObjectName> v_ObjectNames = v_MBeanServer.queryNames(new ObjectName("Catalina:type=Connector,*"), null);
                
                    if ( Help.isNull(v_ObjectNames) ) 
                    {
                        // 没有发现JVM中关联的MBeanServer : " + mBeanServer.getDefaultDomain() + " 中的对象名称.";
                        continue;
                    }
                    
                    for (ObjectName v_ObjectName : v_ObjectNames) 
                    {
                        String v_Scheme = (String) v_MBeanServer.getAttribute(v_ObjectName ,"scheme");
                        if ( Help.isNull(v_Scheme) || !StringHelp.isContains(v_Scheme.toLowerCase() ,"http" ,"https") )
                        {
                            continue;
                        }
                        
                        String    v_IPs   = Help.getIPs();
                        String [] v_IPArr = v_IPs.split(" ");
                        
                        for (String v_IPName : v_IPArr)
                        {
                            String [] v_IPNameArr = v_IPName.split("=");
                            
                            if ( v_IPNameArr.length < 2 || v_IPNameArr[1].equals("127.0.0.1") )
                            {
                                continue;
                            }
                            
                            XHttp v_Server = new XHttp();
                            
                            v_Server.setComment(v_LocalID);
                            v_Server.setProtocol(v_Scheme);
                            v_Server.setIp(v_IPNameArr[1]);
                            v_Server.setPort((Integer) v_MBeanServer.getAttribute(v_ObjectName ,"port"));
                            
                            v_Ret.add(v_Server);
                        }
                    }
                } 
                catch (Exception exce) 
                {
                    $Logger.error(exce);
                }
            }
        }
        
        return v_Ret;
    }
    
}
