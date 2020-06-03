/*    */ package org.springframework.beans.factory.support;
/*    */ 
/*    */ import org.springframework.beans.factory.BeanDefinitionStoreException;
/*    */ import org.springframework.beans.factory.config.BeanDefinition;
/*    */ import org.springframework.lang.NonNull;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BeanDefinitionOverrideException
/*    */   extends BeanDefinitionStoreException
/*    */ {
/*    */   private final BeanDefinition beanDefinition;
/*    */   private final BeanDefinition existingDefinition;
/*    */   
/*    */   public BeanDefinitionOverrideException(String beanName, BeanDefinition beanDefinition, BeanDefinition existingDefinition) {
/* 49 */     super(beanDefinition.getResourceDescription(), beanName, "Cannot register bean definition [" + beanDefinition + "] for bean '" + beanName + "': There is already [" + existingDefinition + "] bound.");
/*    */ 
/*    */     
/* 52 */     this.beanDefinition = beanDefinition;
/* 53 */     this.existingDefinition = existingDefinition;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @NonNull
/*    */   public String getResourceDescription() {
/* 63 */     return String.valueOf(super.getResourceDescription());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @NonNull
/*    */   public String getBeanName() {
/* 72 */     return String.valueOf(super.getBeanName());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public BeanDefinition getBeanDefinition() {
/* 80 */     return this.beanDefinition;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public BeanDefinition getExistingDefinition() {
/* 88 */     return this.existingDefinition;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/support/BeanDefinitionOverrideException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */