/*     */ package org.springframework.context.annotation;
/*     */ 
/*     */ import java.util.Map;
/*     */ import javax.management.MBeanServer;
/*     */ import javax.naming.NamingException;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.BeanFactoryAware;
/*     */ import org.springframework.context.EnvironmentAware;
/*     */ import org.springframework.core.annotation.AnnotationAttributes;
/*     */ import org.springframework.core.env.Environment;
/*     */ import org.springframework.core.type.AnnotationMetadata;
/*     */ import org.springframework.jmx.MBeanServerNotFoundException;
/*     */ import org.springframework.jmx.export.annotation.AnnotationMBeanExporter;
/*     */ import org.springframework.jmx.support.RegistrationPolicy;
/*     */ import org.springframework.jmx.support.WebSphereMBeanServerFactoryBean;
/*     */ import org.springframework.jndi.JndiLocatorDelegate;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
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
/*     */ @Configuration
/*     */ public class MBeanExportConfiguration
/*     */   implements ImportAware, EnvironmentAware, BeanFactoryAware
/*     */ {
/*     */   private static final String MBEAN_EXPORTER_BEAN_NAME = "mbeanExporter";
/*     */   @Nullable
/*     */   private AnnotationAttributes enableMBeanExport;
/*     */   @Nullable
/*     */   private Environment environment;
/*     */   @Nullable
/*     */   private BeanFactory beanFactory;
/*     */   
/*     */   public void setImportMetadata(AnnotationMetadata importMetadata) {
/*  68 */     Map<String, Object> map = importMetadata.getAnnotationAttributes(EnableMBeanExport.class.getName());
/*  69 */     this.enableMBeanExport = AnnotationAttributes.fromMap(map);
/*  70 */     if (this.enableMBeanExport == null) {
/*  71 */       throw new IllegalArgumentException("@EnableMBeanExport is not present on importing class " + importMetadata
/*  72 */           .getClassName());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void setEnvironment(Environment environment) {
/*  78 */     this.environment = environment;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBeanFactory(BeanFactory beanFactory) {
/*  83 */     this.beanFactory = beanFactory;
/*     */   }
/*     */ 
/*     */   
/*     */   @Bean(name = {"mbeanExporter"})
/*     */   @Role(2)
/*     */   public AnnotationMBeanExporter mbeanExporter() {
/*  90 */     AnnotationMBeanExporter exporter = new AnnotationMBeanExporter();
/*  91 */     Assert.state((this.enableMBeanExport != null), "No EnableMBeanExport annotation found");
/*  92 */     setupDomain(exporter, this.enableMBeanExport);
/*  93 */     setupServer(exporter, this.enableMBeanExport);
/*  94 */     setupRegistrationPolicy(exporter, this.enableMBeanExport);
/*  95 */     return exporter;
/*     */   }
/*     */   
/*     */   private void setupDomain(AnnotationMBeanExporter exporter, AnnotationAttributes enableMBeanExport) {
/*  99 */     String defaultDomain = enableMBeanExport.getString("defaultDomain");
/* 100 */     if (StringUtils.hasLength(defaultDomain) && this.environment != null) {
/* 101 */       defaultDomain = this.environment.resolvePlaceholders(defaultDomain);
/*     */     }
/* 103 */     if (StringUtils.hasText(defaultDomain)) {
/* 104 */       exporter.setDefaultDomain(defaultDomain);
/*     */     }
/*     */   }
/*     */   
/*     */   private void setupServer(AnnotationMBeanExporter exporter, AnnotationAttributes enableMBeanExport) {
/* 109 */     String server = enableMBeanExport.getString("server");
/* 110 */     if (StringUtils.hasLength(server) && this.environment != null) {
/* 111 */       server = this.environment.resolvePlaceholders(server);
/*     */     }
/* 113 */     if (StringUtils.hasText(server)) {
/* 114 */       Assert.state((this.beanFactory != null), "No BeanFactory set");
/* 115 */       exporter.setServer((MBeanServer)this.beanFactory.getBean(server, MBeanServer.class));
/*     */     } else {
/*     */       
/* 118 */       SpecificPlatform specificPlatform = SpecificPlatform.get();
/* 119 */       if (specificPlatform != null) {
/* 120 */         MBeanServer mbeanServer = specificPlatform.getMBeanServer();
/* 121 */         if (mbeanServer != null) {
/* 122 */           exporter.setServer(mbeanServer);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void setupRegistrationPolicy(AnnotationMBeanExporter exporter, AnnotationAttributes enableMBeanExport) {
/* 129 */     RegistrationPolicy registrationPolicy = (RegistrationPolicy)enableMBeanExport.getEnum("registration");
/* 130 */     exporter.setRegistrationPolicy(registrationPolicy);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public enum SpecificPlatform
/*     */   {
/* 142 */     WEBLOGIC("weblogic.management.Helper")
/*     */     {
/*     */       public MBeanServer getMBeanServer() {
/*     */         try {
/* 146 */           return (MBeanServer)(new JndiLocatorDelegate()).lookup("java:comp/env/jmx/runtime", MBeanServer.class);
/*     */         }
/* 148 */         catch (NamingException ex) {
/* 149 */           throw new MBeanServerNotFoundException("Failed to retrieve WebLogic MBeanServer from JNDI", ex);
/*     */ 
/*     */ 
/*     */         
/*     */         }
/*     */       
/*     */       }
/*     */     },
/* 157 */     WEBSPHERE("com.ibm.websphere.management.AdminServiceFactory")
/*     */     {
/*     */       public MBeanServer getMBeanServer() {
/* 160 */         WebSphereMBeanServerFactoryBean fb = new WebSphereMBeanServerFactoryBean();
/* 161 */         fb.afterPropertiesSet();
/* 162 */         return fb.getObject();
/*     */       }
/*     */     };
/*     */     
/*     */     private final String identifyingClass;
/*     */     
/*     */     SpecificPlatform(String identifyingClass) {
/* 169 */       this.identifyingClass = identifyingClass;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public static SpecificPlatform get() {
/* 177 */       ClassLoader classLoader = MBeanExportConfiguration.class.getClassLoader();
/* 178 */       for (SpecificPlatform environment : values()) {
/* 179 */         if (ClassUtils.isPresent(environment.identifyingClass, classLoader)) {
/* 180 */           return environment;
/*     */         }
/*     */       } 
/* 183 */       return null;
/*     */     }
/*     */     
/*     */     @Nullable
/*     */     public abstract MBeanServer getMBeanServer();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/annotation/MBeanExportConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */