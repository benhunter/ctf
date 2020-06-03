/*    */ package org.springframework.beans.factory;
/*    */ 
/*    */ import org.springframework.beans.BeansException;
/*    */ import org.springframework.util.ClassUtils;
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
/*    */ public class BeanNotOfRequiredTypeException
/*    */   extends BeansException
/*    */ {
/*    */   private final String beanName;
/*    */   private final Class<?> requiredType;
/*    */   private final Class<?> actualType;
/*    */   
/*    */   public BeanNotOfRequiredTypeException(String beanName, Class<?> requiredType, Class<?> actualType) {
/* 49 */     super("Bean named '" + beanName + "' is expected to be of type '" + ClassUtils.getQualifiedName(requiredType) + "' but was actually of type '" + 
/* 50 */         ClassUtils.getQualifiedName(actualType) + "'");
/* 51 */     this.beanName = beanName;
/* 52 */     this.requiredType = requiredType;
/* 53 */     this.actualType = actualType;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getBeanName() {
/* 61 */     return this.beanName;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Class<?> getRequiredType() {
/* 68 */     return this.requiredType;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Class<?> getActualType() {
/* 75 */     return this.actualType;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/BeanNotOfRequiredTypeException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */