/*    */ package org.springframework.aop.framework.autoproxy.target;
/*    */ 
/*    */ import org.springframework.aop.target.AbstractBeanFactoryBasedTargetSource;
/*    */ import org.springframework.aop.target.CommonsPool2TargetSource;
/*    */ import org.springframework.aop.target.PrototypeTargetSource;
/*    */ import org.springframework.aop.target.ThreadLocalTargetSource;
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
/*    */ public class QuickTargetSourceCreator
/*    */   extends AbstractBeanFactoryBasedTargetSourceCreator
/*    */ {
/*    */   public static final String PREFIX_COMMONS_POOL = ":";
/*    */   public static final String PREFIX_THREAD_LOCAL = "%";
/*    */   public static final String PREFIX_PROTOTYPE = "!";
/*    */   
/*    */   @Nullable
/*    */   protected final AbstractBeanFactoryBasedTargetSource createBeanFactoryBasedTargetSource(Class<?> beanClass, String beanName) {
/* 60 */     if (beanName.startsWith(":")) {
/* 61 */       CommonsPool2TargetSource cpts = new CommonsPool2TargetSource();
/* 62 */       cpts.setMaxSize(25);
/* 63 */       return (AbstractBeanFactoryBasedTargetSource)cpts;
/*    */     } 
/* 65 */     if (beanName.startsWith("%")) {
/* 66 */       return (AbstractBeanFactoryBasedTargetSource)new ThreadLocalTargetSource();
/*    */     }
/* 68 */     if (beanName.startsWith("!")) {
/* 69 */       return (AbstractBeanFactoryBasedTargetSource)new PrototypeTargetSource();
/*    */     }
/*    */ 
/*    */     
/* 73 */     return null;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/framework/autoproxy/target/QuickTargetSourceCreator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */