/*    */ package org.springframework.web.multipart;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import org.springframework.core.io.AbstractResource;
/*    */ import org.springframework.util.Assert;
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
/*    */ class MultipartFileResource
/*    */   extends AbstractResource
/*    */ {
/*    */   private final MultipartFile multipartFile;
/*    */   
/*    */   public MultipartFileResource(MultipartFile multipartFile) {
/* 39 */     Assert.notNull(multipartFile, "MultipartFile must not be null");
/* 40 */     this.multipartFile = multipartFile;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean exists() {
/* 49 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isOpen() {
/* 57 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public long contentLength() {
/* 62 */     return this.multipartFile.getSize();
/*    */   }
/*    */ 
/*    */   
/*    */   public String getFilename() {
/* 67 */     return this.multipartFile.getOriginalFilename();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public InputStream getInputStream() throws IOException, IllegalStateException {
/* 76 */     return this.multipartFile.getInputStream();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getDescription() {
/* 84 */     return "MultipartFile resource [" + this.multipartFile.getName() + "]";
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(Object other) {
/* 90 */     return (this == other || (other instanceof MultipartFileResource && ((MultipartFileResource)other).multipartFile
/* 91 */       .equals(this.multipartFile)));
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 96 */     return this.multipartFile.hashCode();
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/multipart/MultipartFileResource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */