/*    */ package org.springframework.web.accept;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
/*    */ import org.springframework.http.MediaType;
/*    */ import org.springframework.util.Assert;
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
/*    */ public class FixedContentNegotiationStrategy
/*    */   implements ContentNegotiationStrategy
/*    */ {
/* 37 */   private static final Log logger = LogFactory.getLog(FixedContentNegotiationStrategy.class);
/*    */ 
/*    */ 
/*    */   
/*    */   private final List<MediaType> contentTypes;
/*    */ 
/*    */ 
/*    */   
/*    */   public FixedContentNegotiationStrategy(MediaType contentType) {
/* 46 */     this(Collections.singletonList(contentType));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public FixedContentNegotiationStrategy(List<MediaType> contentTypes) {
/* 57 */     Assert.notNull(contentTypes, "'contentTypes' must not be null");
/* 58 */     this.contentTypes = Collections.unmodifiableList(contentTypes);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public List<MediaType> getContentTypes() {
/* 66 */     return this.contentTypes;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public List<MediaType> resolveMediaTypes(NativeWebRequest request) {
/* 72 */     return this.contentTypes;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/accept/FixedContentNegotiationStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */