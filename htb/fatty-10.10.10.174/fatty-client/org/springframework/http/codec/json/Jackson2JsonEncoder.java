/*    */ package org.springframework.http.codec.json;
/*    */ 
/*    */ import com.fasterxml.jackson.core.PrettyPrinter;
/*    */ import com.fasterxml.jackson.core.util.DefaultIndenter;
/*    */ import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
/*    */ import com.fasterxml.jackson.databind.ObjectMapper;
/*    */ import com.fasterxml.jackson.databind.ObjectWriter;
/*    */ import com.fasterxml.jackson.databind.SerializationFeature;
/*    */ import java.util.Collections;
/*    */ import java.util.Map;
/*    */ import org.springframework.core.ResolvableType;
/*    */ import org.springframework.http.MediaType;
/*    */ import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.util.MimeType;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Jackson2JsonEncoder
/*    */   extends AbstractJackson2Encoder
/*    */ {
/*    */   @Nullable
/*    */   private final PrettyPrinter ssePrettyPrinter;
/*    */   
/*    */   public Jackson2JsonEncoder() {
/* 54 */     this(Jackson2ObjectMapperBuilder.json().build(), new MimeType[0]);
/*    */   }
/*    */   
/*    */   public Jackson2JsonEncoder(ObjectMapper mapper, MimeType... mimeTypes) {
/* 58 */     super(mapper, mimeTypes);
/* 59 */     setStreamingMediaTypes(Collections.singletonList(MediaType.APPLICATION_STREAM_JSON));
/* 60 */     this.ssePrettyPrinter = initSsePrettyPrinter();
/*    */   }
/*    */   
/*    */   private static PrettyPrinter initSsePrettyPrinter() {
/* 64 */     DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
/* 65 */     printer.indentObjectsWith((DefaultPrettyPrinter.Indenter)new DefaultIndenter("  ", "\ndata:"));
/* 66 */     return (PrettyPrinter)printer;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected ObjectWriter customizeWriter(ObjectWriter writer, @Nullable MimeType mimeType, ResolvableType elementType, @Nullable Map<String, Object> hints) {
/* 74 */     return (this.ssePrettyPrinter != null && MediaType.TEXT_EVENT_STREAM
/* 75 */       .isCompatibleWith(mimeType) && writer
/* 76 */       .getConfig().isEnabled(SerializationFeature.INDENT_OUTPUT)) ? writer
/* 77 */       .with(this.ssePrettyPrinter) : writer;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/codec/json/Jackson2JsonEncoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */