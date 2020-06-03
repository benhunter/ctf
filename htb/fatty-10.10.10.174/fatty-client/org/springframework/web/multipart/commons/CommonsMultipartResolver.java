/*     */ package org.springframework.web.multipart.commons;
/*     */ 
/*     */ import java.util.List;
/*     */ import javax.servlet.ServletContext;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.apache.commons.fileupload.FileItem;
/*     */ import org.apache.commons.fileupload.FileItemFactory;
/*     */ import org.apache.commons.fileupload.FileUpload;
/*     */ import org.apache.commons.fileupload.FileUploadBase;
/*     */ import org.apache.commons.fileupload.FileUploadException;
/*     */ import org.apache.commons.fileupload.servlet.ServletFileUpload;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.web.context.ServletContextAware;
/*     */ import org.springframework.web.multipart.MaxUploadSizeExceededException;
/*     */ import org.springframework.web.multipart.MultipartException;
/*     */ import org.springframework.web.multipart.MultipartHttpServletRequest;
/*     */ import org.springframework.web.multipart.MultipartResolver;
/*     */ import org.springframework.web.multipart.support.AbstractMultipartHttpServletRequest;
/*     */ import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CommonsMultipartResolver
/*     */   extends CommonsFileUploadSupport
/*     */   implements MultipartResolver, ServletContextAware
/*     */ {
/*     */   private boolean resolveLazily = false;
/*     */   
/*     */   public CommonsMultipartResolver() {}
/*     */   
/*     */   public CommonsMultipartResolver(ServletContext servletContext) {
/*  86 */     this();
/*  87 */     setServletContext(servletContext);
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
/*     */   public void setResolveLazily(boolean resolveLazily) {
/* 100 */     this.resolveLazily = resolveLazily;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected FileUpload newFileUpload(FileItemFactory fileItemFactory) {
/* 111 */     return (FileUpload)new ServletFileUpload(fileItemFactory);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setServletContext(ServletContext servletContext) {
/* 116 */     if (!isUploadTempDirSpecified()) {
/* 117 */       getFileItemFactory().setRepository(WebUtils.getTempDir(servletContext));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isMultipart(HttpServletRequest request) {
/* 124 */     return ServletFileUpload.isMultipartContent(request);
/*     */   }
/*     */ 
/*     */   
/*     */   public MultipartHttpServletRequest resolveMultipart(final HttpServletRequest request) throws MultipartException {
/* 129 */     Assert.notNull(request, "Request must not be null");
/* 130 */     if (this.resolveLazily) {
/* 131 */       return (MultipartHttpServletRequest)new DefaultMultipartHttpServletRequest(request)
/*     */         {
/*     */           protected void initializeMultipart() {
/* 134 */             CommonsFileUploadSupport.MultipartParsingResult parsingResult = CommonsMultipartResolver.this.parseRequest(request);
/* 135 */             setMultipartFiles(parsingResult.getMultipartFiles());
/* 136 */             setMultipartParameters(parsingResult.getMultipartParameters());
/* 137 */             setMultipartParameterContentTypes(parsingResult.getMultipartParameterContentTypes());
/*     */           }
/*     */         };
/*     */     }
/*     */     
/* 142 */     CommonsFileUploadSupport.MultipartParsingResult parsingResult = parseRequest(request);
/* 143 */     return (MultipartHttpServletRequest)new DefaultMultipartHttpServletRequest(request, parsingResult.getMultipartFiles(), parsingResult
/* 144 */         .getMultipartParameters(), parsingResult.getMultipartParameterContentTypes());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected CommonsFileUploadSupport.MultipartParsingResult parseRequest(HttpServletRequest request) throws MultipartException {
/* 155 */     String encoding = determineEncoding(request);
/* 156 */     FileUpload fileUpload = prepareFileUpload(encoding);
/*     */     try {
/* 158 */       List<FileItem> fileItems = ((ServletFileUpload)fileUpload).parseRequest(request);
/* 159 */       return parseFileItems(fileItems, encoding);
/*     */     }
/* 161 */     catch (org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException ex) {
/* 162 */       throw new MaxUploadSizeExceededException(fileUpload.getSizeMax(), ex);
/*     */     }
/* 164 */     catch (org.apache.commons.fileupload.FileUploadBase.FileSizeLimitExceededException ex) {
/* 165 */       throw new MaxUploadSizeExceededException(fileUpload.getFileSizeMax(), ex);
/*     */     }
/* 167 */     catch (FileUploadException ex) {
/* 168 */       throw new MultipartException("Failed to parse multipart servlet request", ex);
/*     */     } 
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
/*     */   protected String determineEncoding(HttpServletRequest request) {
/* 183 */     String encoding = request.getCharacterEncoding();
/* 184 */     if (encoding == null) {
/* 185 */       encoding = getDefaultEncoding();
/*     */     }
/* 187 */     return encoding;
/*     */   }
/*     */ 
/*     */   
/*     */   public void cleanupMultipart(MultipartHttpServletRequest request) {
/* 192 */     if (!(request instanceof AbstractMultipartHttpServletRequest) || ((AbstractMultipartHttpServletRequest)request)
/* 193 */       .isResolved())
/*     */       try {
/* 195 */         cleanupFileItems(request.getMultiFileMap());
/*     */       }
/* 197 */       catch (Throwable ex) {
/* 198 */         this.logger.warn("Failed to perform multipart cleanup for servlet request", ex);
/*     */       }  
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/multipart/commons/CommonsMultipartResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */