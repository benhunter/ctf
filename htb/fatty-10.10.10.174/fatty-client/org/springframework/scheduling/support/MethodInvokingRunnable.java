/*    */ package org.springframework.scheduling.support;
/*    */ 
/*    */ import java.lang.reflect.InvocationTargetException;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
/*    */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*    */ import org.springframework.beans.factory.InitializingBean;
/*    */ import org.springframework.beans.support.ArgumentConvertingMethodInvoker;
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
/*    */ 
/*    */ 
/*    */ public class MethodInvokingRunnable
/*    */   extends ArgumentConvertingMethodInvoker
/*    */   implements Runnable, BeanClassLoaderAware, InitializingBean
/*    */ {
/* 44 */   protected final Log logger = LogFactory.getLog(getClass());
/*    */   
/*    */   @Nullable
/* 47 */   private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
/*    */ 
/*    */ 
/*    */   
/*    */   public void setBeanClassLoader(ClassLoader classLoader) {
/* 52 */     this.beanClassLoader = classLoader;
/*    */   }
/*    */ 
/*    */   
/*    */   protected Class<?> resolveClassName(String className) throws ClassNotFoundException {
/* 57 */     return ClassUtils.forName(className, this.beanClassLoader);
/*    */   }
/*    */ 
/*    */   
/*    */   public void afterPropertiesSet() throws ClassNotFoundException, NoSuchMethodException {
/* 62 */     prepare();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void run() {
/*    */     try {
/* 69 */       invoke();
/*    */     }
/* 71 */     catch (InvocationTargetException ex) {
/* 72 */       this.logger.error(getInvocationFailureMessage(), ex.getTargetException());
/*    */     
/*    */     }
/* 75 */     catch (Throwable ex) {
/* 76 */       this.logger.error(getInvocationFailureMessage(), ex);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected String getInvocationFailureMessage() {
/* 86 */     return "Invocation of method '" + getTargetMethod() + "' on target class [" + 
/* 87 */       getTargetClass() + "] failed";
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/scheduling/support/MethodInvokingRunnable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */