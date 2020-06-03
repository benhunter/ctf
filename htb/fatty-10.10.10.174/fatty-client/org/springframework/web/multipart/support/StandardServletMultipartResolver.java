/*     */ package org.springframework.web.multipart.support;
/*     */ 
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.Part;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.multipart.MultipartException;
/*     */ import org.springframework.web.multipart.MultipartHttpServletRequest;
/*     */ import org.springframework.web.multipart.MultipartResolver;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StandardServletMultipartResolver
/*     */   implements MultipartResolver
/*     */ {
/*     */   private boolean resolveLazily = false;
/*     */   
/*     */   public void setResolveLazily(boolean resolveLazily) {
/*  76 */     this.resolveLazily = resolveLazily;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isMultipart(HttpServletRequest request) {
/*  82 */     return StringUtils.startsWithIgnoreCase(request.getContentType(), "multipart/");
/*     */   }
/*     */ 
/*     */   
/*     */   public MultipartHttpServletRequest resolveMultipart(HttpServletRequest request) throws MultipartException {
/*  87 */     return new StandardMultipartHttpServletRequest(request, this.resolveLazily);
/*     */   }
/*     */ 
/*     */   
/*     */   public void cleanupMultipart(MultipartHttpServletRequest request) {
/*  92 */     if (!(request instanceof AbstractMultipartHttpServletRequest) || ((AbstractMultipartHttpServletRequest)request)
/*  93 */       .isResolved())
/*     */       
/*     */       try {
/*     */         
/*  97 */         for (Part part : request.getParts()) {
/*  98 */           if (request.getFile(part.getName()) != null) {
/*  99 */             part.delete();
/*     */           }
/*     */         }
/*     */       
/* 103 */       } catch (Throwable ex) {
/* 104 */         LogFactory.getLog(getClass()).warn("Failed to perform cleanup of multipart items", ex);
/*     */       }  
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/multipart/support/StandardServletMultipartResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */