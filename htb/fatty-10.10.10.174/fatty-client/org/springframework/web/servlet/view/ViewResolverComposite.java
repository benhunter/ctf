/*     */ package org.springframework.web.servlet.view;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import javax.servlet.ServletContext;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.context.ApplicationContextAware;
/*     */ import org.springframework.core.Ordered;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.web.context.ServletContextAware;
/*     */ import org.springframework.web.servlet.View;
/*     */ import org.springframework.web.servlet.ViewResolver;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ViewResolverComposite
/*     */   implements ViewResolver, Ordered, InitializingBean, ApplicationContextAware, ServletContextAware
/*     */ {
/*  46 */   private final List<ViewResolver> viewResolvers = new ArrayList<>();
/*     */   
/*  48 */   private int order = Integer.MAX_VALUE;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setViewResolvers(List<ViewResolver> viewResolvers) {
/*  55 */     this.viewResolvers.clear();
/*  56 */     if (!CollectionUtils.isEmpty(viewResolvers)) {
/*  57 */       this.viewResolvers.addAll(viewResolvers);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ViewResolver> getViewResolvers() {
/*  65 */     return Collections.unmodifiableList(this.viewResolvers);
/*     */   }
/*     */   
/*     */   public void setOrder(int order) {
/*  69 */     this.order = order;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getOrder() {
/*  74 */     return this.order;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
/*  79 */     for (ViewResolver viewResolver : this.viewResolvers) {
/*  80 */       if (viewResolver instanceof ApplicationContextAware) {
/*  81 */         ((ApplicationContextAware)viewResolver).setApplicationContext(applicationContext);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setServletContext(ServletContext servletContext) {
/*  88 */     for (ViewResolver viewResolver : this.viewResolvers) {
/*  89 */       if (viewResolver instanceof ServletContextAware) {
/*  90 */         ((ServletContextAware)viewResolver).setServletContext(servletContext);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() throws Exception {
/*  97 */     for (ViewResolver viewResolver : this.viewResolvers) {
/*  98 */       if (viewResolver instanceof InitializingBean) {
/*  99 */         ((InitializingBean)viewResolver).afterPropertiesSet();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public View resolveViewName(String viewName, Locale locale) throws Exception {
/* 107 */     for (ViewResolver viewResolver : this.viewResolvers) {
/* 108 */       View view = viewResolver.resolveViewName(viewName, locale);
/* 109 */       if (view != null) {
/* 110 */         return view;
/*     */       }
/*     */     } 
/* 113 */     return null;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/view/ViewResolverComposite.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */