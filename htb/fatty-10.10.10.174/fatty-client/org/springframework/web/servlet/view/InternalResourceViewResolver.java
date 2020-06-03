/*     */ package org.springframework.web.servlet.view;
/*     */ 
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.ClassUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class InternalResourceViewResolver
/*     */   extends UrlBasedViewResolver
/*     */ {
/*  51 */   private static final boolean jstlPresent = ClassUtils.isPresent("javax.servlet.jsp.jstl.core.Config", InternalResourceViewResolver.class
/*  52 */       .getClassLoader());
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private Boolean alwaysInclude;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InternalResourceViewResolver() {
/*  64 */     Class<?> viewClass = requiredViewClass();
/*  65 */     if (InternalResourceView.class == viewClass && jstlPresent) {
/*  66 */       viewClass = JstlView.class;
/*     */     }
/*  68 */     setViewClass(viewClass);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InternalResourceViewResolver(String prefix, String suffix) {
/*  79 */     this();
/*  80 */     setPrefix(prefix);
/*  81 */     setSuffix(suffix);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Class<?> requiredViewClass() {
/*  90 */     return InternalResourceView.class;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAlwaysInclude(boolean alwaysInclude) {
/* 100 */     this.alwaysInclude = Boolean.valueOf(alwaysInclude);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected AbstractUrlBasedView buildView(String viewName) throws Exception {
/* 106 */     InternalResourceView view = (InternalResourceView)super.buildView(viewName);
/* 107 */     if (this.alwaysInclude != null) {
/* 108 */       view.setAlwaysInclude(this.alwaysInclude.booleanValue());
/*     */     }
/* 110 */     view.setPreventDispatchLoop(true);
/* 111 */     return view;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/view/InternalResourceViewResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */