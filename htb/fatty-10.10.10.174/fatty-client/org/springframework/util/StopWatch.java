/*     */ package org.springframework.util;
/*     */ 
/*     */ import java.text.NumberFormat;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import org.springframework.lang.Nullable;
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
/*     */ public class StopWatch
/*     */ {
/*     */   private final String id;
/*     */   private boolean keepTaskList = true;
/*  54 */   private final List<TaskInfo> taskList = new LinkedList<>();
/*     */ 
/*     */   
/*     */   private long startTimeMillis;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private String currentTaskName;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private TaskInfo lastTaskInfo;
/*     */ 
/*     */   
/*     */   private int taskCount;
/*     */ 
/*     */   
/*     */   private long totalTimeMillis;
/*     */ 
/*     */ 
/*     */   
/*     */   public StopWatch() {
/*  76 */     this("");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StopWatch(String id) {
/*  87 */     this.id = id;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getId() {
/*  98 */     return this.id;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setKeepTaskList(boolean keepTaskList) {
/* 107 */     this.keepTaskList = keepTaskList;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void start() throws IllegalStateException {
/* 117 */     start("");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void start(String taskName) throws IllegalStateException {
/* 127 */     if (this.currentTaskName != null) {
/* 128 */       throw new IllegalStateException("Can't start StopWatch: it's already running");
/*     */     }
/* 130 */     this.currentTaskName = taskName;
/* 131 */     this.startTimeMillis = System.currentTimeMillis();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void stop() throws IllegalStateException {
/* 141 */     if (this.currentTaskName == null) {
/* 142 */       throw new IllegalStateException("Can't stop StopWatch: it's not running");
/*     */     }
/* 144 */     long lastTime = System.currentTimeMillis() - this.startTimeMillis;
/* 145 */     this.totalTimeMillis += lastTime;
/* 146 */     this.lastTaskInfo = new TaskInfo(this.currentTaskName, lastTime);
/* 147 */     if (this.keepTaskList) {
/* 148 */       this.taskList.add(this.lastTaskInfo);
/*     */     }
/* 150 */     this.taskCount++;
/* 151 */     this.currentTaskName = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isRunning() {
/* 159 */     return (this.currentTaskName != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String currentTaskName() {
/* 169 */     return this.currentTaskName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getLastTaskTimeMillis() throws IllegalStateException {
/* 177 */     if (this.lastTaskInfo == null) {
/* 178 */       throw new IllegalStateException("No tasks run: can't get last task interval");
/*     */     }
/* 180 */     return this.lastTaskInfo.getTimeMillis();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getLastTaskName() throws IllegalStateException {
/* 187 */     if (this.lastTaskInfo == null) {
/* 188 */       throw new IllegalStateException("No tasks run: can't get last task name");
/*     */     }
/* 190 */     return this.lastTaskInfo.getTaskName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TaskInfo getLastTaskInfo() throws IllegalStateException {
/* 197 */     if (this.lastTaskInfo == null) {
/* 198 */       throw new IllegalStateException("No tasks run: can't get last task info");
/*     */     }
/* 200 */     return this.lastTaskInfo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getTotalTimeMillis() {
/* 208 */     return this.totalTimeMillis;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getTotalTimeSeconds() {
/* 215 */     return this.totalTimeMillis / 1000.0D;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getTaskCount() {
/* 222 */     return this.taskCount;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TaskInfo[] getTaskInfo() {
/* 229 */     if (!this.keepTaskList) {
/* 230 */       throw new UnsupportedOperationException("Task info is not being kept!");
/*     */     }
/* 232 */     return this.taskList.<TaskInfo>toArray(new TaskInfo[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String shortSummary() {
/* 240 */     return "StopWatch '" + getId() + "': running time (millis) = " + getTotalTimeMillis();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String prettyPrint() {
/* 248 */     StringBuilder sb = new StringBuilder(shortSummary());
/* 249 */     sb.append('\n');
/* 250 */     if (!this.keepTaskList) {
/* 251 */       sb.append("No task info kept");
/*     */     } else {
/*     */       
/* 254 */       sb.append("-----------------------------------------\n");
/* 255 */       sb.append("ms     %     Task name\n");
/* 256 */       sb.append("-----------------------------------------\n");
/* 257 */       NumberFormat nf = NumberFormat.getNumberInstance();
/* 258 */       nf.setMinimumIntegerDigits(5);
/* 259 */       nf.setGroupingUsed(false);
/* 260 */       NumberFormat pf = NumberFormat.getPercentInstance();
/* 261 */       pf.setMinimumIntegerDigits(3);
/* 262 */       pf.setGroupingUsed(false);
/* 263 */       for (TaskInfo task : getTaskInfo()) {
/* 264 */         sb.append(nf.format(task.getTimeMillis())).append("  ");
/* 265 */         sb.append(pf.format(task.getTimeSeconds() / getTotalTimeSeconds())).append("  ");
/* 266 */         sb.append(task.getTaskName()).append("\n");
/*     */       } 
/*     */     } 
/* 269 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 278 */     StringBuilder sb = new StringBuilder(shortSummary());
/* 279 */     if (this.keepTaskList) {
/* 280 */       for (TaskInfo task : getTaskInfo()) {
/* 281 */         sb.append("; [").append(task.getTaskName()).append("] took ").append(task.getTimeMillis());
/* 282 */         long percent = Math.round(100.0D * task.getTimeSeconds() / getTotalTimeSeconds());
/* 283 */         sb.append(" = ").append(percent).append("%");
/*     */       } 
/*     */     } else {
/*     */       
/* 287 */       sb.append("; no task info kept");
/*     */     } 
/* 289 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class TaskInfo
/*     */   {
/*     */     private final String taskName;
/*     */ 
/*     */     
/*     */     private final long timeMillis;
/*     */ 
/*     */     
/*     */     TaskInfo(String taskName, long timeMillis) {
/* 303 */       this.taskName = taskName;
/* 304 */       this.timeMillis = timeMillis;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getTaskName() {
/* 311 */       return this.taskName;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public long getTimeMillis() {
/* 318 */       return this.timeMillis;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public double getTimeSeconds() {
/* 325 */       return this.timeMillis / 1000.0D;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/util/StopWatch.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */