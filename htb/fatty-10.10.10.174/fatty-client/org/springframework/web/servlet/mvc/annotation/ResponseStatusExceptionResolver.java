/*     */ package org.springframework.web.servlet.mvc.annotation;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.springframework.context.MessageSource;
/*     */ import org.springframework.context.MessageSourceAware;
/*     */ import org.springframework.context.i18n.LocaleContextHolder;
/*     */ import org.springframework.core.annotation.AnnotatedElementUtils;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.bind.annotation.ResponseStatus;
/*     */ import org.springframework.web.server.ResponseStatusException;
/*     */ import org.springframework.web.servlet.ModelAndView;
/*     */ import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ResponseStatusExceptionResolver
/*     */   extends AbstractHandlerExceptionResolver
/*     */   implements MessageSourceAware
/*     */ {
/*     */   @Nullable
/*     */   private MessageSource messageSource;
/*     */   
/*     */   public void setMessageSource(MessageSource messageSource) {
/*  64 */     this.messageSource = messageSource;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, @Nullable Object handler, Exception ex) {
/*     */     try {
/*  74 */       if (ex instanceof ResponseStatusException) {
/*  75 */         return resolveResponseStatusException((ResponseStatusException)ex, request, response, handler);
/*     */       }
/*     */       
/*  78 */       ResponseStatus status = (ResponseStatus)AnnotatedElementUtils.findMergedAnnotation(ex.getClass(), ResponseStatus.class);
/*  79 */       if (status != null) {
/*  80 */         return resolveResponseStatus(status, request, response, handler, ex);
/*     */       }
/*     */       
/*  83 */       if (ex.getCause() instanceof Exception) {
/*  84 */         return doResolveException(request, response, handler, (Exception)ex.getCause());
/*     */       }
/*     */     }
/*  87 */     catch (Exception resolveEx) {
/*  88 */       if (this.logger.isWarnEnabled()) {
/*  89 */         this.logger.warn("Failure while trying to resolve exception [" + ex.getClass().getName() + "]", resolveEx);
/*     */       }
/*     */     } 
/*  92 */     return null;
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
/*     */   protected ModelAndView resolveResponseStatus(ResponseStatus responseStatus, HttpServletRequest request, HttpServletResponse response, @Nullable Object handler, Exception ex) throws Exception {
/* 110 */     int statusCode = responseStatus.code().value();
/* 111 */     String reason = responseStatus.reason();
/* 112 */     return applyStatusAndReason(statusCode, reason, response);
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
/*     */   protected ModelAndView resolveResponseStatusException(ResponseStatusException ex, HttpServletRequest request, HttpServletResponse response, @Nullable Object handler) throws Exception {
/* 130 */     int statusCode = ex.getStatus().value();
/* 131 */     String reason = ex.getReason();
/* 132 */     return applyStatusAndReason(statusCode, reason, response);
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
/*     */   protected ModelAndView applyStatusAndReason(int statusCode, @Nullable String reason, HttpServletResponse response) throws IOException {
/* 149 */     if (!StringUtils.hasLength(reason)) {
/* 150 */       response.sendError(statusCode);
/*     */     }
/*     */     else {
/*     */       
/* 154 */       String resolvedReason = (this.messageSource != null) ? this.messageSource.getMessage(reason, null, reason, LocaleContextHolder.getLocale()) : reason;
/*     */       
/* 156 */       response.sendError(statusCode, resolvedReason);
/*     */     } 
/* 158 */     return new ModelAndView();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/mvc/annotation/ResponseStatusExceptionResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */