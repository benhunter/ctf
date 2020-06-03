/*    */ package org.springframework.core.type.classreading;
/*    */ 
/*    */ import java.lang.reflect.Field;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
/*    */ import org.springframework.asm.AnnotationVisitor;
/*    */ import org.springframework.asm.Type;
/*    */ import org.springframework.core.annotation.AnnotationAttributes;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.util.ClassUtils;
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
/*    */ abstract class AbstractRecursiveAnnotationVisitor
/*    */   extends AnnotationVisitor
/*    */ {
/* 44 */   protected final Log logger = LogFactory.getLog(getClass());
/*    */   
/*    */   protected final AnnotationAttributes attributes;
/*    */   
/*    */   @Nullable
/*    */   protected final ClassLoader classLoader;
/*    */ 
/*    */   
/*    */   public AbstractRecursiveAnnotationVisitor(@Nullable ClassLoader classLoader, AnnotationAttributes attributes) {
/* 53 */     super(458752);
/* 54 */     this.classLoader = classLoader;
/* 55 */     this.attributes = attributes;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void visit(String attributeName, Object attributeValue) {
/* 61 */     this.attributes.put(attributeName, attributeValue);
/*    */   }
/*    */ 
/*    */   
/*    */   public AnnotationVisitor visitAnnotation(String attributeName, String asmTypeDescriptor) {
/* 66 */     String annotationType = Type.getType(asmTypeDescriptor).getClassName();
/* 67 */     AnnotationAttributes nestedAttributes = new AnnotationAttributes(annotationType, this.classLoader);
/* 68 */     this.attributes.put(attributeName, nestedAttributes);
/* 69 */     return new RecursiveAnnotationAttributesVisitor(annotationType, nestedAttributes, this.classLoader);
/*    */   }
/*    */ 
/*    */   
/*    */   public AnnotationVisitor visitArray(String attributeName) {
/* 74 */     return new RecursiveAnnotationArrayVisitor(attributeName, this.attributes, this.classLoader);
/*    */   }
/*    */ 
/*    */   
/*    */   public void visitEnum(String attributeName, String asmTypeDescriptor, String attributeValue) {
/* 79 */     Object newValue = getEnumValue(asmTypeDescriptor, attributeValue);
/* 80 */     visit(attributeName, newValue);
/*    */   }
/*    */   
/*    */   protected Object getEnumValue(String asmTypeDescriptor, String attributeValue) {
/* 84 */     Object valueToUse = attributeValue;
/*    */     try {
/* 86 */       Class<?> enumType = ClassUtils.forName(Type.getType(asmTypeDescriptor).getClassName(), this.classLoader);
/* 87 */       Field enumConstant = ReflectionUtils.findField(enumType, attributeValue);
/* 88 */       if (enumConstant != null) {
/* 89 */         ReflectionUtils.makeAccessible(enumConstant);
/* 90 */         valueToUse = enumConstant.get(null);
/*    */       }
/*    */     
/* 93 */     } catch (ClassNotFoundException|NoClassDefFoundError ex) {
/* 94 */       this.logger.debug("Failed to classload enum type while reading annotation metadata", ex);
/*    */     }
/* 96 */     catch (IllegalAccessException|java.security.AccessControlException ex) {
/* 97 */       this.logger.debug("Could not access enum value while reading annotation metadata", ex);
/*    */     } 
/* 99 */     return valueToUse;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/type/classreading/AbstractRecursiveAnnotationVisitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */