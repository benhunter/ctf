/*     */ package org.springframework.web.method.support;
/*     */ 
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.springframework.http.HttpStatus;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.ui.ModelMap;
/*     */ import org.springframework.validation.support.BindingAwareModelMap;
/*     */ import org.springframework.web.bind.support.SessionStatus;
/*     */ import org.springframework.web.bind.support.SimpleSessionStatus;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ModelAndViewContainer
/*     */ {
/*     */   private boolean ignoreDefaultModelOnRedirect = false;
/*     */   @Nullable
/*     */   private Object view;
/*  57 */   private final ModelMap defaultModel = (ModelMap)new BindingAwareModelMap();
/*     */   
/*     */   @Nullable
/*     */   private ModelMap redirectModel;
/*     */   
/*     */   private boolean redirectModelScenario = false;
/*     */   
/*     */   @Nullable
/*     */   private HttpStatus status;
/*     */   
/*  67 */   private final Set<String> noBinding = new HashSet<>(4);
/*     */   
/*  69 */   private final Set<String> bindingDisabled = new HashSet<>(4);
/*     */   
/*  71 */   private final SessionStatus sessionStatus = (SessionStatus)new SimpleSessionStatus();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean requestHandled = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIgnoreDefaultModelOnRedirect(boolean ignoreDefaultModelOnRedirect) {
/*  89 */     this.ignoreDefaultModelOnRedirect = ignoreDefaultModelOnRedirect;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setViewName(@Nullable String viewName) {
/*  97 */     this.view = viewName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getViewName() {
/* 106 */     return (this.view instanceof String) ? (String)this.view : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setView(@Nullable Object view) {
/* 114 */     this.view = view;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object getView() {
/* 123 */     return this.view;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isViewReference() {
/* 131 */     return this.view instanceof String;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ModelMap getModel() {
/* 141 */     if (useDefaultModel()) {
/* 142 */       return this.defaultModel;
/*     */     }
/*     */     
/* 145 */     if (this.redirectModel == null) {
/* 146 */       this.redirectModel = new ModelMap();
/*     */     }
/* 148 */     return this.redirectModel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean useDefaultModel() {
/* 156 */     return (!this.redirectModelScenario || (this.redirectModel == null && !this.ignoreDefaultModelOnRedirect));
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
/*     */   public ModelMap getDefaultModel() {
/* 170 */     return this.defaultModel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRedirectModel(ModelMap redirectModel) {
/* 180 */     this.redirectModel = redirectModel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRedirectModelScenario(boolean redirectModelScenario) {
/* 188 */     this.redirectModelScenario = redirectModelScenario;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStatus(@Nullable HttpStatus status) {
/* 197 */     this.status = status;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public HttpStatus getStatus() {
/* 206 */     return this.status;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBindingDisabled(String attributeName) {
/* 216 */     this.bindingDisabled.add(attributeName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isBindingDisabled(String name) {
/* 224 */     return (this.bindingDisabled.contains(name) || this.noBinding.contains(name));
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
/*     */   public void setBinding(String attributeName, boolean enabled) {
/* 236 */     if (!enabled) {
/* 237 */       this.noBinding.add(attributeName);
/*     */     } else {
/*     */       
/* 240 */       this.noBinding.remove(attributeName);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SessionStatus getSessionStatus() {
/* 249 */     return this.sessionStatus;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRequestHandled(boolean requestHandled) {
/* 260 */     this.requestHandled = requestHandled;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isRequestHandled() {
/* 267 */     return this.requestHandled;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ModelAndViewContainer addAttribute(String name, @Nullable Object value) {
/* 275 */     getModel().addAttribute(name, value);
/* 276 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ModelAndViewContainer addAttribute(Object value) {
/* 284 */     getModel().addAttribute(value);
/* 285 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ModelAndViewContainer addAllAttributes(@Nullable Map<String, ?> attributes) {
/* 293 */     getModel().addAllAttributes(attributes);
/* 294 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ModelAndViewContainer mergeAttributes(@Nullable Map<String, ?> attributes) {
/* 303 */     getModel().mergeAttributes(attributes);
/* 304 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ModelAndViewContainer removeAttributes(@Nullable Map<String, ?> attributes) {
/* 311 */     if (attributes != null) {
/* 312 */       for (String key : attributes.keySet()) {
/* 313 */         getModel().remove(key);
/*     */       }
/*     */     }
/* 316 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsAttribute(String name) {
/* 324 */     return getModel().containsAttribute(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 333 */     StringBuilder sb = new StringBuilder("ModelAndViewContainer: ");
/* 334 */     if (!isRequestHandled()) {
/* 335 */       if (isViewReference()) {
/* 336 */         sb.append("reference to view with name '").append(this.view).append("'");
/*     */       } else {
/*     */         
/* 339 */         sb.append("View is [").append(this.view).append(']');
/*     */       } 
/* 341 */       if (useDefaultModel()) {
/* 342 */         sb.append("; default model ");
/*     */       } else {
/*     */         
/* 345 */         sb.append("; redirect model ");
/*     */       } 
/* 347 */       sb.append(getModel());
/*     */     } else {
/*     */       
/* 350 */       sb.append("Request handled directly");
/*     */     } 
/* 352 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/method/support/ModelAndViewContainer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */