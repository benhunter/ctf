/*    */ package org.springframework.web.servlet.view.script;
/*    */ 
/*    */ import java.util.Locale;
/*    */ import java.util.function.Function;
/*    */ import org.springframework.context.ApplicationContext;
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
/*    */ public class RenderingContext
/*    */ {
/*    */   private final ApplicationContext applicationContext;
/*    */   private final Locale locale;
/*    */   private final Function<String, String> templateLoader;
/*    */   private final String url;
/*    */   
/*    */   public RenderingContext(ApplicationContext applicationContext, Locale locale, Function<String, String> templateLoader, String url) {
/* 55 */     this.applicationContext = applicationContext;
/* 56 */     this.locale = locale;
/* 57 */     this.templateLoader = templateLoader;
/* 58 */     this.url = url;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ApplicationContext getApplicationContext() {
/* 66 */     return this.applicationContext;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Locale getLocale() {
/* 73 */     return this.locale;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Function<String, String> getTemplateLoader() {
/* 81 */     return this.templateLoader;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getUrl() {
/* 88 */     return this.url;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/view/script/RenderingContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */