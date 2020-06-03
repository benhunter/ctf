/*    */ package org.springframework.beans.factory.support;
/*    */ 
/*    */ import org.springframework.beans.factory.config.BeanDefinition;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DefaultBeanNameGenerator
/*    */   implements BeanNameGenerator
/*    */ {
/*    */   public String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry) {
/* 32 */     return BeanDefinitionReaderUtils.generateBeanName(definition, registry);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/support/DefaultBeanNameGenerator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */