/*    */ package org.springframework.context.expression;
/*    */ 
/*    */ import org.springframework.beans.factory.BeanFactory;
/*    */ import org.springframework.expression.AccessException;
/*    */ import org.springframework.expression.EvaluationContext;
/*    */ import org.springframework.expression.PropertyAccessor;
/*    */ import org.springframework.expression.TypedValue;
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
/*    */ public class BeanFactoryAccessor
/*    */   implements PropertyAccessor
/*    */ {
/*    */   public Class<?>[] getSpecificTargetClasses() {
/* 39 */     return new Class[] { BeanFactory.class };
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canRead(EvaluationContext context, @Nullable Object target, String name) throws AccessException {
/* 44 */     return (target instanceof BeanFactory && ((BeanFactory)target).containsBean(name));
/*    */   }
/*    */ 
/*    */   
/*    */   public TypedValue read(EvaluationContext context, @Nullable Object target, String name) throws AccessException {
/* 49 */     Assert.state(target instanceof BeanFactory, "Target must be of type BeanFactory");
/* 50 */     return new TypedValue(((BeanFactory)target).getBean(name));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canWrite(EvaluationContext context, @Nullable Object target, String name) throws AccessException {
/* 55 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void write(EvaluationContext context, @Nullable Object target, String name, @Nullable Object newValue) throws AccessException {
/* 62 */     throw new AccessException("Beans in a BeanFactory are read-only");
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/expression/BeanFactoryAccessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */