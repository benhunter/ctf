/*    */ package org.slf4j;
/*    */ 
/*    */ import org.slf4j.helpers.BasicMarkerFactory;
/*    */ import org.slf4j.helpers.Util;
/*    */ import org.slf4j.spi.SLF4JServiceProvider;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MarkerFactory
/*    */ {
/*    */   static IMarkerFactory MARKER_FACTORY;
/*    */   
/*    */   static {
/* 53 */     SLF4JServiceProvider provider = LoggerFactory.getProvider();
/* 54 */     if (provider != null) {
/* 55 */       provider.initialize();
/* 56 */       MARKER_FACTORY = provider.getMarkerFactory();
/*    */     } else {
/* 58 */       Util.report("Failed to find provider");
/* 59 */       Util.report("Defaulting to BasicMarkerFactory.");
/* 60 */       MARKER_FACTORY = (IMarkerFactory)new BasicMarkerFactory();
/*    */     } 
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
/*    */   public static Marker getMarker(String name) {
/* 73 */     return MARKER_FACTORY.getMarker(name);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Marker getDetachedMarker(String name) {
/* 84 */     return MARKER_FACTORY.getDetachedMarker(name);
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
/*    */   public static IMarkerFactory getIMarkerFactory() {
/* 96 */     return MARKER_FACTORY;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/slf4j/MarkerFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */