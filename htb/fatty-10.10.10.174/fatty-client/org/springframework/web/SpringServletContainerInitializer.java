/*     */ package org.springframework.web;
/*     */ 
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import javax.servlet.ServletContainerInitializer;
/*     */ import javax.servlet.ServletContext;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.annotation.HandlesTypes;
/*     */ import org.springframework.core.annotation.AnnotationAwareOrderComparator;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.ReflectionUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @HandlesTypes({WebApplicationInitializer.class})
/*     */ public class SpringServletContainerInitializer
/*     */   implements ServletContainerInitializer
/*     */ {
/*     */   public void onStartup(@Nullable Set<Class<?>> webAppInitializerClasses, ServletContext servletContext) throws ServletException {
/* 144 */     List<WebApplicationInitializer> initializers = new LinkedList<>();
/*     */     
/* 146 */     if (webAppInitializerClasses != null) {
/* 147 */       for (Class<?> waiClass : webAppInitializerClasses) {
/*     */ 
/*     */         
/* 150 */         if (!waiClass.isInterface() && !Modifier.isAbstract(waiClass.getModifiers()) && WebApplicationInitializer.class
/* 151 */           .isAssignableFrom(waiClass)) {
/*     */           try {
/* 153 */             initializers.add(
/* 154 */                 ReflectionUtils.accessibleConstructor(waiClass, new Class[0]).newInstance(new Object[0]));
/*     */           }
/* 156 */           catch (Throwable ex) {
/* 157 */             throw new ServletException("Failed to instantiate WebApplicationInitializer class", ex);
/*     */           } 
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/* 163 */     if (initializers.isEmpty()) {
/* 164 */       servletContext.log("No Spring WebApplicationInitializer types detected on classpath");
/*     */       
/*     */       return;
/*     */     } 
/* 168 */     servletContext.log(initializers.size() + " Spring WebApplicationInitializers detected on classpath");
/* 169 */     AnnotationAwareOrderComparator.sort(initializers);
/* 170 */     for (WebApplicationInitializer initializer : initializers)
/* 171 */       initializer.onStartup(servletContext); 
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/SpringServletContainerInitializer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */