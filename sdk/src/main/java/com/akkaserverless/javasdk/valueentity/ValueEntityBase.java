/*
 * Copyright 2021 Lightbend Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.akkaserverless.javasdk.valueentity;

import com.akkaserverless.javasdk.Metadata;
import com.akkaserverless.javasdk.Reply;
import com.akkaserverless.javasdk.ServiceCall;
import com.akkaserverless.javasdk.impl.reply.FailureReplyImpl;
import com.akkaserverless.javasdk.impl.reply.ForwardReplyImpl;
import com.akkaserverless.javasdk.impl.reply.MessageReplyImpl;
import com.akkaserverless.javasdk.impl.reply.NoReply;
import com.akkaserverless.javasdk.reply.FailureReply;
import com.akkaserverless.javasdk.reply.ForwardReply;
import com.akkaserverless.javasdk.reply.MessageReply;

// FIXME rename to ValueEntity when the old annotation is removed

/** @param <S> The type of the state for this entity. */
public abstract class ValueEntityBase<S> {

  /**
   * Construct the effect that is returned by the command handler. The effect describes next
   * processing actions, such as emitting events and sending a reply.
   *
   * @param <S> The type of the state for this entity.
   */
  public static class Effects<S> {
    private Effects() {}

    public SecondaryEffects<S> updateState(S newState) {
      throw new UnsupportedOperationException("Not implemented yet"); // FIXME
    }

    public SecondaryEffects<S> deleteState() {
      throw new UnsupportedOperationException("Not implemented yet"); // FIXME
    }

    /**
     * Create a message reply.
     *
     * @param message The payload of the reply.
     * @return A message reply.
     * @param <T> The type of the message that must be returned by this call.
     */
    public <T> MessageReply<T> reply(T message) {
      return reply(message, Metadata.EMPTY);
    }

    /**
     * Create a message reply.
     *
     * @param message The payload of the reply.
     * @param metadata The metadata for the message.
     * @return A message reply.
     * @param <T> The type of the message that must be returned by this call.
     */
    public <T> MessageReply<T> reply(T message, Metadata metadata) {
      return new MessageReplyImpl<>(message, metadata);
    }

    /**
     * Create a forward reply.
     *
     * @param serviceCall The service call representing the forward.
     * @return A forward reply.
     * @param <T> The type of the message that must be returned by this call.
     */
    public <T> ForwardReply<T> forward(ServiceCall serviceCall) {
      return new ForwardReplyImpl<>(serviceCall);
    }

    /**
     * Create a failure reply.
     *
     * @param description The description of the failure.
     * @return A failure reply.
     * @param <T> The type of the message that must be returned by this call.
     */
    public <T> FailureReply<T> failure(String description) {
      return new FailureReplyImpl<>(description);
    }

    /**
     * Create a reply that contains neither a message nor a forward nor a failure.
     *
     * <p>This may be useful for emitting effects without sending a message.
     *
     * @return The reply.
     * @param <T> The type of the message that must be returned by this call.
     */
    public <T> Reply<T> noReply() {
      return NoReply.apply();
    }
  }

  public static class SecondaryEffects<S> {
    private SecondaryEffects() {}

    /**
     * Reply after for example <code>updateState</code>.
     *
     * @param message The payload of the reply.
     * @return A message reply.
     * @param <T> The type of the message that must be returned by this call.
     */
    public <T> MessageReply<T> thenReply(T message) {
      return thenReply(message, Metadata.EMPTY);
    }

    /**
     * Reply after for example <code>updateState</code>.
     *
     * @param message The payload of the reply.
     * @param metadata The metadata for the message.
     * @return A message reply.
     * @param <T> The type of the message that must be returned by this call.
     */
    public <T> MessageReply<T> thenReply(T message, Metadata metadata) {
      throw new UnsupportedOperationException("Not implemented yet"); // FIXME
    }

    // FIXME thenForward
  }

  protected Effects<S> effects() {
    return new Effects<>();
  }
}