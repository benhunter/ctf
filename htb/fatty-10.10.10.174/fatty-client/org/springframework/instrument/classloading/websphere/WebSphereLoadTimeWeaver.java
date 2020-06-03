/*    */ package org.springframework.instrument.classloading.websphere;
/*    */ 
/*    */ import java.lang.instrument.ClassFileTransformer;
/*    */ import org.springframework.core.OverridingClassLoader;
/*    */ import org.springframework.instrument.classloading.LoadTimeWeaver;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.util.Assert;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class WebSphereLoadTimeWeaver
/*    */   implements LoadTimeWeaver
/*    */ {
/*    */   private final WebSphereClassLoaderAdapter classLoader;
/*    */   
/*    */   public WebSphereLoadTimeWeaver() {
/* 45 */     this(ClassUtils.getDefaultClassLoader());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public WebSphereLoadTimeWeaver(@Nullable ClassLoader classLoader) {
/* 54 */     Assert.notNull(classLoader, "ClassLoader must not be null");
/* 55 */     this.classLoader = new WebSphereClassLoaderAdapter(classLoader);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void addTransformer(ClassFileTransformer transformer) {
/* 61 */     this.classLoader.addTransformer(transformer);
/*    */   }
/*    */ 
/*    */   
/*    */   public ClassLoader getInstrumentableClassLoader() {
/* 66 */     return this.classLoader.getClassLoader();
/*    */   }
/*    */ 
/*    */   
/*    */   public ClassLoader getThrowawayClassLoader() {
/* 71 */     return (ClassLoader)new OverridingClassLoader(this.classLoader.getClassLoader(), this.classLoader
/* 72 */         .getThrowawayClassLoader());
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/instrument/classloading/websphere/WebSphereLoadTimeWeaver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */