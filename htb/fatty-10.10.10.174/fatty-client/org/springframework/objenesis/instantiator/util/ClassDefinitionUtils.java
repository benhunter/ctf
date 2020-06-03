/*     */ package org.springframework.objenesis.instantiator.util;
/*     */ 
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.security.AccessController;
/*     */ import java.security.ProtectionDomain;
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
/*     */ public final class ClassDefinitionUtils
/*     */ {
/*     */   public static final byte OPS_aload_0 = 42;
/*     */   public static final byte OPS_invokespecial = -73;
/*     */   public static final byte OPS_return = -79;
/*     */   public static final byte OPS_new = -69;
/*     */   public static final byte OPS_dup = 89;
/*     */   public static final byte OPS_areturn = -80;
/*     */   public static final int CONSTANT_Utf8 = 1;
/*     */   public static final int CONSTANT_Integer = 3;
/*     */   public static final int CONSTANT_Float = 4;
/*     */   public static final int CONSTANT_Long = 5;
/*     */   public static final int CONSTANT_Double = 6;
/*     */   public static final int CONSTANT_Class = 7;
/*     */   public static final int CONSTANT_String = 8;
/*     */   public static final int CONSTANT_Fieldref = 9;
/*     */   public static final int CONSTANT_Methodref = 10;
/*     */   public static final int CONSTANT_InterfaceMethodref = 11;
/*     */   public static final int CONSTANT_NameAndType = 12;
/*     */   public static final int CONSTANT_MethodHandle = 15;
/*     */   public static final int CONSTANT_MethodType = 16;
/*     */   public static final int CONSTANT_InvokeDynamic = 18;
/*     */   public static final int ACC_PUBLIC = 1;
/*     */   public static final int ACC_FINAL = 16;
/*     */   public static final int ACC_SUPER = 32;
/*     */   public static final int ACC_INTERFACE = 512;
/*     */   public static final int ACC_ABSTRACT = 1024;
/*     */   public static final int ACC_SYNTHETIC = 4096;
/*     */   public static final int ACC_ANNOTATION = 8192;
/*     */   public static final int ACC_ENUM = 16384;
/*  65 */   public static final byte[] MAGIC = new byte[] { -54, -2, -70, -66 };
/*  66 */   public static final byte[] VERSION = new byte[] { 0, 0, 0, 49 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  73 */   private static final ProtectionDomain PROTECTION_DOMAIN = AccessController.<ProtectionDomain>doPrivileged(ClassDefinitionUtils.class::getProtectionDomain);
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
/*     */   public static <T> Class<T> defineClass(String className, byte[] b, Class<?> neighbor, ClassLoader loader) throws Exception {
/*  91 */     Class<T> c = (Class)DefineClassHelper.defineClass(className, b, 0, b.length, neighbor, loader, PROTECTION_DOMAIN);
/*     */     
/*  93 */     Class.forName(className, true, loader);
/*  94 */     return c;
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
/*     */   public static byte[] readClass(String className) throws IOException {
/*     */     int length;
/* 107 */     className = ClassUtils.classNameToResource(className);
/*     */     
/* 109 */     byte[] b = new byte[2500];
/*     */ 
/*     */ 
/*     */     
/* 113 */     try (InputStream in = ClassDefinitionUtils.class.getClassLoader().getResourceAsStream(className)) {
/* 114 */       length = in.read(b);
/*     */     } 
/*     */     
/* 117 */     if (length >= 2500) {
/* 118 */       throw new IllegalArgumentException("The class is longer that 2500 bytes which is currently unsupported");
/*     */     }
/*     */     
/* 121 */     byte[] copy = new byte[length];
/* 122 */     System.arraycopy(b, 0, copy, 0, length);
/* 123 */     return copy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void writeClass(String fileName, byte[] bytes) throws IOException {
/* 134 */     try (BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(fileName))) {
/* 135 */       out.write(bytes);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/objenesis/instantiator/util/ClassDefinitionUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */