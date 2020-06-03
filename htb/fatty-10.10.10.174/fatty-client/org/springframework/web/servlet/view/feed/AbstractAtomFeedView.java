/*    */ package org.springframework.web.servlet.view.feed;
/*    */ 
/*    */ import com.rometools.rome.feed.WireFeed;
/*    */ import com.rometools.rome.feed.atom.Entry;
/*    */ import com.rometools.rome.feed.atom.Feed;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class AbstractAtomFeedView
/*    */   extends AbstractFeedView<Feed>
/*    */ {
/*    */   public static final String DEFAULT_FEED_TYPE = "atom_1.0";
/* 54 */   private String feedType = "atom_1.0";
/*    */ 
/*    */   
/*    */   public AbstractAtomFeedView() {
/* 58 */     setContentType("application/atom+xml");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setFeedType(String feedType) {
/* 68 */     this.feedType = feedType;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected Feed newFeed() {
/* 78 */     return new Feed(this.feedType);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected final void buildFeedEntries(Map<String, Object> model, Feed feed, HttpServletRequest request, HttpServletResponse response) throws Exception {
/* 89 */     List<Entry> entries = buildFeedEntries(model, request, response);
/* 90 */     feed.setEntries(entries);
/*    */   }
/*    */   
/*    */   protected abstract List<Entry> buildFeedEntries(Map<String, Object> paramMap, HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse) throws Exception;
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/view/feed/AbstractAtomFeedView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */