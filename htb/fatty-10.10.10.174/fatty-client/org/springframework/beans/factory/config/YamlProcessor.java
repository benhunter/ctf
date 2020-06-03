/*     */ package org.springframework.beans.factory.config;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.core.CollectionFactory;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.yaml.snakeyaml.LoaderOptions;
/*     */ import org.yaml.snakeyaml.Yaml;
/*     */ import org.yaml.snakeyaml.reader.UnicodeReader;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class YamlProcessor
/*     */ {
/*  52 */   private final Log logger = LogFactory.getLog(getClass());
/*     */   
/*  54 */   private ResolutionMethod resolutionMethod = ResolutionMethod.OVERRIDE;
/*     */   
/*  56 */   private Resource[] resources = new Resource[0];
/*     */   
/*  58 */   private List<DocumentMatcher> documentMatchers = Collections.emptyList();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean matchDefault = true;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDocumentMatchers(DocumentMatcher... matchers) {
/*  90 */     this.documentMatchers = Arrays.asList(matchers);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMatchDefault(boolean matchDefault) {
/*  99 */     this.matchDefault = matchDefault;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setResolutionMethod(ResolutionMethod resolutionMethod) {
/* 108 */     Assert.notNull(resolutionMethod, "ResolutionMethod must not be null");
/* 109 */     this.resolutionMethod = resolutionMethod;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setResources(Resource... resources) {
/* 117 */     this.resources = resources;
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
/*     */   protected void process(MatchCallback callback) {
/* 132 */     Yaml yaml = createYaml();
/* 133 */     for (Resource resource : this.resources) {
/* 134 */       boolean found = process(callback, yaml, resource);
/* 135 */       if (this.resolutionMethod == ResolutionMethod.FIRST_FOUND && found) {
/*     */         return;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Yaml createYaml() {
/* 148 */     LoaderOptions options = new LoaderOptions();
/* 149 */     options.setAllowDuplicateKeys(false);
/* 150 */     return new Yaml(options);
/*     */   }
/*     */   
/*     */   private boolean process(MatchCallback callback, Yaml yaml, Resource resource) {
/* 154 */     int count = 0;
/*     */     try {
/* 156 */       if (this.logger.isDebugEnabled()) {
/* 157 */         this.logger.debug("Loading from YAML: " + resource);
/*     */       }
/* 159 */       try (UnicodeReader null = new UnicodeReader(resource.getInputStream())) {
/* 160 */         for (Object object : yaml.loadAll((Reader)unicodeReader)) {
/* 161 */           if (object != null && process(asMap(object), callback)) {
/* 162 */             count++;
/* 163 */             if (this.resolutionMethod == ResolutionMethod.FIRST_FOUND) {
/*     */               break;
/*     */             }
/*     */           } 
/*     */         } 
/* 168 */         if (this.logger.isDebugEnabled()) {
/* 169 */           this.logger.debug("Loaded " + count + " document" + ((count > 1) ? "s" : "") + " from YAML resource: " + resource);
/*     */         }
/*     */       }
/*     */     
/*     */     }
/* 174 */     catch (IOException ex) {
/* 175 */       handleProcessError(resource, ex);
/*     */     } 
/* 177 */     return (count > 0);
/*     */   }
/*     */   
/*     */   private void handleProcessError(Resource resource, IOException ex) {
/* 181 */     if (this.resolutionMethod != ResolutionMethod.FIRST_FOUND && this.resolutionMethod != ResolutionMethod.OVERRIDE_AND_IGNORE)
/*     */     {
/* 183 */       throw new IllegalStateException(ex);
/*     */     }
/* 185 */     if (this.logger.isWarnEnabled()) {
/* 186 */       this.logger.warn("Could not load map from " + resource + ": " + ex.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Map<String, Object> asMap(Object object) {
/* 193 */     Map<String, Object> result = new LinkedHashMap<>();
/* 194 */     if (!(object instanceof Map)) {
/*     */       
/* 196 */       result.put("document", object);
/* 197 */       return result;
/*     */     } 
/*     */     
/* 200 */     Map<Object, Object> map = (Map<Object, Object>)object;
/* 201 */     map.forEach((key, value) -> {
/*     */           if (value instanceof Map) {
/*     */             value = (Object<String, Object>)asMap(value);
/*     */           }
/*     */           
/*     */           if (key instanceof CharSequence) {
/*     */             result.put(key.toString(), value);
/*     */           } else {
/*     */             result.put("[" + key.toString() + "]", value);
/*     */           } 
/*     */         });
/*     */     
/* 213 */     return result;
/*     */   }
/*     */   
/*     */   private boolean process(Map<String, Object> map, MatchCallback callback) {
/* 217 */     Properties properties = CollectionFactory.createStringAdaptingProperties();
/* 218 */     properties.putAll(getFlattenedMap(map));
/*     */     
/* 220 */     if (this.documentMatchers.isEmpty()) {
/* 221 */       if (this.logger.isDebugEnabled()) {
/* 222 */         this.logger.debug("Merging document (no matchers set): " + map);
/*     */       }
/* 224 */       callback.process(properties, map);
/* 225 */       return true;
/*     */     } 
/*     */     
/* 228 */     MatchStatus result = MatchStatus.ABSTAIN;
/* 229 */     for (DocumentMatcher matcher : this.documentMatchers) {
/* 230 */       MatchStatus match = matcher.matches(properties);
/* 231 */       result = MatchStatus.getMostSpecific(match, result);
/* 232 */       if (match == MatchStatus.FOUND) {
/* 233 */         if (this.logger.isDebugEnabled()) {
/* 234 */           this.logger.debug("Matched document with document matcher: " + properties);
/*     */         }
/* 236 */         callback.process(properties, map);
/* 237 */         return true;
/*     */       } 
/*     */     } 
/*     */     
/* 241 */     if (result == MatchStatus.ABSTAIN && this.matchDefault) {
/* 242 */       if (this.logger.isDebugEnabled()) {
/* 243 */         this.logger.debug("Matched document with default matcher: " + map);
/*     */       }
/* 245 */       callback.process(properties, map);
/* 246 */       return true;
/*     */     } 
/*     */     
/* 249 */     if (this.logger.isDebugEnabled()) {
/* 250 */       this.logger.debug("Unmatched document: " + map);
/*     */     }
/* 252 */     return false;
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
/*     */   protected final Map<String, Object> getFlattenedMap(Map<String, Object> source) {
/* 265 */     Map<String, Object> result = new LinkedHashMap<>();
/* 266 */     buildFlattenedMap(result, source, null);
/* 267 */     return result;
/*     */   }
/*     */   
/*     */   private void buildFlattenedMap(Map<String, Object> result, Map<String, Object> source, @Nullable String path) {
/* 271 */     source.forEach((key, value) -> {
/*     */           if (StringUtils.hasText(path)) {
/*     */             if (key.startsWith("[")) {
/*     */               key = path + key;
/*     */             } else {
/*     */               key = path + '.' + key;
/*     */             } 
/*     */           }
/*     */           if (value instanceof String) {
/*     */             result.put(key, value);
/*     */           } else if (value instanceof Map) {
/*     */             Map<String, Object> map = (Map<String, Object>)value;
/*     */             buildFlattenedMap(result, map, key);
/*     */           } else if (value instanceof Collection) {
/*     */             Collection<Object> collection = (Collection<Object>)value;
/*     */             if (collection.isEmpty()) {
/*     */               result.put(key, "");
/*     */             } else {
/*     */               int count = 0;
/*     */               for (Object object : collection) {
/*     */                 buildFlattenedMap(result, Collections.singletonMap("[" + count++ + "]", object), key);
/*     */               }
/*     */             } 
/*     */           } else {
/*     */             result.put(key, (value != null) ? value : "");
/*     */           } 
/*     */         });
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
/*     */   public static interface MatchCallback
/*     */   {
/*     */     void process(Properties param1Properties, Map<String, Object> param1Map);
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
/*     */   public static interface DocumentMatcher
/*     */   {
/*     */     YamlProcessor.MatchStatus matches(Properties param1Properties);
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
/*     */   public enum MatchStatus
/*     */   {
/* 349 */     FOUND,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 354 */     NOT_FOUND,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 359 */     ABSTAIN;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static MatchStatus getMostSpecific(MatchStatus a, MatchStatus b) {
/* 365 */       return (a.ordinal() < b.ordinal()) ? a : b;
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
/*     */   public enum ResolutionMethod
/*     */   {
/* 378 */     OVERRIDE,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 383 */     OVERRIDE_AND_IGNORE,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 388 */     FIRST_FOUND;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/config/YamlProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */