/*     */ package org.springframework.beans.factory.config;
/*     */ 
/*     */ import org.springframework.beans.BeanMetadataElement;
/*     */ import org.springframework.beans.factory.BeanFactoryUtils;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BeanDefinitionHolder
/*     */   implements BeanMetadataElement
/*     */ {
/*     */   private final BeanDefinition beanDefinition;
/*     */   private final String beanName;
/*     */   @Nullable
/*     */   private final String[] aliases;
/*     */   
/*     */   public BeanDefinitionHolder(BeanDefinition beanDefinition, String beanName) {
/*  56 */     this(beanDefinition, beanName, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanDefinitionHolder(BeanDefinition beanDefinition, String beanName, @Nullable String[] aliases) {
/*  66 */     Assert.notNull(beanDefinition, "BeanDefinition must not be null");
/*  67 */     Assert.notNull(beanName, "Bean name must not be null");
/*  68 */     this.beanDefinition = beanDefinition;
/*  69 */     this.beanName = beanName;
/*  70 */     this.aliases = aliases;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanDefinitionHolder(BeanDefinitionHolder beanDefinitionHolder) {
/*  81 */     Assert.notNull(beanDefinitionHolder, "BeanDefinitionHolder must not be null");
/*  82 */     this.beanDefinition = beanDefinitionHolder.getBeanDefinition();
/*  83 */     this.beanName = beanDefinitionHolder.getBeanName();
/*  84 */     this.aliases = beanDefinitionHolder.getAliases();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanDefinition getBeanDefinition() {
/*  92 */     return this.beanDefinition;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getBeanName() {
/*  99 */     return this.beanName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String[] getAliases() {
/* 108 */     return this.aliases;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object getSource() {
/* 118 */     return this.beanDefinition.getSource();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean matchesName(@Nullable String candidateName) {
/* 126 */     return (candidateName != null && (candidateName.equals(this.beanName) || candidateName
/* 127 */       .equals(BeanFactoryUtils.transformedBeanName(this.beanName)) || 
/* 128 */       ObjectUtils.containsElement((Object[])this.aliases, candidateName)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getShortDescription() {
/* 138 */     StringBuilder sb = new StringBuilder();
/* 139 */     sb.append("Bean definition with name '").append(this.beanName).append("'");
/* 140 */     if (this.aliases != null) {
/* 141 */       sb.append(" and aliases [").append(StringUtils.arrayToCommaDelimitedString((Object[])this.aliases)).append("]");
/*     */     }
/* 143 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getLongDescription() {
/* 153 */     StringBuilder sb = new StringBuilder(getShortDescription());
/* 154 */     sb.append(": ").append(this.beanDefinition);
/* 155 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 166 */     return getLongDescription();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 172 */     if (this == other) {
/* 173 */       return true;
/*     */     }
/* 175 */     if (!(other instanceof BeanDefinitionHolder)) {
/* 176 */       return false;
/*     */     }
/* 178 */     BeanDefinitionHolder otherHolder = (BeanDefinitionHolder)other;
/* 179 */     return (this.beanDefinition.equals(otherHolder.beanDefinition) && this.beanName
/* 180 */       .equals(otherHolder.beanName) && 
/* 181 */       ObjectUtils.nullSafeEquals(this.aliases, otherHolder.aliases));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 186 */     int hashCode = this.beanDefinition.hashCode();
/* 187 */     hashCode = 29 * hashCode + this.beanName.hashCode();
/* 188 */     hashCode = 29 * hashCode + ObjectUtils.nullSafeHashCode((Object[])this.aliases);
/* 189 */     return hashCode;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/config/BeanDefinitionHolder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */