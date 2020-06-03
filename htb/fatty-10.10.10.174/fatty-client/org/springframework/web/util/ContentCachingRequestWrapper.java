/*     */ package org.springframework.web.util;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.net.URLEncoder;
/*     */ import java.util.Arrays;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.servlet.ReadListener;
/*     */ import javax.servlet.ServletInputStream;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletRequestWrapper;
/*     */ import org.springframework.http.HttpMethod;
/*     */ import org.springframework.lang.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ContentCachingRequestWrapper
/*     */   extends HttpServletRequestWrapper
/*     */ {
/*     */   private static final String FORM_CONTENT_TYPE = "application/x-www-form-urlencoded";
/*     */   private final ByteArrayOutputStream cachedContent;
/*     */   @Nullable
/*     */   private final Integer contentCacheLimit;
/*     */   @Nullable
/*     */   private ServletInputStream inputStream;
/*     */   @Nullable
/*     */   private BufferedReader reader;
/*     */   
/*     */   public ContentCachingRequestWrapper(HttpServletRequest request) {
/*  72 */     super(request);
/*  73 */     int contentLength = request.getContentLength();
/*  74 */     this.cachedContent = new ByteArrayOutputStream((contentLength >= 0) ? contentLength : 1024);
/*  75 */     this.contentCacheLimit = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ContentCachingRequestWrapper(HttpServletRequest request, int contentCacheLimit) {
/*  86 */     super(request);
/*  87 */     this.cachedContent = new ByteArrayOutputStream(contentCacheLimit);
/*  88 */     this.contentCacheLimit = Integer.valueOf(contentCacheLimit);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ServletInputStream getInputStream() throws IOException {
/*  94 */     if (this.inputStream == null) {
/*  95 */       this.inputStream = new ContentCachingInputStream(getRequest().getInputStream());
/*     */     }
/*  97 */     return this.inputStream;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getCharacterEncoding() {
/* 102 */     String enc = super.getCharacterEncoding();
/* 103 */     return (enc != null) ? enc : "ISO-8859-1";
/*     */   }
/*     */ 
/*     */   
/*     */   public BufferedReader getReader() throws IOException {
/* 108 */     if (this.reader == null) {
/* 109 */       this.reader = new BufferedReader(new InputStreamReader((InputStream)getInputStream(), getCharacterEncoding()));
/*     */     }
/* 111 */     return this.reader;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getParameter(String name) {
/* 116 */     if (this.cachedContent.size() == 0 && isFormPost()) {
/* 117 */       writeRequestParametersToCachedContent();
/*     */     }
/* 119 */     return super.getParameter(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, String[]> getParameterMap() {
/* 124 */     if (this.cachedContent.size() == 0 && isFormPost()) {
/* 125 */       writeRequestParametersToCachedContent();
/*     */     }
/* 127 */     return super.getParameterMap();
/*     */   }
/*     */ 
/*     */   
/*     */   public Enumeration<String> getParameterNames() {
/* 132 */     if (this.cachedContent.size() == 0 && isFormPost()) {
/* 133 */       writeRequestParametersToCachedContent();
/*     */     }
/* 135 */     return super.getParameterNames();
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] getParameterValues(String name) {
/* 140 */     if (this.cachedContent.size() == 0 && isFormPost()) {
/* 141 */       writeRequestParametersToCachedContent();
/*     */     }
/* 143 */     return super.getParameterValues(name);
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isFormPost() {
/* 148 */     String contentType = getContentType();
/* 149 */     return (contentType != null && contentType.contains("application/x-www-form-urlencoded") && HttpMethod.POST
/* 150 */       .matches(getMethod()));
/*     */   }
/*     */   
/*     */   private void writeRequestParametersToCachedContent() {
/*     */     try {
/* 155 */       if (this.cachedContent.size() == 0) {
/* 156 */         String requestEncoding = getCharacterEncoding();
/* 157 */         Map<String, String[]> form = super.getParameterMap();
/* 158 */         for (Iterator<String> nameIterator = form.keySet().iterator(); nameIterator.hasNext(); ) {
/* 159 */           String name = nameIterator.next();
/* 160 */           List<String> values = Arrays.asList((Object[])form.get(name));
/* 161 */           for (Iterator<String> valueIterator = values.iterator(); valueIterator.hasNext(); ) {
/* 162 */             String value = valueIterator.next();
/* 163 */             this.cachedContent.write(URLEncoder.encode(name, requestEncoding).getBytes());
/* 164 */             if (value != null) {
/* 165 */               this.cachedContent.write(61);
/* 166 */               this.cachedContent.write(URLEncoder.encode(value, requestEncoding).getBytes());
/* 167 */               if (valueIterator.hasNext()) {
/* 168 */                 this.cachedContent.write(38);
/*     */               }
/*     */             } 
/*     */           } 
/* 172 */           if (nameIterator.hasNext()) {
/* 173 */             this.cachedContent.write(38);
/*     */           }
/*     */         }
/*     */       
/*     */       } 
/* 178 */     } catch (IOException ex) {
/* 179 */       throw new IllegalStateException("Failed to write request parameters to cached content", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getContentAsByteArray() {
/* 189 */     return this.cachedContent.toByteArray();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void handleContentOverflow(int contentCacheLimit) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class ContentCachingInputStream
/*     */     extends ServletInputStream
/*     */   {
/*     */     private final ServletInputStream is;
/*     */ 
/*     */ 
/*     */     
/*     */     private boolean overflow = false;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ContentCachingInputStream(ServletInputStream is) {
/* 213 */       this.is = is;
/*     */     }
/*     */ 
/*     */     
/*     */     public int read() throws IOException {
/* 218 */       int ch = this.is.read();
/* 219 */       if (ch != -1 && !this.overflow) {
/* 220 */         if (ContentCachingRequestWrapper.this.contentCacheLimit != null && ContentCachingRequestWrapper.this.cachedContent.size() == ContentCachingRequestWrapper.this.contentCacheLimit.intValue()) {
/* 221 */           this.overflow = true;
/* 222 */           ContentCachingRequestWrapper.this.handleContentOverflow(ContentCachingRequestWrapper.this.contentCacheLimit.intValue());
/*     */         } else {
/*     */           
/* 225 */           ContentCachingRequestWrapper.this.cachedContent.write(ch);
/*     */         } 
/*     */       }
/* 228 */       return ch;
/*     */     }
/*     */ 
/*     */     
/*     */     public int read(byte[] b) throws IOException {
/* 233 */       int count = this.is.read(b);
/* 234 */       writeToCache(b, 0, count);
/* 235 */       return count;
/*     */     }
/*     */     
/*     */     private void writeToCache(byte[] b, int off, int count) {
/* 239 */       if (!this.overflow && count > 0) {
/* 240 */         if (ContentCachingRequestWrapper.this.contentCacheLimit != null && count + ContentCachingRequestWrapper.this
/* 241 */           .cachedContent.size() > ContentCachingRequestWrapper.this.contentCacheLimit.intValue()) {
/* 242 */           this.overflow = true;
/* 243 */           ContentCachingRequestWrapper.this.cachedContent.write(b, off, ContentCachingRequestWrapper.this.contentCacheLimit.intValue() - ContentCachingRequestWrapper.this.cachedContent.size());
/* 244 */           ContentCachingRequestWrapper.this.handleContentOverflow(ContentCachingRequestWrapper.this.contentCacheLimit.intValue());
/*     */           return;
/*     */         } 
/* 247 */         ContentCachingRequestWrapper.this.cachedContent.write(b, off, count);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public int read(byte[] b, int off, int len) throws IOException {
/* 253 */       int count = this.is.read(b, off, len);
/* 254 */       writeToCache(b, off, count);
/* 255 */       return count;
/*     */     }
/*     */ 
/*     */     
/*     */     public int readLine(byte[] b, int off, int len) throws IOException {
/* 260 */       int count = this.is.readLine(b, off, len);
/* 261 */       writeToCache(b, off, count);
/* 262 */       return count;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isFinished() {
/* 267 */       return this.is.isFinished();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isReady() {
/* 272 */       return this.is.isReady();
/*     */     }
/*     */ 
/*     */     
/*     */     public void setReadListener(ReadListener readListener) {
/* 277 */       this.is.setReadListener(readListener);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/util/ContentCachingRequestWrapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */