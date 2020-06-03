/*    */ package org.springframework.context.expression;
/*    */ 
/*    */ import org.springframework.core.env.Environment;
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
/*    */ public class EnvironmentAccessor
/*    */   implements PropertyAccessor
/*    */ {
/*    */   public Class<?>[] getSpecificTargetClasses() {
/* 38 */     return new Class[] { Environment.class };
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean canRead(EvaluationContext context, @Nullable Object target, String name) throws AccessException {
/* 47 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public TypedValue read(EvaluationContext context, @Nullable Object target, String name) throws AccessException {
/* 56 */     Assert.state(target instanceof Environment, "Target must be of type Environment");
/* 57 */     return new TypedValue(((Environment)target).getProperty(name));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean canWrite(EvaluationContext context, @Nullable Object target, String name) throws AccessException {
/* 65 */     return false;
/*    */   }
/*    */   
/*    */   public void write(EvaluationContext context, @Nullable Object target, String name, @Nullable Object newValue) throws AccessException {}
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/expression/EnvironmentAccessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */