/*    */ package org.springframework.beans.factory.support;
/*    */ 
/*    */ import org.springframework.beans.factory.config.BeanDefinition;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class GenericBeanDefinition
/*    */   extends AbstractBeanDefinition
/*    */ {
/*    */   @Nullable
/*    */   private String parentName;
/*    */   
/*    */   public GenericBeanDefinition() {}
/*    */   
/*    */   public GenericBeanDefinition(BeanDefinition original) {
/* 64 */     super(original);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setParentName(@Nullable String parentName) {
/* 70 */     this.parentName = parentName;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public String getParentName() {
/* 76 */     return this.parentName;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public AbstractBeanDefinition cloneBeanDefinition() {
/* 82 */     return new GenericBeanDefinition(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object other) {
/* 87 */     return (this == other || (other instanceof GenericBeanDefinition && super.equals(other)));
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 92 */     StringBuilder sb = new StringBuilder("Generic bean");
/* 93 */     if (this.parentName != null) {
/* 94 */       sb.append(" with parent '").append(this.parentName).append("'");
/*    */     }
/* 96 */     sb.append(": ").append(super.toString());
/* 97 */     return sb.toString();
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/support/GenericBeanDefinition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */