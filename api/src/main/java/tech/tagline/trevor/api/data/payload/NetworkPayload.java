package tech.tagline.trevor.api.data.payload;

import com.google.common.graph.Network;

public class NetworkPayload {

  private final Content content;
  private final String source;

  protected NetworkPayload(Content content, String source) {
    this.content = content;
    this.source = source;
  }

  public Content getContent() {
    return content;
  }

  public String getSource() {
    return source;
  }

  public enum Content {
    CONNECT(ConnectPayload.class),
    DISCONNECT(DisconnectPayload.class),
    SERVERCHANGE(ServerChangePayload.class);

    private Class<? extends NetworkPayload> clazz;

    Content(Class<? extends  NetworkPayload> clazz) {
      this.clazz = clazz;
    }

    public Class<? extends NetworkPayload> getContentClass() {
      return clazz;
    }
  }
}
