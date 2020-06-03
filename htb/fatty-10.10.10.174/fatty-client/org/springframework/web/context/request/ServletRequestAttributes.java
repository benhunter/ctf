/*     */ package org.springframework.web.context.request;
/*     */ 
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import javax.servlet.http.HttpSession;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.NumberUtils;
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
/*     */ public class ServletRequestAttributes
/*     */   extends AbstractRequestAttributes
/*     */ {
/*  50 */   public static final String DESTRUCTION_CALLBACK_NAME_PREFIX = ServletRequestAttributes.class
/*  51 */     .getName() + ".DESTRUCTION_CALLBACK.";
/*     */   
/*  53 */   protected static final Set<Class<?>> immutableValueTypes = new HashSet<>(16);
/*     */   
/*     */   static {
/*  56 */     immutableValueTypes.addAll(NumberUtils.STANDARD_NUMBER_TYPES);
/*  57 */     immutableValueTypes.add(Boolean.class);
/*  58 */     immutableValueTypes.add(Character.class);
/*  59 */     immutableValueTypes.add(String.class);
/*     */   }
/*     */ 
/*     */   
/*     */   private final HttpServletRequest request;
/*     */   
/*     */   @Nullable
/*     */   private HttpServletResponse response;
/*     */   
/*     */   @Nullable
/*     */   private volatile HttpSession session;
/*     */   
/*  71 */   private final Map<String, Object> sessionAttributesToUpdate = new ConcurrentHashMap<>(1);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ServletRequestAttributes(HttpServletRequest request) {
/*  79 */     Assert.notNull(request, "Request must not be null");
/*  80 */     this.request = request;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ServletRequestAttributes(HttpServletRequest request, @Nullable HttpServletResponse response) {
/*  89 */     this(request);
/*  90 */     this.response = response;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final HttpServletRequest getRequest() {
/*  98 */     return this.request;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public final HttpServletResponse getResponse() {
/* 106 */     return this.response;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected final HttpSession getSession(boolean allowCreate) {
/* 115 */     if (isRequestActive()) {
/* 116 */       HttpSession httpSession = this.request.getSession(allowCreate);
/* 117 */       this.session = httpSession;
/* 118 */       return httpSession;
/*     */     } 
/*     */ 
/*     */     
/* 122 */     HttpSession session = this.session;
/* 123 */     if (session == null) {
/* 124 */       if (allowCreate) {
/* 125 */         throw new IllegalStateException("No session found and request already completed - cannot create new session!");
/*     */       }
/*     */ 
/*     */       
/* 129 */       session = this.request.getSession(false);
/* 130 */       this.session = session;
/*     */     } 
/*     */     
/* 133 */     return session;
/*     */   }
/*     */ 
/*     */   
/*     */   private HttpSession obtainSession() {
/* 138 */     HttpSession session = getSession(true);
/* 139 */     Assert.state((session != null), "No HttpSession");
/* 140 */     return session;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getAttribute(String name, int scope) {
/* 146 */     if (scope == 0) {
/* 147 */       if (!isRequestActive()) {
/* 148 */         throw new IllegalStateException("Cannot ask for request attribute - request is not active anymore!");
/*     */       }
/*     */       
/* 151 */       return this.request.getAttribute(name);
/*     */     } 
/*     */     
/* 154 */     HttpSession session = getSession(false);
/* 155 */     if (session != null) {
/*     */       try {
/* 157 */         Object value = session.getAttribute(name);
/* 158 */         if (value != null) {
/* 159 */           this.sessionAttributesToUpdate.put(name, value);
/*     */         }
/* 161 */         return value;
/*     */       }
/* 163 */       catch (IllegalStateException illegalStateException) {}
/*     */     }
/*     */ 
/*     */     
/* 167 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAttribute(String name, Object value, int scope) {
/* 173 */     if (scope == 0) {
/* 174 */       if (!isRequestActive()) {
/* 175 */         throw new IllegalStateException("Cannot set request attribute - request is not active anymore!");
/*     */       }
/*     */       
/* 178 */       this.request.setAttribute(name, value);
/*     */     } else {
/*     */       
/* 181 */       HttpSession session = obtainSession();
/* 182 */       this.sessionAttributesToUpdate.remove(name);
/* 183 */       session.setAttribute(name, value);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeAttribute(String name, int scope) {
/* 189 */     if (scope == 0) {
/* 190 */       if (isRequestActive()) {
/* 191 */         removeRequestDestructionCallback(name);
/* 192 */         this.request.removeAttribute(name);
/*     */       } 
/*     */     } else {
/*     */       
/* 196 */       HttpSession session = getSession(false);
/* 197 */       if (session != null) {
/* 198 */         this.sessionAttributesToUpdate.remove(name);
/*     */         try {
/* 200 */           session.removeAttribute(DESTRUCTION_CALLBACK_NAME_PREFIX + name);
/* 201 */           session.removeAttribute(name);
/*     */         }
/* 203 */         catch (IllegalStateException illegalStateException) {}
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getAttributeNames(int scope) {
/* 212 */     if (scope == 0) {
/* 213 */       if (!isRequestActive()) {
/* 214 */         throw new IllegalStateException("Cannot ask for request attributes - request is not active anymore!");
/*     */       }
/*     */       
/* 217 */       return StringUtils.toStringArray(this.request.getAttributeNames());
/*     */     } 
/*     */     
/* 220 */     HttpSession session = getSession(false);
/* 221 */     if (session != null) {
/*     */       try {
/* 223 */         return StringUtils.toStringArray(session.getAttributeNames());
/*     */       }
/* 225 */       catch (IllegalStateException illegalStateException) {}
/*     */     }
/*     */ 
/*     */     
/* 229 */     return new String[0];
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void registerDestructionCallback(String name, Runnable callback, int scope) {
/* 235 */     if (scope == 0) {
/* 236 */       registerRequestDestructionCallback(name, callback);
/*     */     } else {
/*     */       
/* 239 */       registerSessionDestructionCallback(name, callback);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Object resolveReference(String key) {
/* 245 */     if ("request".equals(key)) {
/* 246 */       return this.request;
/*     */     }
/* 248 */     if ("session".equals(key)) {
/* 249 */       return getSession(true);
/*     */     }
/*     */     
/* 252 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSessionId() {
/* 258 */     return obtainSession().getId();
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getSessionMutex() {
/* 263 */     return WebUtils.getSessionMutex(obtainSession());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void updateAccessedSessionAttributes() {
/* 273 */     if (!this.sessionAttributesToUpdate.isEmpty()) {
/*     */       
/* 275 */       HttpSession session = getSession(false);
/* 276 */       if (session != null) {
/*     */         try {
/* 278 */           for (Map.Entry<String, Object> entry : this.sessionAttributesToUpdate.entrySet()) {
/* 279 */             String name = entry.getKey();
/* 280 */             Object newValue = entry.getValue();
/* 281 */             Object oldValue = session.getAttribute(name);
/* 282 */             if (oldValue == newValue && !isImmutableSessionAttribute(name, newValue)) {
/* 283 */               session.setAttribute(name, newValue);
/*     */             }
/*     */           }
/*     */         
/* 287 */         } catch (IllegalStateException illegalStateException) {}
/*     */       }
/*     */ 
/*     */       
/* 291 */       this.sessionAttributesToUpdate.clear();
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
/*     */ 
/*     */   
/*     */   protected boolean isImmutableSessionAttribute(String name, @Nullable Object value) {
/* 308 */     return (value == null || immutableValueTypes.contains(value.getClass()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void registerSessionDestructionCallback(String name, Runnable callback) {
/* 319 */     HttpSession session = obtainSession();
/* 320 */     session.setAttribute(DESTRUCTION_CALLBACK_NAME_PREFIX + name, new DestructionCallbackBindingListener(callback));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 327 */     return this.request.toString();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/context/request/ServletRequestAttributes.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */