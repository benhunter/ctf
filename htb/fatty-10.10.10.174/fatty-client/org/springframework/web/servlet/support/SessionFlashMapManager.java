/*    */ package org.springframework.web.servlet.support;
/*    */ 
/*    */ import java.util.List;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ import javax.servlet.http.HttpSession;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.web.servlet.FlashMap;
/*    */ import org.springframework.web.util.WebUtils;
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
/*    */ public class SessionFlashMapManager
/*    */   extends AbstractFlashMapManager
/*    */ {
/* 37 */   private static final String FLASH_MAPS_SESSION_ATTRIBUTE = SessionFlashMapManager.class.getName() + ".FLASH_MAPS";
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   protected List<FlashMap> retrieveFlashMaps(HttpServletRequest request) {
/* 47 */     HttpSession session = request.getSession(false);
/* 48 */     return (session != null) ? (List<FlashMap>)session.getAttribute(FLASH_MAPS_SESSION_ATTRIBUTE) : null;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void updateFlashMaps(List<FlashMap> flashMaps, HttpServletRequest request, HttpServletResponse response) {
/* 56 */     WebUtils.setSessionAttribute(request, FLASH_MAPS_SESSION_ATTRIBUTE, !flashMaps.isEmpty() ? flashMaps : null);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected Object getFlashMapsMutex(HttpServletRequest request) {
/* 66 */     return WebUtils.getSessionMutex(request.getSession());
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/support/SessionFlashMapManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */