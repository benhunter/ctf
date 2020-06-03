/*     */ package org.springframework.core;
/*     */ 
/*     */ import java.lang.reflect.ParameterizedType;
/*     */ import java.lang.reflect.Type;
/*     */ import org.springframework.util.Assert;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class ParameterizedTypeReference<T>
/*     */ {
/*     */   private final Type type;
/*     */   
/*     */   protected ParameterizedTypeReference() {
/*  50 */     Class<?> parameterizedTypeReferenceSubclass = findParameterizedTypeReferenceSubclass(getClass());
/*  51 */     Type type = parameterizedTypeReferenceSubclass.getGenericSuperclass();
/*  52 */     Assert.isInstanceOf(ParameterizedType.class, type, "Type must be a parameterized type");
/*  53 */     ParameterizedType parameterizedType = (ParameterizedType)type;
/*  54 */     Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
/*  55 */     Assert.isTrue((actualTypeArguments.length == 1), "Number of type arguments must be 1");
/*  56 */     this.type = actualTypeArguments[0];
/*     */   }
/*     */   
/*     */   private ParameterizedTypeReference(Type type) {
/*  60 */     this.type = type;
/*     */   }
/*     */ 
/*     */   
/*     */   public Type getType() {
/*  65 */     return this.type;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/*  70 */     return (this == other || (other instanceof ParameterizedTypeReference && this.type
/*  71 */       .equals(((ParameterizedTypeReference)other).type)));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  76 */     return this.type.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  81 */     return "ParameterizedTypeReference<" + this.type + ">";
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
/*     */   public static <T> ParameterizedTypeReference<T> forType(Type type) {
/*  94 */     return new ParameterizedTypeReference<T>(type) {  }
/*     */       ;
/*     */   }
/*     */   
/*     */   private static Class<?> findParameterizedTypeReferenceSubclass(Class<?> child) {
/*  99 */     Class<?> parent = child.getSuperclass();
/* 100 */     if (Object.class == parent) {
/* 101 */       throw new IllegalStateException("Expected ParameterizedTypeReference superclass");
/*     */     }
/* 103 */     if (ParameterizedTypeReference.class == parent) {
/* 104 */       return child;
/*     */     }
/*     */     
/* 107 */     return findParameterizedTypeReferenceSubclass(parent);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/ParameterizedTypeReference.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */