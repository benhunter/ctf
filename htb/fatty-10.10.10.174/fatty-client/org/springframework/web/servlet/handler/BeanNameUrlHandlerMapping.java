/*    */ package org.springframework.web.servlet.handler;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.springframework.util.StringUtils;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BeanNameUrlHandlerMapping
/*    */   extends AbstractDetectingUrlHandlerMapping
/*    */ {
/*    */   protected String[] determineUrlsForHandler(String beanName) {
/* 58 */     List<String> urls = new ArrayList<>();
/* 59 */     if (beanName.startsWith("/")) {
/* 60 */       urls.add(beanName);
/*    */     }
/* 62 */     String[] aliases = obtainApplicationContext().getAliases(beanName);
/* 63 */     for (String alias : aliases) {
/* 64 */       if (alias.startsWith("/")) {
/* 65 */         urls.add(alias);
/*    */       }
/*    */     } 
/* 68 */     return StringUtils.toStringArray(urls);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/handler/BeanNameUrlHandlerMapping.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */