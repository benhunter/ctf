/*    */ package org.springframework.http.codec;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import java.util.Properties;
/*    */ import org.springframework.beans.BeanUtils;
/*    */ import org.springframework.core.io.ClassPathResource;
/*    */ import org.springframework.core.io.Resource;
/*    */ import org.springframework.core.io.support.PropertiesLoaderUtils;
/*    */ import org.springframework.util.ClassUtils;
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
/*    */ final class CodecConfigurerFactory
/*    */ {
/*    */   private static final String DEFAULT_CONFIGURERS_PATH = "CodecConfigurer.properties";
/* 43 */   private static final Map<Class<?>, Class<?>> defaultCodecConfigurers = new HashMap<>(4);
/*    */   
/*    */   static {
/*    */     try {
/* 47 */       Properties props = PropertiesLoaderUtils.loadProperties((Resource)new ClassPathResource("CodecConfigurer.properties", CodecConfigurerFactory.class));
/*    */       
/* 49 */       for (String ifcName : props.stringPropertyNames()) {
/* 50 */         String implName = props.getProperty(ifcName);
/* 51 */         Class<?> ifc = ClassUtils.forName(ifcName, CodecConfigurerFactory.class.getClassLoader());
/* 52 */         Class<?> impl = ClassUtils.forName(implName, CodecConfigurerFactory.class.getClassLoader());
/* 53 */         defaultCodecConfigurers.put(ifc, impl);
/*    */       }
/*    */     
/* 56 */     } catch (IOException|ClassNotFoundException ex) {
/* 57 */       throw new IllegalStateException(ex);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static <T extends CodecConfigurer> T create(Class<T> ifc) {
/* 68 */     Class<?> impl = defaultCodecConfigurers.get(ifc);
/* 69 */     if (impl == null) {
/* 70 */       throw new IllegalStateException("No default codec configurer found for " + ifc);
/*    */     }
/* 72 */     return (T)BeanUtils.instantiateClass(impl);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/codec/CodecConfigurerFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */