# Simple nGrinder Stomp Client

##### - This is simple client for stomp connection and message send test in ngrinder.


## useage 

1. **git clone** and **mvn build** or **download jar file**.
##### - <a href="https://bit.ly/2TaNkuv" target="_blank">download jar file</a>

2. run ngrinder and move to script tab

3. **uploaded jar file** to **lib** folder 

4. **create script** for **jyphon**

5. **import** stomp client
##### from com.stomp.client import StompClient

6. connection and message send 


## methods
#### 1. addHeader(String headerName, String headerValue)

#### 2. connect(String url)
  - 1 on success Return -1 on failure

#### 3. sendMessage(String topic, String msg)
  - 1 on success Return -1 on failure

#### 4. disconnect()

#### TODO:Subscribe...

## example test script
```
from net.grinder.script.Grinder import grinder
from net.grinder.script import Test
from org.slf4j import LoggerFactory
from HTTPClient import NVPair
from org.json import JSONObject, JSONArray
from com.stomp.client import StompClient

test0 = Test(0, "conn")
params = []
class TestRunner:
	def __init__(self):
		grinder.statistics.delayReports=True
		params.append(NVPair("json", "data"))
		self.conn()

	def __call__(self):
		result = self.client.sendMessage("/chatroom/151", params)
		if not result:
			err("send msg error")
			grinder.statistics.forLastTest.success = 0
		else:
			grinder.statistics.forLastTest.success = 1

	def conn(self):
		self.client = StompClient()
		return self.client.connect("http://localhost:8080/chat")

test0.record(TestRunner.conn)
```
