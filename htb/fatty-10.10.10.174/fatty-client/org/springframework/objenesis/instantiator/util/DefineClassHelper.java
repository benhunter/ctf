/*     */ package org.springframework.objenesis.instantiator.util;
/*     */ 
/*     */ import java.lang.invoke.MethodHandle;
/*     */ import java.lang.invoke.MethodHandles;
/*     */ import java.lang.invoke.MethodType;
/*     */ import java.security.ProtectionDomain;
/*     */ import org.springframework.objenesis.ObjenesisException;
/*     */ import org.springframework.objenesis.strategy.PlatformDescription;
/*     */ import sun.misc.Unsafe;
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
/*     */ public final class DefineClassHelper
/*     */ {
/*     */   private static abstract class Helper
/*     */   {
/*     */     private Helper() {}
/*     */     
/*     */     abstract Class<?> defineClass(String param1String, byte[] param1ArrayOfbyte, int param1Int1, int param1Int2, Class<?> param1Class, ClassLoader param1ClassLoader, ProtectionDomain param1ProtectionDomain);
/*     */   }
/*     */   
/*     */   private static class Java8
/*     */     extends Helper
/*     */   {
/*     */     private final MethodHandle defineClass;
/*     */     
/*     */     private Java8() {
/*  43 */       this.defineClass = defineClass();
/*     */     } private MethodHandle defineClass() {
/*     */       MethodHandle m;
/*  46 */       MethodType mt = MethodType.methodType(Class.class, String.class, new Class[] { byte[].class, int.class, int.class, ClassLoader.class, ProtectionDomain.class });
/*     */       
/*     */       try {
/*  49 */         m = MethodHandles.publicLookup().findVirtual(Unsafe.class, "defineClass", mt);
/*  50 */       } catch (NoSuchMethodException|IllegalAccessException e) {
/*  51 */         throw new ObjenesisException(e);
/*     */       } 
/*  53 */       Unsafe unsafe = UnsafeUtils.getUnsafe();
/*  54 */       return m.bindTo(unsafe);
/*     */     }
/*     */ 
/*     */     
/*     */     Class<?> defineClass(String className, byte[] b, int off, int len, Class<?> neighbor, ClassLoader loader, ProtectionDomain protectionDomain) {
/*     */       try {
/*  60 */         return this.defineClass.invokeExact(className, b, off, len, loader, protectionDomain);
/*  61 */       } catch (Throwable e) {
/*  62 */         if (e instanceof Error) {
/*  63 */           throw (Error)e;
/*     */         }
/*  65 */         if (e instanceof RuntimeException) {
/*  66 */           throw (RuntimeException)e;
/*     */         }
/*  68 */         throw new ObjenesisException(e);
/*     */       } 
/*     */     } }
/*     */   private static class Java11 extends Helper { private final Class<?> module; private final MethodHandles.Lookup lookup;
/*     */     private final MethodHandle getModule;
/*     */     
/*     */     private Java11() {
/*  75 */       this.module = module();
/*  76 */       this.lookup = MethodHandles.lookup();
/*  77 */       this.getModule = getModule();
/*  78 */       this.addReads = addReads();
/*  79 */       this.privateLookupIn = privateLookupIn();
/*  80 */       this.defineClass = defineClass();
/*     */     } private final MethodHandle addReads; private final MethodHandle privateLookupIn; private final MethodHandle defineClass;
/*     */     private Class<?> module() {
/*     */       try {
/*  84 */         return Class.forName("java.lang.Module");
/*  85 */       } catch (ClassNotFoundException e) {
/*  86 */         throw new ObjenesisException(e);
/*     */       } 
/*     */     }
/*     */     
/*     */     private MethodHandle getModule() {
/*     */       try {
/*  92 */         return this.lookup.findVirtual(Class.class, "getModule", MethodType.methodType(this.module));
/*  93 */       } catch (NoSuchMethodException|IllegalAccessException e) {
/*  94 */         throw new ObjenesisException(e);
/*     */       } 
/*     */     }
/*     */     
/*     */     private MethodHandle addReads() {
/*     */       try {
/* 100 */         return this.lookup.findVirtual(this.module, "addReads", MethodType.methodType(this.module, this.module));
/* 101 */       } catch (NoSuchMethodException|IllegalAccessException e) {
/* 102 */         throw new ObjenesisException(e);
/*     */       } 
/*     */     }
/*     */     
/*     */     private MethodHandle privateLookupIn() {
/*     */       try {
/* 108 */         return this.lookup.findStatic(MethodHandles.class, "privateLookupIn", MethodType.methodType(MethodHandles.Lookup.class, Class.class, new Class[] { MethodHandles.Lookup.class }));
/* 109 */       } catch (NoSuchMethodException|IllegalAccessException e) {
/* 110 */         throw new ObjenesisException(e);
/*     */       } 
/*     */     }
/*     */     
/*     */     private MethodHandle defineClass() {
/*     */       try {
/* 116 */         return this.lookup.findVirtual(MethodHandles.Lookup.class, "defineClass", MethodType.methodType(Class.class, byte[].class));
/* 117 */       } catch (NoSuchMethodException|IllegalAccessException e) {
/* 118 */         throw new ObjenesisException(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     Class<?> defineClass(String className, byte[] b, int off, int len, Class<?> neighbor, ClassLoader loader, ProtectionDomain protectionDomain) {
/*     */       try {
/* 125 */         Object module = this.getModule.invokeWithArguments(new Object[] { DefineClassHelper.class });
/* 126 */         Object neighborModule = this.getModule.invokeWithArguments(new Object[] { neighbor });
/* 127 */         this.addReads.invokeWithArguments(new Object[] { module, neighborModule });
/* 128 */         MethodHandles.Lookup prvlookup = this.privateLookupIn.invokeExact(neighbor, this.lookup);
/* 129 */         return this.defineClass.invokeExact(prvlookup, b);
/* 130 */       } catch (Throwable e) {
/* 131 */         throw new ObjenesisException(neighbor.getName() + " has no permission to define the class", e);
/*     */       } 
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 138 */   private static final Helper privileged = PlatformDescription.isAfterJava11() ? new Java11() : new Java8();
/*     */ 
/*     */ 
/*     */   
/*     */   public static Class<?> defineClass(String name, byte[] b, int off, int len, Class<?> neighbor, ClassLoader loader, ProtectionDomain protectionDomain) {
/* 143 */     return privileged.defineClass(name, b, off, len, neighbor, loader, protectionDomain);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/objenesis/instantiator/util/DefineClassHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */