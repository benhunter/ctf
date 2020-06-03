/*    */ package org.apache.commons.logging.impl;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import org.apache.commons.logging.Log;
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
/*    */ public class NoOpLog
/*    */   implements Log, Serializable
/*    */ {
/*    */   public NoOpLog() {}
/*    */   
/*    */   public NoOpLog(String name) {}
/*    */   
/*    */   public boolean isFatalEnabled() {
/* 41 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isErrorEnabled() {
/* 46 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isWarnEnabled() {
/* 51 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isInfoEnabled() {
/* 56 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isDebugEnabled() {
/* 61 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isTraceEnabled() {
/* 66 */     return false;
/*    */   }
/*    */   
/*    */   public void fatal(Object message) {}
/*    */   
/*    */   public void fatal(Object message, Throwable t) {}
/*    */   
/*    */   public void error(Object message) {}
/*    */   
/*    */   public void error(Object message, Throwable t) {}
/*    */   
/*    */   public void warn(Object message) {}
/*    */   
/*    */   public void warn(Object message, Throwable t) {}
/*    */   
/*    */   public void info(Object message) {}
/*    */   
/*    */   public void info(Object message, Throwable t) {}
/*    */   
/*    */   public void debug(Object message) {}
/*    */   
/*    */   public void debug(Object message, Throwable t) {}
/*    */   
/*    */   public void trace(Object message) {}
/*    */   
/*    */   public void trace(Object message, Throwable t) {}
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/apache/commons/logging/impl/NoOpLog.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */