/*    */ package org.springframework.instrument.classloading;
/*    */ 
/*    */ import java.lang.instrument.ClassFileTransformer;
/*    */ import org.springframework.core.OverridingClassLoader;
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
/*    */ public class SimpleInstrumentableClassLoader
/*    */   extends OverridingClassLoader
/*    */ {
/*    */   private final WeavingTransformer weavingTransformer;
/*    */   
/*    */   static {
/* 36 */     ClassLoader.registerAsParallelCapable();
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
/*    */   public SimpleInstrumentableClassLoader(@Nullable ClassLoader parent) {
/* 48 */     super(parent);
/* 49 */     this.weavingTransformer = new WeavingTransformer(parent);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void addTransformer(ClassFileTransformer transformer) {
/* 58 */     this.weavingTransformer.addTransformer(transformer);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected byte[] transformIfNecessary(String name, byte[] bytes) {
/* 64 */     return this.weavingTransformer.transformIfNecessary(name, bytes);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/instrument/classloading/SimpleInstrumentableClassLoader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */