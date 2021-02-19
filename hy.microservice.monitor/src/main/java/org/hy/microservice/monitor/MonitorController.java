package org.hy.microservice.monitor;

import org.hy.common.app.Param;
import org.hy.common.xml.log.Logger;
import org.hy.microservice.monitor.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;





/**
 * 监控的控制层
 *
 * @author      ZhengWei(HY)
 * @createDate  2020-10-19
 * @version     v1.0
 */
@Controller
@RequestMapping("monitor")
public class MonitorController
{
    
    private static final Logger $Logger = new Logger(MonitorController.class);
    
    @Autowired
    @Qualifier("UserService")
    public UserService userService;
    
    @Autowired
    @Qualifier("MS_Monitor_IsCheckToken")
    public Param isCheckToken;
    
    
    
    
}
