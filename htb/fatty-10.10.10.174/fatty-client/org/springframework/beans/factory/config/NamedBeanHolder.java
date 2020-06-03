/*    */ package org.springframework.beans.factory.config;
/*    */ 
/*    */ import org.springframework.beans.factory.NamedBean;
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
/*    */ 
/*    */ public class NamedBeanHolder<T>
/*    */   implements NamedBean
/*    */ {
/*    */   private final String beanName;
/*    */   private final T beanInstance;
/*    */   
/*    */   public NamedBeanHolder(String beanName, T beanInstance) {
/* 43 */     Assert.notNull(beanName, "Bean name must not be null");
/* 44 */     this.beanName = beanName;
/* 45 */     this.beanInstance = beanInstance;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getBeanName() {
/* 54 */     return this.beanName;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public T getBeanInstance() {
/* 61 */     return this.beanInstance;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/config/NamedBeanHolder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */