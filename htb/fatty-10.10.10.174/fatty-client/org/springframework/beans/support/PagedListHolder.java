/*     */ package org.springframework.beans.support;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
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
/*     */ public class PagedListHolder<E>
/*     */   implements Serializable
/*     */ {
/*     */   public static final int DEFAULT_PAGE_SIZE = 10;
/*     */   public static final int DEFAULT_MAX_LINKED_PAGES = 10;
/*  67 */   private List<E> source = Collections.emptyList();
/*     */   
/*     */   @Nullable
/*     */   private Date refreshDate;
/*     */   
/*     */   @Nullable
/*     */   private SortDefinition sort;
/*     */   
/*     */   @Nullable
/*     */   private SortDefinition sortUsed;
/*     */   
/*  78 */   private int pageSize = 10;
/*     */   
/*  80 */   private int page = 0;
/*     */   
/*     */   private boolean newPageSet;
/*     */   
/*  84 */   private int maxLinkedPages = 10;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PagedListHolder() {
/*  93 */     this(new ArrayList<>(0));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PagedListHolder(List<E> source) {
/* 103 */     this(source, new MutableSortDefinition(true));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PagedListHolder(List<E> source, SortDefinition sort) {
/* 112 */     setSource(source);
/* 113 */     setSort(sort);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSource(List<E> source) {
/* 121 */     Assert.notNull(source, "Source List must not be null");
/* 122 */     this.source = source;
/* 123 */     this.refreshDate = new Date();
/* 124 */     this.sortUsed = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<E> getSource() {
/* 131 */     return this.source;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Date getRefreshDate() {
/* 139 */     return this.refreshDate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSort(@Nullable SortDefinition sort) {
/* 148 */     this.sort = sort;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public SortDefinition getSort() {
/* 156 */     return this.sort;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPageSize(int pageSize) {
/* 165 */     if (pageSize != this.pageSize) {
/* 166 */       this.pageSize = pageSize;
/* 167 */       if (!this.newPageSet) {
/* 168 */         this.page = 0;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getPageSize() {
/* 177 */     return this.pageSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPage(int page) {
/* 185 */     this.page = page;
/* 186 */     this.newPageSet = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getPage() {
/* 194 */     this.newPageSet = false;
/* 195 */     if (this.page >= getPageCount()) {
/* 196 */       this.page = getPageCount() - 1;
/*     */     }
/* 198 */     return this.page;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaxLinkedPages(int maxLinkedPages) {
/* 205 */     this.maxLinkedPages = maxLinkedPages;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMaxLinkedPages() {
/* 212 */     return this.maxLinkedPages;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getPageCount() {
/* 220 */     float nrOfPages = getNrOfElements() / getPageSize();
/* 221 */     return (int)((nrOfPages > (int)nrOfPages || nrOfPages == 0.0D) ? (nrOfPages + 1.0F) : nrOfPages);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFirstPage() {
/* 228 */     return (getPage() == 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isLastPage() {
/* 235 */     return (getPage() == getPageCount() - 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void previousPage() {
/* 243 */     if (!isFirstPage()) {
/* 244 */       this.page--;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void nextPage() {
/* 253 */     if (!isLastPage()) {
/* 254 */       this.page++;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getNrOfElements() {
/* 262 */     return getSource().size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getFirstElementOnPage() {
/* 270 */     return getPageSize() * getPage();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getLastElementOnPage() {
/* 278 */     int endIndex = getPageSize() * (getPage() + 1);
/* 279 */     int size = getNrOfElements();
/* 280 */     return ((endIndex > size) ? size : endIndex) - 1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<E> getPageList() {
/* 287 */     return getSource().subList(getFirstElementOnPage(), getLastElementOnPage() + 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getFirstLinkedPage() {
/* 294 */     return Math.max(0, getPage() - getMaxLinkedPages() / 2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getLastLinkedPage() {
/* 301 */     return Math.min(getFirstLinkedPage() + getMaxLinkedPages() - 1, getPageCount() - 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void resort() {
/* 312 */     SortDefinition sort = getSort();
/* 313 */     if (sort != null && !sort.equals(this.sortUsed)) {
/* 314 */       this.sortUsed = copySortDefinition(sort);
/* 315 */       doSort(getSource(), sort);
/* 316 */       setPage(0);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SortDefinition copySortDefinition(SortDefinition sort) {
/* 333 */     return new MutableSortDefinition(sort);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doSort(List<E> source, SortDefinition sort) {
/* 344 */     PropertyComparator.sort(source, sort);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/support/PagedListHolder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */