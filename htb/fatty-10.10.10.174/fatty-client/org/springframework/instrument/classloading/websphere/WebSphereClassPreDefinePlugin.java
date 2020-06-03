/*    */ package org.springframework.instrument.classloading.websphere;
/*    */ 
/*    */ import java.lang.instrument.ClassFileTransformer;
/*    */ import java.lang.reflect.InvocationHandler;
/*    */ import java.lang.reflect.Method;
/*    */ import java.security.CodeSource;
/*    */ import org.springframework.util.FileCopyUtils;
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
/*    */ class WebSphereClassPreDefinePlugin
/*    */   implements InvocationHandler
/*    */ {
/*    */   private final ClassFileTransformer transformer;
/*    */   
/*    */   public WebSphereClassPreDefinePlugin(ClassFileTransformer transformer) {
/* 47 */     this.transformer = transformer;
/* 48 */     ClassLoader classLoader = transformer.getClass().getClassLoader();
/*    */ 
/*    */     
/*    */     try {
/* 52 */       String dummyClass = Dummy.class.getName().replace('.', '/');
/* 53 */       byte[] bytes = FileCopyUtils.copyToByteArray(classLoader.getResourceAsStream(dummyClass + ".class"));
/* 54 */       transformer.transform(classLoader, dummyClass, null, null, bytes);
/*    */     }
/* 56 */     catch (Throwable ex) {
/* 57 */       throw new IllegalArgumentException("Cannot load transformer", ex);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
/* 64 */     String name = method.getName();
/* 65 */     if ("equals".equals(name)) {
/* 66 */       return Boolean.valueOf((proxy == args[0]));
/*    */     }
/* 68 */     if ("hashCode".equals(name)) {
/* 69 */       return Integer.valueOf(hashCode());
/*    */     }
/* 71 */     if ("toString".equals(name)) {
/* 72 */       return toString();
/*    */     }
/* 74 */     if ("transformClass".equals(name)) {
/* 75 */       return transform((String)args[0], (byte[])args[1], (CodeSource)args[2], (ClassLoader)args[3]);
/*    */     }
/*    */     
/* 78 */     throw new IllegalArgumentException("Unknown method: " + method);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected byte[] transform(String className, byte[] classfileBuffer, CodeSource codeSource, ClassLoader classLoader) throws Exception {
/* 86 */     byte[] result = this.transformer.transform(classLoader, className.replace('.', '/'), null, null, classfileBuffer);
/* 87 */     return (result != null) ? result : classfileBuffer;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 92 */     return getClass().getName() + " for transformer: " + this.transformer;
/*    */   }
/*    */   
/*    */   private static class Dummy {}
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/instrument/classloading/websphere/WebSphereClassPreDefinePlugin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */