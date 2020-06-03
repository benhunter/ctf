/*    */ package org.springframework.core.log;
/*    */ 
/*    */ import java.util.function.Function;
/*    */ import org.apache.commons.logging.Log;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class LogFormatUtils
/*    */ {
/*    */   public static String formatValue(@Nullable Object value, boolean limitLength) {
/*    */     String str;
/* 47 */     if (value == null) {
/* 48 */       return "";
/*    */     }
/*    */     
/* 51 */     if (value instanceof CharSequence) {
/* 52 */       str = "\"" + value + "\"";
/*    */     } else {
/*    */       
/*    */       try {
/* 56 */         str = value.toString();
/*    */       }
/* 58 */       catch (Throwable ex) {
/* 59 */         str = ex.toString();
/*    */       } 
/*    */     } 
/* 62 */     return (limitLength && str.length() > 100) ? (str.substring(0, 100) + " (truncated)...") : str;
/*    */   }
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
/*    */   public static void traceDebug(Log logger, Function<Boolean, String> messageFactory) {
/* 84 */     if (logger.isDebugEnabled()) {
/* 85 */       boolean traceEnabled = logger.isTraceEnabled();
/* 86 */       String logMessage = messageFactory.apply(Boolean.valueOf(traceEnabled));
/* 87 */       if (traceEnabled) {
/* 88 */         logger.trace(logMessage);
/*    */       } else {
/*    */         
/* 91 */         logger.debug(logMessage);
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/log/LogFormatUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */