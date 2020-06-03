/*    */ package org.springframework.http.converter.json;
/*    */ 
/*    */ import com.google.gson.GsonBuilder;
/*    */ import com.google.gson.JsonDeserializationContext;
/*    */ import com.google.gson.JsonDeserializer;
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonParseException;
/*    */ import com.google.gson.JsonPrimitive;
/*    */ import com.google.gson.JsonSerializationContext;
/*    */ import com.google.gson.JsonSerializer;
/*    */ import java.lang.reflect.Type;
/*    */ import org.springframework.util.Base64Utils;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class GsonBuilderUtils
/*    */ {
/*    */   public static GsonBuilder gsonBuilderWithBase64EncodedByteArrays() {
/* 52 */     GsonBuilder builder = new GsonBuilder();
/* 53 */     builder.registerTypeHierarchyAdapter(byte[].class, new Base64TypeAdapter());
/* 54 */     return builder;
/*    */   }
/*    */   
/*    */   private static class Base64TypeAdapter
/*    */     implements JsonSerializer<byte[]>, JsonDeserializer<byte[]> {
/*    */     private Base64TypeAdapter() {}
/*    */     
/*    */     public JsonElement serialize(byte[] src, Type typeOfSrc, JsonSerializationContext context) {
/* 62 */       return (JsonElement)new JsonPrimitive(Base64Utils.encodeToString(src));
/*    */     }
/*    */ 
/*    */     
/*    */     public byte[] deserialize(JsonElement json, Type type, JsonDeserializationContext cxt) {
/* 67 */       return Base64Utils.decodeFromString(json.getAsString());
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/converter/json/GsonBuilderUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */