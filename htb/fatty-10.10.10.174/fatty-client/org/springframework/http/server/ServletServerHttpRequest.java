/*     */ package org.springframework.http.server;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.Writer;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URLEncoder;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.security.Principal;
/*     */ import java.util.Arrays;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.HttpMethod;
/*     */ import org.springframework.http.InvalidMediaTypeException;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.LinkedCaseInsensitiveMap;
/*     */ import org.springframework.util.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ServletServerHttpRequest
/*     */   implements ServerHttpRequest
/*     */ {
/*     */   protected static final String FORM_CONTENT_TYPE = "application/x-www-form-urlencoded";
/*  60 */   protected static final Charset FORM_CHARSET = StandardCharsets.UTF_8;
/*     */ 
/*     */ 
/*     */   
/*     */   private final HttpServletRequest servletRequest;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private URI uri;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private HttpHeaders headers;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private ServerHttpAsyncRequestControl asyncRequestControl;
/*     */ 
/*     */ 
/*     */   
/*     */   public ServletServerHttpRequest(HttpServletRequest servletRequest) {
/*  81 */     Assert.notNull(servletRequest, "HttpServletRequest must not be null");
/*  82 */     this.servletRequest = servletRequest;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpServletRequest getServletRequest() {
/*  90 */     return this.servletRequest;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public HttpMethod getMethod() {
/*  96 */     return HttpMethod.resolve(this.servletRequest.getMethod());
/*     */   }
/*     */ 
/*     */   
/*     */   public String getMethodValue() {
/* 101 */     return this.servletRequest.getMethod();
/*     */   }
/*     */ 
/*     */   
/*     */   public URI getURI() {
/* 106 */     if (this.uri == null) {
/* 107 */       String urlString = null;
/* 108 */       boolean hasQuery = false;
/*     */       try {
/* 110 */         StringBuffer url = this.servletRequest.getRequestURL();
/* 111 */         String query = this.servletRequest.getQueryString();
/* 112 */         hasQuery = StringUtils.hasText(query);
/* 113 */         if (hasQuery) {
/* 114 */           url.append('?').append(query);
/*     */         }
/* 116 */         urlString = url.toString();
/* 117 */         this.uri = new URI(urlString);
/*     */       }
/* 119 */       catch (URISyntaxException ex) {
/* 120 */         if (!hasQuery) {
/* 121 */           throw new IllegalStateException("Could not resolve HttpServletRequest as URI: " + urlString, ex);
/*     */         }
/*     */ 
/*     */         
/*     */         try {
/* 126 */           urlString = this.servletRequest.getRequestURL().toString();
/* 127 */           this.uri = new URI(urlString);
/*     */         }
/* 129 */         catch (URISyntaxException ex2) {
/* 130 */           throw new IllegalStateException("Could not resolve HttpServletRequest as URI: " + urlString, ex2);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 135 */     return this.uri;
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpHeaders getHeaders() {
/* 140 */     if (this.headers == null) {
/* 141 */       this.headers = new HttpHeaders();
/*     */       
/* 143 */       for (Enumeration<?> names = this.servletRequest.getHeaderNames(); names.hasMoreElements(); ) {
/* 144 */         String headerName = (String)names.nextElement();
/* 145 */         Enumeration<?> headerValues = this.servletRequest.getHeaders(headerName);
/* 146 */         while (headerValues.hasMoreElements()) {
/* 147 */           String headerValue = (String)headerValues.nextElement();
/* 148 */           this.headers.add(headerName, headerValue);
/*     */         } 
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/* 155 */         MediaType contentType = this.headers.getContentType();
/* 156 */         if (contentType == null) {
/* 157 */           String requestContentType = this.servletRequest.getContentType();
/* 158 */           if (StringUtils.hasLength(requestContentType)) {
/* 159 */             contentType = MediaType.parseMediaType(requestContentType);
/* 160 */             this.headers.setContentType(contentType);
/*     */           } 
/*     */         } 
/* 163 */         if (contentType != null && contentType.getCharset() == null) {
/* 164 */           String requestEncoding = this.servletRequest.getCharacterEncoding();
/* 165 */           if (StringUtils.hasLength(requestEncoding)) {
/* 166 */             Charset charSet = Charset.forName(requestEncoding);
/* 167 */             LinkedCaseInsensitiveMap<String, String> linkedCaseInsensitiveMap = new LinkedCaseInsensitiveMap();
/* 168 */             linkedCaseInsensitiveMap.putAll(contentType.getParameters());
/* 169 */             linkedCaseInsensitiveMap.put("charset", charSet.toString());
/* 170 */             MediaType mediaType = new MediaType(contentType.getType(), contentType.getSubtype(), (Map)linkedCaseInsensitiveMap);
/* 171 */             this.headers.setContentType(mediaType);
/*     */           }
/*     */         
/*     */         } 
/* 175 */       } catch (InvalidMediaTypeException invalidMediaTypeException) {}
/*     */ 
/*     */ 
/*     */       
/* 179 */       if (this.headers.getContentLength() < 0L) {
/* 180 */         int requestContentLength = this.servletRequest.getContentLength();
/* 181 */         if (requestContentLength != -1) {
/* 182 */           this.headers.setContentLength(requestContentLength);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 187 */     return this.headers;
/*     */   }
/*     */ 
/*     */   
/*     */   public Principal getPrincipal() {
/* 192 */     return this.servletRequest.getUserPrincipal();
/*     */   }
/*     */ 
/*     */   
/*     */   public InetSocketAddress getLocalAddress() {
/* 197 */     return new InetSocketAddress(this.servletRequest.getLocalName(), this.servletRequest.getLocalPort());
/*     */   }
/*     */ 
/*     */   
/*     */   public InetSocketAddress getRemoteAddress() {
/* 202 */     return new InetSocketAddress(this.servletRequest.getRemoteHost(), this.servletRequest.getRemotePort());
/*     */   }
/*     */ 
/*     */   
/*     */   public InputStream getBody() throws IOException {
/* 207 */     if (isFormPost(this.servletRequest)) {
/* 208 */       return getBodyFromServletRequestParameters(this.servletRequest);
/*     */     }
/*     */     
/* 211 */     return (InputStream)this.servletRequest.getInputStream();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ServerHttpAsyncRequestControl getAsyncRequestControl(ServerHttpResponse response) {
/* 217 */     if (this.asyncRequestControl == null) {
/* 218 */       if (!ServletServerHttpResponse.class.isInstance(response)) {
/* 219 */         throw new IllegalArgumentException("Response must be a ServletServerHttpResponse: " + response
/* 220 */             .getClass());
/*     */       }
/* 222 */       ServletServerHttpResponse servletServerResponse = (ServletServerHttpResponse)response;
/* 223 */       this.asyncRequestControl = new ServletServerHttpAsyncRequestControl(this, servletServerResponse);
/*     */     } 
/* 225 */     return this.asyncRequestControl;
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean isFormPost(HttpServletRequest request) {
/* 230 */     String contentType = request.getContentType();
/* 231 */     return (contentType != null && contentType.contains("application/x-www-form-urlencoded") && HttpMethod.POST
/* 232 */       .matches(request.getMethod()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static InputStream getBodyFromServletRequestParameters(HttpServletRequest request) throws IOException {
/* 242 */     ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
/* 243 */     Writer writer = new OutputStreamWriter(bos, FORM_CHARSET);
/*     */     
/* 245 */     Map<String, String[]> form = request.getParameterMap();
/* 246 */     for (Iterator<String> nameIterator = form.keySet().iterator(); nameIterator.hasNext(); ) {
/* 247 */       String name = nameIterator.next();
/* 248 */       List<String> values = Arrays.asList((Object[])form.get(name));
/* 249 */       for (Iterator<String> valueIterator = values.iterator(); valueIterator.hasNext(); ) {
/* 250 */         String value = valueIterator.next();
/* 251 */         writer.write(URLEncoder.encode(name, FORM_CHARSET.name()));
/* 252 */         if (value != null) {
/* 253 */           writer.write(61);
/* 254 */           writer.write(URLEncoder.encode(value, FORM_CHARSET.name()));
/* 255 */           if (valueIterator.hasNext()) {
/* 256 */             writer.write(38);
/*     */           }
/*     */         } 
/*     */       } 
/* 260 */       if (nameIterator.hasNext()) {
/* 261 */         writer.append('&');
/*     */       }
/*     */     } 
/* 264 */     writer.flush();
/*     */     
/* 266 */     return new ByteArrayInputStream(bos.toByteArray());
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/server/ServletServerHttpRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */