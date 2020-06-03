/*    */ package org.springframework.web.servlet.view.feed;
/*    */ 
/*    */ import com.rometools.rome.feed.WireFeed;
/*    */ import com.rometools.rome.io.WireFeedOutput;
/*    */ import java.io.OutputStream;
/*    */ import java.io.OutputStreamWriter;
/*    */ import java.util.Map;
/*    */ import javax.servlet.ServletOutputStream;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ import org.springframework.util.StringUtils;
/*    */ import org.springframework.web.servlet.view.AbstractView;
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
/*    */ public abstract class AbstractFeedView<T extends WireFeed>
/*    */   extends AbstractView
/*    */ {
/*    */   protected final void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
/* 57 */     T wireFeed = newFeed();
/* 58 */     buildFeedMetadata(model, wireFeed, request);
/* 59 */     buildFeedEntries(model, wireFeed, request, response);
/*    */     
/* 61 */     setResponseContentType(request, response);
/* 62 */     if (!StringUtils.hasText(wireFeed.getEncoding())) {
/* 63 */       wireFeed.setEncoding("UTF-8");
/*    */     }
/*    */     
/* 66 */     WireFeedOutput feedOutput = new WireFeedOutput();
/* 67 */     ServletOutputStream out = response.getOutputStream();
/* 68 */     feedOutput.output((WireFeed)wireFeed, new OutputStreamWriter((OutputStream)out, wireFeed.getEncoding()));
/* 69 */     out.flush();
/*    */   }
/*    */   
/*    */   protected abstract T newFeed();
/*    */   
/*    */   protected void buildFeedMetadata(Map<String, Object> model, T feed, HttpServletRequest request) {}
/*    */   
/*    */   protected abstract void buildFeedEntries(Map<String, Object> paramMap, T paramT, HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse) throws Exception;
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/view/feed/AbstractFeedView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */