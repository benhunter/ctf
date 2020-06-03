/*    */ package org.springframework.web.bind.support;
/*    */ 
/*    */ import org.springframework.web.bind.WebDataBinder;
/*    */ import org.springframework.web.context.request.WebRequest;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface WebBindingInitializer
/*    */ {
/*    */   void initBinder(WebDataBinder paramWebDataBinder);
/*    */   
/*    */   @Deprecated
/*    */   default void initBinder(WebDataBinder binder, WebRequest request) {
/* 47 */     initBinder(binder);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/bind/support/WebBindingInitializer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */