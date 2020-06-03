/*    */ package org.springframework.beans.factory.wiring;
/*    */ 
/*    */ import org.springframework.util.Assert;
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
/*    */ public class ClassNameBeanWiringInfoResolver
/*    */   implements BeanWiringInfoResolver
/*    */ {
/*    */   public BeanWiringInfo resolveWiringInfo(Object beanInstance) {
/* 36 */     Assert.notNull(beanInstance, "Bean instance must not be null");
/* 37 */     return new BeanWiringInfo(ClassUtils.getUserClass(beanInstance).getName(), true);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/wiring/ClassNameBeanWiringInfoResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */