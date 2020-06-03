/*    */ package org.springframework.scripting.config;
/*    */ 
/*    */ import org.springframework.beans.factory.config.BeanDefinition;
/*    */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*    */ import org.springframework.beans.factory.support.RootBeanDefinition;
/*    */ import org.springframework.scripting.support.ScriptFactoryPostProcessor;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class LangNamespaceUtils
/*    */ {
/*    */   private static final String SCRIPT_FACTORY_POST_PROCESSOR_BEAN_NAME = "org.springframework.scripting.config.scriptFactoryPostProcessor";
/*    */   
/*    */   public static BeanDefinition registerScriptFactoryPostProcessorIfNecessary(BeanDefinitionRegistry registry) {
/*    */     RootBeanDefinition rootBeanDefinition;
/* 50 */     if (registry.containsBeanDefinition("org.springframework.scripting.config.scriptFactoryPostProcessor")) {
/* 51 */       BeanDefinition beanDefinition = registry.getBeanDefinition("org.springframework.scripting.config.scriptFactoryPostProcessor");
/*    */     } else {
/*    */       
/* 54 */       rootBeanDefinition = new RootBeanDefinition(ScriptFactoryPostProcessor.class);
/* 55 */       registry.registerBeanDefinition("org.springframework.scripting.config.scriptFactoryPostProcessor", (BeanDefinition)rootBeanDefinition);
/*    */     } 
/* 57 */     return (BeanDefinition)rootBeanDefinition;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/scripting/config/LangNamespaceUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */