/*    */ package org.slf4j.log4j12;
/*    */ 
/*    */ import java.util.concurrent.ConcurrentHashMap;
/*    */ import java.util.concurrent.ConcurrentMap;
/*    */ import org.apache.log4j.LogManager;
/*    */ import org.apache.log4j.Logger;
/*    */ import org.slf4j.ILoggerFactory;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.helpers.Util;
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
/*    */ public class Log4jLoggerFactory
/*    */   implements ILoggerFactory
/*    */ {
/*    */   private static final String LOG4J_DELEGATION_LOOP_URL = "http://www.slf4j.org/codes.html#log4jDelegationLoop";
/*    */   ConcurrentMap<String, Logger> loggerMap;
/*    */   
/*    */   static {
/*    */     try {
/* 48 */       Class.forName("org.apache.log4j.Log4jLoggerFactory");
/* 49 */       String part1 = "Detected both log4j-over-slf4j.jar AND bound slf4j-log4j12.jar on the class path, preempting StackOverflowError. ";
/* 50 */       String part2 = "See also http://www.slf4j.org/codes.html#log4jDelegationLoop for more details.";
/*    */       
/* 52 */       Util.report(part1);
/* 53 */       Util.report(part2);
/* 54 */       throw new IllegalStateException(part1 + part2);
/* 55 */     } catch (ClassNotFoundException classNotFoundException) {
/*    */       return;
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Log4jLoggerFactory() {
/* 64 */     this.loggerMap = new ConcurrentHashMap<String, Logger>();
/*    */     
/* 66 */     LogManager.getRootLogger();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Logger getLogger(String name) {
/*    */     Logger log4jLogger;
/* 75 */     Logger slf4jLogger = this.loggerMap.get(name);
/* 76 */     if (slf4jLogger != null) {
/* 77 */       return slf4jLogger;
/*    */     }
/*    */     
/* 80 */     if (name.equalsIgnoreCase("ROOT")) {
/* 81 */       log4jLogger = LogManager.getRootLogger();
/*    */     } else {
/* 83 */       log4jLogger = LogManager.getLogger(name);
/*    */     } 
/* 85 */     Log4jLoggerAdapter log4jLoggerAdapter = new Log4jLoggerAdapter(log4jLogger);
/* 86 */     Logger oldInstance = (Logger)this.loggerMap.putIfAbsent(name, log4jLoggerAdapter);
/* 87 */     return (oldInstance == null) ? (Logger)log4jLoggerAdapter : oldInstance;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/slf4j/log4j12/Log4jLoggerFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */