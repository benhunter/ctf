/*     */ package org.springframework.web.method;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.function.Predicate;
/*     */ import org.springframework.core.annotation.AnnotationUtils;
/*     */ import org.springframework.util.ClassUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class HandlerTypePredicate
/*     */   implements Predicate<Class<?>>
/*     */ {
/*     */   private final Set<String> basePackages;
/*     */   private final List<Class<?>> assignableTypes;
/*     */   private final List<Class<? extends Annotation>> annotations;
/*     */   
/*     */   private HandlerTypePredicate(Set<String> basePackages, List<Class<?>> assignableTypes, List<Class<? extends Annotation>> annotations) {
/*  65 */     this.basePackages = Collections.unmodifiableSet(basePackages);
/*  66 */     this.assignableTypes = Collections.unmodifiableList(assignableTypes);
/*  67 */     this.annotations = Collections.unmodifiableList(annotations);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean test(Class<?> controllerType) {
/*  73 */     if (!hasSelectors()) {
/*  74 */       return true;
/*     */     }
/*  76 */     if (controllerType != null) {
/*  77 */       for (String basePackage : this.basePackages) {
/*  78 */         if (controllerType.getName().startsWith(basePackage)) {
/*  79 */           return true;
/*     */         }
/*     */       } 
/*  82 */       for (Class<?> clazz : this.assignableTypes) {
/*  83 */         if (ClassUtils.isAssignable(clazz, controllerType)) {
/*  84 */           return true;
/*     */         }
/*     */       } 
/*  87 */       for (Class<? extends Annotation> annotationClass : this.annotations) {
/*  88 */         if (AnnotationUtils.findAnnotation(controllerType, annotationClass) != null) {
/*  89 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/*  93 */     return false;
/*     */   }
/*     */   
/*     */   private boolean hasSelectors() {
/*  97 */     return (!this.basePackages.isEmpty() || !this.assignableTypes.isEmpty() || !this.annotations.isEmpty());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HandlerTypePredicate forAnyHandlerType() {
/* 107 */     return new HandlerTypePredicate(
/* 108 */         Collections.emptySet(), Collections.emptyList(), Collections.emptyList());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HandlerTypePredicate forBasePackage(String... packages) {
/* 116 */     return (new Builder()).basePackage(packages).build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HandlerTypePredicate forBasePackageClass(Class<?>... packageClasses) {
/* 125 */     return (new Builder()).basePackageClass(packageClasses).build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HandlerTypePredicate forAssignableType(Class<?>... types) {
/* 133 */     return (new Builder()).assignableType(types).build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @SafeVarargs
/*     */   public static HandlerTypePredicate forAnnotation(Class<? extends Annotation>... annotations) {
/* 142 */     return (new Builder()).annotation(annotations).build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Builder builder() {
/* 149 */     return new Builder();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Builder
/*     */   {
/* 158 */     private final Set<String> basePackages = new LinkedHashSet<>();
/*     */     
/* 160 */     private final List<Class<?>> assignableTypes = new ArrayList<>();
/*     */     
/* 162 */     private final List<Class<? extends Annotation>> annotations = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder basePackage(String... packages) {
/* 169 */       Arrays.<String>stream(packages).filter(StringUtils::hasText).forEach(this::addBasePackage);
/* 170 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder basePackageClass(Class<?>... packageClasses) {
/* 179 */       Arrays.<Class<?>>stream(packageClasses).forEach(clazz -> addBasePackage(ClassUtils.getPackageName(clazz)));
/* 180 */       return this;
/*     */     }
/*     */     
/*     */     private void addBasePackage(String basePackage) {
/* 184 */       this.basePackages.add(basePackage.endsWith(".") ? basePackage : (basePackage + "."));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder assignableType(Class<?>... types) {
/* 192 */       this.assignableTypes.addAll(Arrays.asList(types));
/* 193 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public final Builder annotation(Class<? extends Annotation>... annotations) {
/* 202 */       this.annotations.addAll(Arrays.asList(annotations));
/* 203 */       return this;
/*     */     }
/*     */     
/*     */     public HandlerTypePredicate build() {
/* 207 */       return new HandlerTypePredicate(this.basePackages, this.assignableTypes, this.annotations);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/method/HandlerTypePredicate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */