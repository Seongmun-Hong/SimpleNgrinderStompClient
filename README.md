# NgrinderStompTest

test script example
```
import random
from net.grinder.script.Grinder import grinder
from net.grinder.script import Test
from net.grinder.plugin.http import HTTPRequest
from net.grinder.plugin.http import HTTPPluginControl
from org.slf4j import LoggerFactory
from ch.qos.logback.classic import Level
from ch.qos.logback.classic import Logger
from HTTPClient import NVPair
from org.json import JSONObject, JSONArray
from com.stomp.client import StompClient

test0 = Test(0, "conn")
class TestRunner:
	# initlialize a thread 
	def __init__(self):
		grinder.statistics.delayReports=True
		self.conn()

	# test method		
	def __call__(self):
		result = self.client.sendMsg("hello", "155")
		if not result:
			err("send msg error")
			grinder.statistics.forLastTest.success = 0
		else:
			grinder.statistics.forLastTest.success = 1

	def conn(self):
		self.client = StompClient()
		return self.client.connect("http://192.168.0.6:8080/gs-guide-websocket")

test0.record(TestRunner.conn)
```
