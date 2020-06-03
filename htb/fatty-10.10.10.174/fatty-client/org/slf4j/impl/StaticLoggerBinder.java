/*    */ package org.slf4j.impl;
/*    */ 
/*    */ import org.apache.log4j.Level;
/*    */ import org.slf4j.ILoggerFactory;
/*    */ import org.slf4j.helpers.Util;
/*    */ import org.slf4j.log4j12.Log4jLoggerFactory;
/*    */ import org.slf4j.spi.LoggerFactoryBinder;
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
/*    */ 
/*    */ public class StaticLoggerBinder
/*    */   implements LoggerFactoryBinder
/*    */ {
/* 49 */   private static final StaticLoggerBinder SINGLETON = new StaticLoggerBinder();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static final StaticLoggerBinder getSingleton() {
/* 57 */     return SINGLETON;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 64 */   public static String REQUESTED_API_VERSION = "1.6.99";
/*    */   
/* 66 */   private static final String loggerFactoryClassStr = Log4jLoggerFactory.class.getName();
/*    */ 
/*    */ 
/*    */   
/*    */   private final ILoggerFactory loggerFactory;
/*    */ 
/*    */ 
/*    */   
/*    */   private StaticLoggerBinder() {
/* 75 */     this.loggerFactory = (ILoggerFactory)new Log4jLoggerFactory();
/*    */     
/*    */     try {
/* 78 */       Level level = Level.TRACE;
/* 79 */     } catch (NoSuchFieldError nsfe) {
/* 80 */       Util.report("This version of SLF4J requires log4j version 1.2.12 or later. See also http://www.slf4j.org/codes.html#log4j_version");
/*    */     } 
/*    */   }
/*    */   
/*    */   public ILoggerFactory getLoggerFactory() {
/* 85 */     return this.loggerFactory;
/*    */   }
/*    */   
/*    */   public String getLoggerFactoryClassStr() {
/* 89 */     return loggerFactoryClassStr;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/slf4j/impl/StaticLoggerBinder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */