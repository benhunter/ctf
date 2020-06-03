/*     */ package org.springframework.web.servlet.mvc.support;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.ConversionNotSupportedException;
/*     */ import org.springframework.beans.TypeMismatchException;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.http.converter.HttpMessageNotReadableException;
/*     */ import org.springframework.http.converter.HttpMessageNotWritableException;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.validation.BindException;
/*     */ import org.springframework.web.HttpMediaTypeNotAcceptableException;
/*     */ import org.springframework.web.HttpMediaTypeNotSupportedException;
/*     */ import org.springframework.web.HttpRequestMethodNotSupportedException;
/*     */ import org.springframework.web.bind.MethodArgumentNotValidException;
/*     */ import org.springframework.web.bind.MissingPathVariableException;
/*     */ import org.springframework.web.bind.MissingServletRequestParameterException;
/*     */ import org.springframework.web.bind.ServletRequestBindingException;
/*     */ import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
/*     */ import org.springframework.web.multipart.support.MissingServletRequestPartException;
/*     */ import org.springframework.web.servlet.ModelAndView;
/*     */ import org.springframework.web.servlet.NoHandlerFoundException;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultHandlerExceptionResolver
/*     */   extends AbstractHandlerExceptionResolver
/*     */ {
/*     */   public static final String PAGE_NOT_FOUND_LOG_CATEGORY = "org.springframework.web.servlet.PageNotFound";
/* 154 */   protected static final Log pageNotFoundLogger = LogFactory.getLog("org.springframework.web.servlet.PageNotFound");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultHandlerExceptionResolver() {
/* 161 */     setOrder(2147483647);
/* 162 */     setWarnLogCategory(getClass().getName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, @Nullable Object handler, Exception ex) {
/*     */     try {
/* 172 */       if (ex instanceof HttpRequestMethodNotSupportedException) {
/* 173 */         return handleHttpRequestMethodNotSupported((HttpRequestMethodNotSupportedException)ex, request, response, handler);
/*     */       }
/*     */       
/* 176 */       if (ex instanceof HttpMediaTypeNotSupportedException) {
/* 177 */         return handleHttpMediaTypeNotSupported((HttpMediaTypeNotSupportedException)ex, request, response, handler);
/*     */       }
/*     */       
/* 180 */       if (ex instanceof HttpMediaTypeNotAcceptableException) {
/* 181 */         return handleHttpMediaTypeNotAcceptable((HttpMediaTypeNotAcceptableException)ex, request, response, handler);
/*     */       }
/*     */       
/* 184 */       if (ex instanceof MissingPathVariableException) {
/* 185 */         return handleMissingPathVariable((MissingPathVariableException)ex, request, response, handler);
/*     */       }
/*     */       
/* 188 */       if (ex instanceof MissingServletRequestParameterException) {
/* 189 */         return handleMissingServletRequestParameter((MissingServletRequestParameterException)ex, request, response, handler);
/*     */       }
/*     */       
/* 192 */       if (ex instanceof ServletRequestBindingException) {
/* 193 */         return handleServletRequestBindingException((ServletRequestBindingException)ex, request, response, handler);
/*     */       }
/*     */       
/* 196 */       if (ex instanceof ConversionNotSupportedException) {
/* 197 */         return handleConversionNotSupported((ConversionNotSupportedException)ex, request, response, handler);
/*     */       }
/*     */       
/* 200 */       if (ex instanceof TypeMismatchException) {
/* 201 */         return handleTypeMismatch((TypeMismatchException)ex, request, response, handler);
/*     */       }
/*     */       
/* 204 */       if (ex instanceof HttpMessageNotReadableException) {
/* 205 */         return handleHttpMessageNotReadable((HttpMessageNotReadableException)ex, request, response, handler);
/*     */       }
/*     */       
/* 208 */       if (ex instanceof HttpMessageNotWritableException) {
/* 209 */         return handleHttpMessageNotWritable((HttpMessageNotWritableException)ex, request, response, handler);
/*     */       }
/*     */       
/* 212 */       if (ex instanceof MethodArgumentNotValidException) {
/* 213 */         return handleMethodArgumentNotValidException((MethodArgumentNotValidException)ex, request, response, handler);
/*     */       }
/*     */       
/* 216 */       if (ex instanceof MissingServletRequestPartException) {
/* 217 */         return handleMissingServletRequestPartException((MissingServletRequestPartException)ex, request, response, handler);
/*     */       }
/*     */       
/* 220 */       if (ex instanceof BindException) {
/* 221 */         return handleBindException((BindException)ex, request, response, handler);
/*     */       }
/* 223 */       if (ex instanceof NoHandlerFoundException) {
/* 224 */         return handleNoHandlerFoundException((NoHandlerFoundException)ex, request, response, handler);
/*     */       }
/*     */       
/* 227 */       if (ex instanceof AsyncRequestTimeoutException) {
/* 228 */         return handleAsyncRequestTimeoutException((AsyncRequestTimeoutException)ex, request, response, handler);
/*     */       
/*     */       }
/*     */     }
/* 232 */     catch (Exception handlerEx) {
/* 233 */       if (this.logger.isWarnEnabled()) {
/* 234 */         this.logger.warn("Failure while trying to resolve exception [" + ex.getClass().getName() + "]", handlerEx);
/*     */       }
/*     */     } 
/* 237 */     return null;
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
/*     */   
/*     */   protected ModelAndView handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpServletRequest request, HttpServletResponse response, @Nullable Object handler) throws IOException {
/* 256 */     String[] supportedMethods = ex.getSupportedMethods();
/* 257 */     if (supportedMethods != null) {
/* 258 */       response.setHeader("Allow", StringUtils.arrayToDelimitedString((Object[])supportedMethods, ", "));
/*     */     }
/* 260 */     response.sendError(405, ex.getMessage());
/* 261 */     return new ModelAndView();
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
/*     */   
/*     */   protected ModelAndView handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpServletRequest request, HttpServletResponse response, @Nullable Object handler) throws IOException {
/* 280 */     response.sendError(415);
/* 281 */     List<MediaType> mediaTypes = ex.getSupportedMediaTypes();
/* 282 */     if (!CollectionUtils.isEmpty(mediaTypes)) {
/* 283 */       response.setHeader("Accept", MediaType.toString(mediaTypes));
/*     */     }
/* 285 */     return new ModelAndView();
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
/*     */   
/*     */   protected ModelAndView handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex, HttpServletRequest request, HttpServletResponse response, @Nullable Object handler) throws IOException {
/* 304 */     response.sendError(406);
/* 305 */     return new ModelAndView();
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
/*     */   
/*     */   protected ModelAndView handleMissingPathVariable(MissingPathVariableException ex, HttpServletRequest request, HttpServletResponse response, @Nullable Object handler) throws IOException {
/* 324 */     response.sendError(500, ex.getMessage());
/* 325 */     return new ModelAndView();
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
/*     */   protected ModelAndView handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpServletRequest request, HttpServletResponse response, @Nullable Object handler) throws IOException {
/* 343 */     response.sendError(400, ex.getMessage());
/* 344 */     return new ModelAndView();
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
/*     */   protected ModelAndView handleServletRequestBindingException(ServletRequestBindingException ex, HttpServletRequest request, HttpServletResponse response, @Nullable Object handler) throws IOException {
/* 361 */     response.sendError(400, ex.getMessage());
/* 362 */     return new ModelAndView();
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
/*     */   protected ModelAndView handleConversionNotSupported(ConversionNotSupportedException ex, HttpServletRequest request, HttpServletResponse response, @Nullable Object handler) throws IOException {
/* 380 */     sendServerError((Exception)ex, request, response);
/* 381 */     return new ModelAndView();
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
/*     */   protected ModelAndView handleTypeMismatch(TypeMismatchException ex, HttpServletRequest request, HttpServletResponse response, @Nullable Object handler) throws IOException {
/* 398 */     response.sendError(400);
/* 399 */     return new ModelAndView();
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
/*     */   
/*     */   protected ModelAndView handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpServletRequest request, HttpServletResponse response, @Nullable Object handler) throws IOException {
/* 418 */     response.sendError(400);
/* 419 */     return new ModelAndView();
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
/*     */ 
/*     */   
/*     */   protected ModelAndView handleHttpMessageNotWritable(HttpMessageNotWritableException ex, HttpServletRequest request, HttpServletResponse response, @Nullable Object handler) throws IOException {
/* 439 */     sendServerError((Exception)ex, request, response);
/* 440 */     return new ModelAndView();
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
/*     */   protected ModelAndView handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request, HttpServletResponse response, @Nullable Object handler) throws IOException {
/* 456 */     response.sendError(400);
/* 457 */     return new ModelAndView();
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
/*     */   protected ModelAndView handleMissingServletRequestPartException(MissingServletRequestPartException ex, HttpServletRequest request, HttpServletResponse response, @Nullable Object handler) throws IOException {
/* 473 */     response.sendError(400, ex.getMessage());
/* 474 */     return new ModelAndView();
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
/*     */   protected ModelAndView handleBindException(BindException ex, HttpServletRequest request, HttpServletResponse response, @Nullable Object handler) throws IOException {
/* 491 */     response.sendError(400);
/* 492 */     return new ModelAndView();
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
/*     */ 
/*     */   
/*     */   protected ModelAndView handleNoHandlerFoundException(NoHandlerFoundException ex, HttpServletRequest request, HttpServletResponse response, @Nullable Object handler) throws IOException {
/* 512 */     pageNotFoundLogger.warn(ex.getMessage());
/* 513 */     response.sendError(404);
/* 514 */     return new ModelAndView();
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
/*     */   protected ModelAndView handleAsyncRequestTimeoutException(AsyncRequestTimeoutException ex, HttpServletRequest request, HttpServletResponse response, @Nullable Object handler) throws IOException {
/* 532 */     if (!response.isCommitted()) {
/* 533 */       response.sendError(503);
/*     */     } else {
/*     */       
/* 536 */       this.logger.warn("Async request timed out");
/*     */     } 
/* 538 */     return new ModelAndView();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void sendServerError(Exception ex, HttpServletRequest request, HttpServletResponse response) throws IOException {
/* 548 */     request.setAttribute("javax.servlet.error.exception", ex);
/* 549 */     response.sendError(500);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/mvc/support/DefaultHandlerExceptionResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */