/*    */ package org.springframework.aop.config;
/*    */ 
/*    */ import org.springframework.beans.factory.config.BeanDefinition;
/*    */ import org.springframework.beans.factory.config.BeanReference;
/*    */ import org.springframework.beans.factory.parsing.CompositeComponentDefinition;
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
/*    */ public class AspectComponentDefinition
/*    */   extends CompositeComponentDefinition
/*    */ {
/*    */   private final BeanDefinition[] beanDefinitions;
/*    */   private final BeanReference[] beanReferences;
/*    */   
/*    */   public AspectComponentDefinition(String aspectName, @Nullable BeanDefinition[] beanDefinitions, @Nullable BeanReference[] beanReferences, @Nullable Object source) {
/* 44 */     super(aspectName, source);
/* 45 */     this.beanDefinitions = (beanDefinitions != null) ? beanDefinitions : new BeanDefinition[0];
/* 46 */     this.beanReferences = (beanReferences != null) ? beanReferences : new BeanReference[0];
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public BeanDefinition[] getBeanDefinitions() {
/* 52 */     return this.beanDefinitions;
/*    */   }
/*    */ 
/*    */   
/*    */   public BeanReference[] getBeanReferences() {
/* 57 */     return this.beanReferences;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/config/AspectComponentDefinition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */