/*    */ package org.slf4j.log4j12;
/*    */ 
/*    */ import org.apache.log4j.Level;
/*    */ import org.slf4j.ILoggerFactory;
/*    */ import org.slf4j.IMarkerFactory;
/*    */ import org.slf4j.helpers.BasicMarkerFactory;
/*    */ import org.slf4j.helpers.Util;
/*    */ import org.slf4j.spi.MDCAdapter;
/*    */ import org.slf4j.spi.SLF4JServiceProvider;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Log4j12ServiceProvider
/*    */   implements SLF4JServiceProvider
/*    */ {
/* 18 */   public static String REQUESTED_API_VERSION = "1.8.99";
/*    */   
/*    */   private ILoggerFactory loggerFactory;
/*    */   
/*    */   private IMarkerFactory markerFactory;
/*    */   private MDCAdapter mdcAdapter;
/*    */   
/*    */   public Log4j12ServiceProvider() {
/*    */     try {
/* 27 */       Level level = Level.TRACE;
/* 28 */     } catch (NoSuchFieldError nsfe) {
/* 29 */       Util.report("This version of SLF4J requires log4j version 1.2.12 or later. See also http://www.slf4j.org/codes.html#log4j_version");
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void initialize() {
/* 35 */     this.loggerFactory = new Log4jLoggerFactory();
/* 36 */     this.markerFactory = (IMarkerFactory)new BasicMarkerFactory();
/* 37 */     this.mdcAdapter = new Log4jMDCAdapter();
/*    */   }
/*    */   
/*    */   public ILoggerFactory getLoggerFactory() {
/* 41 */     return this.loggerFactory;
/*    */   }
/*    */   
/*    */   public IMarkerFactory getMarkerFactory() {
/* 45 */     return this.markerFactory;
/*    */   }
/*    */   
/*    */   public MDCAdapter getMDCAdapter() {
/* 49 */     return this.mdcAdapter;
/*    */   }
/*    */   
/*    */   public String getRequesteApiVersion() {
/* 53 */     return REQUESTED_API_VERSION;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/slf4j/log4j12/Log4j12ServiceProvider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */