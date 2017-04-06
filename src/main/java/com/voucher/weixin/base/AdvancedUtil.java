package com.voucher.weixin.base;


import java.util.Date;

import org.slf4j.Logger;

import com.alibaba.fastjson.JSONObject;


public class AdvancedUtil {
	
	/*�����������ͨ���ص���code��ȡһ���������ҳ��Ȩaccess_token
	 * ��ȷʱ���ص�JSON���ݰ����£�
	 * { "access_token":"ACCESS_TOKEN",    
	     "expires_in":7200,    
	     "refresh_token":"REFRESH_TOKEN",    
	     "openid":"OPENID",    
	     "scope":"SCOPE" } 
	 */
	 public static WeixinOauth2Token getOauth2AccessToken(String appId, String appSecret, String code) {
	        WeixinOauth2Token wat = null;
	        // ƴ�������ַ
	        String requestUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
	        requestUrl = requestUrl.replace("APPID", appId);
	        requestUrl = requestUrl.replace("SECRET", appSecret);
	        requestUrl = requestUrl.replace("CODE", code);
	        // ��ȡ��ҳ��Ȩƾ֤
	        
	        System.out.println("requesUrl="+requestUrl);
	        
	        
	        JSONObject jsonObject = CommonUtil.httpsRequest(requestUrl, "GET", null);
	        System.out.println("jsonObject="+jsonObject);
	        
	        if (null != jsonObject) {
	        	try{
		        	  if(jsonObject.getString("errcode")!=null){
		        		String errcode=jsonObject.getString("errcode");
		        		System.out.println("errcoid="+errcode);
                        return null;
		        	  }
		        	 }catch (Exception e) {
						// TODO: handle exception
					}
	        	
	        	
	            try {
	                wat = new WeixinOauth2Token();
	                wat.setAccessToken(jsonObject.getString("access_token"));
	                wat.setExpiresIn(jsonObject.getString("expires_in"));
	                wat.setRefreshToken(jsonObject.getString("refresh_token"));
	                wat.setOpenId(jsonObject.getString("openid"));
	                wat.setScope(jsonObject.getString("scope"));
	            } catch (Exception e) {
	                wat = null;
	                String errorCode = jsonObject.getString("errcode");
	                String errorMsg = jsonObject.getString("errmsg");
	               e.printStackTrace();
	            }
	        }
	        return wat;
	    }
	 
	 //�˷������ڻ�ȡ����access_token
	  public static String getAccessToken(String appId, String appSecret){
		   String requestUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=SECRET";
	        requestUrl = requestUrl.replace("APPID", appId);
	        requestUrl = requestUrl.replace("SECRET", appSecret);
	        
	        JSONObject jsonObject = CommonUtil.httpsRequest(requestUrl, "GET", null);
	        System.out.println("jsonObject="+jsonObject);
	        
	        String access_token=jsonObject.getString("access_token");
	        
	        return access_token;
	 }
	 
	 
	 
	 //��������������ǿ���ͨ��access_token��openid��ȡ�û���Ϣ
	 @SuppressWarnings( { "deprecation", "unchecked" })
	    public static SNSUserInfo getSNSUserInfo(String accessToken, String openId){
	        SNSUserInfo snsUserInfo=new SNSUserInfo();
	        // ƴ�������ַ
	    //    String requestUrl = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID";
	        String requestUrl ="https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
	        requestUrl = requestUrl.replace("ACCESS_TOKEN", accessToken).replace("OPENID", openId);
	        System.out.println("requesturl="+requestUrl);
	        // ͨ����ҳ��Ȩ��ȡ�û���Ϣ
	        JSONObject jsonObject = CommonUtil.httpsRequest(requestUrl, "GET", null);
              System.out.println("json="+jsonObject);
	        if (jsonObject!=null) {
	        	try{
	        		if(jsonObject.getString("errcode")!=null){
		        		String errcode=jsonObject.getString("errcode");
		        		System.out.println("errcoid="+errcode);
                        snsUserInfo.setErrorCode(errcode);       //����΢�Ŵ�����룬�Ա�getUserInfo������������access_token
                        return snsUserInfo;
	        	  }
	        	 }catch (Exception e) {
					// TODO: handle exception
				}
	            try {
	                // �û��ı�ʶ
	                snsUserInfo.setOpenId(jsonObject.getString("openid"));
	                //�û��Ƿ��ĸù��ںű�ʶ
	                snsUserInfo.setSubScribe(Short.parseShort(jsonObject.getString("subscribe")));
	                // �ǳ�
	                snsUserInfo.setNickname(jsonObject.getString("nickname"));
	                // �Ա�1�����ԣ�2��Ů�ԣ�0��δ֪��
	                snsUserInfo.setSex(Short.parseShort(jsonObject.getString("sex")));
	                //�û�������
	                snsUserInfo.setLanguage(jsonObject.getString("language"));
	                // �û����ڹ���
	                snsUserInfo.setCountry(jsonObject.getString("country"));
	                // �û�����ʡ��
	                snsUserInfo.setProvince(jsonObject.getString("province"));
	                // �û����ڳ���
	                snsUserInfo.setCity(jsonObject.getString("city"));
	                // �û�ͷ��
	                snsUserInfo.setHeadImgUrl(jsonObject.getString("headimgurl"));
	                //�û���עʱ��
	                Date date= new Date(Integer.parseInt(jsonObject.getString("subscribe_time")));
	                snsUserInfo.setSubScribeTime(date);
	                System.out.println("substiem="+date);
	                //���ں���Ӫ�߶Է�˿�ı�ע
	                snsUserInfo.setRemark(jsonObject.getString("remark"));
	                //�û����ڵķ���ID
	                snsUserInfo.setGroupId(jsonObject.getString("groupid"));
	                //ֻ�����û������ںŰ󶨵�΢�ſ���ƽ̨�ʺź󣬲Ż���ָ��ֶΡ�
	                snsUserInfo.setUnionid(jsonObject.getString("unionid"));
	                // �û���Ȩ��Ϣ
	                snsUserInfo.setPrivilegeList(jsonObject.getJSONArray("privilege"));
	            } catch (Exception e) {
	                snsUserInfo = null;
	                String errorCode = jsonObject.getString("errcode");
	                String errorMsg = jsonObject.getString("errmsg");
	               e.printStackTrace();
	            }
	        }else{
	        	System.out.println("json=null");
	        }
	        return snsUserInfo;
	    }
	 
}
