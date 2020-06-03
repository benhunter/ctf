/*    */ package org.springframework.beans.factory.parsing;
/*    */ 
/*    */ import org.springframework.beans.BeanMetadataElement;
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
/*    */ public class AliasDefinition
/*    */   implements BeanMetadataElement
/*    */ {
/*    */   private final String beanName;
/*    */   private final String alias;
/*    */   @Nullable
/*    */   private final Object source;
/*    */   
/*    */   public AliasDefinition(String beanName, String alias) {
/* 46 */     this(beanName, alias, null);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public AliasDefinition(String beanName, String alias, @Nullable Object source) {
/* 56 */     Assert.notNull(beanName, "Bean name must not be null");
/* 57 */     Assert.notNull(alias, "Alias must not be null");
/* 58 */     this.beanName = beanName;
/* 59 */     this.alias = alias;
/* 60 */     this.source = source;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final String getBeanName() {
/* 68 */     return this.beanName;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final String getAlias() {
/* 75 */     return this.alias;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public final Object getSource() {
/* 81 */     return this.source;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/parsing/AliasDefinition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */