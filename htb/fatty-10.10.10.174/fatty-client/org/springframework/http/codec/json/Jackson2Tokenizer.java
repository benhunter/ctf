/*     */ package org.springframework.http.codec.json;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonFactory;
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonProcessingException;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.core.async.ByteArrayFeeder;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.ObjectMapper;
/*     */ import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext;
/*     */ import com.fasterxml.jackson.databind.util.TokenBuffer;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.springframework.core.codec.DecodingException;
/*     */ import org.springframework.core.io.buffer.DataBuffer;
/*     */ import org.springframework.core.io.buffer.DataBufferUtils;
/*     */ import reactor.core.publisher.Flux;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class Jackson2Tokenizer
/*     */ {
/*     */   private final JsonParser parser;
/*     */   private final DeserializationContext deserializationContext;
/*     */   private final boolean tokenizeArrayElements;
/*     */   private TokenBuffer tokenBuffer;
/*     */   private int objectDepth;
/*     */   private int arrayDepth;
/*     */   private final ByteArrayFeeder inputFeeder;
/*     */   
/*     */   private Jackson2Tokenizer(JsonParser parser, DeserializationContext deserializationContext, boolean tokenizeArrayElements) {
/*  71 */     this.parser = parser;
/*  72 */     this.deserializationContext = deserializationContext;
/*  73 */     this.tokenizeArrayElements = tokenizeArrayElements;
/*  74 */     this.tokenBuffer = new TokenBuffer(parser, deserializationContext);
/*  75 */     this.inputFeeder = (ByteArrayFeeder)this.parser.getNonBlockingInputFeeder();
/*     */   }
/*     */ 
/*     */   
/*     */   private Flux<TokenBuffer> tokenize(DataBuffer dataBuffer) {
/*  80 */     byte[] bytes = new byte[dataBuffer.readableByteCount()];
/*  81 */     dataBuffer.read(bytes);
/*  82 */     DataBufferUtils.release(dataBuffer);
/*     */     
/*     */     try {
/*  85 */       this.inputFeeder.feedInput(bytes, 0, bytes.length);
/*  86 */       return parseTokenBufferFlux();
/*     */     }
/*  88 */     catch (JsonProcessingException ex) {
/*  89 */       return Flux.error((Throwable)new DecodingException("JSON decoding error: " + ex.getOriginalMessage(), (Throwable)ex));
/*     */     }
/*  91 */     catch (IOException ex) {
/*  92 */       return Flux.error(ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   private Flux<TokenBuffer> endOfInput() {
/*  97 */     this.inputFeeder.endOfInput();
/*     */     try {
/*  99 */       return parseTokenBufferFlux();
/*     */     }
/* 101 */     catch (JsonProcessingException ex) {
/* 102 */       return Flux.error((Throwable)new DecodingException("JSON decoding error: " + ex.getOriginalMessage(), (Throwable)ex));
/*     */     }
/* 104 */     catch (IOException ex) {
/* 105 */       return Flux.error(ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   private Flux<TokenBuffer> parseTokenBufferFlux() throws IOException {
/* 110 */     List<TokenBuffer> result = new ArrayList<>();
/*     */     
/*     */     while (true) {
/* 113 */       JsonToken token = this.parser.nextToken();
/*     */       
/* 115 */       if (token == JsonToken.NOT_AVAILABLE || (token == null && (
/* 116 */         token = this.parser.nextToken()) == null)) {
/*     */         break;
/*     */       }
/* 119 */       updateDepth(token);
/* 120 */       if (!this.tokenizeArrayElements) {
/* 121 */         processTokenNormal(token, result);
/*     */         continue;
/*     */       } 
/* 124 */       processTokenArray(token, result);
/*     */     } 
/*     */     
/* 127 */     return Flux.fromIterable(result);
/*     */   }
/*     */   
/*     */   private void updateDepth(JsonToken token) {
/* 131 */     switch (token) {
/*     */       case START_OBJECT:
/* 133 */         this.objectDepth++;
/*     */         break;
/*     */       case END_OBJECT:
/* 136 */         this.objectDepth--;
/*     */         break;
/*     */       case START_ARRAY:
/* 139 */         this.arrayDepth++;
/*     */         break;
/*     */       case END_ARRAY:
/* 142 */         this.arrayDepth--;
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void processTokenNormal(JsonToken token, List<TokenBuffer> result) throws IOException {
/* 148 */     this.tokenBuffer.copyCurrentEvent(this.parser);
/*     */     
/* 150 */     if ((token.isStructEnd() || token.isScalarValue()) && this.objectDepth == 0 && this.arrayDepth == 0) {
/* 151 */       result.add(this.tokenBuffer);
/* 152 */       this.tokenBuffer = new TokenBuffer(this.parser, this.deserializationContext);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void processTokenArray(JsonToken token, List<TokenBuffer> result) throws IOException {
/* 158 */     if (!isTopLevelArrayToken(token)) {
/* 159 */       this.tokenBuffer.copyCurrentEvent(this.parser);
/*     */     }
/*     */     
/* 162 */     if (this.objectDepth == 0 && (this.arrayDepth == 0 || this.arrayDepth == 1) && (token == JsonToken.END_OBJECT || token
/* 163 */       .isScalarValue())) {
/* 164 */       result.add(this.tokenBuffer);
/* 165 */       this.tokenBuffer = new TokenBuffer(this.parser, this.deserializationContext);
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean isTopLevelArrayToken(JsonToken token) {
/* 170 */     return (this.objectDepth == 0 && ((token == JsonToken.START_ARRAY && this.arrayDepth == 1) || (token == JsonToken.END_ARRAY && this.arrayDepth == 0)));
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
/*     */   public static Flux<TokenBuffer> tokenize(Flux<DataBuffer> dataBuffers, JsonFactory jsonFactory, ObjectMapper objectMapper, boolean tokenizeArrayElements) {
/*     */     try {
/*     */       DefaultDeserializationContext defaultDeserializationContext;
/* 188 */       JsonParser parser = jsonFactory.createNonBlockingByteArrayParser();
/* 189 */       DeserializationContext context = objectMapper.getDeserializationContext();
/* 190 */       if (context instanceof DefaultDeserializationContext) {
/* 191 */         defaultDeserializationContext = ((DefaultDeserializationContext)context).createInstance(objectMapper
/* 192 */             .getDeserializationConfig(), parser, objectMapper.getInjectableValues());
/*     */       }
/* 194 */       Jackson2Tokenizer tokenizer = new Jackson2Tokenizer(parser, (DeserializationContext)defaultDeserializationContext, tokenizeArrayElements);
/* 195 */       return dataBuffers.flatMap(tokenizer::tokenize, Flux::error, tokenizer::endOfInput);
/*     */     }
/* 197 */     catch (IOException ex) {
/* 198 */       return Flux.error(ex);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/codec/json/Jackson2Tokenizer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */