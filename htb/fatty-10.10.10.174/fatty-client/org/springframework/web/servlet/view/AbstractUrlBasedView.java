/*    */ package org.springframework.web.servlet.view;
/*    */ 
/*    */ import java.util.Locale;
/*    */ import org.springframework.beans.factory.InitializingBean;
/*    */ import org.springframework.lang.Nullable;
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
/*    */ public abstract class AbstractUrlBasedView
/*    */   extends AbstractView
/*    */   implements InitializingBean
/*    */ {
/*    */   @Nullable
/*    */   private String url;
/*    */   
/*    */   protected AbstractUrlBasedView() {}
/*    */   
/*    */   protected AbstractUrlBasedView(String url) {
/* 48 */     this.url = url;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setUrl(@Nullable String url) {
/* 57 */     this.url = url;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public String getUrl() {
/* 65 */     return this.url;
/*    */   }
/*    */ 
/*    */   
/*    */   public void afterPropertiesSet() throws Exception {
/* 70 */     if (isUrlRequired() && getUrl() == null) {
/* 71 */       throw new IllegalArgumentException("Property 'url' is required");
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected boolean isUrlRequired() {
/* 81 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean checkResource(Locale locale) throws Exception {
/* 93 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 98 */     return super.toString() + "; URL [" + getUrl() + "]";
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/view/AbstractUrlBasedView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */