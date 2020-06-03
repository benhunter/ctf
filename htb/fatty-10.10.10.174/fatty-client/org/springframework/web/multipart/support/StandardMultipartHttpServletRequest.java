/*     */ package org.springframework.web.multipart.support;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.Serializable;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Enumeration;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.mail.internet.MimeUtility;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.Part;
/*     */ import org.springframework.http.ContentDisposition;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.FileCopyUtils;
/*     */ import org.springframework.util.LinkedMultiValueMap;
/*     */ import org.springframework.util.MultiValueMap;
/*     */ import org.springframework.web.multipart.MaxUploadSizeExceededException;
/*     */ import org.springframework.web.multipart.MultipartException;
/*     */ import org.springframework.web.multipart.MultipartFile;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StandardMultipartHttpServletRequest
/*     */   extends AbstractMultipartHttpServletRequest
/*     */ {
/*     */   @Nullable
/*     */   private Set<String> multipartParameterNames;
/*     */   
/*     */   public StandardMultipartHttpServletRequest(HttpServletRequest request) throws MultipartException {
/*  71 */     this(request, false);
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
/*     */   public StandardMultipartHttpServletRequest(HttpServletRequest request, boolean lazyParsing) throws MultipartException {
/*  85 */     super(request);
/*  86 */     if (!lazyParsing) {
/*  87 */       parseRequest(request);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void parseRequest(HttpServletRequest request) {
/*     */     try {
/*  94 */       Collection<Part> parts = request.getParts();
/*  95 */       this.multipartParameterNames = new LinkedHashSet<>(parts.size());
/*  96 */       LinkedMultiValueMap linkedMultiValueMap = new LinkedMultiValueMap(parts.size());
/*  97 */       for (Part part : parts) {
/*  98 */         String headerValue = part.getHeader("Content-Disposition");
/*  99 */         ContentDisposition disposition = ContentDisposition.parse(headerValue);
/* 100 */         String filename = disposition.getFilename();
/* 101 */         if (filename != null) {
/* 102 */           if (filename.startsWith("=?") && filename.endsWith("?=")) {
/* 103 */             filename = MimeDelegate.decode(filename);
/*     */           }
/* 105 */           linkedMultiValueMap.add(part.getName(), new StandardMultipartFile(part, filename));
/*     */           continue;
/*     */         } 
/* 108 */         this.multipartParameterNames.add(part.getName());
/*     */       } 
/*     */       
/* 111 */       setMultipartFiles((MultiValueMap<String, MultipartFile>)linkedMultiValueMap);
/*     */     }
/* 113 */     catch (Throwable ex) {
/* 114 */       handleParseFailure(ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void handleParseFailure(Throwable ex) {
/* 119 */     String msg = ex.getMessage();
/* 120 */     if (msg != null && msg.contains("size") && msg.contains("exceed")) {
/* 121 */       throw new MaxUploadSizeExceededException(-1L, ex);
/*     */     }
/* 123 */     throw new MultipartException("Failed to parse multipart servlet request", ex);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void initializeMultipart() {
/* 128 */     parseRequest(getRequest());
/*     */   }
/*     */ 
/*     */   
/*     */   public Enumeration<String> getParameterNames() {
/* 133 */     if (this.multipartParameterNames == null) {
/* 134 */       initializeMultipart();
/*     */     }
/* 136 */     if (this.multipartParameterNames.isEmpty()) {
/* 137 */       return super.getParameterNames();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 142 */     Set<String> paramNames = new LinkedHashSet<>();
/* 143 */     Enumeration<String> paramEnum = super.getParameterNames();
/* 144 */     while (paramEnum.hasMoreElements()) {
/* 145 */       paramNames.add(paramEnum.nextElement());
/*     */     }
/* 147 */     paramNames.addAll(this.multipartParameterNames);
/* 148 */     return Collections.enumeration(paramNames);
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, String[]> getParameterMap() {
/* 153 */     if (this.multipartParameterNames == null) {
/* 154 */       initializeMultipart();
/*     */     }
/* 156 */     if (this.multipartParameterNames.isEmpty()) {
/* 157 */       return super.getParameterMap();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 162 */     Map<String, String[]> paramMap = (Map)new LinkedHashMap<>(super.getParameterMap());
/* 163 */     for (String paramName : this.multipartParameterNames) {
/* 164 */       if (!paramMap.containsKey(paramName)) {
/* 165 */         paramMap.put(paramName, getParameterValues(paramName));
/*     */       }
/*     */     } 
/* 168 */     return paramMap;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getMultipartContentType(String paramOrFileName) {
/*     */     try {
/* 174 */       Part part = getPart(paramOrFileName);
/* 175 */       return (part != null) ? part.getContentType() : null;
/*     */     }
/* 177 */     catch (Throwable ex) {
/* 178 */       throw new MultipartException("Could not access multipart servlet request", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpHeaders getMultipartHeaders(String paramOrFileName) {
/*     */     try {
/* 185 */       Part part = getPart(paramOrFileName);
/* 186 */       if (part != null) {
/* 187 */         HttpHeaders headers = new HttpHeaders();
/* 188 */         for (String headerName : part.getHeaderNames()) {
/* 189 */           headers.put(headerName, new ArrayList(part.getHeaders(headerName)));
/*     */         }
/* 191 */         return headers;
/*     */       } 
/*     */       
/* 194 */       return null;
/*     */     
/*     */     }
/* 197 */     catch (Throwable ex) {
/* 198 */       throw new MultipartException("Could not access multipart servlet request", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class StandardMultipartFile
/*     */     implements MultipartFile, Serializable
/*     */   {
/*     */     private final Part part;
/*     */ 
/*     */     
/*     */     private final String filename;
/*     */ 
/*     */     
/*     */     public StandardMultipartFile(Part part, String filename) {
/* 214 */       this.part = part;
/* 215 */       this.filename = filename;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getName() {
/* 220 */       return this.part.getName();
/*     */     }
/*     */ 
/*     */     
/*     */     public String getOriginalFilename() {
/* 225 */       return this.filename;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getContentType() {
/* 230 */       return this.part.getContentType();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 235 */       return (this.part.getSize() == 0L);
/*     */     }
/*     */ 
/*     */     
/*     */     public long getSize() {
/* 240 */       return this.part.getSize();
/*     */     }
/*     */ 
/*     */     
/*     */     public byte[] getBytes() throws IOException {
/* 245 */       return FileCopyUtils.copyToByteArray(this.part.getInputStream());
/*     */     }
/*     */ 
/*     */     
/*     */     public InputStream getInputStream() throws IOException {
/* 250 */       return this.part.getInputStream();
/*     */     }
/*     */ 
/*     */     
/*     */     public void transferTo(File dest) throws IOException, IllegalStateException {
/* 255 */       this.part.write(dest.getPath());
/* 256 */       if (dest.isAbsolute() && !dest.exists())
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 263 */         FileCopyUtils.copy(this.part.getInputStream(), Files.newOutputStream(dest.toPath(), new java.nio.file.OpenOption[0]));
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void transferTo(Path dest) throws IOException, IllegalStateException {
/* 269 */       FileCopyUtils.copy(this.part.getInputStream(), Files.newOutputStream(dest, new java.nio.file.OpenOption[0]));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class MimeDelegate
/*     */   {
/*     */     public static String decode(String value) {
/*     */       try {
/* 281 */         return MimeUtility.decodeText(value);
/*     */       }
/* 283 */       catch (UnsupportedEncodingException ex) {
/* 284 */         throw new IllegalStateException(ex);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/multipart/support/StandardMultipartHttpServletRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */