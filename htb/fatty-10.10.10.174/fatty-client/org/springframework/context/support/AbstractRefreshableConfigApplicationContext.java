/*     */ package org.springframework.context.support;
/*     */ 
/*     */ import org.springframework.beans.factory.BeanNameAware;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
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
/*     */ 
/*     */ public abstract class AbstractRefreshableConfigApplicationContext
/*     */   extends AbstractRefreshableApplicationContext
/*     */   implements BeanNameAware, InitializingBean
/*     */ {
/*     */   @Nullable
/*     */   private String[] configLocations;
/*     */   private boolean setIdCalled = false;
/*     */   
/*     */   public AbstractRefreshableConfigApplicationContext() {}
/*     */   
/*     */   public AbstractRefreshableConfigApplicationContext(@Nullable ApplicationContext parent) {
/*  59 */     super(parent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setConfigLocation(String location) {
/*  69 */     setConfigLocations(StringUtils.tokenizeToStringArray(location, ",; \t\n"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setConfigLocations(@Nullable String... locations) {
/*  77 */     if (locations != null) {
/*  78 */       Assert.noNullElements((Object[])locations, "Config locations must not be null");
/*  79 */       this.configLocations = new String[locations.length];
/*  80 */       for (int i = 0; i < locations.length; i++) {
/*  81 */         this.configLocations[i] = resolvePath(locations[i]).trim();
/*     */       }
/*     */     } else {
/*     */       
/*  85 */       this.configLocations = null;
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
/*     */   @Nullable
/*     */   protected String[] getConfigLocations() {
/* 101 */     return (this.configLocations != null) ? this.configLocations : getDefaultConfigLocations();
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
/*     */   @Nullable
/*     */   protected String[] getDefaultConfigLocations() {
/* 114 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String resolvePath(String path) {
/* 125 */     return getEnvironment().resolveRequiredPlaceholders(path);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setId(String id) {
/* 131 */     super.setId(id);
/* 132 */     this.setIdCalled = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBeanName(String name) {
/* 141 */     if (!this.setIdCalled) {
/* 142 */       super.setId(name);
/* 143 */       setDisplayName("ApplicationContext '" + name + "'");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() {
/* 153 */     if (!isActive())
/* 154 */       refresh(); 
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/support/AbstractRefreshableConfigApplicationContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */