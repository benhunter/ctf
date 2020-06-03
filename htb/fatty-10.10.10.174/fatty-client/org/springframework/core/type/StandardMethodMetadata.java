/*     */ package org.springframework.core.type;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.Map;
/*     */ import org.springframework.core.annotation.AnnotatedElementUtils;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.MultiValueMap;
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
/*     */ public class StandardMethodMetadata
/*     */   implements MethodMetadata
/*     */ {
/*     */   private final Method introspectedMethod;
/*     */   private final boolean nestedAnnotationsAsMap;
/*     */   
/*     */   public StandardMethodMetadata(Method introspectedMethod) {
/*  50 */     this(introspectedMethod, false);
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
/*     */   public StandardMethodMetadata(Method introspectedMethod, boolean nestedAnnotationsAsMap) {
/*  65 */     Assert.notNull(introspectedMethod, "Method must not be null");
/*  66 */     this.introspectedMethod = introspectedMethod;
/*  67 */     this.nestedAnnotationsAsMap = nestedAnnotationsAsMap;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Method getIntrospectedMethod() {
/*  75 */     return this.introspectedMethod;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getMethodName() {
/*  80 */     return this.introspectedMethod.getName();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDeclaringClassName() {
/*  85 */     return this.introspectedMethod.getDeclaringClass().getName();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getReturnTypeName() {
/*  90 */     return this.introspectedMethod.getReturnType().getName();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAbstract() {
/*  95 */     return Modifier.isAbstract(this.introspectedMethod.getModifiers());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isStatic() {
/* 100 */     return Modifier.isStatic(this.introspectedMethod.getModifiers());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFinal() {
/* 105 */     return Modifier.isFinal(this.introspectedMethod.getModifiers());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOverridable() {
/* 110 */     return (!isStatic() && !isFinal() && !Modifier.isPrivate(this.introspectedMethod.getModifiers()));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAnnotated(String annotationName) {
/* 115 */     return AnnotatedElementUtils.isAnnotated(this.introspectedMethod, annotationName);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Map<String, Object> getAnnotationAttributes(String annotationName) {
/* 121 */     return getAnnotationAttributes(annotationName, false);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Map<String, Object> getAnnotationAttributes(String annotationName, boolean classValuesAsString) {
/* 127 */     return (Map<String, Object>)AnnotatedElementUtils.getMergedAnnotationAttributes(this.introspectedMethod, annotationName, classValuesAsString, this.nestedAnnotationsAsMap);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public MultiValueMap<String, Object> getAllAnnotationAttributes(String annotationName) {
/* 134 */     return getAllAnnotationAttributes(annotationName, false);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public MultiValueMap<String, Object> getAllAnnotationAttributes(String annotationName, boolean classValuesAsString) {
/* 140 */     return AnnotatedElementUtils.getAllAnnotationAttributes(this.introspectedMethod, annotationName, classValuesAsString, this.nestedAnnotationsAsMap);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/type/StandardMethodMetadata.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */