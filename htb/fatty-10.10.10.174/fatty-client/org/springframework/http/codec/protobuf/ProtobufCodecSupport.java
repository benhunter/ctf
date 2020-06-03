/*    */ package org.springframework.http.codec.protobuf;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
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
/*    */ public abstract class ProtobufCodecSupport
/*    */ {
/* 34 */   static final List<MimeType> MIME_TYPES = Collections.unmodifiableList(
/* 35 */       Arrays.asList(new MimeType[] { new MimeType("application", "x-protobuf"), new MimeType("application", "octet-stream") }));
/*    */ 
/*    */   
/*    */   static final String DELIMITED_KEY = "delimited";
/*    */ 
/*    */   
/*    */   static final String DELIMITED_VALUE = "true";
/*    */ 
/*    */   
/*    */   protected boolean supportsMimeType(@Nullable MimeType mimeType) {
/* 45 */     return (mimeType == null || MIME_TYPES.stream().anyMatch(m -> m.isCompatibleWith(mimeType)));
/*    */   }
/*    */   
/*    */   protected List<MimeType> getMimeTypes() {
/* 49 */     return MIME_TYPES;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/codec/protobuf/ProtobufCodecSupport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */