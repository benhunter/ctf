/*    */ package org.apache.log4j.lf5.viewer.categoryexplorer;
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
/*    */ 
/*    */ 
/*    */ public class CategoryElement
/*    */ {
/*    */   protected String _categoryTitle;
/*    */   
/*    */   public CategoryElement() {}
/*    */   
/*    */   public CategoryElement(String title) {
/* 51 */     this._categoryTitle = title;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getTitle() {
/* 59 */     return this._categoryTitle;
/*    */   }
/*    */   
/*    */   public void setTitle(String title) {
/* 63 */     this._categoryTitle = title;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/apache/log4j/lf5/viewer/categoryexplorer/CategoryElement.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */