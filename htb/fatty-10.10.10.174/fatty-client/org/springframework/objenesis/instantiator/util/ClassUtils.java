/*    */ package org.springframework.objenesis.instantiator.util;
/*    */ 
/*    */ import org.springframework.objenesis.ObjenesisException;
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
/*    */ public final class ClassUtils
/*    */ {
/*    */   public static String classNameToInternalClassName(String className) {
/* 38 */     return className.replace('.', '/');
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static String classNameToResource(String className) {
/* 49 */     return classNameToInternalClassName(className) + ".class";
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
/*    */   public static <T> Class<T> getExistingClass(ClassLoader classLoader, String className) {
/*    */     try {
/* 63 */       return (Class)Class.forName(className, true, classLoader);
/*    */     }
/* 65 */     catch (ClassNotFoundException e) {
/* 66 */       return null;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public static <T> T newInstance(Class<T> clazz) {
/*    */     try {
/* 73 */       return clazz.newInstance();
/* 74 */     } catch (InstantiationException|IllegalAccessException e) {
/* 75 */       throw new ObjenesisException(e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/objenesis/instantiator/util/ClassUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */