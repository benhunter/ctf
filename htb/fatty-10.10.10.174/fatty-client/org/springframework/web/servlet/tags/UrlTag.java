/*     */ package org.springframework.web.servlet.tags;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.charset.UnsupportedCharsetException;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import javax.servlet.ServletRequest;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import javax.servlet.jsp.JspException;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.servlet.support.RequestDataValueProcessor;
/*     */ import org.springframework.web.util.JavaScriptUtils;
/*     */ import org.springframework.web.util.TagUtils;
/*     */ import org.springframework.web.util.UriUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class UrlTag
/*     */   extends HtmlEscapingAwareTag
/*     */   implements ParamAware
/*     */ {
/*     */   private static final String URL_TEMPLATE_DELIMITER_PREFIX = "{";
/*     */   private static final String URL_TEMPLATE_DELIMITER_SUFFIX = "}";
/*     */   private static final String URL_TYPE_ABSOLUTE = "://";
/* 147 */   private List<Param> params = Collections.emptyList();
/*     */   
/* 149 */   private Set<String> templateParams = Collections.emptySet();
/*     */   
/*     */   @Nullable
/*     */   private UrlType type;
/*     */   
/*     */   @Nullable
/*     */   private String value;
/*     */   
/*     */   @Nullable
/*     */   private String context;
/*     */   
/*     */   @Nullable
/*     */   private String var;
/*     */   
/* 163 */   private int scope = 1;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean javaScriptEscape = false;
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValue(String value) {
/* 172 */     if (value.contains("://")) {
/* 173 */       this.type = UrlType.ABSOLUTE;
/* 174 */       this.value = value;
/*     */     }
/* 176 */     else if (value.startsWith("/")) {
/* 177 */       this.type = UrlType.CONTEXT_RELATIVE;
/* 178 */       this.value = value;
/*     */     } else {
/*     */       
/* 181 */       this.type = UrlType.RELATIVE;
/* 182 */       this.value = value;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setContext(String context) {
/* 191 */     if (context.startsWith("/")) {
/* 192 */       this.context = context;
/*     */     } else {
/*     */       
/* 195 */       this.context = "/" + context;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setVar(String var) {
/* 204 */     this.var = var;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setScope(String scope) {
/* 212 */     this.scope = TagUtils.getScope(scope);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setJavaScriptEscape(boolean javaScriptEscape) throws JspException {
/* 220 */     this.javaScriptEscape = javaScriptEscape;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addParam(Param param) {
/* 225 */     this.params.add(param);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int doStartTagInternal() throws JspException {
/* 231 */     this.params = new LinkedList<>();
/* 232 */     this.templateParams = new HashSet<>();
/* 233 */     return 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public int doEndTag() throws JspException {
/* 238 */     String url = createUrl();
/*     */     
/* 240 */     RequestDataValueProcessor processor = getRequestContext().getRequestDataValueProcessor();
/* 241 */     ServletRequest request = this.pageContext.getRequest();
/* 242 */     if (processor != null && request instanceof HttpServletRequest) {
/* 243 */       url = processor.processUrl((HttpServletRequest)request, url);
/*     */     }
/*     */     
/* 246 */     if (this.var == null) {
/*     */       
/*     */       try {
/* 249 */         this.pageContext.getOut().print(url);
/*     */       }
/* 251 */       catch (IOException ex) {
/* 252 */         throw new JspException(ex);
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 257 */       this.pageContext.setAttribute(this.var, url, this.scope);
/*     */     } 
/* 259 */     return 6;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   String createUrl() throws JspException {
/* 268 */     Assert.state((this.value != null), "No value set");
/* 269 */     HttpServletRequest request = (HttpServletRequest)this.pageContext.getRequest();
/* 270 */     HttpServletResponse response = (HttpServletResponse)this.pageContext.getResponse();
/*     */     
/* 272 */     StringBuilder url = new StringBuilder();
/* 273 */     if (this.type == UrlType.CONTEXT_RELATIVE)
/*     */     {
/* 275 */       if (this.context == null) {
/* 276 */         url.append(request.getContextPath());
/*     */       
/*     */       }
/* 279 */       else if (this.context.endsWith("/")) {
/* 280 */         url.append(this.context.substring(0, this.context.length() - 1));
/*     */       } else {
/*     */         
/* 283 */         url.append(this.context);
/*     */       } 
/*     */     }
/*     */     
/* 287 */     if (this.type != UrlType.RELATIVE && this.type != UrlType.ABSOLUTE && !this.value.startsWith("/")) {
/* 288 */       url.append("/");
/*     */     }
/* 290 */     url.append(replaceUriTemplateParams(this.value, this.params, this.templateParams));
/* 291 */     url.append(createQueryString(this.params, this.templateParams, (url.indexOf("?") == -1)));
/*     */     
/* 293 */     String urlStr = url.toString();
/* 294 */     if (this.type != UrlType.ABSOLUTE)
/*     */     {
/*     */       
/* 297 */       urlStr = response.encodeURL(urlStr);
/*     */     }
/*     */ 
/*     */     
/* 301 */     urlStr = htmlEscape(urlStr);
/* 302 */     urlStr = this.javaScriptEscape ? JavaScriptUtils.javaScriptEscape(urlStr) : urlStr;
/*     */     
/* 304 */     return urlStr;
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
/*     */   protected String createQueryString(List<Param> params, Set<String> usedParams, boolean includeQueryStringDelimiter) throws JspException {
/* 321 */     String encoding = this.pageContext.getResponse().getCharacterEncoding();
/* 322 */     StringBuilder qs = new StringBuilder();
/* 323 */     for (Param param : params) {
/* 324 */       if (!usedParams.contains(param.getName()) && StringUtils.hasLength(param.getName())) {
/* 325 */         if (includeQueryStringDelimiter && qs.length() == 0) {
/* 326 */           qs.append("?");
/*     */         } else {
/*     */           
/* 329 */           qs.append("&");
/*     */         } 
/*     */         try {
/* 332 */           qs.append(UriUtils.encodeQueryParam(param.getName(), encoding));
/* 333 */           if (param.getValue() != null) {
/* 334 */             qs.append("=");
/* 335 */             qs.append(UriUtils.encodeQueryParam(param.getValue(), encoding));
/*     */           }
/*     */         
/* 338 */         } catch (UnsupportedCharsetException ex) {
/* 339 */           throw new JspException(ex);
/*     */         } 
/*     */       } 
/*     */     } 
/* 343 */     return qs.toString();
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
/*     */   protected String replaceUriTemplateParams(String uri, List<Param> params, Set<String> usedParams) throws JspException {
/* 358 */     String encoding = this.pageContext.getResponse().getCharacterEncoding();
/* 359 */     for (Param param : params) {
/* 360 */       String template = "{" + param.getName() + "}";
/* 361 */       if (uri.contains(template)) {
/* 362 */         usedParams.add(param.getName());
/* 363 */         String value = param.getValue();
/*     */         try {
/* 365 */           uri = StringUtils.replace(uri, template, (value != null) ? 
/* 366 */               UriUtils.encodePath(value, encoding) : "");
/*     */         }
/* 368 */         catch (UnsupportedCharsetException ex) {
/* 369 */           throw new JspException(ex);
/*     */         } 
/*     */         continue;
/*     */       } 
/* 373 */       template = "{/" + param.getName() + "}";
/* 374 */       if (uri.contains(template)) {
/* 375 */         usedParams.add(param.getName());
/* 376 */         String value = param.getValue();
/*     */         try {
/* 378 */           uri = StringUtils.replace(uri, template, (value != null) ? 
/* 379 */               UriUtils.encodePathSegment(value, encoding) : "");
/*     */         }
/* 381 */         catch (UnsupportedCharsetException ex) {
/* 382 */           throw new JspException(ex);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 387 */     return uri;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private enum UrlType
/*     */   {
/* 396 */     CONTEXT_RELATIVE, RELATIVE, ABSOLUTE;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/tags/UrlTag.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */