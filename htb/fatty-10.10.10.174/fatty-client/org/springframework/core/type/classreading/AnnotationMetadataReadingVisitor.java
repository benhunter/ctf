/*     */ package org.springframework.core.type.classreading;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.springframework.asm.AnnotationVisitor;
/*     */ import org.springframework.asm.Attribute;
/*     */ import org.springframework.asm.FieldVisitor;
/*     */ import org.springframework.asm.MethodVisitor;
/*     */ import org.springframework.asm.Type;
/*     */ import org.springframework.core.annotation.AnnotationAttributes;
/*     */ import org.springframework.core.annotation.AnnotationUtils;
/*     */ import org.springframework.core.type.AnnotationMetadata;
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
/*     */ public class AnnotationMetadataReadingVisitor
/*     */   extends ClassMetadataReadingVisitor
/*     */   implements AnnotationMetadata
/*     */ {
/*     */   @Nullable
/*     */   protected final ClassLoader classLoader;
/*  56 */   protected final Set<String> annotationSet = new LinkedHashSet<>(4);
/*     */   
/*  58 */   protected final Map<String, Set<String>> metaAnnotationMap = new LinkedHashMap<>(4);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  65 */   protected final LinkedMultiValueMap<String, AnnotationAttributes> attributesMap = new LinkedMultiValueMap(4);
/*     */   
/*  67 */   protected final Set<MethodMetadata> methodMetadataSet = new LinkedHashSet<>(4);
/*     */ 
/*     */   
/*     */   public AnnotationMetadataReadingVisitor(@Nullable ClassLoader classLoader) {
/*  71 */     this.classLoader = classLoader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
/*  79 */     if ((access & 0x40) != 0) {
/*  80 */       return super.visitMethod(access, name, desc, signature, exceptions);
/*     */     }
/*  82 */     return new MethodMetadataReadingVisitor(name, access, getClassName(), 
/*  83 */         Type.getReturnType(desc).getClassName(), this.classLoader, this.methodMetadataSet);
/*     */   }
/*     */ 
/*     */   
/*     */   public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
/*  88 */     String className = Type.getType(desc).getClassName();
/*  89 */     this.annotationSet.add(className);
/*  90 */     return new AnnotationAttributesReadingVisitor(className, (MultiValueMap<String, AnnotationAttributes>)this.attributesMap, this.metaAnnotationMap, this.classLoader);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<String> getAnnotationTypes() {
/*  97 */     return this.annotationSet;
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<String> getMetaAnnotationTypes(String annotationName) {
/* 102 */     Set<String> metaAnnotationTypes = this.metaAnnotationMap.get(annotationName);
/* 103 */     return (metaAnnotationTypes != null) ? metaAnnotationTypes : Collections.<String>emptySet();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasAnnotation(String annotationName) {
/* 108 */     return this.annotationSet.contains(annotationName);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasMetaAnnotation(String metaAnnotationType) {
/* 113 */     Collection<Set<String>> allMetaTypes = this.metaAnnotationMap.values();
/* 114 */     for (Set<String> metaTypes : allMetaTypes) {
/* 115 */       if (metaTypes.contains(metaAnnotationType)) {
/* 116 */         return true;
/*     */       }
/*     */     } 
/* 119 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAnnotated(String annotationName) {
/* 124 */     return (!AnnotationUtils.isInJavaLangAnnotationPackage(annotationName) && this.attributesMap
/* 125 */       .containsKey(annotationName));
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public AnnotationAttributes getAnnotationAttributes(String annotationName) {
/* 131 */     return getAnnotationAttributes(annotationName, false);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public AnnotationAttributes getAnnotationAttributes(String annotationName, boolean classValuesAsString) {
/* 137 */     AnnotationAttributes raw = AnnotationReadingVisitorUtils.getMergedAnnotationAttributes(this.attributesMap, this.metaAnnotationMap, annotationName);
/*     */     
/* 139 */     if (raw == null) {
/* 140 */       return null;
/*     */     }
/* 142 */     return AnnotationReadingVisitorUtils.convertClassValues("class '" + 
/* 143 */         getClassName() + "'", this.classLoader, raw, classValuesAsString);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public MultiValueMap<String, Object> getAllAnnotationAttributes(String annotationName) {
/* 149 */     return getAllAnnotationAttributes(annotationName, false);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public MultiValueMap<String, Object> getAllAnnotationAttributes(String annotationName, boolean classValuesAsString) {
/* 155 */     LinkedMultiValueMap linkedMultiValueMap = new LinkedMultiValueMap();
/* 156 */     List<AnnotationAttributes> attributes = this.attributesMap.get(annotationName);
/* 157 */     if (attributes == null) {
/* 158 */       return null;
/*     */     }
/* 160 */     for (AnnotationAttributes raw : attributes) {
/* 161 */       for (Map.Entry<String, Object> entry : (Iterable<Map.Entry<String, Object>>)AnnotationReadingVisitorUtils.convertClassValues("class '" + 
/* 162 */           getClassName() + "'", this.classLoader, raw, classValuesAsString).entrySet()) {
/* 163 */         linkedMultiValueMap.add(entry.getKey(), entry.getValue());
/*     */       }
/*     */     } 
/* 166 */     return (MultiValueMap<String, Object>)linkedMultiValueMap;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasAnnotatedMethods(String annotationName) {
/* 171 */     for (MethodMetadata methodMetadata : this.methodMetadataSet) {
/* 172 */       if (methodMetadata.isAnnotated(annotationName)) {
/* 173 */         return true;
/*     */       }
/*     */     } 
/* 176 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<MethodMetadata> getAnnotatedMethods(String annotationName) {
/* 181 */     Set<MethodMetadata> annotatedMethods = new LinkedHashSet<>(4);
/* 182 */     for (MethodMetadata methodMetadata : this.methodMetadataSet) {
/* 183 */       if (methodMetadata.isAnnotated(annotationName)) {
/* 184 */         annotatedMethods.add(methodMetadata);
/*     */       }
/*     */     } 
/* 187 */     return annotatedMethods;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/type/classreading/AnnotationMetadataReadingVisitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */