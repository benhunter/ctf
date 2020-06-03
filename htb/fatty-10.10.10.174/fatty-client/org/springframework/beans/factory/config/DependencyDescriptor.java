/*     */ package org.springframework.beans.factory.config;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.Serializable;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.ParameterizedType;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import kotlin.reflect.KProperty;
/*     */ import kotlin.reflect.jvm.ReflectJvmMapping;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.InjectionPoint;
/*     */ import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
/*     */ import org.springframework.core.GenericTypeResolver;
/*     */ import org.springframework.core.KotlinDetector;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.core.ParameterNameDiscoverer;
/*     */ import org.springframework.core.ResolvableType;
/*     */ import org.springframework.core.convert.TypeDescriptor;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.ObjectUtils;
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
/*     */ public class DependencyDescriptor
/*     */   extends InjectionPoint
/*     */   implements Serializable
/*     */ {
/*     */   private final Class<?> declaringClass;
/*     */   @Nullable
/*     */   private String methodName;
/*     */   @Nullable
/*     */   private Class<?>[] parameterTypes;
/*     */   private int parameterIndex;
/*     */   @Nullable
/*     */   private String fieldName;
/*     */   private final boolean required;
/*     */   private final boolean eager;
/*  73 */   private int nestingLevel = 1;
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private Class<?> containingClass;
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private volatile transient ResolvableType resolvableType;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private volatile transient TypeDescriptor typeDescriptor;
/*     */ 
/*     */ 
/*     */   
/*     */   public DependencyDescriptor(MethodParameter methodParameter, boolean required) {
/*  92 */     this(methodParameter, required, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DependencyDescriptor(MethodParameter methodParameter, boolean required, boolean eager) {
/* 103 */     super(methodParameter);
/*     */     
/* 105 */     this.declaringClass = methodParameter.getDeclaringClass();
/* 106 */     if (methodParameter.getMethod() != null) {
/* 107 */       this.methodName = methodParameter.getMethod().getName();
/*     */     }
/* 109 */     this.parameterTypes = methodParameter.getExecutable().getParameterTypes();
/* 110 */     this.parameterIndex = methodParameter.getParameterIndex();
/* 111 */     this.containingClass = methodParameter.getContainingClass();
/* 112 */     this.required = required;
/* 113 */     this.eager = eager;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DependencyDescriptor(Field field, boolean required) {
/* 123 */     this(field, required, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DependencyDescriptor(Field field, boolean required, boolean eager) {
/* 134 */     super(field);
/*     */     
/* 136 */     this.declaringClass = field.getDeclaringClass();
/* 137 */     this.fieldName = field.getName();
/* 138 */     this.required = required;
/* 139 */     this.eager = eager;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DependencyDescriptor(DependencyDescriptor original) {
/* 147 */     super(original);
/*     */     
/* 149 */     this.declaringClass = original.declaringClass;
/* 150 */     this.methodName = original.methodName;
/* 151 */     this.parameterTypes = original.parameterTypes;
/* 152 */     this.parameterIndex = original.parameterIndex;
/* 153 */     this.fieldName = original.fieldName;
/* 154 */     this.containingClass = original.containingClass;
/* 155 */     this.required = original.required;
/* 156 */     this.eager = original.eager;
/* 157 */     this.nestingLevel = original.nestingLevel;
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
/*     */   public boolean isRequired() {
/* 169 */     if (!this.required) {
/* 170 */       return false;
/*     */     }
/*     */     
/* 173 */     if (this.field != null) {
/* 174 */       return (this.field.getType() != Optional.class && !hasNullableAnnotation() && (
/* 175 */         !KotlinDetector.isKotlinReflectPresent() || 
/* 176 */         !KotlinDetector.isKotlinType(this.field.getDeclaringClass()) || 
/* 177 */         !KotlinDelegate.isNullable(this.field)));
/*     */     }
/*     */     
/* 180 */     return !obtainMethodParameter().isOptional();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean hasNullableAnnotation() {
/* 190 */     for (Annotation ann : getAnnotations()) {
/* 191 */       if ("Nullable".equals(ann.annotationType().getSimpleName())) {
/* 192 */         return true;
/*     */       }
/*     */     } 
/* 195 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEager() {
/* 203 */     return this.eager;
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
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object resolveNotUnique(ResolvableType type, Map<String, Object> matchingBeans) throws BeansException {
/* 221 */     throw new NoUniqueBeanDefinitionException(type, matchingBeans.keySet());
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   @Nullable
/*     */   public Object resolveNotUnique(Class<?> type, Map<String, Object> matchingBeans) throws BeansException {
/* 241 */     throw new NoUniqueBeanDefinitionException(type, matchingBeans.keySet());
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
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object resolveShortcut(BeanFactory beanFactory) throws BeansException {
/* 258 */     return null;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object resolveCandidate(String beanName, Class<?> requiredType, BeanFactory beanFactory) throws BeansException {
/* 277 */     return beanFactory.getBean(beanName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void increaseNestingLevel() {
/* 286 */     this.nestingLevel++;
/* 287 */     this.resolvableType = null;
/* 288 */     if (this.methodParameter != null) {
/* 289 */       this.methodParameter.increaseNestingLevel();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setContainingClass(Class<?> containingClass) {
/* 300 */     this.containingClass = containingClass;
/* 301 */     this.resolvableType = null;
/* 302 */     if (this.methodParameter != null) {
/* 303 */       GenericTypeResolver.resolveParameterType(this.methodParameter, containingClass);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResolvableType getResolvableType() {
/* 312 */     ResolvableType resolvableType = this.resolvableType;
/* 313 */     if (resolvableType == null) {
/*     */ 
/*     */       
/* 316 */       resolvableType = (this.field != null) ? ResolvableType.forField(this.field, this.nestingLevel, this.containingClass) : ResolvableType.forMethodParameter(obtainMethodParameter());
/* 317 */       this.resolvableType = resolvableType;
/*     */     } 
/* 319 */     return resolvableType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypeDescriptor getTypeDescriptor() {
/* 327 */     TypeDescriptor typeDescriptor = this.typeDescriptor;
/* 328 */     if (typeDescriptor == null) {
/*     */ 
/*     */       
/* 331 */       typeDescriptor = (this.field != null) ? new TypeDescriptor(getResolvableType(), getDependencyType(), getAnnotations()) : new TypeDescriptor(obtainMethodParameter());
/* 332 */       this.typeDescriptor = typeDescriptor;
/*     */     } 
/* 334 */     return typeDescriptor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean fallbackMatchAllowed() {
/* 345 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DependencyDescriptor forFallbackMatch() {
/* 354 */     return new DependencyDescriptor(this)
/*     */       {
/*     */         public boolean fallbackMatchAllowed() {
/* 357 */           return true;
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void initParameterNameDiscovery(@Nullable ParameterNameDiscoverer parameterNameDiscoverer) {
/* 369 */     if (this.methodParameter != null) {
/* 370 */       this.methodParameter.initParameterNameDiscovery(parameterNameDiscoverer);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getDependencyName() {
/* 380 */     return (this.field != null) ? this.field.getName() : obtainMethodParameter().getParameterName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<?> getDependencyType() {
/* 388 */     if (this.field != null) {
/* 389 */       if (this.nestingLevel > 1) {
/* 390 */         Type type = this.field.getGenericType();
/* 391 */         for (int i = 2; i <= this.nestingLevel; i++) {
/* 392 */           if (type instanceof ParameterizedType) {
/* 393 */             Type[] args = ((ParameterizedType)type).getActualTypeArguments();
/* 394 */             type = args[args.length - 1];
/*     */           } 
/*     */         } 
/* 397 */         if (type instanceof Class) {
/* 398 */           return (Class)type;
/*     */         }
/* 400 */         if (type instanceof ParameterizedType) {
/* 401 */           Type arg = ((ParameterizedType)type).getRawType();
/* 402 */           if (arg instanceof Class) {
/* 403 */             return (Class)arg;
/*     */           }
/*     */         } 
/* 406 */         return Object.class;
/*     */       } 
/*     */       
/* 409 */       return this.field.getType();
/*     */     } 
/*     */ 
/*     */     
/* 413 */     return obtainMethodParameter().getNestedParameterType();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 420 */     if (this == other) {
/* 421 */       return true;
/*     */     }
/* 423 */     if (!super.equals(other)) {
/* 424 */       return false;
/*     */     }
/* 426 */     DependencyDescriptor otherDesc = (DependencyDescriptor)other;
/* 427 */     return (this.required == otherDesc.required && this.eager == otherDesc.eager && this.nestingLevel == otherDesc.nestingLevel && this.containingClass == otherDesc.containingClass);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 433 */     return 31 * super.hashCode() + ObjectUtils.nullSafeHashCode(this.containingClass);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
/* 443 */     ois.defaultReadObject();
/*     */ 
/*     */     
/*     */     try {
/* 447 */       if (this.fieldName != null) {
/* 448 */         this.field = this.declaringClass.getDeclaredField(this.fieldName);
/*     */       } else {
/*     */         
/* 451 */         if (this.methodName != null) {
/* 452 */           this
/* 453 */             .methodParameter = new MethodParameter(this.declaringClass.getDeclaredMethod(this.methodName, this.parameterTypes), this.parameterIndex);
/*     */         } else {
/*     */           
/* 456 */           this
/* 457 */             .methodParameter = new MethodParameter(this.declaringClass.getDeclaredConstructor(this.parameterTypes), this.parameterIndex);
/*     */         } 
/* 459 */         for (int i = 1; i < this.nestingLevel; i++) {
/* 460 */           this.methodParameter.increaseNestingLevel();
/*     */         }
/*     */       }
/*     */     
/* 464 */     } catch (Throwable ex) {
/* 465 */       throw new IllegalStateException("Could not find original class structure", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class KotlinDelegate
/*     */   {
/*     */     public static boolean isNullable(Field field) {
/* 479 */       KProperty<?> property = ReflectJvmMapping.getKotlinProperty(field);
/* 480 */       return (property != null && property.getReturnType().isMarkedNullable());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/config/DependencyDescriptor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */