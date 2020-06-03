/*    */ package org.springframework.aop.aspectj.annotation;
/*    */ 
/*    */ import org.springframework.aop.aspectj.SimpleAspectInstanceFactory;
/*    */ import org.springframework.core.annotation.OrderUtils;
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
/*    */ public class SimpleMetadataAwareAspectInstanceFactory
/*    */   extends SimpleAspectInstanceFactory
/*    */   implements MetadataAwareAspectInstanceFactory
/*    */ {
/*    */   private final AspectMetadata metadata;
/*    */   
/*    */   public SimpleMetadataAwareAspectInstanceFactory(Class<?> aspectClass, String aspectName) {
/* 43 */     super(aspectClass);
/* 44 */     this.metadata = new AspectMetadata(aspectClass, aspectName);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public final AspectMetadata getAspectMetadata() {
/* 50 */     return this.metadata;
/*    */   }
/*    */ 
/*    */   
/*    */   public Object getAspectCreationMutex() {
/* 55 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getOrderForAspectClass(Class<?> aspectClass) {
/* 60 */     return OrderUtils.getOrder(aspectClass, 2147483647);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/aspectj/annotation/SimpleMetadataAwareAspectInstanceFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */