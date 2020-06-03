/*    */ package org.springframework.web.client;
/*    */ 
/*    */ import java.io.IOException;
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
/*    */ public class ResourceAccessException
/*    */   extends RestClientException
/*    */ {
/*    */   private static final long serialVersionUID = -8513182514355844870L;
/*    */   
/*    */   public ResourceAccessException(String msg) {
/* 37 */     super(msg);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ResourceAccessException(String msg, IOException ex) {
/* 46 */     super(msg, ex);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/client/ResourceAccessException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */