/*     */ package org.springframework.web.servlet.view.json;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonView;
/*     */ import com.fasterxml.jackson.core.JsonEncoding;
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.databind.ObjectMapper;
/*     */ import com.fasterxml.jackson.databind.ObjectWriter;
/*     */ import com.fasterxml.jackson.databind.SerializationFeature;
/*     */ import com.fasterxml.jackson.databind.ser.FilterProvider;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.util.Map;
/*     */ import javax.servlet.ServletOutputStream;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.springframework.http.converter.json.MappingJacksonValue;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.web.servlet.view.AbstractView;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractJackson2View
/*     */   extends AbstractView
/*     */ {
/*     */   private ObjectMapper objectMapper;
/*  56 */   private JsonEncoding encoding = JsonEncoding.UTF8;
/*     */   
/*     */   @Nullable
/*     */   private Boolean prettyPrint;
/*     */   
/*     */   private boolean disableCaching = true;
/*     */   
/*     */   protected boolean updateContentLength = false;
/*     */ 
/*     */   
/*     */   protected AbstractJackson2View(ObjectMapper objectMapper, String contentType) {
/*  67 */     this.objectMapper = objectMapper;
/*  68 */     configurePrettyPrint();
/*  69 */     setContentType(contentType);
/*  70 */     setExposePathVariables(false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setObjectMapper(ObjectMapper objectMapper) {
/*  81 */     this.objectMapper = objectMapper;
/*  82 */     configurePrettyPrint();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ObjectMapper getObjectMapper() {
/*  89 */     return this.objectMapper;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEncoding(JsonEncoding encoding) {
/*  97 */     Assert.notNull(encoding, "'encoding' must not be null");
/*  98 */     this.encoding = encoding;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final JsonEncoding getEncoding() {
/* 105 */     return this.encoding;
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
/*     */   public void setPrettyPrint(boolean prettyPrint) {
/* 118 */     this.prettyPrint = Boolean.valueOf(prettyPrint);
/* 119 */     configurePrettyPrint();
/*     */   }
/*     */   
/*     */   private void configurePrettyPrint() {
/* 123 */     if (this.prettyPrint != null) {
/* 124 */       this.objectMapper.configure(SerializationFeature.INDENT_OUTPUT, this.prettyPrint.booleanValue());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDisableCaching(boolean disableCaching) {
/* 133 */     this.disableCaching = disableCaching;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUpdateContentLength(boolean updateContentLength) {
/* 143 */     this.updateContentLength = updateContentLength;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void prepareResponse(HttpServletRequest request, HttpServletResponse response) {
/* 148 */     setResponseContentType(request, response);
/* 149 */     response.setCharacterEncoding(this.encoding.getJavaName());
/* 150 */     if (this.disableCaching) {
/* 151 */       response.addHeader("Cache-Control", "no-store");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
/*     */     ServletOutputStream servletOutputStream;
/* 159 */     ByteArrayOutputStream temporaryStream = null;
/*     */ 
/*     */     
/* 162 */     if (this.updateContentLength) {
/* 163 */       temporaryStream = createTemporaryOutputStream();
/* 164 */       OutputStream stream = temporaryStream;
/*     */     } else {
/*     */       
/* 167 */       servletOutputStream = response.getOutputStream();
/*     */     } 
/*     */     
/* 170 */     Object value = filterAndWrapModel(model, request);
/* 171 */     writeContent((OutputStream)servletOutputStream, value);
/*     */     
/* 173 */     if (temporaryStream != null) {
/* 174 */       writeToResponse(response, temporaryStream);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object filterAndWrapModel(Map<String, Object> model, HttpServletRequest request) {
/* 185 */     Object value = filterModel(model);
/* 186 */     Class<?> serializationView = (Class)model.get(JsonView.class.getName());
/* 187 */     FilterProvider filters = (FilterProvider)model.get(FilterProvider.class.getName());
/* 188 */     if (serializationView != null || filters != null) {
/* 189 */       MappingJacksonValue container = new MappingJacksonValue(value);
/* 190 */       if (serializationView != null) {
/* 191 */         container.setSerializationView(serializationView);
/*     */       }
/* 193 */       if (filters != null) {
/* 194 */         container.setFilters(filters);
/*     */       }
/* 196 */       value = container;
/*     */     } 
/* 198 */     return value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeContent(OutputStream stream, Object object) throws IOException {
/* 208 */     JsonGenerator generator = this.objectMapper.getFactory().createGenerator(stream, this.encoding);
/* 209 */     writePrefix(generator, object);
/*     */     
/* 211 */     Object value = object;
/* 212 */     Class<?> serializationView = null;
/* 213 */     FilterProvider filters = null;
/*     */     
/* 215 */     if (value instanceof MappingJacksonValue) {
/* 216 */       MappingJacksonValue container = (MappingJacksonValue)value;
/* 217 */       value = container.getValue();
/* 218 */       serializationView = container.getSerializationView();
/* 219 */       filters = container.getFilters();
/*     */     } 
/*     */ 
/*     */     
/* 223 */     ObjectWriter objectWriter = (serializationView != null) ? this.objectMapper.writerWithView(serializationView) : this.objectMapper.writer();
/* 224 */     if (filters != null) {
/* 225 */       objectWriter = objectWriter.with(filters);
/*     */     }
/* 227 */     objectWriter.writeValue(generator, value);
/*     */     
/* 229 */     writeSuffix(generator, object);
/* 230 */     generator.flush();
/*     */   }
/*     */   
/*     */   public abstract void setModelKey(String paramString);
/*     */   
/*     */   protected abstract Object filterModel(Map<String, Object> paramMap);
/*     */   
/*     */   protected void writePrefix(JsonGenerator generator, Object object) throws IOException {}
/*     */   
/*     */   protected void writeSuffix(JsonGenerator generator, Object object) throws IOException {}
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/view/json/AbstractJackson2View.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */