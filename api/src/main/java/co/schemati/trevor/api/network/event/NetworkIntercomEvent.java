package co.schemati.trevor.api.network.event;

import co.schemati.trevor.api.network.payload.NetworkPayload;

public interface NetworkIntercomEvent extends NetworkEvent {

  NetworkPayload<?> payload();
}
