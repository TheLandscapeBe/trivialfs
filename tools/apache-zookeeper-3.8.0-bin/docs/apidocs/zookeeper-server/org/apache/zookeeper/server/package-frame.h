<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!-- NewPage -->
<html lang="en">
<head>
<!-- Generated by javadoc (1.8.0_282) on Fri Feb 25 08:50:57 UTC 2022 -->
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>org.apache.zookeeper.server (Apache ZooKeeper - Server 3.8.0 API)</title>
<meta name="date" content="2022-02-25">
<link rel="stylesheet" type="text/css" href="../../../../stylesheet.css" title="Style">
<script type="text/javascript" src="../../../../script.js"></script>
</head>
<body>
<h1 class="bar"><a href="../../../../org/apache/zookeeper/server/package-summary.html" target="classFrame">org.apache.zookeeper.server</a></h1>
<div class="indexContainer">
<h2 title="Interfaces">Interfaces</h2>
<ul title="Interfaces">
<li><a href="ConnectionMXBean.html" title="interface in org.apache.zookeeper.server" target="classFrame"><span class="interfaceName">ConnectionMXBean</span></a></li>
<li><a href="DataTreeMXBean.html" title="interface in org.apache.zookeeper.server" target="classFrame"><span class="interfaceName">DataTreeMXBean</span></a></li>
<li><a href="NodeHashMap.html" title="interface in org.apache.zookeeper.server" target="classFrame"><span class="interfaceName">NodeHashMap</span></a></li>
<li><a href="RequestProcessor.html" title="interface in org.apache.zookeeper.server" target="classFrame"><span class="interfaceName">RequestProcessor</span></a></li>
<li><a href="ServerStats.Provider.html" title="interface in org.apache.zookeeper.server" target="classFrame"><span class="interfaceName">ServerStats.Provider</span></a></li>
<li><a href="SessionTracker.html" title="interface in org.apache.zookeeper.server" target="classFrame"><span class="interfaceName">SessionTracker</span></a></li>
<li><a href="SessionTracker.Session.html" title="interface in org.apache.zookeeper.server" target="classFrame"><span class="interfaceName">SessionTracker.Session</span></a></li>
<li><a href="SessionTracker.SessionExpirer.html" title="interface in org.apache.zookeeper.server" target="classFrame"><span class="interfaceName">SessionTracker.SessionExpirer</span></a></li>
<li><a href="ZooKeeperServerListener.html" title="interface in org.apache.zookeeper.server" target="classFrame"><span class="interfaceName">ZooKeeperServerListener</span></a></li>
<li><a href="ZooKeeperServerMXBean.html" title="interface in org.apache.zookeeper.server" target="classFrame"><span class="interfaceName">ZooKeeperServerMXBean</span></a></li>
</ul>
<h2 title="Classes">Classes</h2>
<ul title="Classes">
<li><a href="AuthenticationHelper.html" title="class in org.apache.zookeeper.server" target="classFrame">AuthenticationHelper</a></li>
<li><a href="BlueThrottle.html" title="class in org.apache.zookeeper.server" target="classFrame">BlueThrottle</a></li>
<li><a href="ByteBufferInputStream.html" title="class in org.apache.zookeeper.server" target="classFrame">ByteBufferInputStream</a></li>
<li><a href="ByteBufferOutputStream.html" title="class in org.apache.zookeeper.server" target="classFrame">ByteBufferOutputStream</a></li>
<li><a href="ConnectionBean.html" title="class in org.apache.zookeeper.server" target="classFrame">ConnectionBean</a></li>
<li><a href="ContainerManager.html" title="class in org.apache.zookeeper.server" target="classFrame">ContainerManager</a></li>
<li><a href="DatadirCleanupManager.html" title="class in org.apache.zookeeper.server" target="classFrame">DatadirCleanupManager</a></li>
<li><a href="DataNode.html" title="class in org.apache.zookeeper.server" target="classFrame">DataNode</a></li>
<li><a href="DataTree.html" title="class in org.apache.zookeeper.server" target="classFrame">DataTree</a></li>
<li><a href="DataTree.ProcessTxnResult.html" title="class in org.apache.zookeeper.server" target="classFrame">DataTree.ProcessTxnResult</a></li>
<li><a href="DataTreeBean.html" title="class in org.apache.zookeeper.server" target="classFrame">DataTreeBean</a></li>
<li><a href="DigestCalculator.html" title="class in org.apache.zookeeper.server" target="classFrame">DigestCalculator</a></li>
<li><a href="DumbWatcher.html" title="class in org.apache.zookeeper.server" target="classFrame">DumbWatcher</a></li>
<li><a href="ExpiryQueue.html" title="class in org.apache.zookeeper.server" target="classFrame">ExpiryQueue</a></li>
<li><a href="FinalRequestProcessor.html" title="class in org.apache.zookeeper.server" target="classFrame">FinalRequestProcessor</a></li>
<li><a href="NettyServerCnxn.html" title="class in org.apache.zookeeper.server" target="classFrame">NettyServerCnxn</a></li>
<li><a href="NettyServerCnxnFactory.html" title="class in org.apache.zookeeper.server" target="classFrame">NettyServerCnxnFactory</a></li>
<li><a href="NIOServerCnxn.html" title="class in org.apache.zookeeper.server" target="classFrame">NIOServerCnxn</a></li>
<li><a href="NIOServerCnxnFactory.html" title="class in org.apache.zookeeper.server" target="classFrame">NIOServerCnxnFactory</a></li>
<li><a href="NodeHashMapImpl.html" title="class in org.apache.zookeeper.server" target="classFrame">NodeHashMapImpl</a></li>
<li><a href="ObserverBean.html" title="class in org.apache.zookeeper.server" target="classFrame">ObserverBean</a></li>
<li><a href="PrepRequestProcessor.html" title="class in org.apache.zookeeper.server" target="classFrame">PrepRequestProcessor</a></li>
<li><a href="PurgeTxnLog.html" title="class in org.apache.zookeeper.server" target="classFrame">PurgeTxnLog</a></li>
<li><a href="RateLogger.html" title="class in org.apache.zookeeper.server" target="classFrame">RateLogger</a></li>
<li><a href="ReferenceCountedACLCache.html" title="class in org.apache.zookeeper.server" target="classFrame">ReferenceCountedACLCache</a></li>
<li><a href="Request.html" title="class in org.apache.zookeeper.server" target="classFrame">Request</a></li>
<li><a href="RequestThrottler.html" title="class in org.apache.zookeeper.server" target="classFrame">RequestThrottler</a></li>
<li><a href="ResponseCache.html" title="class in org.apache.zookeeper.server" target="classFrame">ResponseCache</a></li>
<li><a href="ServerCnxn.html" title="class in org.apache.zookeeper.server" target="classFrame">ServerCnxn</a></li>
<li><a href="ServerCnxnFactory.html" title="class in org.apache.zookeeper.server" target="classFrame">ServerCnxnFactory</a></li>
<li><a href="ServerCnxnHelper.html" title="class in org.apache.zookeeper.server" target="classFrame">ServerCnxnHelper</a></li>
<li><a href="ServerConfig.html" title="class in org.apache.zookeeper.server" target="classFrame">ServerConfig</a></li>
<li><a href="ServerMetrics.html" title="class in org.apache.zookeeper.server" target="classFrame">ServerMetrics</a></li>
<li><a href="ServerStats.html" title="class in org.apache.zookeeper.server" target="classFrame">ServerStats</a></li>
<li><a href="SessionTrackerImpl.html" title="class in org.apache.zookeeper.server" target="classFrame">SessionTrackerImpl</a></li>
<li><a href="SessionTrackerImpl.SessionImpl.html" title="class in org.apache.zookeeper.server" target="classFrame">SessionTrackerImpl.SessionImpl</a></li>
<li><a href="SnapshotComparer.html" title="class in org.apache.zookeeper.server" target="classFrame">SnapshotComparer</a></li>
<li><a href="SnapshotFormatter.html" title="class in org.apache.zookeeper.server" target="classFrame">SnapshotFormatter</a></li>
<li><a href="SyncRequestProcessor.html" title="class in org.apache.zookeeper.server" target="classFrame">SyncRequestProcessor</a></li>
<li><a href="TraceFormatter.html" title="class in org.apache.zookeeper.server" target="classFrame">TraceFormatter</a></li>
<li><a href="TxnLogEntry.html" title="class in org.apache.zookeeper.server" target="classFrame">TxnLogEntry</a></li>
<li><a href="TxnLogProposalIterator.html" title="class in org.apache.zookeeper.server" target="classFrame">TxnLogProposalIterator</a></li>
<li><a href="UnimplementedRequestProcessor.html" title="class in org.apache.zookeeper.server" target="classFrame">UnimplementedRequestProcessor</a></li>
<li><a href="WorkerService.html" title="class in org.apache.zookeeper.server" target="classFrame">WorkerService</a></li>
<li><a href="WorkerService.WorkRequest.html" title="class in org.apache.zookeeper.server" target="classFrame">WorkerService.WorkRequest</a></li>
<li><a href="ZKDatabase.html" title="class in org.apache.zookeeper.server" target="classFrame">ZKDatabase</a></li>
<li><a href="ZooKeeperCriticalThread.html" title="class in org.apache.zookeeper.server" target="classFrame">ZooKeeperCriticalThread</a></li>
<li><a href="ZooKeeperSaslServer.html" title="class in org.apache.zookeeper.server" target="classFrame">ZooKeeperSaslServer</a></li>
<li><a href="ZooKeeperServer.html" title="class in org.apache.zookeeper.server" target="classFrame">ZooKeeperServer</a></li>
<li><a href="ZooKeeperServerBean.html" title="class in org.apache.zookeeper.server" target="classFrame">ZooKeeperServerBean</a></li>
<li><a href="ZooKeeperServerConf.html" title="class in org.apache.zookeeper.server" target="classFrame">ZooKeeperServerConf</a></li>
<li><a href="ZooKeeperServerMain.html" title="class in org.apache.zookeeper.server" target="classFrame">ZooKeeperServerMain</a></li>
<li><a href="ZooKeeperServerShutdownHandler.html" title="class in org.apache.zookeeper.server" target="classFrame">ZooKeeperServerShutdownHandler</a></li>
<li><a href="ZooKeeperThread.html" title="class in org.apache.zookeeper.server" target="classFrame">ZooKeeperThread</a></li>
<li><a href="ZooTrace.html" title="class in org.apache.zookeeper.server" target="classFrame">ZooTrace</a></li>
</ul>
<h2 title="Enums">Enums</h2>
<ul title="Enums">
<li><a href="DatadirCleanupManager.PurgeTaskStatus.html" title="enum in org.apache.zookeeper.server" target="classFrame">DatadirCleanupManager.PurgeTaskStatus</a></li>
<li><a href="EphemeralType.html" title="enum in org.apache.zookeeper.server" target="classFrame">EphemeralType</a></li>
<li><a href="EphemeralTypeEmulate353.html" title="enum in org.apache.zookeeper.server" target="classFrame">EphemeralTypeEmulate353</a></li>
<li><a href="ExitCode.html" title="enum in org.apache.zookeeper.server" target="classFrame">ExitCode</a></li>
<li><a href="NettyServerCnxn.HandshakeState.html" title="enum in org.apache.zookeeper.server" target="classFrame">NettyServerCnxn.HandshakeState</a></li>
<li><a href="PrepRequestProcessor.DigestOpCode.html" title="enum in org.apache.zookeeper.server" target="classFrame">PrepRequestProcessor.DigestOpCode</a></li>
<li><a href="ServerCnxn.DisconnectReason.html" title="enum in org.apache.zookeeper.server" target="classFrame">ServerCnxn.DisconnectReason</a></li>
<li><a href="ZooKeeperServer.State.html" title="enum in org.apache.zookeeper.server" target="classFrame">ZooKeeperServer.State</a></li>
</ul>
<h2 title="Exceptions">Exceptions</h2>
<ul title="Exceptions">
<li><a href="ClientCnxnLimitException.html" title="class in org.apache.zookeeper.server" target="classFrame">ClientCnxnLimitException</a></li>
<li><a href="RequestProcessor.RequestProcessorException.html" title="class in org.apache.zookeeper.server" target="classFrame">RequestProcessor.RequestProcessorException</a></li>
<li><a href="ServerCnxn.CloseRequestException.html" title="class in org.apache.zookeeper.server" target="classFrame">ServerCnxn.CloseRequestException</a></li>
<li><a href="ServerCnxn.EndOfStreamException.html" title="class in org.apache.zookeeper.server" target="classFrame">ServerCnxn.EndOfStreamException</a></li>
<li><a href="ZooKeeperServer.MissingSessionException.html" title="class in org.apache.zookeeper.server" target="classFrame">ZooKeeperServer.MissingSessionException</a></li>
</ul>
</div>
</body>
</html>
