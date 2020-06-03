/*     */ package org.springframework.web.servlet.view.json;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonView;
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.databind.ObjectMapper;
/*     */ import com.fasterxml.jackson.databind.ser.FilterProvider;
/*     */ import java.io.IOException;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
/*     */ import org.springframework.lang.Nullable;
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
/*     */ public class MappingJackson2JsonView
/*     */   extends AbstractJackson2View
/*     */ {
/*     */   public static final String DEFAULT_CONTENT_TYPE = "application/json";
/*     */   @Nullable
/*     */   private String jsonPrefix;
/*     */   @Nullable
/*     */   private Set<String> modelKeys;
/*     */   private boolean extractValueFromSingleKeyModel = false;
/*     */   
/*     */   public MappingJackson2JsonView() {
/*  78 */     super(Jackson2ObjectMapperBuilder.json().build(), "application/json");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MappingJackson2JsonView(ObjectMapper objectMapper) {
/*  87 */     super(objectMapper, "application/json");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setJsonPrefix(String jsonPrefix) {
/*  97 */     this.jsonPrefix = jsonPrefix;
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
/*     */   public void setPrefixJson(boolean prefixJson) {
/* 109 */     this.jsonPrefix = prefixJson ? ")]}', " : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setModelKey(String modelKey) {
/* 117 */     this.modelKeys = Collections.singleton(modelKey);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setModelKeys(@Nullable Set<String> modelKeys) {
/* 125 */     this.modelKeys = modelKeys;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public final Set<String> getModelKeys() {
/* 133 */     return this.modelKeys;
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
/*     */   public void setExtractValueFromSingleKeyModel(boolean extractValueFromSingleKeyModel) {
/* 145 */     this.extractValueFromSingleKeyModel = extractValueFromSingleKeyModel;
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
/*     */   protected Object filterModel(Map<String, Object> model) {
/* 158 */     Map<String, Object> result = new HashMap<>(model.size());
/* 159 */     Set<String> modelKeys = !CollectionUtils.isEmpty(this.modelKeys) ? this.modelKeys : model.keySet();
/* 160 */     model.forEach((clazz, value) -> {
/*     */           if (!(value instanceof org.springframework.validation.BindingResult) && modelKeys.contains(clazz) && !clazz.equals(JsonView.class.getName()) && !clazz.equals(FilterProvider.class.getName())) {
/*     */             result.put(clazz, value);
/*     */           }
/*     */         });
/*     */ 
/*     */     
/* 167 */     return (this.extractValueFromSingleKeyModel && result.size() == 1) ? result.values().iterator().next() : result;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void writePrefix(JsonGenerator generator, Object object) throws IOException {
/* 172 */     if (this.jsonPrefix != null)
/* 173 */       generator.writeRaw(this.jsonPrefix); 
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/view/json/MappingJackson2JsonView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */