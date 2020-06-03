/*    */ package org.springframework.beans.factory.config;
/*    */ 
/*    */ import org.springframework.lang.Nullable;
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
/*    */ 
/*    */ public class RuntimeBeanNameReference
/*    */   implements BeanReference
/*    */ {
/*    */   private final String beanName;
/*    */   @Nullable
/*    */   private Object source;
/*    */   
/*    */   public RuntimeBeanNameReference(String beanName) {
/* 45 */     Assert.hasText(beanName, "'beanName' must not be empty");
/* 46 */     this.beanName = beanName;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getBeanName() {
/* 51 */     return this.beanName;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setSource(@Nullable Object source) {
/* 59 */     this.source = source;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public Object getSource() {
/* 65 */     return this.source;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(Object other) {
/* 71 */     if (this == other) {
/* 72 */       return true;
/*    */     }
/* 74 */     if (!(other instanceof RuntimeBeanNameReference)) {
/* 75 */       return false;
/*    */     }
/* 77 */     RuntimeBeanNameReference that = (RuntimeBeanNameReference)other;
/* 78 */     return this.beanName.equals(that.beanName);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 83 */     return this.beanName.hashCode();
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 88 */     return '<' + getBeanName() + '>';
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/config/RuntimeBeanNameReference.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */