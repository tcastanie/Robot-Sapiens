package madkit.system;

import java.util.StringTokenizer;

import madkit.kernel.Agent;
import madkit.kernel.AgentAddress;
import madkit.kernel.Message;
import madkit.kernel.StringMessage;

public class Pager extends Agent {
  boolean distributed=false;
  String community="public";
  String group="system";
  String role="pager";


  private boolean showHeader=true;
  public void setShowHeader(boolean b){showHeader=b;}
  public boolean isShowHeader(){return showHeader;}


  public Pager() {
  }

  public Pager(boolean dist, String c, String g, String r){
    distributed=dist;
    community=c;
    group=g;
    role=r;
  }

  public Pager(String g){
    group=g;
  }

  public Pager(boolean dist, String g){
    distributed = dist;
    group = g;
  }

  void displayMessage(String s, AgentAddress a){
      if (isShowHeader()){
            println("MESSAGE from " + a);
            println("--------------------------------------");
      }
      println(s);
  }

  public void activate(){
    createGroup(distributed,community,group,null,null);
    requestRole(community,group,role,null);
  }

  public void live(){
    while (true){
      Message m = waitNextMessage();
      if (m instanceof StringMessage){
          StringMessage msg = (StringMessage) m;
          String content = msg.getString();
          StringTokenizer st = new StringTokenizer(msg.getString()," ");
          String token = st.nextToken();
          if (token.equalsIgnoreCase("$display")){
             String s = content.substring("$display".length()+1,content.length());
             displayMessage(s,m.getSender());
          } else
             displayMessage(msg.getString(),m.getSender());
        }
      }
    }
}
