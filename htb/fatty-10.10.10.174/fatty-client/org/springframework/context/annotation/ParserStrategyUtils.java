/*    */ package org.springframework.context.annotation;
/*    */ 
/*    */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*    */ import org.springframework.beans.factory.BeanFactory;
/*    */ import org.springframework.beans.factory.BeanFactoryAware;
/*    */ import org.springframework.beans.factory.config.ConfigurableBeanFactory;
/*    */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*    */ import org.springframework.context.EnvironmentAware;
/*    */ import org.springframework.context.ResourceLoaderAware;
/*    */ import org.springframework.core.env.Environment;
/*    */ import org.springframework.core.io.ResourceLoader;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ abstract class ParserStrategyUtils
/*    */ {
/*    */   public static void invokeAwareMethods(Object parserStrategyBean, Environment environment, ResourceLoader resourceLoader, BeanDefinitionRegistry registry) {
/* 47 */     if (parserStrategyBean instanceof org.springframework.beans.factory.Aware) {
/* 48 */       if (parserStrategyBean instanceof BeanClassLoaderAware) {
/*    */         
/* 50 */         ClassLoader classLoader = (registry instanceof ConfigurableBeanFactory) ? ((ConfigurableBeanFactory)registry).getBeanClassLoader() : resourceLoader.getClassLoader();
/* 51 */         if (classLoader != null) {
/* 52 */           ((BeanClassLoaderAware)parserStrategyBean).setBeanClassLoader(classLoader);
/*    */         }
/*    */       } 
/* 55 */       if (parserStrategyBean instanceof BeanFactoryAware && registry instanceof BeanFactory) {
/* 56 */         ((BeanFactoryAware)parserStrategyBean).setBeanFactory((BeanFactory)registry);
/*    */       }
/* 58 */       if (parserStrategyBean instanceof EnvironmentAware) {
/* 59 */         ((EnvironmentAware)parserStrategyBean).setEnvironment(environment);
/*    */       }
/* 61 */       if (parserStrategyBean instanceof ResourceLoaderAware)
/* 62 */         ((ResourceLoaderAware)parserStrategyBean).setResourceLoader(resourceLoader); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/annotation/ParserStrategyUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */