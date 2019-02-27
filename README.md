# NgrinderStompTest



nGrinder test script example with jython

```
from net.grinder.script.Grinder import grinder
from net.grinder.script import Test
from org.slf4j import LoggerFactory
from HTTPClient import NVPair
from org.json import JSONObject, JSONArray
from com.stomp.client import StompClient

test0 = Test(0, "conn")
class TestRunner:
	def __init__(self):
		grinder.statistics.delayReports=True
		self.conn()

	def __call__(self):
		result = self.client.sendMessage("/chatroom/151", "{\"sender\":1,\"msg\":\"msg\",\"msg_type\":\"m\"}")
		if not result:
			err("send msg error")
			grinder.statistics.forLastTest.success = 0
		else:
			grinder.statistics.forLastTest.success = 1
		self.client.disconnect()

	def conn(self):
		self.client = StompClient()
		return self.client.connect("http://localhost:8080/chat")

test0.record(TestRunner.conn)
```
