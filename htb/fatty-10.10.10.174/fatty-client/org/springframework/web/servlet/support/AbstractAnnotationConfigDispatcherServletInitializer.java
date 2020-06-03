/*    */ package org.springframework.web.servlet.support;
/*    */ 
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.util.ObjectUtils;
/*    */ import org.springframework.web.context.WebApplicationContext;
/*    */ import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
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
/*    */ public abstract class AbstractAnnotationConfigDispatcherServletInitializer
/*    */   extends AbstractDispatcherServletInitializer
/*    */ {
/*    */   @Nullable
/*    */   protected WebApplicationContext createRootApplicationContext() {
/* 56 */     Class<?>[] configClasses = getRootConfigClasses();
/* 57 */     if (!ObjectUtils.isEmpty((Object[])configClasses)) {
/* 58 */       AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
/* 59 */       context.register(configClasses);
/* 60 */       return (WebApplicationContext)context;
/*    */     } 
/*    */     
/* 63 */     return null;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected WebApplicationContext createServletApplicationContext() {
/* 74 */     AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
/* 75 */     Class<?>[] configClasses = getServletConfigClasses();
/* 76 */     if (!ObjectUtils.isEmpty((Object[])configClasses)) {
/* 77 */       context.register(configClasses);
/*    */     }
/* 79 */     return (WebApplicationContext)context;
/*    */   }
/*    */   
/*    */   @Nullable
/*    */   protected abstract Class<?>[] getRootConfigClasses();
/*    */   
/*    */   @Nullable
/*    */   protected abstract Class<?>[] getServletConfigClasses();
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/support/AbstractAnnotationConfigDispatcherServletInitializer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */