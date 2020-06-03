/*     */ package org.springframework.beans.factory.config;
/*     */ 
/*     */ import java.util.Properties;
/*     */ import java.util.prefs.BackingStoreException;
/*     */ import java.util.prefs.Preferences;
/*     */ import org.springframework.beans.factory.BeanDefinitionStoreException;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.lang.Nullable;
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
/*     */ public class PreferencesPlaceholderConfigurer
/*     */   extends PropertyPlaceholderConfigurer
/*     */   implements InitializingBean
/*     */ {
/*     */   @Nullable
/*     */   private String systemTreePath;
/*     */   @Nullable
/*     */   private String userTreePath;
/*  54 */   private Preferences systemPrefs = Preferences.systemRoot();
/*     */   
/*  56 */   private Preferences userPrefs = Preferences.userRoot();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSystemTreePath(String systemTreePath) {
/*  64 */     this.systemTreePath = systemTreePath;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUserTreePath(String userTreePath) {
/*  72 */     this.userTreePath = userTreePath;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() {
/*  82 */     if (this.systemTreePath != null) {
/*  83 */       this.systemPrefs = this.systemPrefs.node(this.systemTreePath);
/*     */     }
/*  85 */     if (this.userTreePath != null) {
/*  86 */       this.userPrefs = this.userPrefs.node(this.userTreePath);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String resolvePlaceholder(String placeholder, Properties props) {
/*  97 */     String path = null;
/*  98 */     String key = placeholder;
/*  99 */     int endOfPath = placeholder.lastIndexOf('/');
/* 100 */     if (endOfPath != -1) {
/* 101 */       path = placeholder.substring(0, endOfPath);
/* 102 */       key = placeholder.substring(endOfPath + 1);
/*     */     } 
/* 104 */     String value = resolvePlaceholder(path, key, this.userPrefs);
/* 105 */     if (value == null) {
/* 106 */       value = resolvePlaceholder(path, key, this.systemPrefs);
/* 107 */       if (value == null) {
/* 108 */         value = props.getProperty(placeholder);
/*     */       }
/*     */     } 
/* 111 */     return value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected String resolvePlaceholder(@Nullable String path, String key, Preferences preferences) {
/* 123 */     if (path != null) {
/*     */       
/*     */       try {
/* 126 */         if (preferences.nodeExists(path)) {
/* 127 */           return preferences.node(path).get(key, null);
/*     */         }
/*     */         
/* 130 */         return null;
/*     */       
/*     */       }
/* 133 */       catch (BackingStoreException ex) {
/* 134 */         throw new BeanDefinitionStoreException("Cannot access specified node path [" + path + "]", ex);
/*     */       } 
/*     */     }
/*     */     
/* 138 */     return preferences.get(key, null);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/config/PreferencesPlaceholderConfigurer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */