/*     */ package org.springframework.web.jsf;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import javax.faces.context.FacesContext;
/*     */ import javax.faces.event.PhaseEvent;
/*     */ import javax.faces.event.PhaseId;
/*     */ import javax.faces.event.PhaseListener;
/*     */ import org.springframework.beans.factory.BeanFactoryUtils;
/*     */ import org.springframework.beans.factory.ListableBeanFactory;
/*     */ import org.springframework.web.context.WebApplicationContext;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DelegatingPhaseListenerMulticaster
/*     */   implements PhaseListener
/*     */ {
/*     */   public PhaseId getPhaseId() {
/*  67 */     return PhaseId.ANY_PHASE;
/*     */   }
/*     */ 
/*     */   
/*     */   public void beforePhase(PhaseEvent event) {
/*  72 */     for (PhaseListener listener : getDelegates(event.getFacesContext())) {
/*  73 */       listener.beforePhase(event);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void afterPhase(PhaseEvent event) {
/*  79 */     for (PhaseListener listener : getDelegates(event.getFacesContext())) {
/*  80 */       listener.afterPhase(event);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Collection<PhaseListener> getDelegates(FacesContext facesContext) {
/*  93 */     ListableBeanFactory bf = getBeanFactory(facesContext);
/*  94 */     return BeanFactoryUtils.beansOfTypeIncludingAncestors(bf, PhaseListener.class, true, false).values();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ListableBeanFactory getBeanFactory(FacesContext facesContext) {
/* 107 */     return (ListableBeanFactory)getWebApplicationContext(facesContext);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected WebApplicationContext getWebApplicationContext(FacesContext facesContext) {
/* 118 */     return FacesContextUtils.getRequiredWebApplicationContext(facesContext);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/jsf/DelegatingPhaseListenerMulticaster.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */