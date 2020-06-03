/*    */ package org.springframework.core.serializer.support;
/*    */ 
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import org.springframework.core.convert.converter.Converter;
/*    */ import org.springframework.core.serializer.DefaultSerializer;
/*    */ import org.springframework.core.serializer.Serializer;
/*    */ import org.springframework.util.Assert;
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
/*    */ public class SerializingConverter
/*    */   implements Converter<Object, byte[]>
/*    */ {
/*    */   private final Serializer<Object> serializer;
/*    */   
/*    */   public SerializingConverter() {
/* 44 */     this.serializer = (Serializer<Object>)new DefaultSerializer();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public SerializingConverter(Serializer<Object> serializer) {
/* 51 */     Assert.notNull(serializer, "Serializer must not be null");
/* 52 */     this.serializer = serializer;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public byte[] convert(Object source) {
/* 61 */     ByteArrayOutputStream byteStream = new ByteArrayOutputStream(1024);
/*    */     try {
/* 63 */       this.serializer.serialize(source, byteStream);
/* 64 */       return byteStream.toByteArray();
/*    */     }
/* 66 */     catch (Throwable ex) {
/* 67 */       throw new SerializationFailedException("Failed to serialize object using " + this.serializer
/* 68 */           .getClass().getSimpleName(), ex);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/serializer/support/SerializingConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */