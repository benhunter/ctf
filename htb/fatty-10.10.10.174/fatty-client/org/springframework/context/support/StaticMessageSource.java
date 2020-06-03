/*    */ package org.springframework.context.support;
/*    */ 
/*    */ import java.text.MessageFormat;
/*    */ import java.util.HashMap;
/*    */ import java.util.Locale;
/*    */ import java.util.Map;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.util.Assert;
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
/*    */ public class StaticMessageSource
/*    */   extends AbstractMessageSource
/*    */ {
/* 40 */   private final Map<String, String> messages = new HashMap<>();
/*    */   
/* 42 */   private final Map<String, MessageFormat> cachedMessageFormats = new HashMap<>();
/*    */ 
/*    */ 
/*    */   
/*    */   protected String resolveCodeWithoutArguments(String code, Locale locale) {
/* 47 */     return this.messages.get(code + '_' + locale.toString());
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   protected MessageFormat resolveCode(String code, Locale locale) {
/* 53 */     String key = code + '_' + locale.toString();
/* 54 */     String msg = this.messages.get(key);
/* 55 */     if (msg == null) {
/* 56 */       return null;
/*    */     }
/* 58 */     synchronized (this.cachedMessageFormats) {
/* 59 */       MessageFormat messageFormat = this.cachedMessageFormats.get(key);
/* 60 */       if (messageFormat == null) {
/* 61 */         messageFormat = createMessageFormat(msg, locale);
/* 62 */         this.cachedMessageFormats.put(key, messageFormat);
/*    */       } 
/* 64 */       return messageFormat;
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void addMessage(String code, Locale locale, String msg) {
/* 75 */     Assert.notNull(code, "Code must not be null");
/* 76 */     Assert.notNull(locale, "Locale must not be null");
/* 77 */     Assert.notNull(msg, "Message must not be null");
/* 78 */     this.messages.put(code + '_' + locale.toString(), msg);
/* 79 */     if (this.logger.isDebugEnabled()) {
/* 80 */       this.logger.debug("Added message [" + msg + "] for code [" + code + "] and Locale [" + locale + "]");
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void addMessages(Map<String, String> messages, Locale locale) {
/* 91 */     Assert.notNull(messages, "Messages Map must not be null");
/* 92 */     messages.forEach((code, msg) -> addMessage(code, locale, msg));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 98 */     return getClass().getName() + ": " + this.messages;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/support/StaticMessageSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */