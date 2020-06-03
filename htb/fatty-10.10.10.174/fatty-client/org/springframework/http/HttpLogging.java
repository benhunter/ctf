/*    */ package org.springframework.http;
/*    */ 
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
/*    */ import org.springframework.core.log.LogDelegateFactory;
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
/*    */ 
/*    */ public abstract class HttpLogging
/*    */ {
/* 45 */   private static final Log fallbackLogger = LogFactory.getLog("org.springframework.web." + HttpLogging.class.getSimpleName());
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Log forLogName(Class<?> primaryLoggerClass) {
/* 56 */     Log primaryLogger = LogFactory.getLog(primaryLoggerClass);
/* 57 */     return forLog(primaryLogger);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Log forLog(Log primaryLogger) {
/* 68 */     return LogDelegateFactory.getCompositeLog(primaryLogger, fallbackLogger, new Log[0]);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/HttpLogging.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */