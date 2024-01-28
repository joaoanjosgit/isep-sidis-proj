package com.example.psoft_22_23_project.subscriptionsmanagement.services;

import com.example.psoft_22_23_project.plansmanagement.model.BonusPlanRequest;
import com.example.psoft_22_23_project.subscriptionsmanagement.api.CreateSubscriptionsRequest;
import com.example.psoft_22_23_project.subscriptionsmanagement.model.SubscriptionDetails;
import com.example.psoft_22_23_project.subscriptionsmanagement.model.Subscriptions;

public interface SubscriptionsService {

    Subscriptions create(CreateSubscriptionsRequest resource);



    Subscriptions cancelSubscription(long desiredVersion);



    Subscriptions renewAnualSubscription(long desiredVersion);


    Subscriptions changePlan(long desiredVersion, String name);


    void saveCreatedSubsRabbit(Subscriptions obj);

    void saveCanceledSubsRabbit(Subscriptions obj, long version);

    void saveRenewedSubsRabbit(Subscriptions obj, long version);

    void saveChangedSubsRabbit(Subscriptions obj, long version);


    boolean createBonusSub(BonusPlanRequest request);
}
