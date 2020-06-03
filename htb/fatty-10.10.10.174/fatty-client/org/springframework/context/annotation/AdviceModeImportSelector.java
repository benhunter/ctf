/*    */ package org.springframework.context.annotation;
/*    */ 
/*    */ import java.lang.annotation.Annotation;
/*    */ import org.springframework.core.GenericTypeResolver;
/*    */ import org.springframework.core.annotation.AnnotationAttributes;
/*    */ import org.springframework.core.type.AnnotatedTypeMetadata;
/*    */ import org.springframework.core.type.AnnotationMetadata;
/*    */ import org.springframework.lang.Nullable;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class AdviceModeImportSelector<A extends Annotation>
/*    */   implements ImportSelector
/*    */ {
/*    */   public static final String DEFAULT_ADVICE_MODE_ATTRIBUTE_NAME = "mode";
/*    */   
/*    */   protected String getAdviceModeAttributeName() {
/* 50 */     return "mode";
/*    */   }
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
/*    */   public final String[] selectImports(AnnotationMetadata importingClassMetadata) {
/* 67 */     Class<?> annType = GenericTypeResolver.resolveTypeArgument(getClass(), AdviceModeImportSelector.class);
/* 68 */     Assert.state((annType != null), "Unresolvable type argument for AdviceModeImportSelector");
/*    */     
/* 70 */     AnnotationAttributes attributes = AnnotationConfigUtils.attributesFor((AnnotatedTypeMetadata)importingClassMetadata, annType);
/* 71 */     if (attributes == null) {
/* 72 */       throw new IllegalArgumentException(String.format("@%s is not present on importing class '%s' as expected", new Object[] { annType
/*    */               
/* 74 */               .getSimpleName(), importingClassMetadata.getClassName() }));
/*    */     }
/*    */     
/* 77 */     AdviceMode adviceMode = (AdviceMode)attributes.getEnum(getAdviceModeAttributeName());
/* 78 */     String[] imports = selectImports(adviceMode);
/* 79 */     if (imports == null) {
/* 80 */       throw new IllegalArgumentException("Unknown AdviceMode: " + adviceMode);
/*    */     }
/* 82 */     return imports;
/*    */   }
/*    */   
/*    */   @Nullable
/*    */   protected abstract String[] selectImports(AdviceMode paramAdviceMode);
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/annotation/AdviceModeImportSelector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */