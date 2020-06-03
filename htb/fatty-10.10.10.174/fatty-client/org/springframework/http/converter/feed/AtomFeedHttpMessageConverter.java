/*    */ package org.springframework.http.converter.feed;
/*    */ 
/*    */ import com.rometools.rome.feed.atom.Feed;
/*    */ import org.springframework.http.MediaType;
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
/*    */ public class AtomFeedHttpMessageConverter
/*    */   extends AbstractWireFeedHttpMessageConverter<Feed>
/*    */ {
/*    */   public AtomFeedHttpMessageConverter() {
/* 41 */     super(new MediaType("application", "atom+xml"));
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean supports(Class<?> clazz) {
/* 46 */     return Feed.class.isAssignableFrom(clazz);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/converter/feed/AtomFeedHttpMessageConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */