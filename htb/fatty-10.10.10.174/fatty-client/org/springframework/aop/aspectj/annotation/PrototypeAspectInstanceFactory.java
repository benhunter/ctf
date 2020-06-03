/*    */ package org.springframework.aop.aspectj.annotation;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import org.springframework.beans.factory.BeanFactory;
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
/*    */ public class PrototypeAspectInstanceFactory
/*    */   extends BeanFactoryAspectInstanceFactory
/*    */   implements Serializable
/*    */ {
/*    */   public PrototypeAspectInstanceFactory(BeanFactory beanFactory, String name) {
/* 48 */     super(beanFactory, name);
/* 49 */     if (!beanFactory.isPrototype(name))
/* 50 */       throw new IllegalArgumentException("Cannot use PrototypeAspectInstanceFactory with bean named '" + name + "': not a prototype"); 
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/aspectj/annotation/PrototypeAspectInstanceFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */