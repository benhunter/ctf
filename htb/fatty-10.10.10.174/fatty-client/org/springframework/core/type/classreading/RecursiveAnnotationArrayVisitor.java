/*    */ package org.springframework.core.type.classreading;
/*    */ 
/*    */ import java.lang.annotation.Annotation;
/*    */ import java.lang.reflect.Array;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.springframework.asm.AnnotationVisitor;
/*    */ import org.springframework.asm.Type;
/*    */ import org.springframework.core.annotation.AnnotationAttributes;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.util.ObjectUtils;
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
/*    */ class RecursiveAnnotationArrayVisitor
/*    */   extends AbstractRecursiveAnnotationVisitor
/*    */ {
/*    */   private final String attributeName;
/* 41 */   private final List<AnnotationAttributes> allNestedAttributes = new ArrayList<>();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public RecursiveAnnotationArrayVisitor(String attributeName, AnnotationAttributes attributes, @Nullable ClassLoader classLoader) {
/* 47 */     super(classLoader, attributes);
/* 48 */     this.attributeName = attributeName;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void visit(String attributeName, Object attributeValue) {
/* 54 */     Object newValue = attributeValue;
/* 55 */     Object existingValue = this.attributes.get(this.attributeName);
/* 56 */     if (existingValue != null) {
/* 57 */       newValue = ObjectUtils.addObjectToArray((Object[])existingValue, newValue);
/*    */     } else {
/*    */       
/* 60 */       Class<?> arrayClass = newValue.getClass();
/* 61 */       if (Enum.class.isAssignableFrom(arrayClass)) {
/* 62 */         while (arrayClass.getSuperclass() != null && !arrayClass.isEnum()) {
/* 63 */           arrayClass = arrayClass.getSuperclass();
/*    */         }
/*    */       }
/* 66 */       Object[] newArray = (Object[])Array.newInstance(arrayClass, 1);
/* 67 */       newArray[0] = newValue;
/* 68 */       newValue = newArray;
/*    */     } 
/* 70 */     this.attributes.put(this.attributeName, newValue);
/*    */   }
/*    */ 
/*    */   
/*    */   public AnnotationVisitor visitAnnotation(String attributeName, String asmTypeDescriptor) {
/* 75 */     String annotationType = Type.getType(asmTypeDescriptor).getClassName();
/* 76 */     AnnotationAttributes nestedAttributes = new AnnotationAttributes(annotationType, this.classLoader);
/* 77 */     this.allNestedAttributes.add(nestedAttributes);
/* 78 */     return new RecursiveAnnotationAttributesVisitor(annotationType, nestedAttributes, this.classLoader);
/*    */   }
/*    */ 
/*    */   
/*    */   public void visitEnd() {
/* 83 */     if (!this.allNestedAttributes.isEmpty()) {
/* 84 */       this.attributes.put(this.attributeName, this.allNestedAttributes.toArray(new AnnotationAttributes[0]));
/*    */     }
/* 86 */     else if (!this.attributes.containsKey(this.attributeName)) {
/* 87 */       Class<? extends Annotation> annotationType = this.attributes.annotationType();
/* 88 */       if (annotationType != null)
/*    */         try {
/* 90 */           Class<?> attributeType = annotationType.getMethod(this.attributeName, new Class[0]).getReturnType();
/* 91 */           if (attributeType.isArray()) {
/* 92 */             Class<?> elementType = attributeType.getComponentType();
/* 93 */             if (elementType.isAnnotation()) {
/* 94 */               elementType = AnnotationAttributes.class;
/*    */             }
/* 96 */             this.attributes.put(this.attributeName, Array.newInstance(elementType, 0));
/*    */           }
/*    */         
/* 99 */         } catch (NoSuchMethodException noSuchMethodException) {} 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/type/classreading/RecursiveAnnotationArrayVisitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */