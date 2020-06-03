/*     */ package org.springframework.web.servlet.mvc.method.annotation;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Set;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.http.server.ServerHttpResponse;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SseEmitter
/*     */   extends ResponseBodyEmitter
/*     */ {
/*  42 */   static final MediaType TEXT_PLAIN = new MediaType("text", "plain", StandardCharsets.UTF_8);
/*     */   
/*  44 */   static final MediaType UTF8_TEXT_EVENTSTREAM = new MediaType("text", "event-stream", StandardCharsets.UTF_8);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SseEmitter() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SseEmitter(Long timeout) {
/*  63 */     super(timeout);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void extendResponse(ServerHttpResponse outputMessage) {
/*  69 */     super.extendResponse(outputMessage);
/*     */     
/*  71 */     HttpHeaders headers = outputMessage.getHeaders();
/*  72 */     if (headers.getContentType() == null) {
/*  73 */       headers.setContentType(UTF8_TEXT_EVENTSTREAM);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void send(Object object) throws IOException {
/*  95 */     send(object, null);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void send(Object object, @Nullable MediaType mediaType) throws IOException {
/* 116 */     send(event().data(object, mediaType));
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
/*     */   public void send(SseEventBuilder builder) throws IOException {
/* 130 */     Set<ResponseBodyEmitter.DataWithMediaType> dataToSend = builder.build();
/* 131 */     synchronized (this) {
/* 132 */       for (ResponseBodyEmitter.DataWithMediaType entry : dataToSend) {
/* 133 */         super.send(entry.getData(), entry.getMediaType());
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 140 */     return "SseEmitter@" + ObjectUtils.getIdentityHexString(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public static SseEventBuilder event() {
/* 145 */     return new SseEventBuilderImpl();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class SseEventBuilderImpl
/*     */     implements SseEventBuilder
/*     */   {
/* 198 */     private final Set<ResponseBodyEmitter.DataWithMediaType> dataToSend = new LinkedHashSet<>(4);
/*     */     
/*     */     @Nullable
/*     */     private StringBuilder sb;
/*     */ 
/*     */     
/*     */     public SseEmitter.SseEventBuilder id(String id) {
/* 205 */       append("id:").append(id).append("\n");
/* 206 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public SseEmitter.SseEventBuilder name(String name) {
/* 211 */       append("event:").append(name).append("\n");
/* 212 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public SseEmitter.SseEventBuilder reconnectTime(long reconnectTimeMillis) {
/* 217 */       append("retry:").append(String.valueOf(reconnectTimeMillis)).append("\n");
/* 218 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public SseEmitter.SseEventBuilder comment(String comment) {
/* 223 */       append(":").append(comment).append("\n");
/* 224 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public SseEmitter.SseEventBuilder data(Object object) {
/* 229 */       return data(object, null);
/*     */     }
/*     */ 
/*     */     
/*     */     public SseEmitter.SseEventBuilder data(Object object, @Nullable MediaType mediaType) {
/* 234 */       append("data:");
/* 235 */       saveAppendedText();
/* 236 */       this.dataToSend.add(new ResponseBodyEmitter.DataWithMediaType(object, mediaType));
/* 237 */       append("\n");
/* 238 */       return this;
/*     */     }
/*     */     
/*     */     SseEventBuilderImpl append(String text) {
/* 242 */       if (this.sb == null) {
/* 243 */         this.sb = new StringBuilder();
/*     */       }
/* 245 */       this.sb.append(text);
/* 246 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<ResponseBodyEmitter.DataWithMediaType> build() {
/* 251 */       if (!StringUtils.hasLength(this.sb) && this.dataToSend.isEmpty()) {
/* 252 */         return Collections.emptySet();
/*     */       }
/* 254 */       append("\n");
/* 255 */       saveAppendedText();
/* 256 */       return this.dataToSend;
/*     */     }
/*     */     
/*     */     private void saveAppendedText() {
/* 260 */       if (this.sb != null) {
/* 261 */         this.dataToSend.add(new ResponseBodyEmitter.DataWithMediaType(this.sb.toString(), SseEmitter.TEXT_PLAIN));
/* 262 */         this.sb = null;
/*     */       } 
/*     */     }
/*     */     
/*     */     private SseEventBuilderImpl() {}
/*     */   }
/*     */   
/*     */   public static interface SseEventBuilder {
/*     */     SseEventBuilder id(String param1String);
/*     */     
/*     */     SseEventBuilder name(String param1String);
/*     */     
/*     */     SseEventBuilder reconnectTime(long param1Long);
/*     */     
/*     */     SseEventBuilder comment(String param1String);
/*     */     
/*     */     SseEventBuilder data(Object param1Object);
/*     */     
/*     */     SseEventBuilder data(Object param1Object, @Nullable MediaType param1MediaType);
/*     */     
/*     */     Set<ResponseBodyEmitter.DataWithMediaType> build();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/mvc/method/annotation/SseEmitter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */