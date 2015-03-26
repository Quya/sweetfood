package com.quya.controller.user;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.quya.common.utils.exception.BusinessException;
import com.quya.controller.BaseJsonController;
import com.quya.model.User;
import com.quya.service.user.UserService;

@Controller
@RequestMapping("/userController")
public class UserController extends BaseJsonController{
	private UserService userService;

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	@RequestMapping("/regist.do")
	public String regist(HttpServletRequest request, HttpServletResponse response, RedirectAttributes attr){
		
		JSONObject reqJson = getJSONParams(request);		 
		 String power=reqJson.get("power").toString();
	
		 User user=new User();
		 user.setEmail(reqJson.get("email").toString());
		 user.setCredits(0);
		 user.setName(reqJson.get("username").toString());
		 user.setEmail(reqJson.get("email").toString());
		 user.setPassword(reqJson.get("password").toString());
		 user.setPhone(reqJson.get("phone").toString());
		 user.setSex(reqJson.get("sex").toString()); 
		 user.setPower(Integer.valueOf(power));
		 user.setPhotoId(0);
		 try{
			 userService.regist(user);
			 if(power.equals("1")){				 
				 System.out.println("商家注册成功");
				 request.getSession().setAttribute("user", user);// A session must be created before the response has been committed
				 sendMessageForObject(response, "1", "注册成功");
			 }else if(power.equals("2")){
				 System.out.println("客户注册成功");
				 request.getSession().setAttribute("user", user);
				 sendMessageForObject(response, "2", "注册成功");
			 }
			 System.out.println(user.toString());

		 }catch (BusinessException e) {
		            JSONObject ob = new JSONObject();
		            ob.put("state", "0");
		            ob.put("type", e.getExceptionType());
		            ob.put("value", e.getMessage());
		            this.sendMessage(response, ob.toString());
		        } catch (Exception e1) {
		        	e1.printStackTrace();
		            this.sendMessageForObject(response, "0", "出现系统错误，请及时与系统管理员联系");
		        }
		return null;
		
	}
	
	
}
