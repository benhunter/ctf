/*     */ package org.springframework.web.context.request;
/*     */ 
/*     */ import java.security.Principal;
/*     */ import java.util.Iterator;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import javax.faces.context.ExternalContext;
/*     */ import javax.faces.context.FacesContext;
/*     */ import org.springframework.lang.Nullable;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FacesWebRequest
/*     */   extends FacesRequestAttributes
/*     */   implements NativeWebRequest
/*     */ {
/*     */   public FacesWebRequest(FacesContext facesContext) {
/*  45 */     super(facesContext);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getNativeRequest() {
/*  51 */     return getExternalContext().getRequest();
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getNativeResponse() {
/*  56 */     return getExternalContext().getResponse();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T getNativeRequest(@Nullable Class<T> requiredType) {
/*  62 */     if (requiredType != null) {
/*  63 */       Object request = getExternalContext().getRequest();
/*  64 */       if (requiredType.isInstance(request)) {
/*  65 */         return (T)request;
/*     */       }
/*     */     } 
/*  68 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T getNativeResponse(@Nullable Class<T> requiredType) {
/*  74 */     if (requiredType != null) {
/*  75 */       Object response = getExternalContext().getResponse();
/*  76 */       if (requiredType.isInstance(response)) {
/*  77 */         return (T)response;
/*     */       }
/*     */     } 
/*  80 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getHeader(String headerName) {
/*  87 */     return (String)getExternalContext().getRequestHeaderMap().get(headerName);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String[] getHeaderValues(String headerName) {
/*  93 */     return (String[])getExternalContext().getRequestHeaderValuesMap().get(headerName);
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<String> getHeaderNames() {
/*  98 */     return getExternalContext().getRequestHeaderMap().keySet().iterator();
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getParameter(String paramName) {
/* 104 */     return (String)getExternalContext().getRequestParameterMap().get(paramName);
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<String> getParameterNames() {
/* 109 */     return getExternalContext().getRequestParameterNames();
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String[] getParameterValues(String paramName) {
/* 115 */     return (String[])getExternalContext().getRequestParameterValuesMap().get(paramName);
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, String[]> getParameterMap() {
/* 120 */     return getExternalContext().getRequestParameterValuesMap();
/*     */   }
/*     */ 
/*     */   
/*     */   public Locale getLocale() {
/* 125 */     return getFacesContext().getExternalContext().getRequestLocale();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getContextPath() {
/* 130 */     return getFacesContext().getExternalContext().getRequestContextPath();
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getRemoteUser() {
/* 136 */     return getFacesContext().getExternalContext().getRemoteUser();
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Principal getUserPrincipal() {
/* 142 */     return getFacesContext().getExternalContext().getUserPrincipal();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isUserInRole(String role) {
/* 147 */     return getFacesContext().getExternalContext().isUserInRole(role);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSecure() {
/* 152 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean checkNotModified(long lastModifiedTimestamp) {
/* 157 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean checkNotModified(@Nullable String eTag) {
/* 162 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean checkNotModified(@Nullable String etag, long lastModifiedTimestamp) {
/* 167 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDescription(boolean includeClientInfo) {
/* 172 */     ExternalContext externalContext = getExternalContext();
/* 173 */     StringBuilder sb = new StringBuilder();
/* 174 */     sb.append("context=").append(externalContext.getRequestContextPath());
/* 175 */     if (includeClientInfo) {
/* 176 */       Object session = externalContext.getSession(false);
/* 177 */       if (session != null) {
/* 178 */         sb.append(";session=").append(getSessionId());
/*     */       }
/* 180 */       String user = externalContext.getRemoteUser();
/* 181 */       if (StringUtils.hasLength(user)) {
/* 182 */         sb.append(";user=").append(user);
/*     */       }
/*     */     } 
/* 185 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 191 */     return "FacesWebRequest: " + getDescription(true);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/context/request/FacesWebRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */