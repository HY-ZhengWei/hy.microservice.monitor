package org.hy.microservice.monitor.user;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.hy.common.Date;
import org.hy.common.Help;
import org.hy.common.Return;
import org.hy.common.license.AppKey;
import org.hy.common.license.sign.ISignaturer;
import org.hy.common.xml.XHttp;
import org.hy.common.xml.XJSON;
import org.hy.common.xml.annotation.Xjava;
import org.hy.common.xml.log.Logger;
import org.hy.microservice.common.BaseResp;





/**
 * 集成认证中心的用户业务类
 *
 * @author      ZhengWei(HY)
 * @createDate  2021-02-04
 * @version     v1.0
 */
@Xjava
public class XSSOUserService
{
    
    public  static final String $Succeed = "200";
    
    private static final Logger $Logger = Logger.getLogger(XSSOUserService.class);
    
    
    
    /** 当前的票据 */
    private static TokenInfo         $Token           = null;
    
    /** 获取票据的时间 */
    private static long              $TokenTime       = 0L;
    
    /** 获取票据的过期时长（单位：秒） */
    private static int               $TokenExpire     = 0;
    
    
    
    @Xjava(ref="XHTTP_MS_Monitor_GetAccessToken")
    private XHttp xhGetAccessToken;
    
    @Xjava(ref="XHTTP_MS_Monitor_SetLoginUser")
    private XHttp xhSetLoginUser;
    
    @Xjava(ref="MS_Monitor_AppKey")
    private AppKey appKey;
    
    /** 数字签名 */
    @Xjava(ref="MS_Monitor_Signaturer")
    private ISignaturer signaturer;
    
    
    
    /**
     * 获取票据
     * 
     * @author      ZhengWei(HY)
     * @createDate  2021-02-04
     * @version     v1.0
     *
     * @return
     */
    public TokenInfo getToken()
    {
        long v_Timestamp = Date.getNowTime().getTime();
        if ( $TokenTime + $TokenExpire * 1000 > v_Timestamp )
        {
            return $Token;
        }
        
        try
        {
            String v_Signature = this.signaturer.sign("appKey" + appKey.getAppKey() + "timestamp" + v_Timestamp);
            Map<String ,Object> v_ReqParams = new HashMap<String ,Object>();
            v_ReqParams.put("appKey"    ,appKey.getAppKey());
            v_ReqParams.put("timestamp" ,v_Timestamp);
            v_ReqParams.put("signature" ,URLEncoder.encode(v_Signature ,"UTF-8"));
            
            // $Logger.error("集成认证登录：" + appKey.getAppKey() + " - " + v_Timestamp + " - " + v_Signature);
            
            Return<?> v_Ret = xhGetAccessToken.request(v_ReqParams);
            
            if ( v_Ret != null && v_Ret.booleanValue() && !Help.isNull(v_Ret.getParamStr()) )
            {
                XJSON v_XJson = new XJSON();
                
                TokenResponse v_Data = (TokenResponse)v_XJson.toJava(v_Ret.getParamStr() ,TokenResponse.class);
                
                if ( v_Data != null )
                {
                    if ( $Succeed.equals(v_Data.getCode()) && v_Data.getData() != null && v_Data.getData().getData() != null )
                    {
                        TokenInfo v_Token = v_Data.getData().getData();
                        $Logger.info("获取Token：" + v_Token.getAccessToken() + " ,过期时长：" + v_Token.getExpire());
                        $TokenTime   = Date.getNowTime().getTime();
                        $TokenExpire = v_Token.getExpire() - 10;    // 为容错而减10秒
                        $Token       = v_Token;
                        return v_Token;
                    }
                    else
                    {
                        $Logger.error("获取Token异常：" + v_Data.getCode() + " - " + v_Data.getMessage());
                    }
                }
            }
        }
        catch (Exception exce)
        {
            exce.printStackTrace();
        }
        
        return null;
    }
    
    
    
    /**
     * 用户登录
     * 
     * @author      ZhengWei(HY)
     * @createDate  2021-02-04
     * @version     v1.0
     *
     * @param i_Code
     * @param i_UserSSO
     */
    public boolean loginUser(String i_Code ,UserSSO i_UserSSO)
    {
        i_UserSSO.setAppKey(this.appKey.getAppKey());
        
        Map<String ,Object> v_ReqParams = new HashMap<String ,Object>();
        v_ReqParams.put("code" ,i_Code);
        
        try
        {
            XJSON v_XJson = new XJSON();
            v_XJson.setReturnNVL(false);
            
            Return<?> v_Ret = xhSetLoginUser.request(v_ReqParams ,v_XJson.toJson(i_UserSSO).toJSONString());
            
            if ( v_Ret != null && v_Ret.booleanValue() && !Help.isNull(v_Ret.getParamStr()) )
            {
                BaseResp v_Data = (BaseResp)v_XJson.toJava(v_Ret.getParamStr() ,BaseResp.class);
                
                if ( v_Data != null )
                {
                    if ( $Succeed.equals(v_Data.getCode()) )
                    {
                        return true;
                    }
                    else
                    {
                        return false;
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
     * 用户登录
     * 
     * @author      ZhengWei(HY)
     * @createDate  2021-02-04
     * @version     v1.0
     *
     * @param i_UserSSO
     * @return  返回票据Token
     */
    public String loginUser(UserSSO i_UserSSO)
    {
        TokenInfo v_Token = this.getToken();
        
        if ( v_Token != null )
        {
            String v_Code = v_Token.getCode();
            
            v_Token.setCode(null);
            
            if ( !Help.isNull(v_Code) )
            {
                this.loginUser(v_Code ,i_UserSSO);
            }
            
            return v_Token.getAccessToken();
        }
        else
        {
            return null;
        }
    }
    
}
