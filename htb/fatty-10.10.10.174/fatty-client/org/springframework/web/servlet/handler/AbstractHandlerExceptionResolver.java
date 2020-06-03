/*     */ package org.springframework.web.servlet.handler;
/*     */ 
/*     */ import java.util.Set;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.core.Ordered;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.servlet.HandlerExceptionResolver;
/*     */ import org.springframework.web.servlet.ModelAndView;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractHandlerExceptionResolver
/*     */   implements HandlerExceptionResolver, Ordered
/*     */ {
/*     */   private static final String HEADER_CACHE_CONTROL = "Cache-Control";
/*  50 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/*  52 */   private int order = Integer.MAX_VALUE;
/*     */   
/*     */   @Nullable
/*     */   private Set<?> mappedHandlers;
/*     */   
/*     */   @Nullable
/*     */   private Class<?>[] mappedHandlerClasses;
/*     */   
/*     */   @Nullable
/*     */   private Log warnLogger;
/*     */   
/*     */   private boolean preventResponseCaching = false;
/*     */ 
/*     */   
/*     */   public void setOrder(int order) {
/*  67 */     this.order = order;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getOrder() {
/*  72 */     return this.order;
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
/*     */   public void setMappedHandlers(Set<?> mappedHandlers) {
/*  84 */     this.mappedHandlers = mappedHandlers;
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
/*     */   public void setMappedHandlerClasses(Class<?>... mappedHandlerClasses) {
/*  97 */     this.mappedHandlerClasses = mappedHandlerClasses;
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
/*     */   public void setWarnLogCategory(String loggerName) {
/* 113 */     this.warnLogger = StringUtils.hasLength(loggerName) ? LogFactory.getLog(loggerName) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPreventResponseCaching(boolean preventResponseCaching) {
/* 123 */     this.preventResponseCaching = preventResponseCaching;
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
/*     */   @Nullable
/*     */   public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, @Nullable Object handler, Exception ex) {
/* 138 */     if (shouldApplyTo(request, handler)) {
/* 139 */       prepareResponse(ex, response);
/* 140 */       ModelAndView result = doResolveException(request, response, handler, ex);
/* 141 */       if (result != null) {
/*     */         
/* 143 */         if (this.logger.isDebugEnabled() && (this.warnLogger == null || !this.warnLogger.isWarnEnabled())) {
/* 144 */           this.logger.debug("Resolved [" + ex + "]" + (result.isEmpty() ? "" : (" to " + result)));
/*     */         }
/*     */         
/* 147 */         logException(ex, request);
/*     */       } 
/* 149 */       return result;
/*     */     } 
/*     */     
/* 152 */     return null;
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
/*     */ 
/*     */   
/*     */   protected boolean shouldApplyTo(HttpServletRequest request, @Nullable Object handler) {
/* 170 */     if (handler != null) {
/* 171 */       if (this.mappedHandlers != null && this.mappedHandlers.contains(handler)) {
/* 172 */         return true;
/*     */       }
/* 174 */       if (this.mappedHandlerClasses != null) {
/* 175 */         for (Class<?> handlerClass : this.mappedHandlerClasses) {
/* 176 */           if (handlerClass.isInstance(handler)) {
/* 177 */             return true;
/*     */           }
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 183 */     return (this.mappedHandlers == null && this.mappedHandlerClasses == null);
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
/*     */   protected void logException(Exception ex, HttpServletRequest request) {
/* 197 */     if (this.warnLogger != null && this.warnLogger.isWarnEnabled()) {
/* 198 */       this.warnLogger.warn(buildLogMessage(ex, request));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String buildLogMessage(Exception ex, HttpServletRequest request) {
/* 209 */     return "Resolved [" + ex + "]";
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
/*     */   protected void prepareResponse(Exception ex, HttpServletResponse response) {
/* 222 */     if (this.preventResponseCaching) {
/* 223 */       preventCaching(response);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void preventCaching(HttpServletResponse response) {
/* 233 */     response.addHeader("Cache-Control", "no-store");
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   protected abstract ModelAndView doResolveException(HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse, @Nullable Object paramObject, Exception paramException);
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/handler/AbstractHandlerExceptionResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */