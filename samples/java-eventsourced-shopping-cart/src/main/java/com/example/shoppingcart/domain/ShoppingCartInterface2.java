package com.example.shoppingcart.domain;

import com.akkaserverless.javasdk.Effect;
import com.akkaserverless.javasdk.eventsourcedentity.CommandContext;
import com.akkaserverless.javasdk.eventsourcedentity.CommandHandler;
import com.akkaserverless.javasdk.eventsourcedentity.EventHandler;
import com.akkaserverless.javasdk.eventsourcedentity.EventSourcedEntityBase;
import com.example.shoppingcart.ShoppingCartApi;
import com.google.protobuf.Empty;

/** An event sourced entity. */
public abstract class ShoppingCartInterface2 extends EventSourcedEntityBase<ShoppingCartDomain.Cart> {

    protected abstract ShoppingCartDomain.Cart emptyState();

    @CommandHandler(name = "AddItem")
    public abstract Effect<Empty> addItem(
            ShoppingCartDomain.Cart currentState,
            ShoppingCartApi.AddLineItem command,
            CommandContext context);

    @CommandHandler(name = "RemoveItem")
    public abstract Effect<Empty> removeItem(
            ShoppingCartDomain.Cart currentState,
            ShoppingCartApi.RemoveLineItem command,
            CommandContext context);

    @CommandHandler(name = "GetCart")
    public abstract Effect<ShoppingCartApi.Cart> getCart(
            ShoppingCartDomain.Cart currentState,
            ShoppingCartApi.GetShoppingCart command,
            CommandContext context);

    @EventHandler
    protected abstract ShoppingCartDomain.Cart itemAdded(
            ShoppingCartDomain.Cart currentState,
            ShoppingCartDomain.ItemAdded event);
    
    @EventHandler
    protected abstract ShoppingCartDomain.Cart itemRemoved(
            ShoppingCartDomain.Cart currentState,
            ShoppingCartDomain.ItemRemoved event);
}
