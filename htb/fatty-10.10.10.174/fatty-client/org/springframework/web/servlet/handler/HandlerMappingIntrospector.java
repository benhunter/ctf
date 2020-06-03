/*     */ package org.springframework.web.servlet.handler;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletRequestWrapper;
/*     */ import org.springframework.beans.factory.BeanFactoryUtils;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.beans.factory.ListableBeanFactory;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.context.ApplicationContextAware;
/*     */ import org.springframework.core.annotation.AnnotationAwareOrderComparator;
/*     */ import org.springframework.core.io.ClassPathResource;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.core.io.support.PropertiesLoaderUtils;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.cors.CorsConfiguration;
/*     */ import org.springframework.web.cors.CorsConfigurationSource;
/*     */ import org.springframework.web.servlet.DispatcherServlet;
/*     */ import org.springframework.web.servlet.HandlerExecutionChain;
/*     */ import org.springframework.web.servlet.HandlerInterceptor;
/*     */ import org.springframework.web.servlet.HandlerMapping;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HandlerMappingIntrospector
/*     */   implements CorsConfigurationSource, ApplicationContextAware, InitializingBean
/*     */ {
/*     */   @Nullable
/*     */   private ApplicationContext applicationContext;
/*     */   @Nullable
/*     */   private List<HandlerMapping> handlerMappings;
/*     */   
/*     */   public HandlerMappingIntrospector() {}
/*     */   
/*     */   @Deprecated
/*     */   public HandlerMappingIntrospector(ApplicationContext context) {
/*  86 */     this.handlerMappings = initHandlerMappings(context);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<HandlerMapping> getHandlerMappings() {
/*  94 */     return (this.handlerMappings != null) ? this.handlerMappings : Collections.<HandlerMapping>emptyList();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setApplicationContext(ApplicationContext applicationContext) {
/* 100 */     this.applicationContext = applicationContext;
/*     */   }
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() {
/* 105 */     if (this.handlerMappings == null) {
/* 106 */       Assert.notNull(this.applicationContext, "No ApplicationContext");
/* 107 */       this.handlerMappings = initHandlerMappings(this.applicationContext);
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
/*     */   @Nullable
/*     */   public MatchableHandlerMapping getMatchableHandlerMapping(HttpServletRequest request) throws Exception {
/* 124 */     Assert.notNull(this.handlerMappings, "Handler mappings not initialized");
/* 125 */     RequestAttributeChangeIgnoringWrapper requestAttributeChangeIgnoringWrapper = new RequestAttributeChangeIgnoringWrapper(request);
/* 126 */     for (HandlerMapping handlerMapping : this.handlerMappings) {
/* 127 */       Object handler = handlerMapping.getHandler((HttpServletRequest)requestAttributeChangeIgnoringWrapper);
/* 128 */       if (handler == null) {
/*     */         continue;
/*     */       }
/* 131 */       if (handlerMapping instanceof MatchableHandlerMapping) {
/* 132 */         return (MatchableHandlerMapping)handlerMapping;
/*     */       }
/* 134 */       throw new IllegalStateException("HandlerMapping is not a MatchableHandlerMapping");
/*     */     } 
/* 136 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
/* 142 */     Assert.notNull(this.handlerMappings, "Handler mappings not initialized");
/* 143 */     RequestAttributeChangeIgnoringWrapper requestAttributeChangeIgnoringWrapper = new RequestAttributeChangeIgnoringWrapper(request);
/* 144 */     for (HandlerMapping handlerMapping : this.handlerMappings) {
/* 145 */       HandlerExecutionChain handler = null;
/*     */       try {
/* 147 */         handler = handlerMapping.getHandler((HttpServletRequest)requestAttributeChangeIgnoringWrapper);
/*     */       }
/* 149 */       catch (Exception exception) {}
/*     */ 
/*     */       
/* 152 */       if (handler == null) {
/*     */         continue;
/*     */       }
/* 155 */       if (handler.getInterceptors() != null) {
/* 156 */         for (HandlerInterceptor interceptor : handler.getInterceptors()) {
/* 157 */           if (interceptor instanceof CorsConfigurationSource) {
/* 158 */             return ((CorsConfigurationSource)interceptor).getCorsConfiguration((HttpServletRequest)requestAttributeChangeIgnoringWrapper);
/*     */           }
/*     */         } 
/*     */       }
/* 162 */       if (handler.getHandler() instanceof CorsConfigurationSource) {
/* 163 */         return ((CorsConfigurationSource)handler.getHandler()).getCorsConfiguration((HttpServletRequest)requestAttributeChangeIgnoringWrapper);
/*     */       }
/*     */     } 
/* 166 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   private static List<HandlerMapping> initHandlerMappings(ApplicationContext applicationContext) {
/* 171 */     Map<String, HandlerMapping> beans = BeanFactoryUtils.beansOfTypeIncludingAncestors((ListableBeanFactory)applicationContext, HandlerMapping.class, true, false);
/*     */     
/* 173 */     if (!beans.isEmpty()) {
/* 174 */       List<HandlerMapping> mappings = new ArrayList<>(beans.values());
/* 175 */       AnnotationAwareOrderComparator.sort(mappings);
/* 176 */       return Collections.unmodifiableList(mappings);
/*     */     } 
/* 178 */     return Collections.unmodifiableList(initFallback(applicationContext));
/*     */   }
/*     */   
/*     */   private static List<HandlerMapping> initFallback(ApplicationContext applicationContext) {
/*     */     Properties props;
/* 183 */     String path = "DispatcherServlet.properties";
/*     */     try {
/* 185 */       ClassPathResource classPathResource = new ClassPathResource(path, DispatcherServlet.class);
/* 186 */       props = PropertiesLoaderUtils.loadProperties((Resource)classPathResource);
/*     */     }
/* 188 */     catch (IOException ex) {
/* 189 */       throw new IllegalStateException("Could not load '" + path + "': " + ex.getMessage());
/*     */     } 
/*     */     
/* 192 */     String value = props.getProperty(HandlerMapping.class.getName());
/* 193 */     String[] names = StringUtils.commaDelimitedListToStringArray(value);
/* 194 */     List<HandlerMapping> result = new ArrayList<>(names.length);
/* 195 */     for (String name : names) {
/*     */       try {
/* 197 */         Class<?> clazz = ClassUtils.forName(name, DispatcherServlet.class.getClassLoader());
/* 198 */         Object mapping = applicationContext.getAutowireCapableBeanFactory().createBean(clazz);
/* 199 */         result.add((HandlerMapping)mapping);
/*     */       }
/* 201 */       catch (ClassNotFoundException ex) {
/* 202 */         throw new IllegalStateException("Could not find default HandlerMapping [" + name + "]");
/*     */       } 
/*     */     } 
/* 205 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class RequestAttributeChangeIgnoringWrapper
/*     */     extends HttpServletRequestWrapper
/*     */   {
/*     */     public RequestAttributeChangeIgnoringWrapper(HttpServletRequest request) {
/* 215 */       super(request);
/*     */     }
/*     */     
/*     */     public void setAttribute(String name, Object value) {}
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/handler/HandlerMappingIntrospector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */