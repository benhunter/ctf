/*     */ package org.springframework.util.xml;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.NoSuchElementException;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.events.Characters;
/*     */ import javax.xml.stream.events.XMLEvent;
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
/*     */ class ListBasedXMLEventReader
/*     */   extends AbstractXMLEventReader
/*     */ {
/*     */   private final List<XMLEvent> events;
/*     */   @Nullable
/*     */   private XMLEvent currentEvent;
/*  45 */   private int cursor = 0;
/*     */ 
/*     */   
/*     */   public ListBasedXMLEventReader(List<XMLEvent> events) {
/*  49 */     Assert.notNull(events, "XMLEvent List must not be null");
/*  50 */     this.events = new ArrayList<>(events);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasNext() {
/*  56 */     return (this.cursor < this.events.size());
/*     */   }
/*     */ 
/*     */   
/*     */   public XMLEvent nextEvent() {
/*  61 */     if (hasNext()) {
/*  62 */       this.currentEvent = this.events.get(this.cursor);
/*  63 */       this.cursor++;
/*  64 */       return this.currentEvent;
/*     */     } 
/*     */     
/*  67 */     throw new NoSuchElementException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public XMLEvent peek() {
/*  74 */     if (hasNext()) {
/*  75 */       return this.events.get(this.cursor);
/*     */     }
/*     */     
/*  78 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getElementText() throws XMLStreamException {
/*  84 */     checkIfClosed();
/*  85 */     if (this.currentEvent == null || !this.currentEvent.isStartElement()) {
/*  86 */       throw new XMLStreamException("Not at START_ELEMENT: " + this.currentEvent);
/*     */     }
/*     */     
/*  89 */     StringBuilder builder = new StringBuilder();
/*     */     while (true) {
/*  91 */       XMLEvent event = nextEvent();
/*  92 */       if (event.isEndElement()) {
/*     */         break;
/*     */       }
/*  95 */       if (!event.isCharacters()) {
/*  96 */         throw new XMLStreamException("Unexpected non-text event: " + event);
/*     */       }
/*  98 */       Characters characters = event.asCharacters();
/*  99 */       if (!characters.isIgnorableWhiteSpace()) {
/* 100 */         builder.append(event.asCharacters().getData());
/*     */       }
/*     */     } 
/* 103 */     return builder.toString();
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public XMLEvent nextTag() throws XMLStreamException {
/*     */     XMLEvent event;
/* 109 */     checkIfClosed();
/*     */     
/*     */     while (true) {
/* 112 */       event = nextEvent();
/* 113 */       switch (event.getEventType()) {
/*     */         case 1:
/*     */         case 2:
/* 116 */           return event;
/*     */         case 8:
/* 118 */           return null;
/*     */         case 3:
/*     */         case 5:
/*     */         case 6:
/*     */           continue;
/*     */         case 4:
/*     */         case 12:
/* 125 */           if (!event.asCharacters().isWhiteSpace())
/* 126 */             throw new XMLStreamException("Non-ignorable whitespace CDATA or CHARACTERS event: " + event); 
/*     */           continue;
/*     */       } 
/*     */       break;
/*     */     } 
/* 131 */     throw new XMLStreamException("Expected START_ELEMENT or END_ELEMENT: " + event);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() {
/* 138 */     super.close();
/* 139 */     this.events.clear();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/util/xml/ListBasedXMLEventReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */