/*    */ package org.springframework.beans.factory.support;
/*    */ 
/*    */ import java.io.FileNotFoundException;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import org.springframework.beans.factory.config.BeanDefinition;
/*    */ import org.springframework.core.io.AbstractResource;
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
/*    */ class BeanDefinitionResource
/*    */   extends AbstractResource
/*    */ {
/*    */   private final BeanDefinition beanDefinition;
/*    */   
/*    */   public BeanDefinitionResource(BeanDefinition beanDefinition) {
/* 45 */     Assert.notNull(beanDefinition, "BeanDefinition must not be null");
/* 46 */     this.beanDefinition = beanDefinition;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final BeanDefinition getBeanDefinition() {
/* 53 */     return this.beanDefinition;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean exists() {
/* 59 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isReadable() {
/* 64 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public InputStream getInputStream() throws IOException {
/* 69 */     throw new FileNotFoundException("Resource cannot be opened because it points to " + 
/* 70 */         getDescription());
/*    */   }
/*    */ 
/*    */   
/*    */   public String getDescription() {
/* 75 */     return "BeanDefinition defined in " + this.beanDefinition.getResourceDescription();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(Object other) {
/* 84 */     return (this == other || (other instanceof BeanDefinitionResource && ((BeanDefinitionResource)other).beanDefinition
/* 85 */       .equals(this.beanDefinition)));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 93 */     return this.beanDefinition.hashCode();
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/support/BeanDefinitionResource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */