/*     */ package org.springframework.web.servlet.view.xml;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonView;
/*     */ import com.fasterxml.jackson.databind.ObjectMapper;
/*     */ import com.fasterxml.jackson.dataformat.xml.XmlMapper;
/*     */ import java.util.Map;
/*     */ import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.web.servlet.view.json.AbstractJackson2View;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MappingJackson2XmlView
/*     */   extends AbstractJackson2View
/*     */ {
/*     */   public static final String DEFAULT_CONTENT_TYPE = "application/xml";
/*     */   @Nullable
/*     */   private String modelKey;
/*     */   
/*     */   public MappingJackson2XmlView() {
/*  65 */     super(Jackson2ObjectMapperBuilder.xml().build(), "application/xml");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MappingJackson2XmlView(XmlMapper xmlMapper) {
/*  74 */     super((ObjectMapper)xmlMapper, "application/xml");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setModelKey(String modelKey) {
/*  80 */     this.modelKey = modelKey;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Object filterModel(Map<String, Object> model) {
/*  85 */     Object value = null;
/*  86 */     if (this.modelKey != null) {
/*  87 */       value = model.get(this.modelKey);
/*  88 */       if (value == null) {
/*  89 */         throw new IllegalStateException("Model contains no object with key [" + this.modelKey + "]");
/*     */       }
/*     */     }
/*     */     else {
/*     */       
/*  94 */       for (Map.Entry<String, Object> entry : model.entrySet()) {
/*  95 */         if (!(entry.getValue() instanceof org.springframework.validation.BindingResult) && !((String)entry.getKey()).equals(JsonView.class.getName())) {
/*  96 */           if (value != null) {
/*  97 */             throw new IllegalStateException("Model contains more than one object to render, only one is supported");
/*     */           }
/*  99 */           value = entry.getValue();
/*     */         } 
/*     */       } 
/*     */     } 
/* 103 */     Assert.state((value != null), "Model contains no object to render");
/* 104 */     return value;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/view/xml/MappingJackson2XmlView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */