/*     */ package org.springframework.core.type.classreading;
/*     */ 
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.springframework.asm.AnnotationVisitor;
/*     */ import org.springframework.asm.MethodVisitor;
/*     */ import org.springframework.asm.Type;
/*     */ import org.springframework.core.annotation.AnnotationAttributes;
/*     */ import org.springframework.core.type.MethodMetadata;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.LinkedMultiValueMap;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MethodMetadataReadingVisitor
/*     */   extends MethodVisitor
/*     */   implements MethodMetadata
/*     */ {
/*     */   protected final String methodName;
/*     */   protected final int access;
/*     */   protected final String declaringClassName;
/*     */   protected final String returnTypeName;
/*     */   @Nullable
/*     */   protected final ClassLoader classLoader;
/*     */   protected final Set<MethodMetadata> methodMetadataSet;
/*  62 */   protected final Map<String, Set<String>> metaAnnotationMap = new LinkedHashMap<>(4);
/*     */   
/*  64 */   protected final LinkedMultiValueMap<String, AnnotationAttributes> attributesMap = new LinkedMultiValueMap(4);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MethodMetadataReadingVisitor(String methodName, int access, String declaringClassName, String returnTypeName, @Nullable ClassLoader classLoader, Set<MethodMetadata> methodMetadataSet) {
/*  70 */     super(458752);
/*  71 */     this.methodName = methodName;
/*  72 */     this.access = access;
/*  73 */     this.declaringClassName = declaringClassName;
/*  74 */     this.returnTypeName = returnTypeName;
/*  75 */     this.classLoader = classLoader;
/*  76 */     this.methodMetadataSet = methodMetadataSet;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
/*  82 */     this.methodMetadataSet.add(this);
/*  83 */     String className = Type.getType(desc).getClassName();
/*  84 */     return new AnnotationAttributesReadingVisitor(className, (MultiValueMap<String, AnnotationAttributes>)this.attributesMap, this.metaAnnotationMap, this.classLoader);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMethodName() {
/*  91 */     return this.methodName;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAbstract() {
/*  96 */     return ((this.access & 0x400) != 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isStatic() {
/* 101 */     return ((this.access & 0x8) != 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFinal() {
/* 106 */     return ((this.access & 0x10) != 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOverridable() {
/* 111 */     return (!isStatic() && !isFinal() && (this.access & 0x2) == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAnnotated(String annotationName) {
/* 116 */     return this.attributesMap.containsKey(annotationName);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public AnnotationAttributes getAnnotationAttributes(String annotationName) {
/* 122 */     return getAnnotationAttributes(annotationName, false);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public AnnotationAttributes getAnnotationAttributes(String annotationName, boolean classValuesAsString) {
/* 128 */     AnnotationAttributes raw = AnnotationReadingVisitorUtils.getMergedAnnotationAttributes(this.attributesMap, this.metaAnnotationMap, annotationName);
/*     */     
/* 130 */     if (raw == null) {
/* 131 */       return null;
/*     */     }
/* 133 */     return AnnotationReadingVisitorUtils.convertClassValues("method '" + 
/* 134 */         getMethodName() + "'", this.classLoader, raw, classValuesAsString);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public MultiValueMap<String, Object> getAllAnnotationAttributes(String annotationName) {
/* 140 */     return getAllAnnotationAttributes(annotationName, false);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public MultiValueMap<String, Object> getAllAnnotationAttributes(String annotationName, boolean classValuesAsString) {
/* 146 */     if (!this.attributesMap.containsKey(annotationName)) {
/* 147 */       return null;
/*     */     }
/* 149 */     LinkedMultiValueMap linkedMultiValueMap = new LinkedMultiValueMap();
/* 150 */     List<AnnotationAttributes> attributesList = this.attributesMap.get(annotationName);
/* 151 */     if (attributesList != null) {
/* 152 */       for (AnnotationAttributes annotationAttributes : attributesList) {
/* 153 */         AnnotationAttributes convertedAttributes = AnnotationReadingVisitorUtils.convertClassValues("method '" + 
/* 154 */             getMethodName() + "'", this.classLoader, annotationAttributes, classValuesAsString);
/* 155 */         convertedAttributes.forEach(linkedMultiValueMap::add);
/*     */       } 
/*     */     }
/* 158 */     return (MultiValueMap<String, Object>)linkedMultiValueMap;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDeclaringClassName() {
/* 163 */     return this.declaringClassName;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getReturnTypeName() {
/* 168 */     return this.returnTypeName;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/type/classreading/MethodMetadataReadingVisitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */