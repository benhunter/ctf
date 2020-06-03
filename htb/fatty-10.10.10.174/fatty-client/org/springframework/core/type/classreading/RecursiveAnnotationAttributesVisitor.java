/*    */ package org.springframework.core.type.classreading;
/*    */ 
/*    */ import org.springframework.core.annotation.AnnotationAttributes;
/*    */ import org.springframework.core.annotation.AnnotationUtils;
/*    */ import org.springframework.lang.Nullable;
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
/*    */ class RecursiveAnnotationAttributesVisitor
/*    */   extends AbstractRecursiveAnnotationVisitor
/*    */ {
/*    */   protected final String annotationType;
/*    */   
/*    */   public RecursiveAnnotationAttributesVisitor(String annotationType, AnnotationAttributes attributes, @Nullable ClassLoader classLoader) {
/* 39 */     super(classLoader, attributes);
/* 40 */     this.annotationType = annotationType;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void visitEnd() {
/* 46 */     AnnotationUtils.registerDefaultValues(this.attributes);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/type/classreading/RecursiveAnnotationAttributesVisitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */