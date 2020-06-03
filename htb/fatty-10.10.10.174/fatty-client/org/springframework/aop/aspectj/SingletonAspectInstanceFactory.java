/*    */ package org.springframework.aop.aspectj;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import org.springframework.core.Ordered;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.util.Assert;
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
/*    */ public class SingletonAspectInstanceFactory
/*    */   implements AspectInstanceFactory, Serializable
/*    */ {
/*    */   private final Object aspectInstance;
/*    */   
/*    */   public SingletonAspectInstanceFactory(Object aspectInstance) {
/* 46 */     Assert.notNull(aspectInstance, "Aspect instance must not be null");
/* 47 */     this.aspectInstance = aspectInstance;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public final Object getAspectInstance() {
/* 53 */     return this.aspectInstance;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public ClassLoader getAspectClassLoader() {
/* 59 */     return this.aspectInstance.getClass().getClassLoader();
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
/*    */   
/*    */   public int getOrder() {
/* 72 */     if (this.aspectInstance instanceof Ordered) {
/* 73 */       return ((Ordered)this.aspectInstance).getOrder();
/*    */     }
/* 75 */     return getOrderForAspectClass(this.aspectInstance.getClass());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected int getOrderForAspectClass(Class<?> aspectClass) {
/* 86 */     return Integer.MAX_VALUE;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/aspectj/SingletonAspectInstanceFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */