/*    */ package org.springframework.context.expression;
/*    */ 
/*    */ import org.springframework.beans.factory.config.BeanExpressionContext;
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
/*    */ public class BeanExpressionContextAccessor
/*    */   implements PropertyAccessor
/*    */ {
/*    */   public boolean canRead(EvaluationContext context, @Nullable Object target, String name) throws AccessException {
/* 39 */     return (target instanceof BeanExpressionContext && ((BeanExpressionContext)target).containsObject(name));
/*    */   }
/*    */ 
/*    */   
/*    */   public TypedValue read(EvaluationContext context, @Nullable Object target, String name) throws AccessException {
/* 44 */     Assert.state(target instanceof BeanExpressionContext, "Target must be of type BeanExpressionContext");
/* 45 */     return new TypedValue(((BeanExpressionContext)target).getObject(name));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canWrite(EvaluationContext context, @Nullable Object target, String name) throws AccessException {
/* 50 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void write(EvaluationContext context, @Nullable Object target, String name, @Nullable Object newValue) throws AccessException {
/* 57 */     throw new AccessException("Beans in a BeanFactory are read-only");
/*    */   }
/*    */ 
/*    */   
/*    */   public Class<?>[] getSpecificTargetClasses() {
/* 62 */     return new Class[] { BeanExpressionContext.class };
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/expression/BeanExpressionContextAccessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */