/*    */ package org.springframework.web.servlet.resource;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.springframework.core.io.ByteArrayResource;
/*    */ import org.springframework.core.io.Resource;
/*    */ import org.springframework.lang.Nullable;
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
/*    */ 
/*    */ public class TransformedResource
/*    */   extends ByteArrayResource
/*    */ {
/*    */   @Nullable
/*    */   private final String filename;
/*    */   private final long lastModified;
/*    */   
/*    */   public TransformedResource(Resource original, byte[] transformedContent) {
/* 43 */     super(transformedContent);
/* 44 */     this.filename = original.getFilename();
/*    */     try {
/* 46 */       this.lastModified = original.lastModified();
/*    */     }
/* 48 */     catch (IOException ex) {
/*    */       
/* 50 */       throw new IllegalArgumentException(ex);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public String getFilename() {
/* 58 */     return this.filename;
/*    */   }
/*    */ 
/*    */   
/*    */   public long lastModified() throws IOException {
/* 63 */     return this.lastModified;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/resource/TransformedResource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */