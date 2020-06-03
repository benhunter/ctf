/*     */ package org.springframework.expression.spel.support;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.core.convert.TypeDescriptor;
/*     */ import org.springframework.expression.AccessException;
/*     */ import org.springframework.expression.ConstructorExecutor;
/*     */ import org.springframework.expression.ConstructorResolver;
/*     */ import org.springframework.expression.EvaluationContext;
/*     */ import org.springframework.expression.EvaluationException;
/*     */ import org.springframework.expression.TypeConverter;
/*     */ import org.springframework.lang.Nullable;
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
/*     */ 
/*     */ 
/*     */ public class ReflectiveConstructorResolver
/*     */   implements ConstructorResolver
/*     */ {
/*     */   @Nullable
/*     */   public ConstructorExecutor resolve(EvaluationContext context, String typeName, List<TypeDescriptor> argumentTypes) throws AccessException {
/*     */     try {
/*  58 */       TypeConverter typeConverter = context.getTypeConverter();
/*  59 */       Class<?> type = context.getTypeLocator().findType(typeName);
/*  60 */       Constructor[] arrayOfConstructor = (Constructor[])type.getConstructors();
/*     */       
/*  62 */       Arrays.sort(arrayOfConstructor, (c1, c2) -> {
/*     */             int c1pl = c1.getParameterCount();
/*     */             
/*     */             int c2pl = c2.getParameterCount();
/*     */             return Integer.compare(c1pl, c2pl);
/*     */           });
/*  68 */       Constructor<?> closeMatch = null;
/*  69 */       Constructor<?> matchRequiringConversion = null;
/*     */       
/*  71 */       for (Constructor<?> ctor : arrayOfConstructor) {
/*  72 */         Class<?>[] paramTypes = ctor.getParameterTypes();
/*  73 */         List<TypeDescriptor> paramDescriptors = new ArrayList<>(paramTypes.length);
/*  74 */         for (int i = 0; i < paramTypes.length; i++) {
/*  75 */           paramDescriptors.add(new TypeDescriptor(new MethodParameter(ctor, i)));
/*     */         }
/*  77 */         ReflectionHelper.ArgumentsMatchInfo matchInfo = null;
/*  78 */         if (ctor.isVarArgs() && argumentTypes.size() >= paramTypes.length - 1) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*  85 */           matchInfo = ReflectionHelper.compareArgumentsVarargs(paramDescriptors, argumentTypes, typeConverter);
/*     */         }
/*  87 */         else if (paramTypes.length == argumentTypes.size()) {
/*     */           
/*  89 */           matchInfo = ReflectionHelper.compareArguments(paramDescriptors, argumentTypes, typeConverter);
/*     */         } 
/*  91 */         if (matchInfo != null) {
/*  92 */           if (matchInfo.isExactMatch()) {
/*  93 */             return new ReflectiveConstructorExecutor(ctor);
/*     */           }
/*  95 */           if (matchInfo.isCloseMatch()) {
/*  96 */             closeMatch = ctor;
/*     */           }
/*  98 */           else if (matchInfo.isMatchRequiringConversion()) {
/*  99 */             matchRequiringConversion = ctor;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 104 */       if (closeMatch != null) {
/* 105 */         return new ReflectiveConstructorExecutor(closeMatch);
/*     */       }
/* 107 */       if (matchRequiringConversion != null) {
/* 108 */         return new ReflectiveConstructorExecutor(matchRequiringConversion);
/*     */       }
/*     */       
/* 111 */       return null;
/*     */     
/*     */     }
/* 114 */     catch (EvaluationException ex) {
/* 115 */       throw new AccessException("Failed to resolve constructor", ex);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/expression/spel/support/ReflectiveConstructorResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */