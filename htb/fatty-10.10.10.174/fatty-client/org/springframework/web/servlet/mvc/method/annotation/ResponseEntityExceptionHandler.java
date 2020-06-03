/*     */ package org.springframework.web.servlet.mvc.method.annotation;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.ConversionNotSupportedException;
/*     */ import org.springframework.beans.TypeMismatchException;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.HttpMethod;
/*     */ import org.springframework.http.HttpStatus;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.http.ResponseEntity;
/*     */ import org.springframework.http.converter.HttpMessageNotReadableException;
/*     */ import org.springframework.http.converter.HttpMessageNotWritableException;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.util.MultiValueMap;
/*     */ import org.springframework.validation.BindException;
/*     */ import org.springframework.web.HttpMediaTypeNotAcceptableException;
/*     */ import org.springframework.web.HttpMediaTypeNotSupportedException;
/*     */ import org.springframework.web.HttpRequestMethodNotSupportedException;
/*     */ import org.springframework.web.bind.MethodArgumentNotValidException;
/*     */ import org.springframework.web.bind.MissingPathVariableException;
/*     */ import org.springframework.web.bind.MissingServletRequestParameterException;
/*     */ import org.springframework.web.bind.ServletRequestBindingException;
/*     */ import org.springframework.web.bind.annotation.ExceptionHandler;
/*     */ import org.springframework.web.context.request.ServletWebRequest;
/*     */ import org.springframework.web.context.request.WebRequest;
/*     */ import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
/*     */ import org.springframework.web.multipart.support.MissingServletRequestPartException;
/*     */ import org.springframework.web.servlet.NoHandlerFoundException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class ResponseEntityExceptionHandler
/*     */ {
/*     */   public static final String PAGE_NOT_FOUND_LOG_CATEGORY = "org.springframework.web.servlet.PageNotFound";
/*  92 */   protected static final Log pageNotFoundLogger = LogFactory.getLog("org.springframework.web.servlet.PageNotFound");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  97 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ExceptionHandler({HttpRequestMethodNotSupportedException.class, HttpMediaTypeNotSupportedException.class, HttpMediaTypeNotAcceptableException.class, MissingPathVariableException.class, MissingServletRequestParameterException.class, ServletRequestBindingException.class, ConversionNotSupportedException.class, TypeMismatchException.class, HttpMessageNotReadableException.class, HttpMessageNotWritableException.class, MethodArgumentNotValidException.class, MissingServletRequestPartException.class, BindException.class, NoHandlerFoundException.class, AsyncRequestTimeoutException.class})
/*     */   @Nullable
/*     */   public final ResponseEntity<Object> handleException(Exception ex, WebRequest request) throws Exception {
/* 124 */     HttpHeaders headers = new HttpHeaders();
/*     */     
/* 126 */     if (ex instanceof HttpRequestMethodNotSupportedException) {
/* 127 */       HttpStatus status = HttpStatus.METHOD_NOT_ALLOWED;
/* 128 */       return handleHttpRequestMethodNotSupported((HttpRequestMethodNotSupportedException)ex, headers, status, request);
/*     */     } 
/* 130 */     if (ex instanceof HttpMediaTypeNotSupportedException) {
/* 131 */       HttpStatus status = HttpStatus.UNSUPPORTED_MEDIA_TYPE;
/* 132 */       return handleHttpMediaTypeNotSupported((HttpMediaTypeNotSupportedException)ex, headers, status, request);
/*     */     } 
/* 134 */     if (ex instanceof HttpMediaTypeNotAcceptableException) {
/* 135 */       HttpStatus status = HttpStatus.NOT_ACCEPTABLE;
/* 136 */       return handleHttpMediaTypeNotAcceptable((HttpMediaTypeNotAcceptableException)ex, headers, status, request);
/*     */     } 
/* 138 */     if (ex instanceof MissingPathVariableException) {
/* 139 */       HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
/* 140 */       return handleMissingPathVariable((MissingPathVariableException)ex, headers, status, request);
/*     */     } 
/* 142 */     if (ex instanceof MissingServletRequestParameterException) {
/* 143 */       HttpStatus status = HttpStatus.BAD_REQUEST;
/* 144 */       return handleMissingServletRequestParameter((MissingServletRequestParameterException)ex, headers, status, request);
/*     */     } 
/* 146 */     if (ex instanceof ServletRequestBindingException) {
/* 147 */       HttpStatus status = HttpStatus.BAD_REQUEST;
/* 148 */       return handleServletRequestBindingException((ServletRequestBindingException)ex, headers, status, request);
/*     */     } 
/* 150 */     if (ex instanceof ConversionNotSupportedException) {
/* 151 */       HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
/* 152 */       return handleConversionNotSupported((ConversionNotSupportedException)ex, headers, status, request);
/*     */     } 
/* 154 */     if (ex instanceof TypeMismatchException) {
/* 155 */       HttpStatus status = HttpStatus.BAD_REQUEST;
/* 156 */       return handleTypeMismatch((TypeMismatchException)ex, headers, status, request);
/*     */     } 
/* 158 */     if (ex instanceof HttpMessageNotReadableException) {
/* 159 */       HttpStatus status = HttpStatus.BAD_REQUEST;
/* 160 */       return handleHttpMessageNotReadable((HttpMessageNotReadableException)ex, headers, status, request);
/*     */     } 
/* 162 */     if (ex instanceof HttpMessageNotWritableException) {
/* 163 */       HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
/* 164 */       return handleHttpMessageNotWritable((HttpMessageNotWritableException)ex, headers, status, request);
/*     */     } 
/* 166 */     if (ex instanceof MethodArgumentNotValidException) {
/* 167 */       HttpStatus status = HttpStatus.BAD_REQUEST;
/* 168 */       return handleMethodArgumentNotValid((MethodArgumentNotValidException)ex, headers, status, request);
/*     */     } 
/* 170 */     if (ex instanceof MissingServletRequestPartException) {
/* 171 */       HttpStatus status = HttpStatus.BAD_REQUEST;
/* 172 */       return handleMissingServletRequestPart((MissingServletRequestPartException)ex, headers, status, request);
/*     */     } 
/* 174 */     if (ex instanceof BindException) {
/* 175 */       HttpStatus status = HttpStatus.BAD_REQUEST;
/* 176 */       return handleBindException((BindException)ex, headers, status, request);
/*     */     } 
/* 178 */     if (ex instanceof NoHandlerFoundException) {
/* 179 */       HttpStatus status = HttpStatus.NOT_FOUND;
/* 180 */       return handleNoHandlerFoundException((NoHandlerFoundException)ex, headers, status, request);
/*     */     } 
/* 182 */     if (ex instanceof AsyncRequestTimeoutException) {
/* 183 */       HttpStatus status = HttpStatus.SERVICE_UNAVAILABLE;
/* 184 */       return handleAsyncRequestTimeoutException((AsyncRequestTimeoutException)ex, headers, status, request);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 191 */     throw ex;
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
/*     */   protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
/* 208 */     pageNotFoundLogger.warn(ex.getMessage());
/*     */     
/* 210 */     Set<HttpMethod> supportedMethods = ex.getSupportedHttpMethods();
/* 211 */     if (!CollectionUtils.isEmpty(supportedMethods)) {
/* 212 */       headers.setAllow(supportedMethods);
/*     */     }
/* 214 */     return handleExceptionInternal((Exception)ex, null, headers, status, request);
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
/*     */   protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
/* 230 */     List<MediaType> mediaTypes = ex.getSupportedMediaTypes();
/* 231 */     if (!CollectionUtils.isEmpty(mediaTypes)) {
/* 232 */       headers.setAccept(mediaTypes);
/*     */     }
/*     */     
/* 235 */     return handleExceptionInternal((Exception)ex, null, headers, status, request);
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
/*     */   protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
/* 250 */     return handleExceptionInternal((Exception)ex, null, headers, status, request);
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
/*     */   protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
/* 266 */     return handleExceptionInternal((Exception)ex, null, headers, status, request);
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
/*     */   protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
/* 281 */     return handleExceptionInternal((Exception)ex, null, headers, status, request);
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
/*     */   protected ResponseEntity<Object> handleServletRequestBindingException(ServletRequestBindingException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
/* 296 */     return handleExceptionInternal((Exception)ex, null, headers, status, request);
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
/*     */   protected ResponseEntity<Object> handleConversionNotSupported(ConversionNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
/* 311 */     return handleExceptionInternal((Exception)ex, null, headers, status, request);
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
/*     */   protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
/* 326 */     return handleExceptionInternal((Exception)ex, null, headers, status, request);
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
/*     */   protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
/* 341 */     return handleExceptionInternal((Exception)ex, null, headers, status, request);
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
/*     */   protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
/* 356 */     return handleExceptionInternal((Exception)ex, null, headers, status, request);
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
/*     */   protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
/* 371 */     return handleExceptionInternal((Exception)ex, null, headers, status, request);
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
/*     */   protected ResponseEntity<Object> handleMissingServletRequestPart(MissingServletRequestPartException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
/* 386 */     return handleExceptionInternal((Exception)ex, null, headers, status, request);
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
/*     */   protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
/* 401 */     return handleExceptionInternal((Exception)ex, null, headers, status, request);
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
/*     */   protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
/* 417 */     return handleExceptionInternal((Exception)ex, null, headers, status, request);
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
/*     */   @Nullable
/*     */   protected ResponseEntity<Object> handleAsyncRequestTimeoutException(AsyncRequestTimeoutException ex, HttpHeaders headers, HttpStatus status, WebRequest webRequest) {
/* 434 */     if (webRequest instanceof ServletWebRequest) {
/* 435 */       ServletWebRequest servletWebRequest = (ServletWebRequest)webRequest;
/* 436 */       HttpServletResponse response = servletWebRequest.getResponse();
/* 437 */       if (response != null && response.isCommitted()) {
/* 438 */         if (this.logger.isWarnEnabled()) {
/* 439 */           this.logger.warn("Async request timed out");
/*     */         }
/* 441 */         return null;
/*     */       } 
/*     */     } 
/*     */     
/* 445 */     return handleExceptionInternal((Exception)ex, null, headers, status, webRequest);
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
/*     */   protected ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
/* 462 */     if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
/* 463 */       request.setAttribute("javax.servlet.error.exception", ex, 0);
/*     */     }
/* 465 */     return new ResponseEntity(body, (MultiValueMap)headers, status);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/mvc/method/annotation/ResponseEntityExceptionHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */