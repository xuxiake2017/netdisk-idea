package group.xuxiake.filter;

import com.google.gson.Gson;
import group.xuxiake.entity.UserNetdisk;
import group.xuxiake.util.NetdiskConstant;
import group.xuxiake.util.NetdiskErrMsgConstant;
import group.xuxiake.util.Result;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.filter.authc.UserFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class VerifyUserFilter extends UserFilter {

    @Override
    protected boolean isAccessAllowed(ServletRequest arg0, ServletResponse arg1, Object arg2) {

        HttpServletRequest request = (HttpServletRequest) arg0;
        HttpServletResponse response = (HttpServletResponse) arg1;
        if (SecurityUtils.getSubject().isAuthenticated()) {
            UserNetdisk userNetdisk = (UserNetdisk) SecurityUtils.getSubject().getPrincipal();
            Integer userStatus = new Integer(userNetdisk.getUserStatus());
            if (userStatus == NetdiskConstant.USER_STATUS_NORMAL || userStatus == NetdiskConstant.USER_STATUS_VIP) {
                return true;
            } else {
                response.setContentType("application/json;charset=UTF-8");
                try(PrintWriter writer = response.getWriter()) {
                    Gson gson = new Gson();
                    Result result = new Result();
                    result.setCode(NetdiskErrMsgConstant.USER_IS_NOT_VERIFY);
                    result.setMsg(NetdiskErrMsgConstant.getErrMessage(NetdiskErrMsgConstant.USER_IS_NOT_VERIFY));
                    writer.println(gson.toJson(result));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            response.setContentType("application/json;charset=UTF-8");
            try(PrintWriter writer = response.getWriter()) {
                Gson gson = new Gson();
                Result result = new Result();
                result.setCode(NetdiskErrMsgConstant.UN_AUTHENTICATED);
                result.setMsg(NetdiskErrMsgConstant.getErrMessage(NetdiskErrMsgConstant.UN_AUTHENTICATED));
                writer.println(gson.toJson(result));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        return super.onAccessDenied(request, response);
    }
}
