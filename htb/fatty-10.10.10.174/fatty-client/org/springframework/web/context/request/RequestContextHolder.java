/*     */ package org.springframework.web.context.request;
/*     */ 
/*     */ import javax.faces.context.FacesContext;
/*     */ import org.springframework.core.NamedInheritableThreadLocal;
/*     */ import org.springframework.core.NamedThreadLocal;
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
/*     */ public abstract class RequestContextHolder
/*     */ {
/*  48 */   private static final boolean jsfPresent = ClassUtils.isPresent("javax.faces.context.FacesContext", RequestContextHolder.class.getClassLoader());
/*     */   
/*  50 */   private static final ThreadLocal<RequestAttributes> requestAttributesHolder = (ThreadLocal<RequestAttributes>)new NamedThreadLocal("Request attributes");
/*     */ 
/*     */   
/*  53 */   private static final ThreadLocal<RequestAttributes> inheritableRequestAttributesHolder = (ThreadLocal<RequestAttributes>)new NamedInheritableThreadLocal("Request context");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void resetRequestAttributes() {
/*  61 */     requestAttributesHolder.remove();
/*  62 */     inheritableRequestAttributesHolder.remove();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setRequestAttributes(@Nullable RequestAttributes attributes) {
/*  72 */     setRequestAttributes(attributes, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setRequestAttributes(@Nullable RequestAttributes attributes, boolean inheritable) {
/*  83 */     if (attributes == null) {
/*  84 */       resetRequestAttributes();
/*     */     
/*     */     }
/*  87 */     else if (inheritable) {
/*  88 */       inheritableRequestAttributesHolder.set(attributes);
/*  89 */       requestAttributesHolder.remove();
/*     */     } else {
/*     */       
/*  92 */       requestAttributesHolder.set(attributes);
/*  93 */       inheritableRequestAttributesHolder.remove();
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
/*     */   public static RequestAttributes getRequestAttributes() {
/* 105 */     RequestAttributes attributes = requestAttributesHolder.get();
/* 106 */     if (attributes == null) {
/* 107 */       attributes = inheritableRequestAttributesHolder.get();
/*     */     }
/* 109 */     return attributes;
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
/*     */   public static RequestAttributes currentRequestAttributes() throws IllegalStateException {
/* 125 */     RequestAttributes attributes = getRequestAttributes();
/* 126 */     if (attributes == null) {
/* 127 */       if (jsfPresent) {
/* 128 */         attributes = FacesRequestAttributesFactory.getFacesRequestAttributes();
/*     */       }
/* 130 */       if (attributes == null) {
/* 131 */         throw new IllegalStateException("No thread-bound request found: Are you referring to request attributes outside of an actual web request, or processing a request outside of the originally receiving thread? If you are actually operating within a web request and still receive this message, your code is probably running outside of DispatcherServlet: In this case, use RequestContextListener or RequestContextFilter to expose the current request.");
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 139 */     return attributes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class FacesRequestAttributesFactory
/*     */   {
/*     */     @Nullable
/*     */     public static RequestAttributes getFacesRequestAttributes() {
/* 150 */       FacesContext facesContext = FacesContext.getCurrentInstance();
/* 151 */       return (facesContext != null) ? new FacesRequestAttributes(facesContext) : null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/context/request/RequestContextHolder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */