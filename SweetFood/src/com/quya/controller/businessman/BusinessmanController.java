package com.quya.controller.businessman;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.quya.controller.BaseJsonController;
import com.quya.model.Address;
import com.quya.model.User;
import com.quya.service.businessman.BusinessmanService;

@Controller
@RequestMapping("businessmanController")
public class BusinessmanController extends BaseJsonController {

	private BusinessmanService businessmanService;

	public BusinessmanService getBusinessmanService() {
		return businessmanService;
	}

	public void setBusinessmanService(BusinessmanService businessmanService) {
		this.businessmanService = businessmanService;
	}
	@RequestMapping("/loadInfo.do")
	public String loadInfo(HttpServletRequest request, HttpServletResponse response, RedirectAttributes attr){
		HttpSession session=request.getSession();
		User user=(User)session.getAttribute("user");		
        this.sendMessageForObject(response, "1",  user);
		return null;
	}

	@RequestMapping("/updateInfo.do")
	public String updateInfo(  MultipartFile photo,HttpServletRequest request, HttpServletResponse response){
		 System.out.println("========================================");
		 String realPath = request.getSession().getServletContext().getRealPath("/sweetfood/common/image");
	        // 上传文件的原名(即上传前的文件名字)
		 String originalFilename = null;
	       
	       originalFilename = photo.getOriginalFilename();
	       System.out.println("文件原名: " + originalFilename);
	       System.out.println("文件名称: " + photo.getName());
	       System.out.println("文件长度: " + photo.getSize());
	       System.out.println("文件类型: " + photo.getContentType());
	       System.out.println("========================================");
		
       
       try {
    	   SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmssSSS");
           String newFileName=sdf.format(new Date())+originalFilename.substring(originalFilename.lastIndexOf('.'));
           
           FileUtils.writeByteArrayToFile(new File(realPath, newFileName), photo.getBytes());
           User user=new User();
           user.setPhotoUrl("../common/image/"+newFileName);
           String prov=new String(request.getParameter("prov").getBytes("iso-8859-1"),"utf-8");
           String city=new String(request.getParameter("city").getBytes("iso-8859-1"),"utf-8");
           String dist=new String(request.getParameter("dist").getBytes("iso-8859-1"),"utf-8");
           String street=new String(request.getParameter("street").getBytes("iso-8859-1"),"utf-8");
           String indroduce=new String(request.getParameter("indroduce").getBytes("iso-8859-1"),"utf-8");
           String username=new String(request.getParameter("username").getBytes("iso-8859-1"),"utf-8");
           //user.setAddress(address);
           Address address=new Address();
           address.setCity(city);
           address.setDist(dist);
           address.setProvince(prov);
           address.setStreet(street);
           address.setId(Integer.valueOf(request.getParameter("addressId")));
           user.setAddress(address);
           user.setEmail(request.getParameter("email"));
           user.setId(Integer.valueOf(request.getParameter("id")));
           user.setIndroduce(indroduce);
           user.setPhone(request.getParameter("phone"));
           user.setName(username);
           user.setPower(Integer.valueOf(request.getParameter("power")));
           user.setSex(request.getParameter("sex"));
           businessmanService.updateInfo(user);
           System.out.println();
           HttpSession session=request.getSession();
           session.setAttribute("user", user);
   		   //User sessionUser=(User)session.getAttribute("user");
   			//System.out.println(user.toString());
           this.sendMessageForObject(response, "1", user);
       } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}catch(Exception e){
		e.printStackTrace();
	}
   	return null;
	}
}
