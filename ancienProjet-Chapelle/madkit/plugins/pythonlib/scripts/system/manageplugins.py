from madkit.kernel import Agent
from madkit.kernel import StringMessage


def activate():
	self.println("salut")
        plugins = self.getAgentsWithRole("system","plugin")
        self.println(str(len(plugins)))
        for x in plugins:
                self.println(str(x))
                self.sendMessage(x,StringMessage("info "))

def live():
	while(1):
 		msg = self.waitNextMessage()
    		self.println(msg.getString())
