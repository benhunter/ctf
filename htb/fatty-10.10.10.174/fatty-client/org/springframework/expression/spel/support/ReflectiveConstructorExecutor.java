/*    */ package org.springframework.expression.spel.support;
/*    */ 
/*    */ import java.lang.reflect.Constructor;
/*    */ import org.springframework.expression.AccessException;
/*    */ import org.springframework.expression.ConstructorExecutor;
/*    */ import org.springframework.expression.EvaluationContext;
/*    */ import org.springframework.expression.TypedValue;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.util.ReflectionUtils;
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
/*    */ public class ReflectiveConstructorExecutor
/*    */   implements ConstructorExecutor
/*    */ {
/*    */   private final Constructor<?> ctor;
/*    */   @Nullable
/*    */   private final Integer varargsPosition;
/*    */   
/*    */   public ReflectiveConstructorExecutor(Constructor<?> ctor) {
/* 45 */     this.ctor = ctor;
/* 46 */     if (ctor.isVarArgs()) {
/* 47 */       Class<?>[] paramTypes = ctor.getParameterTypes();
/* 48 */       this.varargsPosition = Integer.valueOf(paramTypes.length - 1);
/*    */     } else {
/*    */       
/* 51 */       this.varargsPosition = null;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public TypedValue execute(EvaluationContext context, Object... arguments) throws AccessException {
/*    */     try {
/* 58 */       ReflectionHelper.convertArguments(context
/* 59 */           .getTypeConverter(), arguments, this.ctor, this.varargsPosition);
/* 60 */       if (this.ctor.isVarArgs()) {
/* 61 */         arguments = ReflectionHelper.setupArgumentsForVarargsInvocation(this.ctor
/* 62 */             .getParameterTypes(), arguments);
/*    */       }
/* 64 */       ReflectionUtils.makeAccessible(this.ctor);
/* 65 */       return new TypedValue(this.ctor.newInstance(arguments));
/*    */     }
/* 67 */     catch (Exception ex) {
/* 68 */       throw new AccessException("Problem invoking constructor: " + this.ctor, ex);
/*    */     } 
/*    */   }
/*    */   
/*    */   public Constructor<?> getConstructor() {
/* 73 */     return this.ctor;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/expression/spel/support/ReflectiveConstructorExecutor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */