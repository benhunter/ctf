/*    */ package org.springframework.web;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import javax.servlet.ServletException;
/*    */ import org.springframework.http.MediaType;
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
/*    */ 
/*    */ public abstract class HttpMediaTypeException
/*    */   extends ServletException
/*    */ {
/*    */   private final List<MediaType> supportedMediaTypes;
/*    */   
/*    */   protected HttpMediaTypeException(String message) {
/* 42 */     super(message);
/* 43 */     this.supportedMediaTypes = Collections.emptyList();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected HttpMediaTypeException(String message, List<MediaType> supportedMediaTypes) {
/* 51 */     super(message);
/* 52 */     this.supportedMediaTypes = Collections.unmodifiableList(supportedMediaTypes);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public List<MediaType> getSupportedMediaTypes() {
/* 60 */     return this.supportedMediaTypes;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/HttpMediaTypeException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */