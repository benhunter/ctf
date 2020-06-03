/*    */ package org.springframework.core;
/*    */ 
/*    */ import java.lang.annotation.Annotation;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.util.ClassUtils;
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
/*    */ public abstract class KotlinDetector
/*    */ {
/* 37 */   private static final Log logger = LogFactory.getLog(KotlinDetector.class);
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   private static final Class<? extends Annotation> kotlinMetadata;
/*    */ 
/*    */ 
/*    */   
/*    */   static {
/* 46 */     ClassLoader classLoader = KotlinDetector.class.getClassLoader();
/*    */     try {
/* 48 */       metadata = ClassUtils.forName("kotlin.Metadata", classLoader);
/*    */     }
/* 50 */     catch (ClassNotFoundException ex) {
/*    */       
/* 52 */       metadata = null;
/*    */     } 
/* 54 */     kotlinMetadata = (Class)metadata;
/* 55 */     kotlinReflectPresent = ClassUtils.isPresent("kotlin.reflect.full.KClasses", classLoader);
/* 56 */     if (kotlinMetadata != null && !kotlinReflectPresent)
/* 57 */       logger.info("Kotlin reflection implementation not found at runtime, related features won't be available."); 
/*    */   }
/*    */   private static final boolean kotlinReflectPresent;
/*    */   
/*    */   static {
/*    */     Class<?> metadata;
/*    */   }
/*    */   
/*    */   public static boolean isKotlinPresent() {
/* 66 */     return (kotlinMetadata != null);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static boolean isKotlinReflectPresent() {
/* 74 */     return kotlinReflectPresent;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static boolean isKotlinType(Class<?> clazz) {
/* 82 */     return (kotlinMetadata != null && clazz.getDeclaredAnnotation(kotlinMetadata) != null);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/KotlinDetector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */