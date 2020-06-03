/*    */ package org.springframework.web.accept;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import java.util.List;
/*    */ import org.springframework.http.InvalidMediaTypeException;
/*    */ import org.springframework.http.MediaType;
/*    */ import org.springframework.util.CollectionUtils;
/*    */ import org.springframework.web.HttpMediaTypeNotAcceptableException;
/*    */ import org.springframework.web.context.request.NativeWebRequest;
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
/*    */ public class HeaderContentNegotiationStrategy
/*    */   implements ContentNegotiationStrategy
/*    */ {
/*    */   public List<MediaType> resolveMediaTypes(NativeWebRequest request) throws HttpMediaTypeNotAcceptableException {
/* 46 */     String[] headerValueArray = request.getHeaderValues("Accept");
/* 47 */     if (headerValueArray == null) {
/* 48 */       return MEDIA_TYPE_ALL_LIST;
/*    */     }
/*    */     
/* 51 */     List<String> headerValues = Arrays.asList(headerValueArray);
/*    */     try {
/* 53 */       List<MediaType> mediaTypes = MediaType.parseMediaTypes(headerValues);
/* 54 */       MediaType.sortBySpecificityAndQuality(mediaTypes);
/* 55 */       return !CollectionUtils.isEmpty(mediaTypes) ? mediaTypes : MEDIA_TYPE_ALL_LIST;
/*    */     }
/* 57 */     catch (InvalidMediaTypeException ex) {
/* 58 */       throw new HttpMediaTypeNotAcceptableException("Could not parse 'Accept' header " + headerValues + ": " + ex
/* 59 */           .getMessage());
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/accept/HeaderContentNegotiationStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */