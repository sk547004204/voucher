package com.voucher.weixin.MessageTemplate;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.voucher.manage.model.WeiXin;
import com.voucher.manage.service.WeiXinService;
import com.voucher.weixin.base.AdvancedUtil;
import com.voucher.weixin.base.CommonUtil;

public class ChatTemplateProcessor {
	private final String SEND_TEMPLAYE_MESSAGE_URL = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";

	private final int campusId=1;
	
	public String sendTemplateMessage(String accessToken, WxTemplate wxTemplate, WeiXinService weixinService) {	
		String jsonString = new Gson().toJson(wxTemplate).toString();	
		System.out.println("templatemessage="+jsonString);
		String requestUrl = SEND_TEMPLAYE_MESSAGE_URL.replace("ACCESS_TOKEN", accessToken);	
		JSONObject jsonObject = CommonUtil.httpsRequest(requestUrl, "POST", jsonString);
		System.out.println("jsonObject="+jsonObject);	
		if (null != jsonObject) {
			int errorCode = jsonObject.getInteger("errcode");		
			if (0 == errorCode) {
				System.out.println("模板消息发送成功");
				return "模板消息发送成功";
			} else  if(errorCode==40001||errorCode==42001){
				String appId, appSecret;
		        WeiXin weiXin;
				weiXin=weixinService.getCampusById(campusId);
		        
		        appId=weiXin.getAppId();
		        appSecret=weiXin.getAppSecret();
		        
       		      accessToken=AdvancedUtil.getAccessToken(appId, appSecret);
       		     Map<String, Object> paramMap=new HashMap<>();  
       		     paramMap.put("accessToken", accessToken);
      		    paramMap.put("campusId", campusId);
      		    paramMap.put("campusId", campusId);
      		    Date date=new Date();
      		    paramMap.put("createTime", date);
      		    System.out.println("errorcode="+errorCode+"   accessToken="+accessToken);
      		     weixinService.updateCampusById(paramMap);
      		    requestUrl = SEND_TEMPLAYE_MESSAGE_URL.replace("ACCESS_TOKEN", accessToken);
      		    jsonObject = CommonUtil.httpsRequest(requestUrl, "POST", jsonString);
      			System.out.println("jsonObject="+jsonObject);
      			errorCode = jsonObject.getInteger("errcode");		
    			if (0 == errorCode) {
    				System.out.println("模板消息发送成功");
    				return "模板消息发送成功";
    			}else{
    				String errorMsg = jsonObject.getString("errmsg");
    				System.out.println("模板消息发送失败,错误是 "+errorCode+",错误信息是"+ errorMsg);
    			    return "模板消息发送失败,错误是 "+errorCode+",错误信息是"+ errorMsg;
    			}
       		  }else{
				String errorMsg = jsonObject.getString("errmsg");
				System.out.println("模板消息发送失败,错误是 "+errorCode+",错误信息是"+ errorMsg);
			    return "模板消息发送失败,错误是 "+errorCode+",错误信息是"+ errorMsg;
			}
		}else{
		   return "模板消息发送失败 null json";
		}
	}
}
