/*    */ package org.apache.log4j.lf5.viewer.categoryexplorer;
/*    */ 
/*    */ import java.util.Enumeration;
/*    */ import javax.swing.tree.TreeNode;
/*    */ import org.apache.log4j.lf5.LogRecord;
/*    */ import org.apache.log4j.lf5.LogRecordFilter;
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
/*    */ public class CategoryExplorerLogRecordFilter
/*    */   implements LogRecordFilter
/*    */ {
/*    */   protected CategoryExplorerModel _model;
/*    */   
/*    */   public CategoryExplorerLogRecordFilter(CategoryExplorerModel model) {
/* 52 */     this._model = model;
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
/*    */ 
/*    */   
/*    */   public boolean passes(LogRecord record) {
/* 66 */     CategoryPath path = new CategoryPath(record.getCategory());
/* 67 */     return this._model.isCategoryPathActive(path);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void reset() {
/* 74 */     resetAllNodes();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void resetAllNodes() {
/* 82 */     Enumeration nodes = this._model.getRootCategoryNode().depthFirstEnumeration();
/*    */     
/* 84 */     while (nodes.hasMoreElements()) {
/* 85 */       CategoryNode current = (CategoryNode)nodes.nextElement();
/* 86 */       current.resetNumberOfContainedRecords();
/* 87 */       this._model.nodeChanged(current);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/apache/log4j/lf5/viewer/categoryexplorer/CategoryExplorerLogRecordFilter.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */