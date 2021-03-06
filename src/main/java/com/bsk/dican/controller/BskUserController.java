package com.bsk.dican.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bsk.common.vo.JsonResult;
import com.bsk.dican.entity.BskUser;
import com.bsk.dican.entity.BskUserMassage;
import com.bsk.dican.service.BskUserService;

@Controller
@RequestMapping("/user")
public class BskUserController {

	@Autowired
	private BskUserService bskUserService;

	@ResponseBody
	@RequestMapping("/doSaveObject")
	public JsonResult doSaveObject(BskUser bskUser) {
		bskUserService.saveObject(bskUser);
		return JsonResult.ok("save ok");
	}

	@ResponseBody
	@RequestMapping("/doLogin")
	public JsonResult doLogin(String phone, String password,
			HttpServletResponse response, HttpSession session) {
		
		Subject subject = SecurityUtils.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken(phone, password);
		
		subject.login(token);
		
		BskUser user = bskUserService.findObjectByPhone(phone);
		BskUserMassage bskUserMassage = bskUserService.findUserMessageByUserId(user.getId());
		
		Cookie cookie = new Cookie("id", user.getId().toString());
		response.addCookie(cookie);
		session.setAttribute("id", user.getId());

		return JsonResult.ok(bskUserMassage);
	}
}
