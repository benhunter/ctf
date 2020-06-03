/*     */ package org.springframework.web.servlet;
/*     */ 
/*     */ import java.util.Map;
/*     */ import org.springframework.http.HttpStatus;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.ui.ModelMap;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ModelAndView
/*     */ {
/*     */   @Nullable
/*     */   private Object view;
/*     */   @Nullable
/*     */   private ModelMap model;
/*     */   @Nullable
/*     */   private HttpStatus status;
/*     */   private boolean cleared = false;
/*     */   
/*     */   public ModelAndView() {}
/*     */   
/*     */   public ModelAndView(String viewName) {
/*  82 */     this.view = viewName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ModelAndView(View view) {
/*  92 */     this.view = view;
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
/*     */   public ModelAndView(String viewName, @Nullable Map<String, ?> model) {
/* 104 */     this.view = viewName;
/* 105 */     if (model != null) {
/* 106 */       getModelMap().addAllAttributes(model);
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
/*     */   public ModelAndView(View view, @Nullable Map<String, ?> model) {
/* 121 */     this.view = view;
/* 122 */     if (model != null) {
/* 123 */       getModelMap().addAllAttributes(model);
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
/*     */   public ModelAndView(String viewName, HttpStatus status) {
/* 136 */     this.view = viewName;
/* 137 */     this.status = status;
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
/*     */   public ModelAndView(@Nullable String viewName, @Nullable Map<String, ?> model, @Nullable HttpStatus status) {
/* 152 */     this.view = viewName;
/* 153 */     if (model != null) {
/* 154 */       getModelMap().addAllAttributes(model);
/*     */     }
/* 156 */     this.status = status;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ModelAndView(String viewName, String modelName, Object modelObject) {
/* 167 */     this.view = viewName;
/* 168 */     addObject(modelName, modelObject);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ModelAndView(View view, String modelName, Object modelObject) {
/* 178 */     this.view = view;
/* 179 */     addObject(modelName, modelObject);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setViewName(@Nullable String viewName) {
/* 189 */     this.view = viewName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getViewName() {
/* 198 */     return (this.view instanceof String) ? (String)this.view : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setView(@Nullable View view) {
/* 206 */     this.view = view;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public View getView() {
/* 215 */     return (this.view instanceof View) ? (View)this.view : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasView() {
/* 223 */     return (this.view != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isReference() {
/* 232 */     return this.view instanceof String;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected Map<String, Object> getModelInternal() {
/* 241 */     return (Map<String, Object>)this.model;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ModelMap getModelMap() {
/* 248 */     if (this.model == null) {
/* 249 */       this.model = new ModelMap();
/*     */     }
/* 251 */     return this.model;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, Object> getModel() {
/* 259 */     return (Map<String, Object>)getModelMap();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStatus(@Nullable HttpStatus status) {
/* 268 */     this.status = status;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public HttpStatus getStatus() {
/* 277 */     return this.status;
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
/*     */   public ModelAndView addObject(String attributeName, @Nullable Object attributeValue) {
/* 289 */     getModelMap().addAttribute(attributeName, attributeValue);
/* 290 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ModelAndView addObject(Object attributeValue) {
/* 300 */     getModelMap().addAttribute(attributeValue);
/* 301 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ModelAndView addAllObjects(@Nullable Map<String, ?> modelMap) {
/* 311 */     getModelMap().addAllAttributes(modelMap);
/* 312 */     return this;
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
/*     */   public void clear() {
/* 325 */     this.view = null;
/* 326 */     this.model = null;
/* 327 */     this.cleared = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 335 */     return (this.view == null && CollectionUtils.isEmpty((Map)this.model));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean wasCleared() {
/* 346 */     return (this.cleared && isEmpty());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 355 */     return "ModelAndView [view=" + formatView() + "; model=" + this.model + "]";
/*     */   }
/*     */   
/*     */   private String formatView() {
/* 359 */     return isReference() ? ("\"" + this.view + "\"") : ("[" + this.view + "]");
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/ModelAndView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */