/*     */ package org.springframework.http.codec.json;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonView;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.ObjectMapper;
/*     */ import com.fasterxml.jackson.databind.type.TypeFactory;
/*     */ import java.lang.reflect.Type;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.springframework.core.GenericTypeResolver;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.core.ResolvableType;
/*     */ import org.springframework.core.codec.Hints;
/*     */ import org.springframework.http.HttpLogging;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.MimeType;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class Jackson2CodecSupport
/*     */ {
/*  57 */   public static final String JSON_VIEW_HINT = Jackson2CodecSupport.class.getName() + ".jsonView";
/*     */ 
/*     */   
/*     */   private static final String JSON_VIEW_HINT_ERROR = "@JsonView only supported for write hints with exactly 1 class argument: ";
/*     */   
/*  62 */   private static final List<MimeType> DEFAULT_MIME_TYPES = Collections.unmodifiableList(
/*  63 */       Arrays.asList(new MimeType[] { new MimeType("application", "json", StandardCharsets.UTF_8), new MimeType("application", "*+json", StandardCharsets.UTF_8) }));
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  68 */   protected final Log logger = HttpLogging.forLogName(getClass());
/*     */ 
/*     */   
/*     */   private final ObjectMapper objectMapper;
/*     */ 
/*     */   
/*     */   private final List<MimeType> mimeTypes;
/*     */ 
/*     */ 
/*     */   
/*     */   protected Jackson2CodecSupport(ObjectMapper objectMapper, MimeType... mimeTypes) {
/*  79 */     Assert.notNull(objectMapper, "ObjectMapper must not be null");
/*  80 */     this.objectMapper = objectMapper;
/*  81 */     this
/*  82 */       .mimeTypes = !ObjectUtils.isEmpty((Object[])mimeTypes) ? Collections.<MimeType>unmodifiableList(Arrays.asList(mimeTypes)) : DEFAULT_MIME_TYPES;
/*     */   }
/*     */ 
/*     */   
/*     */   public ObjectMapper getObjectMapper() {
/*  87 */     return this.objectMapper;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected List<MimeType> getMimeTypes() {
/*  94 */     return this.mimeTypes;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean supportsMimeType(@Nullable MimeType mimeType) {
/*  99 */     return (mimeType == null || this.mimeTypes.stream().anyMatch(m -> m.isCompatibleWith(mimeType)));
/*     */   }
/*     */   
/*     */   protected JavaType getJavaType(Type type, @Nullable Class<?> contextClass) {
/* 103 */     TypeFactory typeFactory = this.objectMapper.getTypeFactory();
/* 104 */     return typeFactory.constructType(GenericTypeResolver.resolveType(type, contextClass));
/*     */   }
/*     */   
/*     */   protected Map<String, Object> getHints(ResolvableType resolvableType) {
/* 108 */     MethodParameter param = getParameter(resolvableType);
/* 109 */     if (param != null) {
/* 110 */       JsonView annotation = getAnnotation(param, JsonView.class);
/* 111 */       if (annotation != null) {
/* 112 */         Class<?>[] classes = annotation.value();
/* 113 */         Assert.isTrue((classes.length == 1), "@JsonView only supported for write hints with exactly 1 class argument: " + param);
/* 114 */         return Hints.from(JSON_VIEW_HINT, classes[0]);
/*     */       } 
/*     */     } 
/* 117 */     return Hints.none();
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   protected MethodParameter getParameter(ResolvableType type) {
/* 122 */     return (type.getSource() instanceof MethodParameter) ? (MethodParameter)type.getSource() : null;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   protected abstract <A extends java.lang.annotation.Annotation> A getAnnotation(MethodParameter paramMethodParameter, Class<A> paramClass);
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/codec/json/Jackson2CodecSupport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */