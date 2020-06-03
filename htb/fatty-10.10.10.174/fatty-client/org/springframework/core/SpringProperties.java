/*     */ package org.springframework.core;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.URL;
/*     */ import java.util.Properties;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class SpringProperties
/*     */ {
/*     */   private static final String PROPERTIES_RESOURCE_LOCATION = "spring.properties";
/*  53 */   private static final Log logger = LogFactory.getLog(SpringProperties.class);
/*     */   
/*  55 */   private static final Properties localProperties = new Properties();
/*     */ 
/*     */   
/*     */   static {
/*     */     try {
/*  60 */       ClassLoader cl = SpringProperties.class.getClassLoader();
/*     */       
/*  62 */       URL url = (cl != null) ? cl.getResource("spring.properties") : ClassLoader.getSystemResource("spring.properties");
/*  63 */       if (url != null) {
/*  64 */         logger.debug("Found 'spring.properties' file in local classpath");
/*  65 */         InputStream is = url.openStream();
/*     */         try {
/*  67 */           localProperties.load(is);
/*     */         } finally {
/*     */           
/*  70 */           is.close();
/*     */         }
/*     */       
/*     */       } 
/*  74 */     } catch (IOException ex) {
/*  75 */       if (logger.isInfoEnabled()) {
/*  76 */         logger.info("Could not load 'spring.properties' file from local classpath: " + ex);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setProperty(String key, @Nullable String value) {
/*  93 */     if (value != null) {
/*  94 */       localProperties.setProperty(key, value);
/*     */     } else {
/*     */       
/*  97 */       localProperties.remove(key);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public static String getProperty(String key) {
/* 109 */     String value = localProperties.getProperty(key);
/* 110 */     if (value == null) {
/*     */       try {
/* 112 */         value = System.getProperty(key);
/*     */       }
/* 114 */       catch (Throwable ex) {
/* 115 */         if (logger.isDebugEnabled()) {
/* 116 */           logger.debug("Could not retrieve system property '" + key + "': " + ex);
/*     */         }
/*     */       } 
/*     */     }
/* 120 */     return value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setFlag(String key) {
/* 129 */     localProperties.put(key, Boolean.TRUE.toString());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean getFlag(String key) {
/* 139 */     return Boolean.parseBoolean(getProperty(key));
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/SpringProperties.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */