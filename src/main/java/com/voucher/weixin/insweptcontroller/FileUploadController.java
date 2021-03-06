package com.voucher.weixin.insweptcontroller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;  
import org.springframework.web.bind.annotation.RequestMapping;  
import org.springframework.web.bind.annotation.RequestMethod;  
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.voucher.manage.model.Photo;
import com.voucher.manage.service.PhotoService;
import com.voucher.manage.service.UserService;
import com.voucher.manage.service.WeiXinService;
import com.voucher.manage.tools.FileTypeTest;
import com.voucher.weixin.base.AutoAccessToken;  
   
  
@Controller  
@RequestMapping("/mobile/file")  
public class FileUploadController {  

	private WeiXinService weixinService;
	
	private PhotoService photoService;
	
	@Autowired
	public void setweixinService(WeiXinService weiXinService) {
		this.weixinService=weiXinService;
	}
	
	@Autowired
	public void setPhotoService(PhotoService photoService) {
		this.photoService = photoService;
	}
	
    @RequestMapping(value="/upload",method=RequestMethod.GET)  
    public @ResponseBody String fildUpload(HttpServletRequest request,ServletResponse response, 
    		@RequestParam String serverId,  
    		@RequestParam Integer campusId)throws Exception{  
    	HttpServletRequest hrequest = (HttpServletRequest)request;
    	String accessToken;
    	
        //获得物理路径webapp所在路径  
        String pathRoot = request.getSession().getServletContext().getRealPath("");  
        String path="/mobile/photo/";  
        String filePath=pathRoot+path;  
        
         File savePath = new File(filePath);//创建新文件  
         System.out.println("filePath="+filePath);
         if (!savePath.exists()) {   
             savePath.mkdir();   
         }  
 
         final long l = System.currentTimeMillis();
         final int i = (int)( l % 100 );
         String uname = i+serverId;//获取文件名  
         try {  
             File file = new File(savePath+"//"+uname);//创建新文件  
             if(file!=null && !file.exists()){  
                 file.createNewFile();  
             }  
             OutputStream oputstream = new FileOutputStream(file); 
             
             accessToken=AutoAccessToken.get(weixinService, campusId);
             
             String photoUrl="https://api.weixin.qq.com/cgi-bin/media/get?access_token="+accessToken+"&media_id="+serverId;
             System.out.println("photoUrl="+photoUrl);
             URL url = new URL(photoUrl);  
             HttpURLConnection uc = (HttpURLConnection) url.openConnection();  
             uc.setDoInput(true);//设置是否要从 URL 连接读取数据,默认为true  
             uc.connect();  
             InputStream iputstream = uc.getInputStream();  
             System.out.println("file size is:"+uc.getContentLength());//打印文件长度  
             byte[] buffer = new byte[4*1024];  
             int byteRead = -1;     
             while((byteRead=(iputstream.read(buffer)))!= -1){  
                 oputstream.write(buffer, 0, byteRead);  
             }  
             oputstream.flush();    
             iputstream.close();  
             oputstream.close();  
         
             file = new File(savePath+"//"+uname); 
             
         //mime type 检测图片文件类型
         String mimeType="";
         Map<String,String> map=FileTypeTest.getFileType();
         Iterator<Map.Entry<String, String>> entryiterator = map.entrySet().iterator();
         String filetypeHex = String.valueOf(FileTypeTest.getFileHexString(file));
         while (entryiterator.hasNext()) {
             Map.Entry<String,String> entry =  entryiterator.next();
             String fileTypeHexValue = entry.getValue();
             if (filetypeHex.toUpperCase().startsWith(fileTypeHexValue)) {
                 mimeType=entry.getKey();
                 break;
             }
         }
        
        File file2 = new File(savePath+"//"+uname+"."+mimeType); 
        file.renameTo(file2);
        
        String imageUrl="/voucher/mobile/photo/"+uname+"."+mimeType;
        String openId=( String ) hrequest.getSession().getAttribute("openId");
        Date date=new Date();       
        
        Photo photo=new Photo();
        
        photo.setCampusId(campusId);
        photo.setOpenId(openId);
        photo.setImageUrl(imageUrl);
        photo.setCreateTime(date);
        
        photoService.insertPhtoByOpenId(photo);
        
        return savePath+"//"+uname+"."+mimeType;  
      }catch (Exception e) {
		// TODO: handle exception
    	  e.printStackTrace();
    	  return e.toString();
	 }  
    }
}  

