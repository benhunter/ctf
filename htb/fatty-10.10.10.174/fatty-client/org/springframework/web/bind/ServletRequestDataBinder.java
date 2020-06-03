/*     */ package org.springframework.web.bind;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.servlet.ServletRequest;
/*     */ import org.springframework.beans.MutablePropertyValues;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.validation.BindException;
/*     */ import org.springframework.web.multipart.MultipartFile;
/*     */ import org.springframework.web.multipart.MultipartRequest;
/*     */ import org.springframework.web.util.WebUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ServletRequestDataBinder
/*     */   extends WebDataBinder
/*     */ {
/*     */   public ServletRequestDataBinder(@Nullable Object target) {
/*  69 */     super(target);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ServletRequestDataBinder(@Nullable Object target, String objectName) {
/*  79 */     super(target, objectName);
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
/*     */ 
/*     */   
/*     */   public void bind(ServletRequest request) {
/* 101 */     MutablePropertyValues mpvs = new ServletRequestParameterPropertyValues(request);
/* 102 */     MultipartRequest multipartRequest = (MultipartRequest)WebUtils.getNativeRequest(request, MultipartRequest.class);
/* 103 */     if (multipartRequest != null) {
/* 104 */       bindMultipart((Map<String, List<MultipartFile>>)multipartRequest.getMultiFileMap(), mpvs);
/*     */     }
/* 106 */     addBindValues(mpvs, request);
/* 107 */     doBind(mpvs);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addBindValues(MutablePropertyValues mpvs, ServletRequest request) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void closeNoCatch() throws ServletRequestBindingException {
/* 127 */     if (getBindingResult().hasErrors())
/* 128 */       throw new ServletRequestBindingException("Errors binding onto object '" + 
/* 129 */           getBindingResult().getObjectName() + "'", new BindException(
/* 130 */             getBindingResult())); 
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/bind/ServletRequestDataBinder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */