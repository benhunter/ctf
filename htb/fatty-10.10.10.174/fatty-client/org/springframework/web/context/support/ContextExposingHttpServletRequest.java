/*     */ package org.springframework.web.context.support;
/*     */ 
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletRequestWrapper;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.web.context.WebApplicationContext;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ContextExposingHttpServletRequest
/*     */   extends HttpServletRequestWrapper
/*     */ {
/*     */   private final WebApplicationContext webApplicationContext;
/*     */   @Nullable
/*     */   private final Set<String> exposedContextBeanNames;
/*     */   @Nullable
/*     */   private Set<String> explicitAttributes;
/*     */   
/*     */   public ContextExposingHttpServletRequest(HttpServletRequest originalRequest, WebApplicationContext context) {
/*  53 */     this(originalRequest, context, null);
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
/*     */   public ContextExposingHttpServletRequest(HttpServletRequest originalRequest, WebApplicationContext context, @Nullable Set<String> exposedContextBeanNames) {
/*  67 */     super(originalRequest);
/*  68 */     Assert.notNull(context, "WebApplicationContext must not be null");
/*  69 */     this.webApplicationContext = context;
/*  70 */     this.exposedContextBeanNames = exposedContextBeanNames;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final WebApplicationContext getWebApplicationContext() {
/*  78 */     return this.webApplicationContext;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object getAttribute(String name) {
/*  85 */     if ((this.explicitAttributes == null || !this.explicitAttributes.contains(name)) && (this.exposedContextBeanNames == null || this.exposedContextBeanNames
/*  86 */       .contains(name)) && this.webApplicationContext
/*  87 */       .containsBean(name)) {
/*  88 */       return this.webApplicationContext.getBean(name);
/*     */     }
/*     */     
/*  91 */     return super.getAttribute(name);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAttribute(String name, Object value) {
/*  97 */     super.setAttribute(name, value);
/*  98 */     if (this.explicitAttributes == null) {
/*  99 */       this.explicitAttributes = new HashSet<>(8);
/*     */     }
/* 101 */     this.explicitAttributes.add(name);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/context/support/ContextExposingHttpServletRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */