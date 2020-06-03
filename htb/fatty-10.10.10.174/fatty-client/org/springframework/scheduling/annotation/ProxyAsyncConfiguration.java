/*    */ package org.springframework.scheduling.annotation;
/*    */ 
/*    */ import java.lang.annotation.Annotation;
/*    */ import org.springframework.context.annotation.Bean;
/*    */ import org.springframework.context.annotation.Configuration;
/*    */ import org.springframework.context.annotation.Role;
/*    */ import org.springframework.core.annotation.AnnotationUtils;
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
/*    */ @Configuration
/*    */ @Role(2)
/*    */ public class ProxyAsyncConfiguration
/*    */   extends AbstractAsyncConfiguration
/*    */ {
/*    */   @Bean(name = {"org.springframework.context.annotation.internalAsyncAnnotationProcessor"})
/*    */   @Role(2)
/*    */   public AsyncAnnotationBeanPostProcessor asyncAdvisor() {
/* 47 */     Assert.notNull(this.enableAsync, "@EnableAsync annotation metadata was not injected");
/* 48 */     AsyncAnnotationBeanPostProcessor bpp = new AsyncAnnotationBeanPostProcessor();
/* 49 */     bpp.configure(this.executor, this.exceptionHandler);
/* 50 */     Class<? extends Annotation> customAsyncAnnotation = this.enableAsync.getClass("annotation");
/* 51 */     if (customAsyncAnnotation != AnnotationUtils.getDefaultValue(EnableAsync.class, "annotation")) {
/* 52 */       bpp.setAsyncAnnotationType(customAsyncAnnotation);
/*    */     }
/* 54 */     bpp.setProxyTargetClass(this.enableAsync.getBoolean("proxyTargetClass"));
/* 55 */     bpp.setOrder(((Integer)this.enableAsync.getNumber("order")).intValue());
/* 56 */     return bpp;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/scheduling/annotation/ProxyAsyncConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */