/*    */ package org.springframework.core.annotation;
/*    */ 
/*    */ import java.lang.annotation.Annotation;
/*    */ import java.lang.reflect.Method;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.util.ReflectionUtils;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class DefaultAnnotationAttributeExtractor
/*    */   extends AbstractAliasAwareAnnotationAttributeExtractor<Annotation>
/*    */ {
/*    */   DefaultAnnotationAttributeExtractor(Annotation annotation, @Nullable Object annotatedElement) {
/* 46 */     super(annotation.annotationType(), annotatedElement, annotation);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   protected Object getRawAttributeValue(Method attributeMethod) {
/* 53 */     ReflectionUtils.makeAccessible(attributeMethod);
/* 54 */     return ReflectionUtils.invokeMethod(attributeMethod, getSource());
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   protected Object getRawAttributeValue(String attributeName) {
/* 60 */     Method attributeMethod = ReflectionUtils.findMethod(getAnnotationType(), attributeName);
/* 61 */     return (attributeMethod != null) ? getRawAttributeValue(attributeMethod) : null;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/annotation/DefaultAnnotationAttributeExtractor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */