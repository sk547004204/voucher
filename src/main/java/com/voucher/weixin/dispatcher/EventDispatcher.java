package com.voucher.weixin.dispatcher;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.voucher.weixin.message.resp.Article;
import com.voucher.weixin.message.resp.Image;
import com.voucher.weixin.message.resp.ImageMessage;
import com.voucher.weixin.message.resp.NewsMessage;
import com.voucher.weixin.message.resp.TextMessage;
import com.voucher.weixin.util.HttpPostUploadUtil;
import com.voucher.weixin.util.MessageUtil;

public class EventDispatcher {
    public static String processEvent(Map<String, String> map) {
        	String openid = map.get("FromUserName"); // �û� openid
        	String mpid = map.get("ToUserName"); // ���ں�ԭʼ ID
        	ImageMessage imgmsg = new ImageMessage();

        if (map.get("Event").equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) { // ��ע�¼�
        	    System.out.println("==============���ǹ�ע�¼���");
        	   /*
        	    Image img = new Image();
        	    String access_token=map.get("access_token");
        	    HttpPostUploadUtil util=new HttpPostUploadUtil(access_token);
        	    String filepath="C:\\Users\\WangJing\\Desktop\\aaaaaaaaaaaa\\logo.png";  
        	    Map<String, String> textMap = new HashMap<String, String>();  
        	    textMap.put("name", "testname");  
        	    Map<String, String> fileMap = new HashMap<String, String>();  
        	    fileMap.put("userfile", filepath); 
        	    String mediaidrs = util.formUpload(textMap, fileMap);
        	    System.out.println(mediaidrs);
        	    JSONObject jsonObject=JSONObject.parseObject(mediaidrs);
        	    String mediaid=jsonObject.getString("media_id");
        	    img.setMediaId(mediaid);
        	    imgmsg.setImage(img);
        	    System.out.println("meidaid="+mediaid);
        	    return MessageUtil.imageMessageToXml(imgmsg);
        	    */
        	    //��ͨ�ı���Ϣ

                TextMessage txtmsg=new TextMessage();
                txtmsg.setToUserName(openid);
                txtmsg.setFromUserName(mpid);
                txtmsg.setCreateTime(new Date().getTime());
                txtmsg.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);

                String nickName=map.get("nickName");
                
           
                    txtmsg.setContent("��ã�"+nickName);
                    return MessageUtil.textMessageToXml(txtmsg);

        	
        }

        if (map.get("Event").equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)) { //ȡ����ע�¼�
            System.out.println("==============����ȡ����ע�¼���");
        }

        if (map.get("Event").equals(MessageUtil.EVENT_TYPE_SCAN)) { //ɨ���ά���¼�
            System.out.println("==============����ɨ���ά���¼���");
        }

        if (map.get("Event").equals(MessageUtil.EVENT_TYPE_LOCATION)) { //λ���ϱ��¼�
            System.out.println("==============����λ���ϱ��¼���");
        }

        if (map.get("Event").equals(MessageUtil.EVENT_TYPE_CLICK)) { //�Զ���˵�����¼�
            System.out.println("==============�����Զ���˵�����¼���");
            NewsMessage newmsg=new NewsMessage();
            newmsg.setToUserName(openid);
            newmsg.setFromUserName(mpid);
            newmsg.setCreateTime(new Date().getTime());
            newmsg.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
            Article article=new Article();
            article.setDescription("����ͼ����Ϣ 1"); //ͼ����Ϣ������
            article.setPicUrl("http://res.cuiyongzhi.com/2016/03/201603086749_6850.png"); //ͼ����ϢͼƬ��ַ
            article.setTitle("ͼ����Ϣ 1");  //ͼ����Ϣ����
            article.setUrl("http://www.cuiyongzhi.com");  //ͼ�� url ����
            List<Article> list=new ArrayList<Article>();
            list.add(article);     //���﷢�͵��ǵ�ͼ�ģ������Ҫ���Ͷ�ͼ���������� list �м����� Article ���ɣ�
            newmsg.setArticleCount(list.size());
            newmsg.setArticles(list);
            return MessageUtil.newsMessageToXml(newmsg);

        }

        if (map.get("Event").equals(MessageUtil.EVENT_TYPE_VIEW)) { //�Զ���˵� View �¼�
            System.out.println("==============�����Զ���˵� View �¼���");
        }

        return null;
    }
}
