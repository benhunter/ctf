/*    */ package org.springframework.aop.config;
/*    */ 
/*    */ import org.springframework.beans.factory.config.BeanDefinition;
/*    */ import org.springframework.beans.factory.parsing.AbstractComponentDefinition;
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
/*    */ public class PointcutComponentDefinition
/*    */   extends AbstractComponentDefinition
/*    */ {
/*    */   private final String pointcutBeanName;
/*    */   private final BeanDefinition pointcutDefinition;
/*    */   private final String description;
/*    */   
/*    */   public PointcutComponentDefinition(String pointcutBeanName, BeanDefinition pointcutDefinition, String expression) {
/* 41 */     Assert.notNull(pointcutBeanName, "Bean name must not be null");
/* 42 */     Assert.notNull(pointcutDefinition, "Pointcut definition must not be null");
/* 43 */     Assert.notNull(expression, "Expression must not be null");
/* 44 */     this.pointcutBeanName = pointcutBeanName;
/* 45 */     this.pointcutDefinition = pointcutDefinition;
/* 46 */     this.description = "Pointcut <name='" + pointcutBeanName + "', expression=[" + expression + "]>";
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getName() {
/* 52 */     return this.pointcutBeanName;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getDescription() {
/* 57 */     return this.description;
/*    */   }
/*    */ 
/*    */   
/*    */   public BeanDefinition[] getBeanDefinitions() {
/* 62 */     return new BeanDefinition[] { this.pointcutDefinition };
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public Object getSource() {
/* 68 */     return this.pointcutDefinition.getSource();
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/config/PointcutComponentDefinition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */