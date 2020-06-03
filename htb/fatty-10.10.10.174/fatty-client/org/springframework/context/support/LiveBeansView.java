/*     */ package org.springframework.context.support;
/*     */ 
/*     */ import java.lang.management.ManagementFactory;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Set;
/*     */ import javax.management.MBeanServer;
/*     */ import javax.management.ObjectName;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.config.ConfigurableBeanFactory;
/*     */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.context.ApplicationContextAware;
/*     */ import org.springframework.context.ApplicationContextException;
/*     */ import org.springframework.context.ConfigurableApplicationContext;
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
/*     */ 
/*     */ public class LiveBeansView
/*     */   implements LiveBeansViewMBean, ApplicationContextAware
/*     */ {
/*     */   public static final String MBEAN_DOMAIN_PROPERTY_NAME = "spring.liveBeansView.mbeanDomain";
/*     */   public static final String MBEAN_APPLICATION_KEY = "application";
/*  65 */   private static final Set<ConfigurableApplicationContext> applicationContexts = new LinkedHashSet<>();
/*     */   @Nullable
/*     */   private static String applicationName;
/*     */   @Nullable
/*     */   private ConfigurableApplicationContext applicationContext;
/*     */   
/*     */   static void registerApplicationContext(ConfigurableApplicationContext applicationContext) {
/*  72 */     String mbeanDomain = applicationContext.getEnvironment().getProperty("spring.liveBeansView.mbeanDomain");
/*  73 */     if (mbeanDomain != null) {
/*  74 */       synchronized (applicationContexts) {
/*  75 */         if (applicationContexts.isEmpty()) {
/*     */           try {
/*  77 */             MBeanServer server = ManagementFactory.getPlatformMBeanServer();
/*  78 */             applicationName = applicationContext.getApplicationName();
/*  79 */             server.registerMBean(new LiveBeansView(), new ObjectName(mbeanDomain, "application", applicationName));
/*     */           
/*     */           }
/*  82 */           catch (Throwable ex) {
/*  83 */             throw new ApplicationContextException("Failed to register LiveBeansView MBean", ex);
/*     */           } 
/*     */         }
/*  86 */         applicationContexts.add(applicationContext);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   static void unregisterApplicationContext(ConfigurableApplicationContext applicationContext) {
/*  92 */     synchronized (applicationContexts) {
/*  93 */       if (applicationContexts.remove(applicationContext) && applicationContexts.isEmpty()) {
/*     */         try {
/*  95 */           MBeanServer server = ManagementFactory.getPlatformMBeanServer();
/*  96 */           String mbeanDomain = applicationContext.getEnvironment().getProperty("spring.liveBeansView.mbeanDomain");
/*  97 */           if (mbeanDomain != null) {
/*  98 */             server.unregisterMBean(new ObjectName(mbeanDomain, "application", applicationName));
/*     */           }
/*     */         }
/* 101 */         catch (Throwable ex) {
/* 102 */           throw new ApplicationContextException("Failed to unregister LiveBeansView MBean", ex);
/*     */         } finally {
/*     */           
/* 105 */           applicationName = null;
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setApplicationContext(ApplicationContext applicationContext) {
/* 118 */     Assert.isTrue(applicationContext instanceof ConfigurableApplicationContext, "ApplicationContext does not implement ConfigurableApplicationContext");
/*     */     
/* 120 */     this.applicationContext = (ConfigurableApplicationContext)applicationContext;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSnapshotAsJson() {
/*     */     Set<ConfigurableApplicationContext> contexts;
/* 132 */     if (this.applicationContext != null) {
/* 133 */       contexts = Collections.singleton(this.applicationContext);
/*     */     } else {
/*     */       
/* 136 */       contexts = findApplicationContexts();
/*     */     } 
/* 138 */     return generateJson(contexts);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Set<ConfigurableApplicationContext> findApplicationContexts() {
/* 147 */     synchronized (applicationContexts) {
/* 148 */       return new LinkedHashSet<>(applicationContexts);
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
/*     */   protected String generateJson(Set<ConfigurableApplicationContext> contexts) {
/* 164 */     StringBuilder result = new StringBuilder("[\n");
/* 165 */     for (Iterator<ConfigurableApplicationContext> it = contexts.iterator(); it.hasNext(); ) {
/* 166 */       ConfigurableApplicationContext context = it.next();
/* 167 */       result.append("{\n\"context\": \"").append(context.getId()).append("\",\n");
/* 168 */       if (context.getParent() != null) {
/* 169 */         result.append("\"parent\": \"").append(context.getParent().getId()).append("\",\n");
/*     */       } else {
/*     */         
/* 172 */         result.append("\"parent\": null,\n");
/*     */       } 
/* 174 */       result.append("\"beans\": [\n");
/* 175 */       ConfigurableListableBeanFactory bf = context.getBeanFactory();
/* 176 */       String[] beanNames = bf.getBeanDefinitionNames();
/* 177 */       boolean elementAppended = false;
/* 178 */       for (String beanName : beanNames) {
/* 179 */         BeanDefinition bd = bf.getBeanDefinition(beanName);
/* 180 */         if (isBeanEligible(beanName, bd, (ConfigurableBeanFactory)bf)) {
/* 181 */           if (elementAppended) {
/* 182 */             result.append(",\n");
/*     */           }
/* 184 */           result.append("{\n\"bean\": \"").append(beanName).append("\",\n");
/* 185 */           result.append("\"aliases\": ");
/* 186 */           appendArray(result, bf.getAliases(beanName));
/* 187 */           result.append(",\n");
/* 188 */           String scope = bd.getScope();
/* 189 */           if (!StringUtils.hasText(scope)) {
/* 190 */             scope = "singleton";
/*     */           }
/* 192 */           result.append("\"scope\": \"").append(scope).append("\",\n");
/* 193 */           Class<?> beanType = bf.getType(beanName);
/* 194 */           if (beanType != null) {
/* 195 */             result.append("\"type\": \"").append(beanType.getName()).append("\",\n");
/*     */           } else {
/*     */             
/* 198 */             result.append("\"type\": null,\n");
/*     */           } 
/* 200 */           result.append("\"resource\": \"").append(getEscapedResourceDescription(bd)).append("\",\n");
/* 201 */           result.append("\"dependencies\": ");
/* 202 */           appendArray(result, bf.getDependenciesForBean(beanName));
/* 203 */           result.append("\n}");
/* 204 */           elementAppended = true;
/*     */         } 
/*     */       } 
/* 207 */       result.append("]\n");
/* 208 */       result.append("}");
/* 209 */       if (it.hasNext()) {
/* 210 */         result.append(",\n");
/*     */       }
/*     */     } 
/* 213 */     result.append("]");
/* 214 */     return result.toString();
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
/*     */   protected boolean isBeanEligible(String beanName, BeanDefinition bd, ConfigurableBeanFactory bf) {
/* 226 */     return (bd.getRole() != 2 && (
/* 227 */       !bd.isLazyInit() || bf.containsSingleton(beanName)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected String getEscapedResourceDescription(BeanDefinition bd) {
/* 238 */     String resourceDescription = bd.getResourceDescription();
/* 239 */     if (resourceDescription == null) {
/* 240 */       return null;
/*     */     }
/* 242 */     StringBuilder result = new StringBuilder(resourceDescription.length() + 16);
/* 243 */     for (int i = 0; i < resourceDescription.length(); i++) {
/* 244 */       char character = resourceDescription.charAt(i);
/* 245 */       if (character == '\\') {
/* 246 */         result.append('/');
/*     */       }
/* 248 */       else if (character == '"') {
/* 249 */         result.append("\\").append('"');
/*     */       } else {
/*     */         
/* 252 */         result.append(character);
/*     */       } 
/*     */     } 
/* 255 */     return result.toString();
/*     */   }
/*     */   
/*     */   private void appendArray(StringBuilder result, String[] arr) {
/* 259 */     result.append('[');
/* 260 */     if (arr.length > 0) {
/* 261 */       result.append('"');
/*     */     }
/* 263 */     result.append(StringUtils.arrayToDelimitedString((Object[])arr, "\", \""));
/* 264 */     if (arr.length > 0) {
/* 265 */       result.append('"');
/*     */     }
/* 267 */     result.append(']');
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/support/LiveBeansView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */