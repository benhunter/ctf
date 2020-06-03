/*     */ package org.springframework.beans.support;
/*     */ 
/*     */ import java.beans.PropertyEditor;
/*     */ import java.lang.reflect.Method;
/*     */ import org.springframework.beans.PropertyEditorRegistry;
/*     */ import org.springframework.beans.SimpleTypeConverter;
/*     */ import org.springframework.beans.TypeConverter;
/*     */ import org.springframework.beans.TypeMismatchException;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.MethodInvoker;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ArgumentConvertingMethodInvoker
/*     */   extends MethodInvoker
/*     */ {
/*     */   @Nullable
/*     */   private TypeConverter typeConverter;
/*     */   private boolean useDefaultConverter = true;
/*     */   
/*     */   public void setTypeConverter(@Nullable TypeConverter typeConverter) {
/*  59 */     this.typeConverter = typeConverter;
/*  60 */     this.useDefaultConverter = (typeConverter == null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public TypeConverter getTypeConverter() {
/*  72 */     if (this.typeConverter == null && this.useDefaultConverter) {
/*  73 */       this.typeConverter = getDefaultTypeConverter();
/*     */     }
/*  75 */     return this.typeConverter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected TypeConverter getDefaultTypeConverter() {
/*  86 */     return (TypeConverter)new SimpleTypeConverter();
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
/*     */ 
/*     */   
/*     */   public void registerCustomEditor(Class<?> requiredType, PropertyEditor propertyEditor) {
/* 100 */     TypeConverter converter = getTypeConverter();
/* 101 */     if (!(converter instanceof PropertyEditorRegistry)) {
/* 102 */       throw new IllegalStateException("TypeConverter does not implement PropertyEditorRegistry interface: " + converter);
/*     */     }
/*     */     
/* 105 */     ((PropertyEditorRegistry)converter).registerCustomEditor(requiredType, propertyEditor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Method findMatchingMethod() {
/* 115 */     Method matchingMethod = super.findMatchingMethod();
/*     */     
/* 117 */     if (matchingMethod == null)
/*     */     {
/* 119 */       matchingMethod = doFindMatchingMethod(getArguments());
/*     */     }
/* 121 */     if (matchingMethod == null)
/*     */     {
/* 123 */       matchingMethod = doFindMatchingMethod(new Object[] { getArguments() });
/*     */     }
/* 125 */     return matchingMethod;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected Method doFindMatchingMethod(Object[] arguments) {
/* 136 */     TypeConverter converter = getTypeConverter();
/* 137 */     if (converter != null) {
/* 138 */       String targetMethod = getTargetMethod();
/* 139 */       Method matchingMethod = null;
/* 140 */       int argCount = arguments.length;
/* 141 */       Class<?> targetClass = getTargetClass();
/* 142 */       Assert.state((targetClass != null), "No target class set");
/* 143 */       Method[] candidates = ReflectionUtils.getAllDeclaredMethods(targetClass);
/* 144 */       int minTypeDiffWeight = Integer.MAX_VALUE;
/* 145 */       Object[] argumentsToUse = null;
/* 146 */       for (Method candidate : candidates) {
/* 147 */         if (candidate.getName().equals(targetMethod)) {
/*     */           
/* 149 */           Class<?>[] paramTypes = candidate.getParameterTypes();
/* 150 */           if (paramTypes.length == argCount) {
/* 151 */             Object[] convertedArguments = new Object[argCount];
/* 152 */             boolean match = true;
/* 153 */             for (int j = 0; j < argCount && match; j++) {
/*     */               
/*     */               try {
/* 156 */                 convertedArguments[j] = converter.convertIfNecessary(arguments[j], paramTypes[j]);
/*     */               }
/* 158 */               catch (TypeMismatchException ex) {
/*     */                 
/* 160 */                 match = false;
/*     */               } 
/*     */             } 
/* 163 */             if (match) {
/* 164 */               int typeDiffWeight = getTypeDifferenceWeight(paramTypes, convertedArguments);
/* 165 */               if (typeDiffWeight < minTypeDiffWeight) {
/* 166 */                 minTypeDiffWeight = typeDiffWeight;
/* 167 */                 matchingMethod = candidate;
/* 168 */                 argumentsToUse = convertedArguments;
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/* 174 */       if (matchingMethod != null) {
/* 175 */         setArguments(argumentsToUse);
/* 176 */         return matchingMethod;
/*     */       } 
/*     */     } 
/* 179 */     return null;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/support/ArgumentConvertingMethodInvoker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */