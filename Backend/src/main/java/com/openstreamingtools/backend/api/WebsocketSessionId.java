package com.openstreamingtools.backend.api;

import lombok.Getter;
import lombok.Setter;

/**
 * Data transfer object for WebSocket session identifiers.
 * Used to pass session IDs from frontend to backend in API requests.
 */
@Getter
@Setter
public class WebsocketSessionId {
    /** The WebSocket session identifier */
    private String sessionId;
}
