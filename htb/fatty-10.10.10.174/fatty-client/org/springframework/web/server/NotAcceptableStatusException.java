/*    */ package org.springframework.web.server;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import org.springframework.http.HttpStatus;
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
/*    */ public class NotAcceptableStatusException
/*    */   extends ResponseStatusException
/*    */ {
/*    */   private final List<MediaType> supportedMediaTypes;
/*    */   
/*    */   public NotAcceptableStatusException(String reason) {
/* 41 */     super(HttpStatus.NOT_ACCEPTABLE, reason);
/* 42 */     this.supportedMediaTypes = Collections.emptyList();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public NotAcceptableStatusException(List<MediaType> supportedMediaTypes) {
/* 49 */     super(HttpStatus.NOT_ACCEPTABLE, "Could not find acceptable representation");
/* 50 */     this.supportedMediaTypes = Collections.unmodifiableList(supportedMediaTypes);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public List<MediaType> getSupportedMediaTypes() {
/* 59 */     return this.supportedMediaTypes;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/server/NotAcceptableStatusException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */