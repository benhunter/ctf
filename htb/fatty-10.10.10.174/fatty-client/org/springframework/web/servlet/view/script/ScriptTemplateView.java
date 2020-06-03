/*     */ package org.springframework.web.servlet.view.script;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.function.Function;
/*     */ import javax.script.Invocable;
/*     */ import javax.script.ScriptEngine;
/*     */ import javax.script.ScriptEngineManager;
/*     */ import javax.script.ScriptException;
/*     */ import javax.script.SimpleBindings;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.BeanFactoryUtils;
/*     */ import org.springframework.beans.factory.ListableBeanFactory;
/*     */ import org.springframework.beans.factory.NoSuchBeanDefinitionException;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.context.ApplicationContextException;
/*     */ import org.springframework.core.NamedThreadLocal;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.scripting.support.StandardScriptEvalException;
/*     */ import org.springframework.scripting.support.StandardScriptUtils;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.FileCopyUtils;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.servlet.support.RequestContextUtils;
/*     */ import org.springframework.web.servlet.view.AbstractUrlBasedView;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ScriptTemplateView
/*     */   extends AbstractUrlBasedView
/*     */ {
/*     */   public static final String DEFAULT_CONTENT_TYPE = "text/html";
/*  79 */   private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
/*     */ 
/*     */   
/*     */   private static final String DEFAULT_RESOURCE_LOADER_PATH = "classpath:";
/*     */   
/*  84 */   private static final ThreadLocal<Map<Object, ScriptEngine>> enginesHolder = (ThreadLocal<Map<Object, ScriptEngine>>)new NamedThreadLocal("ScriptTemplateView engines");
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private ScriptEngine engine;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private String engineName;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private Boolean sharedEngine;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private String[] scripts;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private String renderObject;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private String renderFunction;
/*     */   
/*     */   @Nullable
/*     */   private Charset charset;
/*     */   
/*     */   @Nullable
/*     */   private String[] resourceLoaderPaths;
/*     */   
/*     */   @Nullable
/*     */   private volatile ScriptEngineManager scriptEngineManager;
/*     */ 
/*     */   
/*     */   public ScriptTemplateView() {
/* 121 */     setContentType(null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ScriptTemplateView(String url) {
/* 129 */     super(url);
/* 130 */     setContentType(null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEngine(ScriptEngine engine) {
/* 138 */     this.engine = engine;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEngineName(String engineName) {
/* 145 */     this.engineName = engineName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSharedEngine(Boolean sharedEngine) {
/* 152 */     this.sharedEngine = sharedEngine;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setScripts(String... scripts) {
/* 159 */     this.scripts = scripts;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRenderObject(String renderObject) {
/* 166 */     this.renderObject = renderObject;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRenderFunction(String functionName) {
/* 173 */     this.renderFunction = functionName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCharset(Charset charset) {
/* 180 */     this.charset = charset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setResourceLoaderPath(String resourceLoaderPath) {
/* 187 */     String[] paths = StringUtils.commaDelimitedListToStringArray(resourceLoaderPath);
/* 188 */     this.resourceLoaderPaths = new String[paths.length + 1];
/* 189 */     this.resourceLoaderPaths[0] = "";
/* 190 */     for (int i = 0; i < paths.length; i++) {
/* 191 */       String path = paths[i];
/* 192 */       if (!path.endsWith("/") && !path.endsWith(":")) {
/* 193 */         path = path + "/";
/*     */       }
/* 195 */       this.resourceLoaderPaths[i + 1] = path;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void initApplicationContext(ApplicationContext context) {
/* 202 */     super.initApplicationContext(context);
/*     */     
/* 204 */     ScriptTemplateConfig viewConfig = autodetectViewConfig();
/* 205 */     if (this.engine == null && viewConfig.getEngine() != null) {
/* 206 */       setEngine(viewConfig.getEngine());
/*     */     }
/* 208 */     if (this.engineName == null && viewConfig.getEngineName() != null) {
/* 209 */       this.engineName = viewConfig.getEngineName();
/*     */     }
/* 211 */     if (this.scripts == null && viewConfig.getScripts() != null) {
/* 212 */       this.scripts = viewConfig.getScripts();
/*     */     }
/* 214 */     if (this.renderObject == null && viewConfig.getRenderObject() != null) {
/* 215 */       this.renderObject = viewConfig.getRenderObject();
/*     */     }
/* 217 */     if (this.renderFunction == null && viewConfig.getRenderFunction() != null) {
/* 218 */       this.renderFunction = viewConfig.getRenderFunction();
/*     */     }
/* 220 */     if (getContentType() == null) {
/* 221 */       setContentType((viewConfig.getContentType() != null) ? viewConfig.getContentType() : "text/html");
/*     */     }
/* 223 */     if (this.charset == null) {
/* 224 */       this.charset = (viewConfig.getCharset() != null) ? viewConfig.getCharset() : DEFAULT_CHARSET;
/*     */     }
/* 226 */     if (this.resourceLoaderPaths == null) {
/* 227 */       String resourceLoaderPath = viewConfig.getResourceLoaderPath();
/* 228 */       setResourceLoaderPath((resourceLoaderPath != null) ? resourceLoaderPath : "classpath:");
/*     */     } 
/* 230 */     if (this.sharedEngine == null && viewConfig.isSharedEngine() != null) {
/* 231 */       this.sharedEngine = viewConfig.isSharedEngine();
/*     */     }
/*     */     
/* 234 */     Assert.isTrue((this.engine == null || this.engineName == null), "You should define either 'engine' or 'engineName', not both.");
/*     */     
/* 236 */     Assert.isTrue((this.engine != null || this.engineName != null), "No script engine found, please specify either 'engine' or 'engineName'.");
/*     */ 
/*     */     
/* 239 */     if (Boolean.FALSE.equals(this.sharedEngine)) {
/* 240 */       Assert.isTrue((this.engineName != null), "When 'sharedEngine' is set to false, you should specify the script engine using the 'engineName' property, not the 'engine' one.");
/*     */ 
/*     */     
/*     */     }
/* 244 */     else if (this.engine != null) {
/* 245 */       loadScripts(this.engine);
/*     */     } else {
/*     */       
/* 248 */       setEngine(createEngineFromName(this.engineName));
/*     */     } 
/*     */     
/* 251 */     if (this.renderFunction != null && this.engine != null) {
/* 252 */       Assert.isInstanceOf(Invocable.class, this.engine, "ScriptEngine must implement Invocable when 'renderFunction' is specified");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected ScriptEngine getEngine() {
/* 258 */     if (Boolean.FALSE.equals(this.sharedEngine)) {
/* 259 */       Map<Object, ScriptEngine> engines = enginesHolder.get();
/* 260 */       if (engines == null) {
/* 261 */         engines = new HashMap<>(4);
/* 262 */         enginesHolder.set(engines);
/*     */       } 
/* 264 */       Assert.state((this.engineName != null), "No engine name specified");
/* 265 */       Object engineKey = !ObjectUtils.isEmpty((Object[])this.scripts) ? new EngineKey(this.engineName, this.scripts) : this.engineName;
/*     */       
/* 267 */       ScriptEngine engine = engines.get(engineKey);
/* 268 */       if (engine == null) {
/* 269 */         engine = createEngineFromName(this.engineName);
/* 270 */         engines.put(engineKey, engine);
/*     */       } 
/* 272 */       return engine;
/*     */     } 
/*     */ 
/*     */     
/* 276 */     Assert.state((this.engine != null), "No shared engine available");
/* 277 */     return this.engine;
/*     */   }
/*     */ 
/*     */   
/*     */   protected ScriptEngine createEngineFromName(String engineName) {
/* 282 */     ScriptEngineManager scriptEngineManager = this.scriptEngineManager;
/* 283 */     if (scriptEngineManager == null) {
/* 284 */       scriptEngineManager = new ScriptEngineManager(obtainApplicationContext().getClassLoader());
/* 285 */       this.scriptEngineManager = scriptEngineManager;
/*     */     } 
/*     */     
/* 288 */     ScriptEngine engine = StandardScriptUtils.retrieveEngineByName(scriptEngineManager, engineName);
/* 289 */     loadScripts(engine);
/* 290 */     return engine;
/*     */   }
/*     */   
/*     */   protected void loadScripts(ScriptEngine engine) {
/* 294 */     if (!ObjectUtils.isEmpty((Object[])this.scripts)) {
/* 295 */       for (String script : this.scripts) {
/* 296 */         Resource resource = getResource(script);
/* 297 */         if (resource == null) {
/* 298 */           throw new IllegalStateException("Script resource [" + script + "] not found");
/*     */         }
/*     */         try {
/* 301 */           engine.eval(new InputStreamReader(resource.getInputStream()));
/*     */         }
/* 303 */         catch (Throwable ex) {
/* 304 */           throw new IllegalStateException("Failed to evaluate script [" + script + "]", ex);
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   protected Resource getResource(String location) {
/* 312 */     if (this.resourceLoaderPaths != null) {
/* 313 */       for (String path : this.resourceLoaderPaths) {
/* 314 */         Resource resource = obtainApplicationContext().getResource(path + location);
/* 315 */         if (resource.exists()) {
/* 316 */           return resource;
/*     */         }
/*     */       } 
/*     */     }
/* 320 */     return null;
/*     */   }
/*     */   
/*     */   protected ScriptTemplateConfig autodetectViewConfig() throws BeansException {
/*     */     try {
/* 325 */       return (ScriptTemplateConfig)BeanFactoryUtils.beanOfTypeIncludingAncestors((ListableBeanFactory)
/* 326 */           obtainApplicationContext(), ScriptTemplateConfig.class, true, false);
/*     */     }
/* 328 */     catch (NoSuchBeanDefinitionException ex) {
/* 329 */       throw new ApplicationContextException("Expected a single ScriptTemplateConfig bean in the current Servlet web application context or the parent root context: ScriptTemplateConfigurer is the usual implementation. This bean may have any name.", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean checkResource(Locale locale) throws Exception {
/* 338 */     String url = getUrl();
/* 339 */     Assert.state((url != null), "'url' not set");
/* 340 */     return (getResource(url) != null);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void prepareResponse(HttpServletRequest request, HttpServletResponse response) {
/* 345 */     super.prepareResponse(request, response);
/*     */     
/* 347 */     setResponseContentType(request, response);
/* 348 */     if (this.charset != null) {
/* 349 */       response.setCharacterEncoding(this.charset.name());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
/*     */     try {
/*     */       Object html;
/* 358 */       ScriptEngine engine = getEngine();
/* 359 */       String url = getUrl();
/* 360 */       Assert.state((url != null), "'url' not set");
/* 361 */       String template = getTemplate(url);
/*     */       
/* 363 */       Function<String, String> templateLoader = path -> {
/*     */           
/*     */           try {
/*     */             return getTemplate(path);
/* 367 */           } catch (IOException ex) {
/*     */             throw new IllegalStateException(ex);
/*     */           } 
/*     */         };
/*     */       
/* 372 */       Locale locale = RequestContextUtils.getLocale(request);
/* 373 */       RenderingContext context = new RenderingContext(obtainApplicationContext(), locale, templateLoader, url);
/*     */ 
/*     */       
/* 376 */       if (this.renderFunction == null) {
/* 377 */         SimpleBindings bindings = new SimpleBindings();
/* 378 */         bindings.putAll(model);
/* 379 */         model.put("renderingContext", context);
/* 380 */         html = engine.eval(template, bindings);
/*     */       }
/* 382 */       else if (this.renderObject != null) {
/* 383 */         Object thiz = engine.eval(this.renderObject);
/* 384 */         html = ((Invocable)engine).invokeMethod(thiz, this.renderFunction, new Object[] { template, model, context });
/*     */       } else {
/*     */         
/* 387 */         html = ((Invocable)engine).invokeFunction(this.renderFunction, new Object[] { template, model, context });
/*     */       } 
/*     */       
/* 390 */       response.getWriter().write(String.valueOf(html));
/*     */     }
/* 392 */     catch (ScriptException ex) {
/* 393 */       throw new ServletException("Failed to render script template", new StandardScriptEvalException(ex));
/*     */     } 
/*     */   }
/*     */   
/*     */   protected String getTemplate(String path) throws IOException {
/* 398 */     Resource resource = getResource(path);
/* 399 */     if (resource == null) {
/* 400 */       throw new IllegalStateException("Template resource [" + path + "] not found");
/*     */     }
/*     */ 
/*     */     
/* 404 */     InputStreamReader reader = (this.charset != null) ? new InputStreamReader(resource.getInputStream(), this.charset) : new InputStreamReader(resource.getInputStream());
/* 405 */     return FileCopyUtils.copyToString(reader);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class EngineKey
/*     */   {
/*     */     private final String engineName;
/*     */ 
/*     */     
/*     */     private final String[] scripts;
/*     */ 
/*     */ 
/*     */     
/*     */     public EngineKey(String engineName, String[] scripts) {
/* 421 */       this.engineName = engineName;
/* 422 */       this.scripts = scripts;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object other) {
/* 427 */       if (this == other) {
/* 428 */         return true;
/*     */       }
/* 430 */       if (!(other instanceof EngineKey)) {
/* 431 */         return false;
/*     */       }
/* 433 */       EngineKey otherKey = (EngineKey)other;
/* 434 */       return (this.engineName.equals(otherKey.engineName) && Arrays.equals((Object[])this.scripts, (Object[])otherKey.scripts));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 439 */       return this.engineName.hashCode() * 29 + Arrays.hashCode((Object[])this.scripts);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/view/script/ScriptTemplateView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */