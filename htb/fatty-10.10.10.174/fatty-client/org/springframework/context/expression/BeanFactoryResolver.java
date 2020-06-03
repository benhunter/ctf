/*    */ package org.springframework.context.expression;
/*    */ 
/*    */ import org.springframework.beans.BeansException;
/*    */ import org.springframework.beans.factory.BeanFactory;
/*    */ import org.springframework.expression.AccessException;
/*    */ import org.springframework.expression.BeanResolver;
/*    */ import org.springframework.expression.EvaluationContext;
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
/*    */ public class BeanFactoryResolver
/*    */   implements BeanResolver
/*    */ {
/*    */   private final BeanFactory beanFactory;
/*    */   
/*    */   public BeanFactoryResolver(BeanFactory beanFactory) {
/* 43 */     Assert.notNull(beanFactory, "BeanFactory must not be null");
/* 44 */     this.beanFactory = beanFactory;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Object resolve(EvaluationContext context, String beanName) throws AccessException {
/*    */     try {
/* 51 */       return this.beanFactory.getBean(beanName);
/*    */     }
/* 53 */     catch (BeansException ex) {
/* 54 */       throw new AccessException("Could not resolve bean reference against BeanFactory", ex);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/expression/BeanFactoryResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */