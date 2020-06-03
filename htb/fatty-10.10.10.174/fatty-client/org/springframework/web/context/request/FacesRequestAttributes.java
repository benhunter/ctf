/*     */ package org.springframework.web.context.request;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Map;
/*     */ import javax.faces.context.ExternalContext;
/*     */ import javax.faces.context.FacesContext;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ReflectionUtils;
/*     */ import org.springframework.util.StringUtils;
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
/*     */ public class FacesRequestAttributes
/*     */   implements RequestAttributes
/*     */ {
/*  56 */   private static final Log logger = LogFactory.getLog(FacesRequestAttributes.class);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final FacesContext facesContext;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FacesRequestAttributes(FacesContext facesContext) {
/*  67 */     Assert.notNull(facesContext, "FacesContext must not be null");
/*  68 */     this.facesContext = facesContext;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final FacesContext getFacesContext() {
/*  76 */     return this.facesContext;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final ExternalContext getExternalContext() {
/*  84 */     return getFacesContext().getExternalContext();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Map<String, Object> getAttributeMap(int scope) {
/*  95 */     if (scope == 0) {
/*  96 */       return getExternalContext().getRequestMap();
/*     */     }
/*     */     
/*  99 */     return getExternalContext().getSessionMap();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getAttribute(String name, int scope) {
/* 106 */     return getAttributeMap(scope).get(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setAttribute(String name, Object value, int scope) {
/* 111 */     getAttributeMap(scope).put(name, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeAttribute(String name, int scope) {
/* 116 */     getAttributeMap(scope).remove(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] getAttributeNames(int scope) {
/* 121 */     return StringUtils.toStringArray(getAttributeMap(scope).keySet());
/*     */   }
/*     */ 
/*     */   
/*     */   public void registerDestructionCallback(String name, Runnable callback, int scope) {
/* 126 */     if (logger.isWarnEnabled()) {
/* 127 */       logger.warn("Could not register destruction callback [" + callback + "] for attribute '" + name + "' because FacesRequestAttributes does not support such callbacks");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object resolveReference(String key) {
/* 134 */     if ("request".equals(key)) {
/* 135 */       return getExternalContext().getRequest();
/*     */     }
/* 137 */     if ("session".equals(key)) {
/* 138 */       return getExternalContext().getSession(true);
/*     */     }
/* 140 */     if ("application".equals(key)) {
/* 141 */       return getExternalContext().getContext();
/*     */     }
/* 143 */     if ("requestScope".equals(key)) {
/* 144 */       return getExternalContext().getRequestMap();
/*     */     }
/* 146 */     if ("sessionScope".equals(key)) {
/* 147 */       return getExternalContext().getSessionMap();
/*     */     }
/* 149 */     if ("applicationScope".equals(key)) {
/* 150 */       return getExternalContext().getApplicationMap();
/*     */     }
/* 152 */     if ("facesContext".equals(key)) {
/* 153 */       return getFacesContext();
/*     */     }
/* 155 */     if ("cookie".equals(key)) {
/* 156 */       return getExternalContext().getRequestCookieMap();
/*     */     }
/* 158 */     if ("header".equals(key)) {
/* 159 */       return getExternalContext().getRequestHeaderMap();
/*     */     }
/* 161 */     if ("headerValues".equals(key)) {
/* 162 */       return getExternalContext().getRequestHeaderValuesMap();
/*     */     }
/* 164 */     if ("param".equals(key)) {
/* 165 */       return getExternalContext().getRequestParameterMap();
/*     */     }
/* 167 */     if ("paramValues".equals(key)) {
/* 168 */       return getExternalContext().getRequestParameterValuesMap();
/*     */     }
/* 170 */     if ("initParam".equals(key)) {
/* 171 */       return getExternalContext().getInitParameterMap();
/*     */     }
/* 173 */     if ("view".equals(key)) {
/* 174 */       return getFacesContext().getViewRoot();
/*     */     }
/* 176 */     if ("viewScope".equals(key)) {
/* 177 */       return getFacesContext().getViewRoot().getViewMap();
/*     */     }
/* 179 */     if ("flash".equals(key)) {
/* 180 */       return getExternalContext().getFlash();
/*     */     }
/* 182 */     if ("resource".equals(key)) {
/* 183 */       return getFacesContext().getApplication().getResourceHandler();
/*     */     }
/*     */     
/* 186 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSessionId() {
/* 192 */     Object session = getExternalContext().getSession(true);
/*     */     
/*     */     try {
/* 195 */       Method getIdMethod = session.getClass().getMethod("getId", new Class[0]);
/* 196 */       return String.valueOf(ReflectionUtils.invokeMethod(getIdMethod, session));
/*     */     }
/* 198 */     catch (NoSuchMethodException ex) {
/* 199 */       throw new IllegalStateException("Session object [" + session + "] does not have a getId() method");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getSessionMutex() {
/* 206 */     ExternalContext externalContext = getExternalContext();
/* 207 */     Object session = externalContext.getSession(true);
/* 208 */     Object mutex = externalContext.getSessionMap().get(WebUtils.SESSION_MUTEX_ATTRIBUTE);
/* 209 */     if (mutex == null) {
/* 210 */       mutex = (session != null) ? session : externalContext;
/*     */     }
/* 212 */     return mutex;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/context/request/FacesRequestAttributes.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */