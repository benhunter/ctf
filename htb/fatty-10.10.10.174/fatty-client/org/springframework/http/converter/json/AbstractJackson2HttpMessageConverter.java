/*     */ package org.springframework.http.converter.json;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonEncoding;
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.core.JsonProcessingException;
/*     */ import com.fasterxml.jackson.core.PrettyPrinter;
/*     */ import com.fasterxml.jackson.core.util.DefaultIndenter;
/*     */ import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.ObjectMapper;
/*     */ import com.fasterxml.jackson.databind.ObjectWriter;
/*     */ import com.fasterxml.jackson.databind.SerializationConfig;
/*     */ import com.fasterxml.jackson.databind.SerializationFeature;
/*     */ import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
/*     */ import com.fasterxml.jackson.databind.ser.FilterProvider;
/*     */ import com.fasterxml.jackson.databind.type.TypeFactory;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Type;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import org.springframework.core.GenericTypeResolver;
/*     */ import org.springframework.http.HttpInputMessage;
/*     */ import org.springframework.http.HttpOutputMessage;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.http.converter.AbstractGenericHttpMessageConverter;
/*     */ import org.springframework.http.converter.HttpMessageConversionException;
/*     */ import org.springframework.http.converter.HttpMessageNotReadableException;
/*     */ import org.springframework.http.converter.HttpMessageNotWritableException;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.TypeUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractJackson2HttpMessageConverter
/*     */   extends AbstractGenericHttpMessageConverter<Object>
/*     */ {
/*  75 */   public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
/*     */ 
/*     */   
/*     */   protected ObjectMapper objectMapper;
/*     */   
/*     */   @Nullable
/*     */   private Boolean prettyPrint;
/*     */   
/*     */   @Nullable
/*     */   private PrettyPrinter ssePrettyPrinter;
/*     */ 
/*     */   
/*     */   protected AbstractJackson2HttpMessageConverter(ObjectMapper objectMapper) {
/*  88 */     this.objectMapper = objectMapper;
/*  89 */     setDefaultCharset(DEFAULT_CHARSET);
/*  90 */     DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
/*  91 */     prettyPrinter.indentObjectsWith((DefaultPrettyPrinter.Indenter)new DefaultIndenter("  ", "\ndata:"));
/*  92 */     this.ssePrettyPrinter = (PrettyPrinter)prettyPrinter;
/*     */   }
/*     */   
/*     */   protected AbstractJackson2HttpMessageConverter(ObjectMapper objectMapper, MediaType supportedMediaType) {
/*  96 */     this(objectMapper);
/*  97 */     setSupportedMediaTypes(Collections.singletonList(supportedMediaType));
/*     */   }
/*     */   
/*     */   protected AbstractJackson2HttpMessageConverter(ObjectMapper objectMapper, MediaType... supportedMediaTypes) {
/* 101 */     this(objectMapper);
/* 102 */     setSupportedMediaTypes(Arrays.asList(supportedMediaTypes));
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
/*     */   public void setObjectMapper(ObjectMapper objectMapper) {
/* 118 */     Assert.notNull(objectMapper, "ObjectMapper must not be null");
/* 119 */     this.objectMapper = objectMapper;
/* 120 */     configurePrettyPrint();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ObjectMapper getObjectMapper() {
/* 127 */     return this.objectMapper;
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
/* 140 */     this.prettyPrint = Boolean.valueOf(prettyPrint);
/* 141 */     configurePrettyPrint();
/*     */   }
/*     */   
/*     */   private void configurePrettyPrint() {
/* 145 */     if (this.prettyPrint != null) {
/* 146 */       this.objectMapper.configure(SerializationFeature.INDENT_OUTPUT, this.prettyPrint.booleanValue());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canRead(Class<?> clazz, @Nullable MediaType mediaType) {
/* 153 */     return canRead(clazz, (Class<?>)null, mediaType);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canRead(Type type, @Nullable Class<?> contextClass, @Nullable MediaType mediaType) {
/* 158 */     if (!canRead(mediaType)) {
/* 159 */       return false;
/*     */     }
/* 161 */     JavaType javaType = getJavaType(type, contextClass);
/* 162 */     AtomicReference<Throwable> causeRef = new AtomicReference<>();
/* 163 */     if (this.objectMapper.canDeserialize(javaType, causeRef)) {
/* 164 */       return true;
/*     */     }
/* 166 */     logWarningIfNecessary((Type)javaType, causeRef.get());
/* 167 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canWrite(Class<?> clazz, @Nullable MediaType mediaType) {
/* 172 */     if (!canWrite(mediaType)) {
/* 173 */       return false;
/*     */     }
/* 175 */     AtomicReference<Throwable> causeRef = new AtomicReference<>();
/* 176 */     if (this.objectMapper.canSerialize(clazz, causeRef)) {
/* 177 */       return true;
/*     */     }
/* 179 */     logWarningIfNecessary(clazz, causeRef.get());
/* 180 */     return false;
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
/*     */   protected void logWarningIfNecessary(Type type, @Nullable Throwable cause) {
/* 192 */     if (cause == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 197 */     boolean debugLevel = (cause instanceof com.fasterxml.jackson.databind.JsonMappingException && cause.getMessage().startsWith("Cannot find"));
/*     */     
/* 199 */     if (debugLevel ? this.logger.isDebugEnabled() : this.logger.isWarnEnabled()) {
/* 200 */       String msg = "Failed to evaluate Jackson " + ((type instanceof JavaType) ? "de" : "") + "serialization for type [" + type + "]";
/*     */       
/* 202 */       if (debugLevel) {
/* 203 */         this.logger.debug(msg, cause);
/*     */       }
/* 205 */       else if (this.logger.isDebugEnabled()) {
/* 206 */         this.logger.warn(msg, cause);
/*     */       } else {
/*     */         
/* 209 */         this.logger.warn(msg + ": " + cause);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object readInternal(Class<?> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
/* 218 */     JavaType javaType = getJavaType(clazz, (Class<?>)null);
/* 219 */     return readJavaType(javaType, inputMessage);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object read(Type type, @Nullable Class<?> contextClass, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
/* 226 */     JavaType javaType = getJavaType(type, contextClass);
/* 227 */     return readJavaType(javaType, inputMessage);
/*     */   }
/*     */   
/*     */   private Object readJavaType(JavaType javaType, HttpInputMessage inputMessage) throws IOException {
/*     */     try {
/* 232 */       if (inputMessage instanceof MappingJacksonInputMessage) {
/* 233 */         Class<?> deserializationView = ((MappingJacksonInputMessage)inputMessage).getDeserializationView();
/* 234 */         if (deserializationView != null) {
/* 235 */           return this.objectMapper.readerWithView(deserializationView).forType(javaType)
/* 236 */             .readValue(inputMessage.getBody());
/*     */         }
/*     */       } 
/* 239 */       return this.objectMapper.readValue(inputMessage.getBody(), javaType);
/*     */     }
/* 241 */     catch (InvalidDefinitionException ex) {
/* 242 */       throw new HttpMessageConversionException("Type definition error: " + ex.getType(), ex);
/*     */     }
/* 244 */     catch (JsonProcessingException ex) {
/* 245 */       throw new HttpMessageNotReadableException("JSON parse error: " + ex.getOriginalMessage(), ex, inputMessage);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeInternal(Object object, @Nullable Type type, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
/* 253 */     MediaType contentType = outputMessage.getHeaders().getContentType();
/* 254 */     JsonEncoding encoding = getJsonEncoding(contentType);
/* 255 */     JsonGenerator generator = this.objectMapper.getFactory().createGenerator(outputMessage.getBody(), encoding);
/*     */     try {
/* 257 */       writePrefix(generator, object);
/*     */       
/* 259 */       Object value = object;
/* 260 */       Class<?> serializationView = null;
/* 261 */       FilterProvider filters = null;
/* 262 */       JavaType javaType = null;
/*     */       
/* 264 */       if (object instanceof MappingJacksonValue) {
/* 265 */         MappingJacksonValue container = (MappingJacksonValue)object;
/* 266 */         value = container.getValue();
/* 267 */         serializationView = container.getSerializationView();
/* 268 */         filters = container.getFilters();
/*     */       } 
/* 270 */       if (type != null && TypeUtils.isAssignable(type, value.getClass())) {
/* 271 */         javaType = getJavaType(type, (Class<?>)null);
/*     */       }
/*     */ 
/*     */       
/* 275 */       ObjectWriter objectWriter = (serializationView != null) ? this.objectMapper.writerWithView(serializationView) : this.objectMapper.writer();
/* 276 */       if (filters != null) {
/* 277 */         objectWriter = objectWriter.with(filters);
/*     */       }
/* 279 */       if (javaType != null && javaType.isContainerType()) {
/* 280 */         objectWriter = objectWriter.forType(javaType);
/*     */       }
/* 282 */       SerializationConfig config = objectWriter.getConfig();
/* 283 */       if (contentType != null && contentType.isCompatibleWith(MediaType.TEXT_EVENT_STREAM) && config
/* 284 */         .isEnabled(SerializationFeature.INDENT_OUTPUT)) {
/* 285 */         objectWriter = objectWriter.with(this.ssePrettyPrinter);
/*     */       }
/* 287 */       objectWriter.writeValue(generator, value);
/*     */       
/* 289 */       writeSuffix(generator, object);
/* 290 */       generator.flush();
/*     */     }
/* 292 */     catch (InvalidDefinitionException ex) {
/* 293 */       throw new HttpMessageConversionException("Type definition error: " + ex.getType(), ex);
/*     */     }
/* 295 */     catch (JsonProcessingException ex) {
/* 296 */       throw new HttpMessageNotWritableException("Could not write JSON: " + ex.getOriginalMessage(), ex);
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
/*     */   protected void writePrefix(JsonGenerator generator, Object object) throws IOException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeSuffix(JsonGenerator generator, Object object) throws IOException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected JavaType getJavaType(Type type, @Nullable Class<?> contextClass) {
/* 324 */     TypeFactory typeFactory = this.objectMapper.getTypeFactory();
/* 325 */     return typeFactory.constructType(GenericTypeResolver.resolveType(type, contextClass));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected JsonEncoding getJsonEncoding(@Nullable MediaType contentType) {
/* 334 */     if (contentType != null && contentType.getCharset() != null) {
/* 335 */       Charset charset = contentType.getCharset();
/* 336 */       for (JsonEncoding encoding : JsonEncoding.values()) {
/* 337 */         if (charset.name().equals(encoding.getJavaName())) {
/* 338 */           return encoding;
/*     */         }
/*     */       } 
/*     */     } 
/* 342 */     return JsonEncoding.UTF8;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected MediaType getDefaultContentType(Object object) throws IOException {
/* 348 */     if (object instanceof MappingJacksonValue) {
/* 349 */       object = ((MappingJacksonValue)object).getValue();
/*     */     }
/* 351 */     return super.getDefaultContentType(object);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Long getContentLength(Object object, @Nullable MediaType contentType) throws IOException {
/* 356 */     if (object instanceof MappingJacksonValue) {
/* 357 */       object = ((MappingJacksonValue)object).getValue();
/*     */     }
/* 359 */     return super.getContentLength(object, contentType);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/converter/json/AbstractJackson2HttpMessageConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */