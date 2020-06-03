/*    */ package org.springframework.web.context.support;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import javax.servlet.ServletException;
/*    */ import javax.servlet.http.HttpServlet;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ import org.springframework.context.support.LiveBeansView;
/*    */ import org.springframework.lang.Nullable;
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
/*    */ public class LiveBeansViewServlet
/*    */   extends HttpServlet
/*    */ {
/*    */   @Nullable
/*    */   private LiveBeansView liveBeansView;
/*    */   
/*    */   public void init() throws ServletException {
/* 48 */     this.liveBeansView = buildLiveBeansView();
/*    */   }
/*    */   
/*    */   protected LiveBeansView buildLiveBeansView() {
/* 52 */     return new ServletContextLiveBeansView(getServletContext());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
/* 60 */     Assert.state((this.liveBeansView != null), "No LiveBeansView available");
/* 61 */     String content = this.liveBeansView.getSnapshotAsJson();
/* 62 */     response.setContentType("application/json");
/* 63 */     response.setContentLength(content.length());
/* 64 */     response.getWriter().write(content);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/context/support/LiveBeansViewServlet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */