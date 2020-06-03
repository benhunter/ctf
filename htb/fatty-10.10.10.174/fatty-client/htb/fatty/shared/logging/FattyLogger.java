/*    */ package htb.fatty.shared.logging;
/*    */ 
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FattyLogger
/*    */ {
/* 12 */   private Logger errorLogger = LoggerFactory.getLogger("errorLogger");
/* 13 */   private Logger infoLogger = LoggerFactory.getLogger("infoLogger");
/*    */ 
/*    */   
/*    */   public void logInfo(String message) {
/* 17 */     this.infoLogger.info(message);
/*    */   }
/*    */   
/*    */   public void logError(String message) {
/* 21 */     this.errorLogger.error(message);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/htb/fatty/shared/logging/FattyLogger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */