/*    */ package org.springframework.validation.beanvalidation;
/*    */ 
/*    */ import java.util.Locale;
/*    */ import javax.validation.MessageInterpolator;
/*    */ import org.springframework.context.i18n.LocaleContextHolder;
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
/*    */ 
/*    */ 
/*    */ public class LocaleContextMessageInterpolator
/*    */   implements MessageInterpolator
/*    */ {
/*    */   private final MessageInterpolator targetInterpolator;
/*    */   
/*    */   public LocaleContextMessageInterpolator(MessageInterpolator targetInterpolator) {
/* 43 */     Assert.notNull(targetInterpolator, "Target MessageInterpolator must not be null");
/* 44 */     this.targetInterpolator = targetInterpolator;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String interpolate(String message, MessageInterpolator.Context context) {
/* 50 */     return this.targetInterpolator.interpolate(message, context, LocaleContextHolder.getLocale());
/*    */   }
/*    */ 
/*    */   
/*    */   public String interpolate(String message, MessageInterpolator.Context context, Locale locale) {
/* 55 */     return this.targetInterpolator.interpolate(message, context, locale);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/validation/beanvalidation/LocaleContextMessageInterpolator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */