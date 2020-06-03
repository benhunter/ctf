/*    */ package org.springframework.aop.config;
/*    */ 
/*    */ import org.springframework.beans.factory.config.BeanDefinition;
/*    */ import org.springframework.beans.factory.parsing.BeanComponentDefinition;
/*    */ import org.springframework.beans.factory.parsing.ComponentDefinition;
/*    */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*    */ import org.springframework.beans.factory.xml.ParserContext;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.w3c.dom.Element;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class AopNamespaceUtils
/*    */ {
/*    */   public static final String PROXY_TARGET_CLASS_ATTRIBUTE = "proxy-target-class";
/*    */   private static final String EXPOSE_PROXY_ATTRIBUTE = "expose-proxy";
/*    */   
/*    */   public static void registerAutoProxyCreatorIfNecessary(ParserContext parserContext, Element sourceElement) {
/* 59 */     BeanDefinition beanDefinition = AopConfigUtils.registerAutoProxyCreatorIfNecessary(parserContext
/* 60 */         .getRegistry(), parserContext.extractSource(sourceElement));
/* 61 */     useClassProxyingIfNecessary(parserContext.getRegistry(), sourceElement);
/* 62 */     registerComponentIfNecessary(beanDefinition, parserContext);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static void registerAspectJAutoProxyCreatorIfNecessary(ParserContext parserContext, Element sourceElement) {
/* 68 */     BeanDefinition beanDefinition = AopConfigUtils.registerAspectJAutoProxyCreatorIfNecessary(parserContext
/* 69 */         .getRegistry(), parserContext.extractSource(sourceElement));
/* 70 */     useClassProxyingIfNecessary(parserContext.getRegistry(), sourceElement);
/* 71 */     registerComponentIfNecessary(beanDefinition, parserContext);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static void registerAspectJAnnotationAutoProxyCreatorIfNecessary(ParserContext parserContext, Element sourceElement) {
/* 77 */     BeanDefinition beanDefinition = AopConfigUtils.registerAspectJAnnotationAutoProxyCreatorIfNecessary(parserContext
/* 78 */         .getRegistry(), parserContext.extractSource(sourceElement));
/* 79 */     useClassProxyingIfNecessary(parserContext.getRegistry(), sourceElement);
/* 80 */     registerComponentIfNecessary(beanDefinition, parserContext);
/*    */   }
/*    */   
/*    */   private static void useClassProxyingIfNecessary(BeanDefinitionRegistry registry, @Nullable Element sourceElement) {
/* 84 */     if (sourceElement != null) {
/* 85 */       boolean proxyTargetClass = Boolean.parseBoolean(sourceElement.getAttribute("proxy-target-class"));
/* 86 */       if (proxyTargetClass) {
/* 87 */         AopConfigUtils.forceAutoProxyCreatorToUseClassProxying(registry);
/*    */       }
/* 89 */       boolean exposeProxy = Boolean.parseBoolean(sourceElement.getAttribute("expose-proxy"));
/* 90 */       if (exposeProxy) {
/* 91 */         AopConfigUtils.forceAutoProxyCreatorToExposeProxy(registry);
/*    */       }
/*    */     } 
/*    */   }
/*    */   
/*    */   private static void registerComponentIfNecessary(@Nullable BeanDefinition beanDefinition, ParserContext parserContext) {
/* 97 */     if (beanDefinition != null)
/* 98 */       parserContext.registerComponent((ComponentDefinition)new BeanComponentDefinition(beanDefinition, "org.springframework.aop.config.internalAutoProxyCreator")); 
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/config/AopNamespaceUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */