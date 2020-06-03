/*    */ package org.springframework.web.servlet.view.tiles3;
/*    */ 
/*    */ import org.apache.tiles.request.render.Renderer;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.web.servlet.view.AbstractUrlBasedView;
/*    */ import org.springframework.web.servlet.view.UrlBasedViewResolver;
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
/*    */ public class TilesViewResolver
/*    */   extends UrlBasedViewResolver
/*    */ {
/*    */   @Nullable
/*    */   private Renderer renderer;
/*    */   @Nullable
/*    */   private Boolean alwaysInclude;
/*    */   
/*    */   public TilesViewResolver() {
/* 44 */     setViewClass(requiredViewClass());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected Class<?> requiredViewClass() {
/* 53 */     return TilesView.class;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setRenderer(Renderer renderer) {
/* 62 */     this.renderer = renderer;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setAlwaysInclude(Boolean alwaysInclude) {
/* 73 */     this.alwaysInclude = alwaysInclude;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected TilesView buildView(String viewName) throws Exception {
/* 79 */     TilesView view = (TilesView)super.buildView(viewName);
/* 80 */     if (this.renderer != null) {
/* 81 */       view.setRenderer(this.renderer);
/*    */     }
/* 83 */     if (this.alwaysInclude != null) {
/* 84 */       view.setAlwaysInclude(this.alwaysInclude.booleanValue());
/*    */     }
/* 86 */     return view;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/view/tiles3/TilesViewResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */