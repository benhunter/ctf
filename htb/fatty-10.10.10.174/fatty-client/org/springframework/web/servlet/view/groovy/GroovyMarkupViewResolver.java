/*    */ package org.springframework.web.servlet.view.groovy;
/*    */ 
/*    */ import java.util.Locale;
/*    */ import org.springframework.web.servlet.view.AbstractTemplateViewResolver;
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
/*    */ public class GroovyMarkupViewResolver
/*    */   extends AbstractTemplateViewResolver
/*    */ {
/*    */   public GroovyMarkupViewResolver() {
/* 46 */     setViewClass(requiredViewClass());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public GroovyMarkupViewResolver(String prefix, String suffix) {
/* 57 */     this();
/* 58 */     setPrefix(prefix);
/* 59 */     setSuffix(suffix);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected Class<?> requiredViewClass() {
/* 65 */     return GroovyMarkupView.class;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected Object getCacheKey(String viewName, Locale locale) {
/* 73 */     return viewName + '_' + locale;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/view/groovy/GroovyMarkupViewResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */