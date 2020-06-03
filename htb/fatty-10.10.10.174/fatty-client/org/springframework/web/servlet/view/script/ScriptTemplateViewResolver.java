/*    */ package org.springframework.web.servlet.view.script;
/*    */ 
/*    */ import org.springframework.web.servlet.view.UrlBasedViewResolver;
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
/*    */ public class ScriptTemplateViewResolver
/*    */   extends UrlBasedViewResolver
/*    */ {
/*    */   public ScriptTemplateViewResolver() {
/* 43 */     setViewClass(requiredViewClass());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ScriptTemplateViewResolver(String prefix, String suffix) {
/* 54 */     this();
/* 55 */     setPrefix(prefix);
/* 56 */     setSuffix(suffix);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected Class<?> requiredViewClass() {
/* 62 */     return ScriptTemplateView.class;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/view/script/ScriptTemplateViewResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */