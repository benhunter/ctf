/*    */ package org.springframework.context.support;
/*    */ 
/*    */ import java.util.Locale;
/*    */ import org.springframework.context.HierarchicalMessageSource;
/*    */ import org.springframework.context.MessageSource;
/*    */ import org.springframework.context.MessageSourceResolvable;
/*    */ import org.springframework.context.NoSuchMessageException;
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
/*    */ public class DelegatingMessageSource
/*    */   extends MessageSourceSupport
/*    */   implements HierarchicalMessageSource
/*    */ {
/*    */   @Nullable
/*    */   private MessageSource parentMessageSource;
/*    */   
/*    */   public void setParentMessageSource(@Nullable MessageSource parent) {
/* 46 */     this.parentMessageSource = parent;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public MessageSource getParentMessageSource() {
/* 52 */     return this.parentMessageSource;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public String getMessage(String code, @Nullable Object[] args, @Nullable String defaultMessage, Locale locale) {
/* 59 */     if (this.parentMessageSource != null) {
/* 60 */       return this.parentMessageSource.getMessage(code, args, defaultMessage, locale);
/*    */     }
/* 62 */     if (defaultMessage != null) {
/* 63 */       return renderDefaultMessage(defaultMessage, args, locale);
/*    */     }
/*    */     
/* 66 */     return null;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getMessage(String code, @Nullable Object[] args, Locale locale) throws NoSuchMessageException {
/* 72 */     if (this.parentMessageSource != null) {
/* 73 */       return this.parentMessageSource.getMessage(code, args, locale);
/*    */     }
/*    */     
/* 76 */     throw new NoSuchMessageException(code, locale);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getMessage(MessageSourceResolvable resolvable, Locale locale) throws NoSuchMessageException {
/* 82 */     if (this.parentMessageSource != null) {
/* 83 */       return this.parentMessageSource.getMessage(resolvable, locale);
/*    */     }
/*    */     
/* 86 */     if (resolvable.getDefaultMessage() != null) {
/* 87 */       return renderDefaultMessage(resolvable.getDefaultMessage(), resolvable.getArguments(), locale);
/*    */     }
/* 89 */     String[] codes = resolvable.getCodes();
/* 90 */     String code = (codes != null && codes.length > 0) ? codes[0] : "";
/* 91 */     throw new NoSuchMessageException(code, locale);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 98 */     return (this.parentMessageSource != null) ? this.parentMessageSource.toString() : "Empty MessageSource";
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/support/DelegatingMessageSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */