/*    */ package org.springframework.remoting.support;
/*    */ 
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
/*    */ import org.springframework.beans.factory.BeanClassLoaderAware;
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
/*    */ public abstract class RemotingSupport
/*    */   implements BeanClassLoaderAware
/*    */ {
/* 36 */   protected final Log logger = LogFactory.getLog(getClass());
/*    */   
/* 38 */   private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
/*    */ 
/*    */ 
/*    */   
/*    */   public void setBeanClassLoader(ClassLoader classLoader) {
/* 43 */     this.beanClassLoader = classLoader;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected ClassLoader getBeanClassLoader() {
/* 51 */     return this.beanClassLoader;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   protected ClassLoader overrideThreadContextClassLoader() {
/* 63 */     return ClassUtils.overrideThreadContextClassLoader(getBeanClassLoader());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void resetThreadContextClassLoader(@Nullable ClassLoader original) {
/* 72 */     if (original != null)
/* 73 */       Thread.currentThread().setContextClassLoader(original); 
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/remoting/support/RemotingSupport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */