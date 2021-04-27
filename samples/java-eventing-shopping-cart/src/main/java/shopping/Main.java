/*
 * Copyright 2019 Lightbend Inc.
 */

// tag::ToProductPopularityAction[]
// tag::RegisterEventSourcedEntity[]
package shopping;

import com.akkaserverless.javasdk.AkkaServerless;
// end::RegisterEventSourcedEntity[]
// end::ToProductPopularityAction[]
import shopping.cart.ShoppingCartEntity;
import shopping.cart.ShoppingCartAnalyticsAction;
import shopping.cart.ShoppingCartTopicAction;
import shopping.cart.ShoppingCartView;
import shopping.cart.TopicPublisherAction;
import shopping.cart.actions.EventsToTopicPublisher;
import shopping.cart.actions.ShoppingCartAnalytics;
import shopping.cart.api.ShoppingCartTopic;
import shopping.cart.api.ShoppingCartApi;
import shopping.cart.domain.ShoppingCartDomain;
import shopping.cart.view.ShoppingCartViewModel;
import shopping.product.ProductPopularityEntity;
import shopping.product.ToProductPopularityAction;
import shopping.product.actions.ToProductPopularity;
import shopping.product.api.ProductPopularityApi;
import shopping.product.domain.ProductPopularityDomain;
// tag::ToProductPopularityAction[]
// tag::RegisterEventSourcedEntity[]

public final class Main {
  public static final void main(String[] args) throws Exception {
    new AkkaServerless()
        // end::ToProductPopularityAction[]
        // event sourced shopping cart entity
        // receives commands from outside the service and persists events to its journal/event log
        .registerEventSourcedEntity(
            ShoppingCartEntity.class,
            ShoppingCartApi.getDescriptor().findServiceByName("ShoppingCartService"),
            ShoppingCartDomain.getDescriptor())
        // end::RegisterEventSourcedEntity[]

        // consume shopping cart events emitted from the ShoppingCartEntity
        // and publish as is to 'shopping-cart-events' topic
        .registerAction(
            TopicPublisherAction.class,
            EventsToTopicPublisher.getDescriptor()
                .findServiceByName("EventsToTopicPublisherService"))
        .registerAction(
            ShoppingCartTopicAction.class,
            ShoppingCartTopic.getDescriptor().findServiceByName("ShoppingCartTopicService"))

        // consume shopping cart events published to 'shopping-cart-events' topic
        .registerAction(
            ShoppingCartAnalyticsAction.class,
            ShoppingCartAnalytics.getDescriptor().findServiceByName("ShoppingCartAnalyticsService"))

        // consume shopping cart events emitted from the ShoppingCartEntity
        // and send as commands to ProductPopularityEntity
        // tag::ToProductPopularityAction[]
        .registerAction(
            ToProductPopularityAction.class,
            ToProductPopularity.getDescriptor().findServiceByName("ToProductPopularityService"))
        // end::ToProductPopularityAction[]

        // value entity tracking product popularity
        .registerValueEntity(
            ProductPopularityEntity.class,
            ProductPopularityApi.getDescriptor().findServiceByName("ProductPopularityService"),
            ProductPopularityDomain.getDescriptor())

        // view of the shopping carts
        .registerView(
            ShoppingCartView.class,
            shopping.cart.view.ShoppingCartViewModel.getDescriptor()
                .findServiceByName("ShoppingCartViewService"),
            "carts",
            ShoppingCartDomain.getDescriptor(),
            ShoppingCartViewModel.getDescriptor())
        // tag::ToProductPopularityAction[]
        // tag::RegisterEventSourcedEntity[]
        .start()
        .toCompletableFuture()
        .get();
  }
}
// end::ToProductPopularityAction[]
// end::RegisterEventSourcedEntity[]
