/*    */ package org.springframework.web.multipart;
/*    */ 
/*    */ import org.springframework.core.NestedRuntimeException;
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
/*    */ 
/*    */ 
/*    */ public class MultipartException
/*    */   extends NestedRuntimeException
/*    */ {
/*    */   public MultipartException(String msg) {
/* 39 */     super(msg);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public MultipartException(String msg, @Nullable Throwable cause) {
/* 48 */     super(msg, cause);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/multipart/MultipartException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */