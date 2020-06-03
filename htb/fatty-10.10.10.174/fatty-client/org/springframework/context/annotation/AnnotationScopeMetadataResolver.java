/*    */ package org.springframework.context.annotation;
/*    */ 
/*    */ import java.lang.annotation.Annotation;
/*    */ import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
/*    */ import org.springframework.beans.factory.config.BeanDefinition;
/*    */ import org.springframework.core.annotation.AnnotationAttributes;
/*    */ import org.springframework.core.type.AnnotatedTypeMetadata;
/*    */ import org.springframework.util.Assert;
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
/*    */ public class AnnotationScopeMetadataResolver
/*    */   implements ScopeMetadataResolver
/*    */ {
/*    */   private final ScopedProxyMode defaultProxyMode;
/* 43 */   protected Class<? extends Annotation> scopeAnnotationType = (Class)Scope.class;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public AnnotationScopeMetadataResolver() {
/* 52 */     this.defaultProxyMode = ScopedProxyMode.NO;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public AnnotationScopeMetadataResolver(ScopedProxyMode defaultProxyMode) {
/* 61 */     Assert.notNull(defaultProxyMode, "'defaultProxyMode' must not be null");
/* 62 */     this.defaultProxyMode = defaultProxyMode;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setScopeAnnotationType(Class<? extends Annotation> scopeAnnotationType) {
/* 72 */     Assert.notNull(scopeAnnotationType, "'scopeAnnotationType' must not be null");
/* 73 */     this.scopeAnnotationType = scopeAnnotationType;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public ScopeMetadata resolveScopeMetadata(BeanDefinition definition) {
/* 79 */     ScopeMetadata metadata = new ScopeMetadata();
/* 80 */     if (definition instanceof AnnotatedBeanDefinition) {
/* 81 */       AnnotatedBeanDefinition annDef = (AnnotatedBeanDefinition)definition;
/* 82 */       AnnotationAttributes attributes = AnnotationConfigUtils.attributesFor((AnnotatedTypeMetadata)annDef
/* 83 */           .getMetadata(), this.scopeAnnotationType);
/* 84 */       if (attributes != null) {
/* 85 */         metadata.setScopeName(attributes.getString("value"));
/* 86 */         ScopedProxyMode proxyMode = (ScopedProxyMode)attributes.getEnum("proxyMode");
/* 87 */         if (proxyMode == ScopedProxyMode.DEFAULT) {
/* 88 */           proxyMode = this.defaultProxyMode;
/*    */         }
/* 90 */         metadata.setScopedProxyMode(proxyMode);
/*    */       } 
/*    */     } 
/* 93 */     return metadata;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/annotation/AnnotationScopeMetadataResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */