/*    */ package org.slf4j.helpers;
/*    */ 
/*    */ import org.slf4j.ILoggerFactory;
/*    */ import org.slf4j.IMarkerFactory;
/*    */ import org.slf4j.spi.MDCAdapter;
/*    */ import org.slf4j.spi.SLF4JServiceProvider;
/*    */ 
/*    */ public class SubstitureServiceProvider implements SLF4JServiceProvider {
/*  9 */   private SubstituteLoggerFactory loggerFactory = new SubstituteLoggerFactory();
/* 10 */   private IMarkerFactory markerFactory = new BasicMarkerFactory();
/* 11 */   private MDCAdapter mdcAdapter = new BasicMDCAdapter();
/*    */ 
/*    */   
/*    */   public ILoggerFactory getLoggerFactory() {
/* 15 */     return this.loggerFactory;
/*    */   }
/*    */   
/*    */   public SubstituteLoggerFactory getSubstituteLoggerFactory() {
/* 19 */     return this.loggerFactory;
/*    */   }
/*    */ 
/*    */   
/*    */   public IMarkerFactory getMarkerFactory() {
/* 24 */     return this.markerFactory;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public MDCAdapter getMDCAdapter() {
/* 30 */     return this.mdcAdapter;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getRequesteApiVersion() {
/* 36 */     throw new UnsupportedOperationException();
/*    */   }
/*    */   
/*    */   public void initialize() {}
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/slf4j/helpers/SubstitureServiceProvider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */