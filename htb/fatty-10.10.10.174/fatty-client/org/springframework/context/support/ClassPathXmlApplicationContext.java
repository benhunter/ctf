/*     */ package org.springframework.context.support;
/*     */ 
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.core.io.ClassPathResource;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
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
/*     */ public class ClassPathXmlApplicationContext
/*     */   extends AbstractXmlApplicationContext
/*     */ {
/*     */   @Nullable
/*     */   private Resource[] configResources;
/*     */   
/*     */   public ClassPathXmlApplicationContext() {}
/*     */   
/*     */   public ClassPathXmlApplicationContext(ApplicationContext parent) {
/*  75 */     super(parent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassPathXmlApplicationContext(String configLocation) throws BeansException {
/*  85 */     this(new String[] { configLocation }, true, (ApplicationContext)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassPathXmlApplicationContext(String... configLocations) throws BeansException {
/*  95 */     this(configLocations, true, (ApplicationContext)null);
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
/*     */   public ClassPathXmlApplicationContext(String[] configLocations, @Nullable ApplicationContext parent) throws BeansException {
/* 109 */     this(configLocations, true, parent);
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
/*     */   public ClassPathXmlApplicationContext(String[] configLocations, boolean refresh) throws BeansException {
/* 123 */     this(configLocations, refresh, (ApplicationContext)null);
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
/*     */   public ClassPathXmlApplicationContext(String[] configLocations, boolean refresh, @Nullable ApplicationContext parent) throws BeansException {
/* 141 */     super(parent);
/* 142 */     setConfigLocations(configLocations);
/* 143 */     if (refresh) {
/* 144 */       refresh();
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
/*     */   public ClassPathXmlApplicationContext(String path, Class<?> clazz) throws BeansException {
/* 163 */     this(new String[] { path }, clazz);
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
/*     */   public ClassPathXmlApplicationContext(String[] paths, Class<?> clazz) throws BeansException {
/* 177 */     this(paths, clazz, (ApplicationContext)null);
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
/*     */   public ClassPathXmlApplicationContext(String[] paths, Class<?> clazz, @Nullable ApplicationContext parent) throws BeansException {
/* 195 */     super(parent);
/* 196 */     Assert.notNull(paths, "Path array must not be null");
/* 197 */     Assert.notNull(clazz, "Class argument must not be null");
/* 198 */     this.configResources = new Resource[paths.length];
/* 199 */     for (int i = 0; i < paths.length; i++) {
/* 200 */       this.configResources[i] = (Resource)new ClassPathResource(paths[i], clazz);
/*     */     }
/* 202 */     refresh();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected Resource[] getConfigResources() {
/* 209 */     return this.configResources;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/support/ClassPathXmlApplicationContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */