/*     */ package org.springframework.web.servlet.view.script;
/*     */ 
/*     */ import java.nio.charset.Charset;
/*     */ import javax.script.ScriptEngine;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ScriptTemplateConfigurer
/*     */   implements ScriptTemplateConfig
/*     */ {
/*     */   @Nullable
/*     */   private ScriptEngine engine;
/*     */   @Nullable
/*     */   private String engineName;
/*     */   @Nullable
/*     */   private Boolean sharedEngine;
/*     */   @Nullable
/*     */   private String[] scripts;
/*     */   @Nullable
/*     */   private String renderObject;
/*     */   @Nullable
/*     */   private String renderFunction;
/*     */   @Nullable
/*     */   private String contentType;
/*     */   @Nullable
/*     */   private Charset charset;
/*     */   @Nullable
/*     */   private String resourceLoaderPath;
/*     */   
/*     */   public ScriptTemplateConfigurer() {}
/*     */   
/*     */   public ScriptTemplateConfigurer(String engineName) {
/*  90 */     this.engineName = engineName;
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
/*     */   public void setEngine(@Nullable ScriptEngine engine) {
/* 104 */     this.engine = engine;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public ScriptEngine getEngine() {
/* 110 */     return this.engine;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEngineName(@Nullable String engineName) {
/* 120 */     this.engineName = engineName;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getEngineName() {
/* 126 */     return this.engineName;
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
/*     */   public void setSharedEngine(@Nullable Boolean sharedEngine) {
/* 143 */     this.sharedEngine = sharedEngine;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Boolean isSharedEngine() {
/* 149 */     return this.sharedEngine;
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
/*     */   public void setScripts(@Nullable String... scriptNames) {
/* 164 */     this.scripts = scriptNames;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String[] getScripts() {
/* 170 */     return this.scripts;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRenderObject(@Nullable String renderObject) {
/* 179 */     this.renderObject = renderObject;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getRenderObject() {
/* 185 */     return this.renderObject;
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
/*     */   public void setRenderFunction(@Nullable String renderFunction) {
/* 200 */     this.renderFunction = renderFunction;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getRenderFunction() {
/* 206 */     return this.renderFunction;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setContentType(@Nullable String contentType) {
/* 215 */     this.contentType = contentType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getContentType() {
/* 225 */     return this.contentType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCharset(@Nullable Charset charset) {
/* 233 */     this.charset = charset;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Charset getCharset() {
/* 239 */     return this.charset;
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
/*     */   public void setResourceLoaderPath(@Nullable String resourceLoaderPath) {
/* 251 */     this.resourceLoaderPath = resourceLoaderPath;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getResourceLoaderPath() {
/* 257 */     return this.resourceLoaderPath;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/view/script/ScriptTemplateConfigurer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */