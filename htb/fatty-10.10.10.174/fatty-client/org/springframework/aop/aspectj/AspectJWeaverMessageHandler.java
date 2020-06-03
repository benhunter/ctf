/*    */ package org.springframework.aop.aspectj;
/*    */ 
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
/*    */ import org.aspectj.bridge.AbortException;
/*    */ import org.aspectj.bridge.IMessage;
/*    */ import org.aspectj.bridge.IMessageHandler;
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
/*    */ public class AspectJWeaverMessageHandler
/*    */   implements IMessageHandler
/*    */ {
/*    */   private static final String AJ_ID = "[AspectJ] ";
/* 48 */   private static final Log logger = LogFactory.getLog("AspectJ Weaver");
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean handleMessage(IMessage message) throws AbortException {
/* 53 */     IMessage.Kind messageKind = message.getKind();
/* 54 */     if (messageKind == IMessage.DEBUG) {
/* 55 */       if (logger.isDebugEnabled()) {
/* 56 */         logger.debug(makeMessageFor(message));
/* 57 */         return true;
/*    */       }
/*    */     
/* 60 */     } else if (messageKind == IMessage.INFO || messageKind == IMessage.WEAVEINFO) {
/* 61 */       if (logger.isInfoEnabled()) {
/* 62 */         logger.info(makeMessageFor(message));
/* 63 */         return true;
/*    */       }
/*    */     
/* 66 */     } else if (messageKind == IMessage.WARNING) {
/* 67 */       if (logger.isWarnEnabled()) {
/* 68 */         logger.warn(makeMessageFor(message));
/* 69 */         return true;
/*    */       }
/*    */     
/* 72 */     } else if (messageKind == IMessage.ERROR) {
/* 73 */       if (logger.isErrorEnabled()) {
/* 74 */         logger.error(makeMessageFor(message));
/* 75 */         return true;
/*    */       }
/*    */     
/* 78 */     } else if (messageKind == IMessage.ABORT && 
/* 79 */       logger.isFatalEnabled()) {
/* 80 */       logger.fatal(makeMessageFor(message));
/* 81 */       return true;
/*    */     } 
/*    */     
/* 84 */     return false;
/*    */   }
/*    */   
/*    */   private String makeMessageFor(IMessage aMessage) {
/* 88 */     return "[AspectJ] " + aMessage.getMessage();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isIgnoring(IMessage.Kind messageKind) {
/* 94 */     return false;
/*    */   }
/*    */   
/*    */   public void dontIgnore(IMessage.Kind messageKind) {}
/*    */   
/*    */   public void ignore(IMessage.Kind kind) {}
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/aspectj/AspectJWeaverMessageHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */