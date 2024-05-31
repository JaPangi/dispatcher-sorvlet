package io.wwan13.dispatchersorvlet.sorvlet;

import io.wwan13.dispatchersorvlet.sorvlet.dto.request.SocketRequest;

public interface ArgumentsResolver {

    Object[] resolve(RequestHandler handler, SocketRequest request);
}
