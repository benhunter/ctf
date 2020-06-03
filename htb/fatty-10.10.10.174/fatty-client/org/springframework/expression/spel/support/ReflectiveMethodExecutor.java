/*     */ package org.springframework.expression.spel.support;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.core.convert.TypeDescriptor;
/*     */ import org.springframework.expression.AccessException;
/*     */ import org.springframework.expression.EvaluationContext;
/*     */ import org.springframework.expression.MethodExecutor;
/*     */ import org.springframework.expression.TypedValue;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ReflectionUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ReflectiveMethodExecutor
/*     */   implements MethodExecutor
/*     */ {
/*     */   private final Method originalMethod;
/*     */   private final Method methodToInvoke;
/*     */   @Nullable
/*     */   private final Integer varargsPosition;
/*     */   private boolean computedPublicDeclaringClass = false;
/*     */   @Nullable
/*     */   private Class<?> publicDeclaringClass;
/*     */   private boolean argumentConversionOccurred = false;
/*     */   
/*     */   public ReflectiveMethodExecutor(Method method) {
/*  61 */     this.originalMethod = method;
/*  62 */     this.methodToInvoke = ClassUtils.getInterfaceMethodIfPossible(method);
/*  63 */     if (method.isVarArgs()) {
/*  64 */       Class<?>[] paramTypes = method.getParameterTypes();
/*  65 */       this.varargsPosition = Integer.valueOf(paramTypes.length - 1);
/*     */     } else {
/*     */       
/*  68 */       this.varargsPosition = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Method getMethod() {
/*  77 */     return this.originalMethod;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Class<?> getPublicDeclaringClass() {
/*  90 */     if (!this.computedPublicDeclaringClass) {
/*  91 */       this
/*  92 */         .publicDeclaringClass = discoverPublicDeclaringClass(this.originalMethod, this.originalMethod.getDeclaringClass());
/*  93 */       this.computedPublicDeclaringClass = true;
/*     */     } 
/*  95 */     return this.publicDeclaringClass;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private Class<?> discoverPublicDeclaringClass(Method method, Class<?> clazz) {
/* 100 */     if (Modifier.isPublic(clazz.getModifiers())) {
/*     */       try {
/* 102 */         clazz.getDeclaredMethod(method.getName(), method.getParameterTypes());
/* 103 */         return clazz;
/*     */       }
/* 105 */       catch (NoSuchMethodException noSuchMethodException) {}
/*     */     }
/*     */ 
/*     */     
/* 109 */     if (clazz.getSuperclass() != null) {
/* 110 */       return discoverPublicDeclaringClass(method, clazz.getSuperclass());
/*     */     }
/* 112 */     return null;
/*     */   }
/*     */   
/*     */   public boolean didArgumentConversionOccur() {
/* 116 */     return this.argumentConversionOccurred;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public TypedValue execute(EvaluationContext context, Object target, Object... arguments) throws AccessException {
/*     */     try {
/* 123 */       this.argumentConversionOccurred = ReflectionHelper.convertArguments(context
/* 124 */           .getTypeConverter(), arguments, this.originalMethod, this.varargsPosition);
/* 125 */       if (this.originalMethod.isVarArgs()) {
/* 126 */         arguments = ReflectionHelper.setupArgumentsForVarargsInvocation(this.originalMethod
/* 127 */             .getParameterTypes(), arguments);
/*     */       }
/* 129 */       ReflectionUtils.makeAccessible(this.methodToInvoke);
/* 130 */       Object value = this.methodToInvoke.invoke(target, arguments);
/* 131 */       return new TypedValue(value, (new TypeDescriptor(new MethodParameter(this.originalMethod, -1))).narrow(value));
/*     */     }
/* 133 */     catch (Exception ex) {
/* 134 */       throw new AccessException("Problem invoking method: " + this.methodToInvoke, ex);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/expression/spel/support/ReflectiveMethodExecutor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */