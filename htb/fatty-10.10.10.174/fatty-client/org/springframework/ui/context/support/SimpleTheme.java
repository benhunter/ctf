/*    */ package org.springframework.ui.context.support;
/*    */ 
/*    */ import org.springframework.context.MessageSource;
/*    */ import org.springframework.ui.context.Theme;
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
/*    */ public class SimpleTheme
/*    */   implements Theme
/*    */ {
/*    */   private final String name;
/*    */   private final MessageSource messageSource;
/*    */   
/*    */   public SimpleTheme(String name, MessageSource messageSource) {
/* 43 */     Assert.notNull(name, "Name must not be null");
/* 44 */     Assert.notNull(messageSource, "MessageSource must not be null");
/* 45 */     this.name = name;
/* 46 */     this.messageSource = messageSource;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public final String getName() {
/* 52 */     return this.name;
/*    */   }
/*    */ 
/*    */   
/*    */   public final MessageSource getMessageSource() {
/* 57 */     return this.messageSource;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/ui/context/support/SimpleTheme.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */