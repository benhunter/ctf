/*    */ package org.springframework.instrument.classloading.weblogic;
/*    */ 
/*    */ import java.lang.instrument.ClassFileTransformer;
/*    */ import java.lang.instrument.IllegalClassFormatException;
/*    */ import java.lang.reflect.InvocationHandler;
/*    */ import java.lang.reflect.Method;
/*    */ import java.util.Hashtable;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class WebLogicClassPreProcessorAdapter
/*    */   implements InvocationHandler
/*    */ {
/*    */   private final ClassFileTransformer transformer;
/*    */   private final ClassLoader loader;
/*    */   
/*    */   public WebLogicClassPreProcessorAdapter(ClassFileTransformer transformer, ClassLoader loader) {
/* 49 */     this.transformer = transformer;
/* 50 */     this.loader = loader;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
/* 57 */     String name = method.getName();
/* 58 */     if ("equals".equals(name)) {
/* 59 */       return Boolean.valueOf((proxy == args[0]));
/*    */     }
/* 61 */     if ("hashCode".equals(name)) {
/* 62 */       return Integer.valueOf(hashCode());
/*    */     }
/* 64 */     if ("toString".equals(name)) {
/* 65 */       return toString();
/*    */     }
/* 67 */     if ("initialize".equals(name)) {
/* 68 */       initialize((Hashtable<?, ?>)args[0]);
/* 69 */       return null;
/*    */     } 
/* 71 */     if ("preProcess".equals(name)) {
/* 72 */       return preProcess((String)args[0], (byte[])args[1]);
/*    */     }
/*    */     
/* 75 */     throw new IllegalArgumentException("Unknown method: " + method);
/*    */   }
/*    */ 
/*    */   
/*    */   public void initialize(Hashtable<?, ?> params) {}
/*    */ 
/*    */   
/*    */   public byte[] preProcess(String className, byte[] classBytes) {
/*    */     try {
/* 84 */       byte[] result = this.transformer.transform(this.loader, className, null, null, classBytes);
/* 85 */       return (result != null) ? result : classBytes;
/*    */     }
/* 87 */     catch (IllegalClassFormatException ex) {
/* 88 */       throw new IllegalStateException("Cannot transform due to illegal class format", ex);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 94 */     return getClass().getName() + " for transformer: " + this.transformer;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/instrument/classloading/weblogic/WebLogicClassPreProcessorAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */