/*    */ package org.springframework.beans.factory.support;
/*    */ 
/*    */ import java.util.Map;
/*    */ import java.util.concurrent.ConcurrentHashMap;
/*    */ import org.springframework.beans.factory.BeanDefinitionStoreException;
/*    */ import org.springframework.beans.factory.NoSuchBeanDefinitionException;
/*    */ import org.springframework.beans.factory.config.BeanDefinition;
/*    */ import org.springframework.core.SimpleAliasRegistry;
/*    */ import org.springframework.util.Assert;
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
/*    */ public class SimpleBeanDefinitionRegistry
/*    */   extends SimpleAliasRegistry
/*    */   implements BeanDefinitionRegistry
/*    */ {
/* 40 */   private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(64);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) throws BeanDefinitionStoreException {
/* 47 */     Assert.hasText(beanName, "'beanName' must not be empty");
/* 48 */     Assert.notNull(beanDefinition, "BeanDefinition must not be null");
/* 49 */     this.beanDefinitionMap.put(beanName, beanDefinition);
/*    */   }
/*    */ 
/*    */   
/*    */   public void removeBeanDefinition(String beanName) throws NoSuchBeanDefinitionException {
/* 54 */     if (this.beanDefinitionMap.remove(beanName) == null) {
/* 55 */       throw new NoSuchBeanDefinitionException(beanName);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public BeanDefinition getBeanDefinition(String beanName) throws NoSuchBeanDefinitionException {
/* 61 */     BeanDefinition bd = this.beanDefinitionMap.get(beanName);
/* 62 */     if (bd == null) {
/* 63 */       throw new NoSuchBeanDefinitionException(beanName);
/*    */     }
/* 65 */     return bd;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean containsBeanDefinition(String beanName) {
/* 70 */     return this.beanDefinitionMap.containsKey(beanName);
/*    */   }
/*    */ 
/*    */   
/*    */   public String[] getBeanDefinitionNames() {
/* 75 */     return StringUtils.toStringArray(this.beanDefinitionMap.keySet());
/*    */   }
/*    */ 
/*    */   
/*    */   public int getBeanDefinitionCount() {
/* 80 */     return this.beanDefinitionMap.size();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isBeanNameInUse(String beanName) {
/* 85 */     return (isAlias(beanName) || containsBeanDefinition(beanName));
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/support/SimpleBeanDefinitionRegistry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */