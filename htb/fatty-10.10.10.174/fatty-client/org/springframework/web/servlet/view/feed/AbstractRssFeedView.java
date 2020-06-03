/*    */ package org.springframework.web.servlet.view.feed;
/*    */ 
/*    */ import com.rometools.rome.feed.WireFeed;
/*    */ import com.rometools.rome.feed.rss.Channel;
/*    */ import com.rometools.rome.feed.rss.Item;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
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
/*    */ 
/*    */ public abstract class AbstractRssFeedView
/*    */   extends AbstractFeedView<Channel>
/*    */ {
/*    */   public AbstractRssFeedView() {
/* 51 */     setContentType("application/rss+xml");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected Channel newFeed() {
/* 61 */     return new Channel("rss_2.0");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected final void buildFeedEntries(Map<String, Object> model, Channel channel, HttpServletRequest request, HttpServletResponse response) throws Exception {
/* 72 */     List<Item> items = buildFeedItems(model, request, response);
/* 73 */     channel.setItems(items);
/*    */   }
/*    */   
/*    */   protected abstract List<Item> buildFeedItems(Map<String, Object> paramMap, HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse) throws Exception;
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/view/feed/AbstractRssFeedView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */