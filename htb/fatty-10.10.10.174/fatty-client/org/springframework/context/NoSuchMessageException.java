/*    */ package org.springframework.context;
/*    */ 
/*    */ import java.util.Locale;
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
/*    */ public class NoSuchMessageException
/*    */   extends RuntimeException
/*    */ {
/*    */   public NoSuchMessageException(String code, Locale locale) {
/* 35 */     super("No message found under code '" + code + "' for locale '" + locale + "'.");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public NoSuchMessageException(String code) {
/* 43 */     super("No message found under code '" + code + "' for locale '" + Locale.getDefault() + "'.");
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/NoSuchMessageException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */