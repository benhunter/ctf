/*     */ package org.springframework.util;
/*     */ 
/*     */ import java.net.DatagramSocket;
/*     */ import java.net.InetAddress;
/*     */ import java.net.ServerSocket;
/*     */ import java.util.Random;
/*     */ import java.util.SortedSet;
/*     */ import java.util.TreeSet;
/*     */ import javax.net.ServerSocketFactory;
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
/*     */ public class SocketUtils
/*     */ {
/*     */   public static final int PORT_RANGE_MIN = 1024;
/*     */   public static final int PORT_RANGE_MAX = 65535;
/*  56 */   private static final Random random = new Random(System.currentTimeMillis());
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
/*     */   public static int findAvailableTcpPort() {
/*  86 */     return findAvailableTcpPort(1024);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int findAvailableTcpPort(int minPort) {
/*  97 */     return findAvailableTcpPort(minPort, 65535);
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
/*     */   public static int findAvailableTcpPort(int minPort, int maxPort) {
/* 109 */     return SocketType.TCP.findAvailablePort(minPort, maxPort);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static SortedSet<Integer> findAvailableTcpPorts(int numRequested) {
/* 120 */     return findAvailableTcpPorts(numRequested, 1024, 65535);
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
/*     */   public static SortedSet<Integer> findAvailableTcpPorts(int numRequested, int minPort, int maxPort) {
/* 133 */     return SocketType.TCP.findAvailablePorts(numRequested, minPort, maxPort);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int findAvailableUdpPort() {
/* 143 */     return findAvailableUdpPort(1024);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int findAvailableUdpPort(int minPort) {
/* 154 */     return findAvailableUdpPort(minPort, 65535);
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
/*     */   public static int findAvailableUdpPort(int minPort, int maxPort) {
/* 166 */     return SocketType.UDP.findAvailablePort(minPort, maxPort);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static SortedSet<Integer> findAvailableUdpPorts(int numRequested) {
/* 177 */     return findAvailableUdpPorts(numRequested, 1024, 65535);
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
/*     */   public static SortedSet<Integer> findAvailableUdpPorts(int numRequested, int minPort, int maxPort) {
/* 190 */     return SocketType.UDP.findAvailablePorts(numRequested, minPort, maxPort);
/*     */   }
/*     */ 
/*     */   
/*     */   private enum SocketType
/*     */   {
/* 196 */     TCP
/*     */     {
/*     */       protected boolean isPortAvailable(int port) {
/*     */         try {
/* 200 */           ServerSocket serverSocket = ServerSocketFactory.getDefault().createServerSocket(port, 1, 
/* 201 */               InetAddress.getByName("localhost"));
/* 202 */           serverSocket.close();
/* 203 */           return true;
/*     */         }
/* 205 */         catch (Exception ex) {
/* 206 */           return false;
/*     */         }
/*     */       
/*     */       }
/*     */     },
/* 211 */     UDP
/*     */     {
/*     */       protected boolean isPortAvailable(int port) {
/*     */         try {
/* 215 */           DatagramSocket socket = new DatagramSocket(port, InetAddress.getByName("localhost"));
/* 216 */           socket.close();
/* 217 */           return true;
/*     */         }
/* 219 */         catch (Exception ex) {
/* 220 */           return false;
/*     */         } 
/*     */       }
/*     */     };
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
/*     */     private int findRandomPort(int minPort, int maxPort) {
/* 239 */       int portRange = maxPort - minPort;
/* 240 */       return minPort + SocketUtils.random.nextInt(portRange + 1);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     int findAvailablePort(int minPort, int maxPort) {
/*     */       int candidatePort;
/* 252 */       Assert.isTrue((minPort > 0), "'minPort' must be greater than 0");
/* 253 */       Assert.isTrue((maxPort >= minPort), "'maxPort' must be greater than or equal to 'minPort'");
/* 254 */       Assert.isTrue((maxPort <= 65535), "'maxPort' must be less than or equal to 65535");
/*     */       
/* 256 */       int portRange = maxPort - minPort;
/*     */       
/* 258 */       int searchCounter = 0;
/*     */       do {
/* 260 */         if (searchCounter > portRange)
/* 261 */           throw new IllegalStateException(String.format("Could not find an available %s port in the range [%d, %d] after %d attempts", new Object[] {
/*     */                   
/* 263 */                   name(), Integer.valueOf(minPort), Integer.valueOf(maxPort), Integer.valueOf(searchCounter)
/*     */                 })); 
/* 265 */         candidatePort = findRandomPort(minPort, maxPort);
/* 266 */         searchCounter++;
/*     */       }
/* 268 */       while (!isPortAvailable(candidatePort));
/*     */       
/* 270 */       return candidatePort;
/*     */     }
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
/*     */     SortedSet<Integer> findAvailablePorts(int numRequested, int minPort, int maxPort) {
/* 283 */       Assert.isTrue((minPort > 0), "'minPort' must be greater than 0");
/* 284 */       Assert.isTrue((maxPort > minPort), "'maxPort' must be greater than 'minPort'");
/* 285 */       Assert.isTrue((maxPort <= 65535), "'maxPort' must be less than or equal to 65535");
/* 286 */       Assert.isTrue((numRequested > 0), "'numRequested' must be greater than 0");
/* 287 */       Assert.isTrue((maxPort - minPort >= numRequested), "'numRequested' must not be greater than 'maxPort' - 'minPort'");
/*     */ 
/*     */       
/* 290 */       SortedSet<Integer> availablePorts = new TreeSet<>();
/* 291 */       int attemptCount = 0;
/* 292 */       while (++attemptCount <= numRequested + 100 && availablePorts.size() < numRequested) {
/* 293 */         availablePorts.add(Integer.valueOf(findAvailablePort(minPort, maxPort)));
/*     */       }
/*     */       
/* 296 */       if (availablePorts.size() != numRequested) {
/* 297 */         throw new IllegalStateException(String.format("Could not find %d available %s ports in the range [%d, %d]", new Object[] {
/*     */                 
/* 299 */                 Integer.valueOf(numRequested), name(), Integer.valueOf(minPort), Integer.valueOf(maxPort)
/*     */               }));
/*     */       }
/* 302 */       return availablePorts;
/*     */     }
/*     */     
/*     */     protected abstract boolean isPortAvailable(int param1Int);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/util/SocketUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */