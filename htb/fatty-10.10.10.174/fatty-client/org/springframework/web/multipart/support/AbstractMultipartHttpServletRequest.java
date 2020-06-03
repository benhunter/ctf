/*     */ package org.springframework.web.multipart.support;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.servlet.ServletRequest;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletRequestWrapper;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.HttpMethod;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.LinkedMultiValueMap;
/*     */ import org.springframework.util.MultiValueMap;
/*     */ import org.springframework.web.multipart.MultipartFile;
/*     */ import org.springframework.web.multipart.MultipartHttpServletRequest;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractMultipartHttpServletRequest
/*     */   extends HttpServletRequestWrapper
/*     */   implements MultipartHttpServletRequest
/*     */ {
/*     */   @Nullable
/*     */   private MultiValueMap<String, MultipartFile> multipartFiles;
/*     */   
/*     */   protected AbstractMultipartHttpServletRequest(HttpServletRequest request) {
/*  55 */     super(request);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpServletRequest getRequest() {
/*  61 */     return (HttpServletRequest)super.getRequest();
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpMethod getRequestMethod() {
/*  66 */     return HttpMethod.resolve(getRequest().getMethod());
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpHeaders getRequestHeaders() {
/*  71 */     HttpHeaders headers = new HttpHeaders();
/*  72 */     Enumeration<String> headerNames = getHeaderNames();
/*  73 */     while (headerNames.hasMoreElements()) {
/*  74 */       String headerName = headerNames.nextElement();
/*  75 */       headers.put(headerName, Collections.list(getHeaders(headerName)));
/*     */     } 
/*  77 */     return headers;
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<String> getFileNames() {
/*  82 */     return getMultipartFiles().keySet().iterator();
/*     */   }
/*     */ 
/*     */   
/*     */   public MultipartFile getFile(String name) {
/*  87 */     return (MultipartFile)getMultipartFiles().getFirst(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<MultipartFile> getFiles(String name) {
/*  92 */     List<MultipartFile> multipartFiles = (List<MultipartFile>)getMultipartFiles().get(name);
/*  93 */     if (multipartFiles != null) {
/*  94 */       return multipartFiles;
/*     */     }
/*     */     
/*  97 */     return Collections.emptyList();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, MultipartFile> getFileMap() {
/* 103 */     return getMultipartFiles().toSingleValueMap();
/*     */   }
/*     */ 
/*     */   
/*     */   public MultiValueMap<String, MultipartFile> getMultiFileMap() {
/* 108 */     return getMultipartFiles();
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
/*     */   public boolean isResolved() {
/* 120 */     return (this.multipartFiles != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void setMultipartFiles(MultiValueMap<String, MultipartFile> multipartFiles) {
/* 129 */     this
/* 130 */       .multipartFiles = (MultiValueMap<String, MultipartFile>)new LinkedMultiValueMap(Collections.unmodifiableMap((Map<? extends String, ? extends MultipartFile>)multipartFiles));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected MultiValueMap<String, MultipartFile> getMultipartFiles() {
/* 139 */     if (this.multipartFiles == null) {
/* 140 */       initializeMultipart();
/*     */     }
/* 142 */     return this.multipartFiles;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void initializeMultipart() {
/* 150 */     throw new IllegalStateException("Multipart request not initialized");
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/multipart/support/AbstractMultipartHttpServletRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */