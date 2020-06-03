/*    */ package org.springframework.context.expression;
/*    */ 
/*    */ import java.lang.reflect.AnnotatedElement;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.util.Assert;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class AnnotatedElementKey
/*    */   implements Comparable<AnnotatedElementKey>
/*    */ {
/*    */   private final AnnotatedElement element;
/*    */   @Nullable
/*    */   private final Class<?> targetClass;
/*    */   
/*    */   public AnnotatedElementKey(AnnotatedElement element, @Nullable Class<?> targetClass) {
/* 47 */     Assert.notNull(element, "AnnotatedElement must not be null");
/* 48 */     this.element = element;
/* 49 */     this.targetClass = targetClass;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(Object other) {
/* 55 */     if (this == other) {
/* 56 */       return true;
/*    */     }
/* 58 */     if (!(other instanceof AnnotatedElementKey)) {
/* 59 */       return false;
/*    */     }
/* 61 */     AnnotatedElementKey otherKey = (AnnotatedElementKey)other;
/* 62 */     return (this.element.equals(otherKey.element) && 
/* 63 */       ObjectUtils.nullSafeEquals(this.targetClass, otherKey.targetClass));
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 68 */     return this.element.hashCode() + ((this.targetClass != null) ? (this.targetClass.hashCode() * 29) : 0);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 73 */     return this.element + ((this.targetClass != null) ? (" on " + this.targetClass) : "");
/*    */   }
/*    */ 
/*    */   
/*    */   public int compareTo(AnnotatedElementKey other) {
/* 78 */     int result = this.element.toString().compareTo(other.element.toString());
/* 79 */     if (result == 0 && this.targetClass != null) {
/* 80 */       if (other.targetClass == null) {
/* 81 */         return 1;
/*    */       }
/* 83 */       result = this.targetClass.getName().compareTo(other.targetClass.getName());
/*    */     } 
/* 85 */     return result;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/expression/AnnotatedElementKey.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */