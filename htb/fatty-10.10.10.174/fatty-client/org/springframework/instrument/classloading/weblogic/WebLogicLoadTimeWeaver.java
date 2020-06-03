/*    */ package org.springframework.instrument.classloading.weblogic;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class WebLogicLoadTimeWeaver
/*    */   implements LoadTimeWeaver
/*    */ {
/*    */   private final WebLogicClassLoaderAdapter classLoader;
/*    */   
/*    */   public WebLogicLoadTimeWeaver() {
/* 48 */     this(ClassUtils.getDefaultClassLoader());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public WebLogicLoadTimeWeaver(@Nullable ClassLoader classLoader) {
/* 57 */     Assert.notNull(classLoader, "ClassLoader must not be null");
/* 58 */     this.classLoader = new WebLogicClassLoaderAdapter(classLoader);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void addTransformer(ClassFileTransformer transformer) {
/* 64 */     this.classLoader.addTransformer(transformer);
/*    */   }
/*    */ 
/*    */   
/*    */   public ClassLoader getInstrumentableClassLoader() {
/* 69 */     return this.classLoader.getClassLoader();
/*    */   }
/*    */ 
/*    */   
/*    */   public ClassLoader getThrowawayClassLoader() {
/* 74 */     return (ClassLoader)new OverridingClassLoader(this.classLoader.getClassLoader(), this.classLoader
/* 75 */         .getThrowawayClassLoader());
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/instrument/classloading/weblogic/WebLogicLoadTimeWeaver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */