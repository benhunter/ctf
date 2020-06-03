/*    */ package org.springframework.web.servlet.resource;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.springframework.core.io.Resource;
/*    */ import org.springframework.util.DigestUtils;
/*    */ import org.springframework.util.FileCopyUtils;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ContentVersionStrategy
/*    */   extends AbstractVersionStrategy
/*    */ {
/*    */   public ContentVersionStrategy() {
/* 38 */     super(new AbstractVersionStrategy.FileNameVersionPathStrategy());
/*    */   }
/*    */ 
/*    */   
/*    */   public String getResourceVersion(Resource resource) {
/*    */     try {
/* 44 */       byte[] content = FileCopyUtils.copyToByteArray(resource.getInputStream());
/* 45 */       return DigestUtils.md5DigestAsHex(content);
/*    */     }
/* 47 */     catch (IOException ex) {
/* 48 */       throw new IllegalStateException("Failed to calculate hash for " + resource, ex);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/resource/ContentVersionStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */