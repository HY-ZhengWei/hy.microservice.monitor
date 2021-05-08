package org.hy.microservice.monitor.message;

import java.util.HashMap;
import java.util.Map;

import org.hy.common.Help;
import org.hy.common.Return;
import org.hy.common.xml.XHttp;
import org.hy.common.xml.XJSON;
import org.hy.common.xml.annotation.Xjava;
import org.hy.microservice.common.BaseResp;
import org.hy.microservice.monitor.user.UserSSO;
import org.hy.microservice.monitor.user.XSSOUserService;





/**
 * 短消息服务
 *
 * @author      ZhengWei(HY)
 * @createDate  2021-02-08
 * @version     v1.0
 */
@Xjava
public class MessageService
{
    public  static final String $Succeed = "200";
    
    @Xjava(ref="XHTTP_MS_Monitor_Message_SendSMS")
    private XHttp xhSendSMS;
    
    @Xjava(ref="XHTTP_MS_Monitor_Message_SendWeiXin")
    private XHttp xhSendWeiXin;
    
    @Xjava
    private XSSOUserService ssoUserService;
    
    
    
    /**
     * 发送短信
     * 
     * @author      ZhengWei(HY)
     * @createDate  2021-02-08
     * @version     v1.0
     *
     * @param i_Phone
     * @param i_Content
     * @return
     */
    public boolean sms(String i_Phone ,String i_Content)
    {
        UserSSO v_UserSSO = new UserSSO();
        
        v_UserSSO.setUserId("hy.micorservice.monitor");
        v_UserSSO.setUserName("监控服务");
        v_UserSSO.setUserSource("监控服务");
        
        String v_Token = ssoUserService.loginUser(v_UserSSO);
        
        if ( Help.isNull(v_UserSSO) )
        {
            return false;
        }
        
        try
        {
            XJSON v_XJson = new XJSON();
            
            Map<String ,Object> v_ReqParams = new HashMap<String ,Object>();
            v_ReqParams.put("token" ,v_Token);
            
            Reciver v_Reciver = new Reciver();
            
            v_Reciver.setPhone(i_Phone);
            v_Reciver.setMessage(i_Content);
            
            Return<?> v_Ret = xhSendSMS.request(v_ReqParams ,v_XJson.toJson(v_Reciver).toJSONString());
            
            if ( v_Ret != null && v_Ret.booleanValue() && !Help.isNull(v_Ret.getParamStr()) )
            {
                BaseResp v_Data = (BaseResp)v_XJson.toJava(v_Ret.getParamStr() ,BaseResp.class);
                
                if ( v_Data != null )
                {
                    if ( $Succeed.equals(v_Data.getCode()) )
                    {
                        return true;
                    }
                }
            }
        }
        catch (Exception exce)
        {
            exce.printStackTrace();
        }
        
        return false;
    }
    
    
    
    /**
     * 发送微信
     * 
     * @author      ZhengWei(HY)
     * @createDate  2021-02-08
     * @version     v1.0
     *
     * @param i_OpenID    微信的OpenID
     * @param i_Title     标题
     * @param i_Content   消息内容
     * @return
     */
    public boolean weixin(String i_Phone ,String i_Title ,String i_Content)
    {
        UserSSO v_UserSSO = new UserSSO();
        
        v_UserSSO.setUserId("hy.micorservice.monitor");
        v_UserSSO.setUserName("监控服务");
        v_UserSSO.setUserSource("监控服务");
        
        String v_Token = ssoUserService.loginUser(v_UserSSO);
        
        if ( Help.isNull(v_UserSSO) )
        {
            return false;
        }
        
        try
        {
            XJSON v_XJson = new XJSON();
            
            Map<String ,Object> v_ReqParams = new HashMap<String ,Object>();
            v_ReqParams.put("token" ,v_Token);
            
            Reciver v_Reciver = new Reciver();
            
            v_Reciver.setPhone(i_Phone);
            v_Reciver.setTitle(i_Title);
            v_Reciver.setMessage(i_Content);
            
            Return<?> v_Ret = xhSendSMS.request(v_ReqParams ,v_XJson.toJson(v_Reciver).toJSONString());
            
            if ( v_Ret != null && v_Ret.booleanValue() && !Help.isNull(v_Ret.getParamStr()) )
            {
                BaseResp v_Data = (BaseResp)v_XJson.toJava(v_Ret.getParamStr() ,BaseResp.class);
                
                if ( v_Data != null )
                {
                    if ( $Succeed.equals(v_Data.getCode()) )
                    {
                        return true;
                    }
                }
            }
        }
        catch (Exception exce)
        {
            exce.printStackTrace();
        }
        
        return false;
    }
    
}
