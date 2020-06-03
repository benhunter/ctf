/*     */ package org.springframework.beans.factory.annotation;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.AnnotatedElement;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.springframework.beans.SimpleTypeConverter;
/*     */ import org.springframework.beans.TypeConverter;
/*     */ import org.springframework.beans.factory.NoSuchBeanDefinitionException;
/*     */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*     */ import org.springframework.beans.factory.config.DependencyDescriptor;
/*     */ import org.springframework.beans.factory.support.AutowireCandidateQualifier;
/*     */ import org.springframework.beans.factory.support.GenericTypeAwareAutowireCandidateResolver;
/*     */ import org.springframework.beans.factory.support.RootBeanDefinition;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.core.annotation.AnnotatedElementUtils;
/*     */ import org.springframework.core.annotation.AnnotationAttributes;
/*     */ import org.springframework.core.annotation.AnnotationUtils;
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
/*     */ public class QualifierAnnotationAutowireCandidateResolver
/*     */   extends GenericTypeAwareAutowireCandidateResolver
/*     */ {
/*  62 */   private final Set<Class<? extends Annotation>> qualifierTypes = new LinkedHashSet<>(2);
/*     */   
/*  64 */   private Class<? extends Annotation> valueAnnotationType = (Class)Value.class;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public QualifierAnnotationAutowireCandidateResolver() {
/*  74 */     this.qualifierTypes.add(Qualifier.class);
/*     */     try {
/*  76 */       this.qualifierTypes.add(ClassUtils.forName("javax.inject.Qualifier", QualifierAnnotationAutowireCandidateResolver.class
/*  77 */             .getClassLoader()));
/*     */     }
/*  79 */     catch (ClassNotFoundException classNotFoundException) {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public QualifierAnnotationAutowireCandidateResolver(Class<? extends Annotation> qualifierType) {
/*  90 */     Assert.notNull(qualifierType, "'qualifierType' must not be null");
/*  91 */     this.qualifierTypes.add(qualifierType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public QualifierAnnotationAutowireCandidateResolver(Set<Class<? extends Annotation>> qualifierTypes) {
/* 100 */     Assert.notNull(qualifierTypes, "'qualifierTypes' must not be null");
/* 101 */     this.qualifierTypes.addAll(qualifierTypes);
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
/*     */   public void addQualifierType(Class<? extends Annotation> qualifierType) {
/* 116 */     this.qualifierTypes.add(qualifierType);
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
/*     */   public void setValueAnnotationType(Class<? extends Annotation> valueAnnotationType) {
/* 129 */     this.valueAnnotationType = valueAnnotationType;
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
/*     */   public boolean isAutowireCandidate(BeanDefinitionHolder bdHolder, DependencyDescriptor descriptor) {
/* 147 */     boolean match = super.isAutowireCandidate(bdHolder, descriptor);
/* 148 */     if (match) {
/* 149 */       match = checkQualifiers(bdHolder, descriptor.getAnnotations());
/* 150 */       if (match) {
/* 151 */         MethodParameter methodParam = descriptor.getMethodParameter();
/* 152 */         if (methodParam != null) {
/* 153 */           Method method = methodParam.getMethod();
/* 154 */           if (method == null || void.class == method.getReturnType()) {
/* 155 */             match = checkQualifiers(bdHolder, methodParam.getMethodAnnotations());
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/* 160 */     return match;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean checkQualifiers(BeanDefinitionHolder bdHolder, Annotation[] annotationsToSearch) {
/* 167 */     if (ObjectUtils.isEmpty((Object[])annotationsToSearch)) {
/* 168 */       return true;
/*     */     }
/* 170 */     SimpleTypeConverter typeConverter = new SimpleTypeConverter();
/* 171 */     for (Annotation annotation : annotationsToSearch) {
/* 172 */       Class<? extends Annotation> type = annotation.annotationType();
/* 173 */       boolean checkMeta = true;
/* 174 */       boolean fallbackToMeta = false;
/* 175 */       if (isQualifier(type)) {
/* 176 */         if (!checkQualifier(bdHolder, annotation, (TypeConverter)typeConverter)) {
/* 177 */           fallbackToMeta = true;
/*     */         } else {
/*     */           
/* 180 */           checkMeta = false;
/*     */         } 
/*     */       }
/* 183 */       if (checkMeta) {
/* 184 */         boolean foundMeta = false;
/* 185 */         for (Annotation metaAnn : type.getAnnotations()) {
/* 186 */           Class<? extends Annotation> metaType = metaAnn.annotationType();
/* 187 */           if (isQualifier(metaType)) {
/* 188 */             foundMeta = true;
/*     */ 
/*     */             
/* 191 */             if ((fallbackToMeta && StringUtils.isEmpty(AnnotationUtils.getValue(metaAnn))) || 
/* 192 */               !checkQualifier(bdHolder, metaAnn, (TypeConverter)typeConverter)) {
/* 193 */               return false;
/*     */             }
/*     */           } 
/*     */         } 
/* 197 */         if (fallbackToMeta && !foundMeta) {
/* 198 */           return false;
/*     */         }
/*     */       } 
/*     */     } 
/* 202 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isQualifier(Class<? extends Annotation> annotationType) {
/* 209 */     for (Class<? extends Annotation> qualifierType : this.qualifierTypes) {
/* 210 */       if (annotationType.equals(qualifierType) || annotationType.isAnnotationPresent(qualifierType)) {
/* 211 */         return true;
/*     */       }
/*     */     } 
/* 214 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean checkQualifier(BeanDefinitionHolder bdHolder, Annotation annotation, TypeConverter typeConverter) {
/* 223 */     Class<? extends Annotation> type = annotation.annotationType();
/* 224 */     RootBeanDefinition bd = (RootBeanDefinition)bdHolder.getBeanDefinition();
/*     */     
/* 226 */     AutowireCandidateQualifier qualifier = bd.getQualifier(type.getName());
/* 227 */     if (qualifier == null) {
/* 228 */       qualifier = bd.getQualifier(ClassUtils.getShortName(type));
/*     */     }
/* 230 */     if (qualifier == null) {
/*     */       
/* 232 */       Annotation targetAnnotation = getQualifiedElementAnnotation(bd, type);
/*     */       
/* 234 */       if (targetAnnotation == null) {
/* 235 */         targetAnnotation = getFactoryMethodAnnotation(bd, type);
/*     */       }
/* 237 */       if (targetAnnotation == null) {
/* 238 */         RootBeanDefinition dbd = getResolvedDecoratedDefinition(bd);
/* 239 */         if (dbd != null) {
/* 240 */           targetAnnotation = getFactoryMethodAnnotation(dbd, type);
/*     */         }
/*     */       } 
/* 243 */       if (targetAnnotation == null) {
/*     */         
/* 245 */         if (getBeanFactory() != null) {
/*     */           try {
/* 247 */             Class<?> beanType = getBeanFactory().getType(bdHolder.getBeanName());
/* 248 */             if (beanType != null) {
/* 249 */               targetAnnotation = AnnotationUtils.getAnnotation(ClassUtils.getUserClass(beanType), type);
/*     */             }
/*     */           }
/* 252 */           catch (NoSuchBeanDefinitionException noSuchBeanDefinitionException) {}
/*     */         }
/*     */ 
/*     */         
/* 256 */         if (targetAnnotation == null && bd.hasBeanClass()) {
/* 257 */           targetAnnotation = AnnotationUtils.getAnnotation(ClassUtils.getUserClass(bd.getBeanClass()), type);
/*     */         }
/*     */       } 
/* 260 */       if (targetAnnotation != null && targetAnnotation.equals(annotation)) {
/* 261 */         return true;
/*     */       }
/*     */     } 
/*     */     
/* 265 */     Map<String, Object> attributes = AnnotationUtils.getAnnotationAttributes(annotation);
/* 266 */     if (attributes.isEmpty() && qualifier == null)
/*     */     {
/* 268 */       return false;
/*     */     }
/* 270 */     for (Map.Entry<String, Object> entry : attributes.entrySet()) {
/* 271 */       String attributeName = entry.getKey();
/* 272 */       Object expectedValue = entry.getValue();
/* 273 */       Object actualValue = null;
/*     */       
/* 275 */       if (qualifier != null) {
/* 276 */         actualValue = qualifier.getAttribute(attributeName);
/*     */       }
/* 278 */       if (actualValue == null)
/*     */       {
/* 280 */         actualValue = bd.getAttribute(attributeName);
/*     */       }
/* 282 */       if (actualValue == null && attributeName.equals("value") && expectedValue instanceof String && bdHolder
/* 283 */         .matchesName((String)expectedValue)) {
/*     */         continue;
/*     */       }
/*     */       
/* 287 */       if (actualValue == null && qualifier != null)
/*     */       {
/* 289 */         actualValue = AnnotationUtils.getDefaultValue(annotation, attributeName);
/*     */       }
/* 291 */       if (actualValue != null) {
/* 292 */         actualValue = typeConverter.convertIfNecessary(actualValue, expectedValue.getClass());
/*     */       }
/* 294 */       if (!expectedValue.equals(actualValue)) {
/* 295 */         return false;
/*     */       }
/*     */     } 
/* 298 */     return true;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   protected Annotation getQualifiedElementAnnotation(RootBeanDefinition bd, Class<? extends Annotation> type) {
/* 303 */     AnnotatedElement qualifiedElement = bd.getQualifiedElement();
/* 304 */     return (qualifiedElement != null) ? AnnotationUtils.getAnnotation(qualifiedElement, type) : null;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   protected Annotation getFactoryMethodAnnotation(RootBeanDefinition bd, Class<? extends Annotation> type) {
/* 309 */     Method resolvedFactoryMethod = bd.getResolvedFactoryMethod();
/* 310 */     return (resolvedFactoryMethod != null) ? AnnotationUtils.getAnnotation(resolvedFactoryMethod, type) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isRequired(DependencyDescriptor descriptor) {
/* 321 */     if (!super.isRequired(descriptor)) {
/* 322 */       return false;
/*     */     }
/* 324 */     Autowired autowired = (Autowired)descriptor.getAnnotation(Autowired.class);
/* 325 */     return (autowired == null || autowired.required());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasQualifier(DependencyDescriptor descriptor) {
/* 335 */     for (Annotation ann : descriptor.getAnnotations()) {
/* 336 */       if (isQualifier(ann.annotationType())) {
/* 337 */         return true;
/*     */       }
/*     */     } 
/* 340 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object getSuggestedValue(DependencyDescriptor descriptor) {
/* 350 */     Object value = findValue(descriptor.getAnnotations());
/* 351 */     if (value == null) {
/* 352 */       MethodParameter methodParam = descriptor.getMethodParameter();
/* 353 */       if (methodParam != null) {
/* 354 */         value = findValue(methodParam.getMethodAnnotations());
/*     */       }
/*     */     } 
/* 357 */     return value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected Object findValue(Annotation[] annotationsToSearch) {
/* 365 */     if (annotationsToSearch.length > 0) {
/* 366 */       AnnotationAttributes attr = AnnotatedElementUtils.getMergedAnnotationAttributes(
/* 367 */           AnnotatedElementUtils.forAnnotations(annotationsToSearch), this.valueAnnotationType);
/* 368 */       if (attr != null) {
/* 369 */         return extractValue(attr);
/*     */       }
/*     */     } 
/* 372 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object extractValue(AnnotationAttributes attr) {
/* 380 */     Object value = attr.get("value");
/* 381 */     if (value == null) {
/* 382 */       throw new IllegalStateException("Value annotation must have a value attribute");
/*     */     }
/* 384 */     return value;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/annotation/QualifierAnnotationAutowireCandidateResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */