/*     */ package org.springframework.core;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.util.StringValueResolver;
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
/*     */ public class SimpleAliasRegistry
/*     */   implements AliasRegistry
/*     */ {
/*  44 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */ 
/*     */   
/*  47 */   private final Map<String, String> aliasMap = new ConcurrentHashMap<>(16);
/*     */ 
/*     */ 
/*     */   
/*     */   public void registerAlias(String name, String alias) {
/*  52 */     Assert.hasText(name, "'name' must not be empty");
/*  53 */     Assert.hasText(alias, "'alias' must not be empty");
/*  54 */     synchronized (this.aliasMap) {
/*  55 */       if (alias.equals(name)) {
/*  56 */         this.aliasMap.remove(alias);
/*  57 */         if (this.logger.isDebugEnabled()) {
/*  58 */           this.logger.debug("Alias definition '" + alias + "' ignored since it points to same name");
/*     */         }
/*     */       } else {
/*     */         
/*  62 */         String registeredName = this.aliasMap.get(alias);
/*  63 */         if (registeredName != null) {
/*  64 */           if (registeredName.equals(name)) {
/*     */             return;
/*     */           }
/*     */           
/*  68 */           if (!allowAliasOverriding()) {
/*  69 */             throw new IllegalStateException("Cannot define alias '" + alias + "' for name '" + name + "': It is already registered for name '" + registeredName + "'.");
/*     */           }
/*     */           
/*  72 */           if (this.logger.isDebugEnabled()) {
/*  73 */             this.logger.debug("Overriding alias '" + alias + "' definition for registered name '" + registeredName + "' with new target name '" + name + "'");
/*     */           }
/*     */         } 
/*     */         
/*  77 */         checkForAliasCircle(name, alias);
/*  78 */         this.aliasMap.put(alias, name);
/*  79 */         if (this.logger.isTraceEnabled()) {
/*  80 */           this.logger.trace("Alias definition '" + alias + "' registered for name '" + name + "'");
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean allowAliasOverriding() {
/*  91 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasAlias(String name, String alias) {
/* 101 */     for (Map.Entry<String, String> entry : this.aliasMap.entrySet()) {
/* 102 */       String registeredName = entry.getValue();
/* 103 */       if (registeredName.equals(name)) {
/* 104 */         String registeredAlias = entry.getKey();
/* 105 */         if (registeredAlias.equals(alias) || hasAlias(registeredAlias, alias)) {
/* 106 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/* 110 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeAlias(String alias) {
/* 115 */     synchronized (this.aliasMap) {
/* 116 */       String name = this.aliasMap.remove(alias);
/* 117 */       if (name == null) {
/* 118 */         throw new IllegalStateException("No alias '" + alias + "' registered");
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAlias(String name) {
/* 125 */     return this.aliasMap.containsKey(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] getAliases(String name) {
/* 130 */     List<String> result = new ArrayList<>();
/* 131 */     synchronized (this.aliasMap) {
/* 132 */       retrieveAliases(name, result);
/*     */     } 
/* 134 */     return StringUtils.toStringArray(result);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void retrieveAliases(String name, List<String> result) {
/* 143 */     this.aliasMap.forEach((alias, registeredName) -> {
/*     */           if (registeredName.equals(name)) {
/*     */             result.add(alias);
/*     */             retrieveAliases(alias, result);
/*     */           } 
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void resolveAliases(StringValueResolver valueResolver) {
/* 159 */     Assert.notNull(valueResolver, "StringValueResolver must not be null");
/* 160 */     synchronized (this.aliasMap) {
/* 161 */       Map<String, String> aliasCopy = new HashMap<>(this.aliasMap);
/* 162 */       aliasCopy.forEach((alias, registeredName) -> {
/*     */             String resolvedAlias = valueResolver.resolveStringValue(alias);
/*     */             String resolvedName = valueResolver.resolveStringValue(registeredName);
/*     */             if (resolvedAlias == null || resolvedName == null || resolvedAlias.equals(resolvedName)) {
/*     */               this.aliasMap.remove(alias);
/*     */             } else if (!resolvedAlias.equals(alias)) {
/*     */               String existingName = this.aliasMap.get(resolvedAlias);
/*     */               if (existingName != null) {
/*     */                 if (existingName.equals(resolvedName)) {
/*     */                   this.aliasMap.remove(alias);
/*     */                   return;
/*     */                 } 
/*     */                 throw new IllegalStateException("Cannot register resolved alias '" + resolvedAlias + "' (original: '" + alias + "') for name '" + resolvedName + "': It is already registered for name '" + registeredName + "'.");
/*     */               } 
/*     */               checkForAliasCircle(resolvedName, resolvedAlias);
/*     */               this.aliasMap.remove(alias);
/*     */               this.aliasMap.put(resolvedAlias, resolvedName);
/*     */             } else if (!registeredName.equals(resolvedName)) {
/*     */               this.aliasMap.put(alias, resolvedName);
/*     */             } 
/*     */           });
/*     */     } 
/*     */   }
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
/*     */   protected void checkForAliasCircle(String name, String alias) {
/* 202 */     if (hasAlias(alias, name)) {
/* 203 */       throw new IllegalStateException("Cannot register alias '" + alias + "' for name '" + name + "': Circular reference - '" + name + "' is a direct or indirect alias for '" + alias + "' already");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String canonicalName(String name) {
/* 215 */     String canonicalName = name;
/*     */ 
/*     */     
/*     */     while (true) {
/* 219 */       String resolvedName = this.aliasMap.get(canonicalName);
/* 220 */       if (resolvedName != null) {
/* 221 */         canonicalName = resolvedName;
/*     */       }
/*     */       
/* 224 */       if (resolvedName == null)
/* 225 */         return canonicalName; 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/SimpleAliasRegistry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */