package madkit.share;
import madkit.kernel.AgentAddress;
import madkit.kernel.Message;



public class IndexMessage extends Message
{
    ListFic index;
    AgentAddress smallServerAddress;
    boolean updatePlugin;

    public IndexMessage(ListFic l,AgentAddress a)
    {
	index = l;
	smallServerAddress = a;
	updatePlugin=false;
    } 

    public IndexMessage(ListFic l,AgentAddress a,boolean u)
    {
	index = l;
	smallServerAddress = a;
	updatePlugin=u;
    }

    public ListFic getListFic() {return index;}
    public boolean getUpdatePlugin() {return updatePlugin;}
    public AgentAddress getSmallServerAddress() {return smallServerAddress;}

}
