/*     */ package org.springframework.beans;
/*     */ 
/*     */ import java.beans.IntrospectionException;
/*     */ import java.beans.PropertyDescriptor;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.core.BridgeMethodResolver;
/*     */ import org.springframework.core.GenericTypeResolver;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ import org.springframework.util.StringUtils;
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
/*     */ final class GenericTypeAwarePropertyDescriptor
/*     */   extends PropertyDescriptor
/*     */ {
/*     */   private final Class<?> beanClass;
/*     */   @Nullable
/*     */   private final Method readMethod;
/*     */   @Nullable
/*     */   private final Method writeMethod;
/*     */   @Nullable
/*     */   private volatile Set<Method> ambiguousWriteMethods;
/*     */   @Nullable
/*     */   private MethodParameter writeMethodParameter;
/*     */   @Nullable
/*     */   private Class<?> propertyType;
/*     */   private final Class<?> propertyEditorClass;
/*     */   
/*     */   public GenericTypeAwarePropertyDescriptor(Class<?> beanClass, String propertyName, @Nullable Method readMethod, @Nullable Method writeMethod, Class<?> propertyEditorClass) throws IntrospectionException {
/*  70 */     super(propertyName, (Method)null, (Method)null);
/*  71 */     this.beanClass = beanClass;
/*     */     
/*  73 */     Method readMethodToUse = (readMethod != null) ? BridgeMethodResolver.findBridgedMethod(readMethod) : null;
/*  74 */     Method writeMethodToUse = (writeMethod != null) ? BridgeMethodResolver.findBridgedMethod(writeMethod) : null;
/*  75 */     if (writeMethodToUse == null && readMethodToUse != null) {
/*     */ 
/*     */ 
/*     */       
/*  79 */       Method candidate = ClassUtils.getMethodIfAvailable(this.beanClass, "set" + 
/*  80 */           StringUtils.capitalize(getName()), (Class[])null);
/*  81 */       if (candidate != null && candidate.getParameterCount() == 1) {
/*  82 */         writeMethodToUse = candidate;
/*     */       }
/*     */     } 
/*  85 */     this.readMethod = readMethodToUse;
/*  86 */     this.writeMethod = writeMethodToUse;
/*     */     
/*  88 */     if (this.writeMethod != null) {
/*  89 */       if (this.readMethod == null) {
/*     */ 
/*     */ 
/*     */         
/*  93 */         Set<Method> ambiguousCandidates = new HashSet<>();
/*  94 */         for (Method method : beanClass.getMethods()) {
/*  95 */           if (method.getName().equals(writeMethodToUse.getName()) && 
/*  96 */             !method.equals(writeMethodToUse) && !method.isBridge() && method
/*  97 */             .getParameterCount() == writeMethodToUse.getParameterCount()) {
/*  98 */             ambiguousCandidates.add(method);
/*     */           }
/*     */         } 
/* 101 */         if (!ambiguousCandidates.isEmpty()) {
/* 102 */           this.ambiguousWriteMethods = ambiguousCandidates;
/*     */         }
/*     */       } 
/* 105 */       this.writeMethodParameter = new MethodParameter(this.writeMethod, 0);
/* 106 */       GenericTypeResolver.resolveParameterType(this.writeMethodParameter, this.beanClass);
/*     */     } 
/*     */     
/* 109 */     if (this.readMethod != null) {
/* 110 */       this.propertyType = GenericTypeResolver.resolveReturnType(this.readMethod, this.beanClass);
/*     */     }
/* 112 */     else if (this.writeMethodParameter != null) {
/* 113 */       this.propertyType = this.writeMethodParameter.getParameterType();
/*     */     } 
/*     */     
/* 116 */     this.propertyEditorClass = propertyEditorClass;
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<?> getBeanClass() {
/* 121 */     return this.beanClass;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Method getReadMethod() {
/* 127 */     return this.readMethod;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Method getWriteMethod() {
/* 133 */     return this.writeMethod;
/*     */   }
/*     */   
/*     */   public Method getWriteMethodForActualAccess() {
/* 137 */     Assert.state((this.writeMethod != null), "No write method available");
/* 138 */     Set<Method> ambiguousCandidates = this.ambiguousWriteMethods;
/* 139 */     if (ambiguousCandidates != null) {
/* 140 */       this.ambiguousWriteMethods = null;
/* 141 */       LogFactory.getLog(GenericTypeAwarePropertyDescriptor.class).warn("Invalid JavaBean property '" + 
/* 142 */           getName() + "' being accessed! Ambiguous write methods found next to actually used [" + this.writeMethod + "]: " + ambiguousCandidates);
/*     */     } 
/*     */     
/* 145 */     return this.writeMethod;
/*     */   }
/*     */   
/*     */   public MethodParameter getWriteMethodParameter() {
/* 149 */     Assert.state((this.writeMethodParameter != null), "No write method available");
/* 150 */     return this.writeMethodParameter;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Class<?> getPropertyType() {
/* 156 */     return this.propertyType;
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<?> getPropertyEditorClass() {
/* 161 */     return this.propertyEditorClass;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 167 */     if (this == other) {
/* 168 */       return true;
/*     */     }
/* 170 */     if (!(other instanceof GenericTypeAwarePropertyDescriptor)) {
/* 171 */       return false;
/*     */     }
/* 173 */     GenericTypeAwarePropertyDescriptor otherPd = (GenericTypeAwarePropertyDescriptor)other;
/* 174 */     return (getBeanClass().equals(otherPd.getBeanClass()) && PropertyDescriptorUtils.equals(this, otherPd));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 179 */     int hashCode = getBeanClass().hashCode();
/* 180 */     hashCode = 29 * hashCode + ObjectUtils.nullSafeHashCode(getReadMethod());
/* 181 */     hashCode = 29 * hashCode + ObjectUtils.nullSafeHashCode(getWriteMethod());
/* 182 */     return hashCode;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/GenericTypeAwarePropertyDescriptor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */