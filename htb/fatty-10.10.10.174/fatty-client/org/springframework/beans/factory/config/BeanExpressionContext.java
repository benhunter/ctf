/*    */ package org.springframework.beans.factory.config;
/*    */ 
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
/*    */ public class BeanExpressionContext
/*    */ {
/*    */   private final ConfigurableBeanFactory beanFactory;
/*    */   @Nullable
/*    */   private final Scope scope;
/*    */   
/*    */   public BeanExpressionContext(ConfigurableBeanFactory beanFactory, @Nullable Scope scope) {
/* 37 */     Assert.notNull(beanFactory, "BeanFactory must not be null");
/* 38 */     this.beanFactory = beanFactory;
/* 39 */     this.scope = scope;
/*    */   }
/*    */   
/*    */   public final ConfigurableBeanFactory getBeanFactory() {
/* 43 */     return this.beanFactory;
/*    */   }
/*    */   
/*    */   @Nullable
/*    */   public final Scope getScope() {
/* 48 */     return this.scope;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean containsObject(String key) {
/* 53 */     return (this.beanFactory.containsBean(key) || (this.scope != null && this.scope
/* 54 */       .resolveContextualObject(key) != null));
/*    */   }
/*    */   
/*    */   @Nullable
/*    */   public Object getObject(String key) {
/* 59 */     if (this.beanFactory.containsBean(key)) {
/* 60 */       return this.beanFactory.getBean(key);
/*    */     }
/* 62 */     if (this.scope != null) {
/* 63 */       return this.scope.resolveContextualObject(key);
/*    */     }
/*    */     
/* 66 */     return null;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(Object other) {
/* 73 */     if (this == other) {
/* 74 */       return true;
/*    */     }
/* 76 */     if (!(other instanceof BeanExpressionContext)) {
/* 77 */       return false;
/*    */     }
/* 79 */     BeanExpressionContext otherContext = (BeanExpressionContext)other;
/* 80 */     return (this.beanFactory == otherContext.beanFactory && this.scope == otherContext.scope);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 85 */     return this.beanFactory.hashCode();
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/config/BeanExpressionContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */