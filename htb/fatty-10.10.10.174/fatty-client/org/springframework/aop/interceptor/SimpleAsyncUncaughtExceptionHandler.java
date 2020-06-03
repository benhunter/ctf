/*    */ package org.springframework.aop.interceptor;
/*    */ 
/*    */ import java.lang.reflect.Method;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
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
/*    */ public class SimpleAsyncUncaughtExceptionHandler
/*    */   implements AsyncUncaughtExceptionHandler
/*    */ {
/* 33 */   private static final Log logger = LogFactory.getLog(SimpleAsyncUncaughtExceptionHandler.class);
/*    */ 
/*    */ 
/*    */   
/*    */   public void handleUncaughtException(Throwable ex, Method method, Object... params) {
/* 38 */     if (logger.isErrorEnabled())
/* 39 */       logger.error("Unexpected exception occurred invoking async method: " + method, ex); 
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/interceptor/SimpleAsyncUncaughtExceptionHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */