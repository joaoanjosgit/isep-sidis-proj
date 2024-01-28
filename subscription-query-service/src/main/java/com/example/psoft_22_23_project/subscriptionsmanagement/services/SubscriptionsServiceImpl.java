package com.example.psoft_22_23_project.subscriptionsmanagement.services;

import com.example.psoft_22_23_project.plansmanagement.model.Plans;
import com.example.psoft_22_23_project.subscriptionsmanagement.api.CreateSubscriptionsRequest;
import com.example.psoft_22_23_project.subscriptionsmanagement.model.SubscriptionDetails;
import com.example.psoft_22_23_project.subscriptionsmanagement.model.Subscriptions;
import com.example.psoft_22_23_project.subscriptionsmanagement.repositories.SubscriptionsRepositoryBD;
import com.example.psoft_22_23_project.usermanagement.model.User;
import com.example.psoft_22_23_project.usermanagement.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubscriptionsServiceImpl implements SubscriptionsService {


    private final SubscriptionsRepositoryBD subscriptionsRepository;
    private final UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(SubscriptionsServiceImpl.class);

    @Override
    public Plans planDetails() {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        int commaIndex = username.indexOf(",");

        String newString;
        if (commaIndex != -1) {
            newString = username.substring(commaIndex + 1);
        } else {
            newString = username;
        }


        User user = userRepository.findByUsername(newString).orElseThrow(() -> new EntityNotFoundException("You need to login in order to check the plan details"));

        Optional<Subscriptions> subscription = subscriptionsRepository.findByActiveStatus_ActiveAndUser(true, user);

        if (subscription.isEmpty())
            throw new IllegalArgumentException("You dont have an active subscription");

        return subscription.get().getPlan();
    }

    @Override
    public void saveCreatedSubsRabbit(Subscriptions obj) {


        if (subscriptionsRepository.findByActiveStatus_ActiveAndIsBonus_BonusAndUser_Id(true, false, obj.getUser().getId()).isEmpty() ||
                subscriptionsRepository.findByActiveStatus_ActiveAndIsBonus_BonusAndUser_Id(true, true, obj.getUser().getId()).isEmpty()) {

            subscriptionsRepository.save(obj);
            logger.info("Subscription added to the local database.");
        } else {
            logger.warn("subscription already exists in the local database. No action taken.");
        }
    }



    @Override
    public void saveCanceledSubsRabbit(Subscriptions obj, long version) {

        try {
            Subscriptions existingSubscription = subscriptionsRepository.findByActiveStatus_ActiveAndUser(true, obj.getUser()).orElse(null);

            if (existingSubscription != null && existingSubscription.getVersion() != version) {

                existingSubscription.deactivate(existingSubscription.getVersion());

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate startDate = LocalDate.parse(existingSubscription.getStartDate().getStartDate(), formatter);


                if (Objects.equals(existingSubscription.getPaymentType().getPaymentType(), "monthly")) {
                    if (startDate.getMonthValue() == LocalDate.now().getMonthValue()) {
                        if (startDate.getYear() != LocalDate.now().getYear()) {
                            existingSubscription.getEndDate().setEndDate(String.valueOf(startDate.plusMonths(1).plusYears(LocalDate.now().getYear() - startDate.getYear())));
                        } else {
                            existingSubscription.getEndDate().setEndDate(String.valueOf(startDate.plusMonths(1)));
                        }

                    } else if (startDate.getDayOfMonth() >= LocalDate.now().getDayOfMonth()) {
                        if (startDate.getYear() != LocalDate.now().getYear()) {
                            existingSubscription.getEndDate().setEndDate(String.valueOf(startDate.plusMonths(1).plusYears(LocalDate.now().getYear() - startDate.getYear())));
                        } else {
                            existingSubscription.getEndDate().setEndDate(String.valueOf(startDate.plusMonths(1)));
                        }

                    } else {
                        if (startDate.getYear() != LocalDate.now().getYear()) {
                            existingSubscription.getEndDate().setEndDate(String.valueOf(startDate.plusMonths((LocalDate.now().getMonthValue() - startDate.getMonthValue()) + 1).plusYears(LocalDate.now().getYear() - startDate.getYear())));
                        } else {
                            existingSubscription.getEndDate().setEndDate(String.valueOf(startDate.plusMonths((LocalDate.now().getMonthValue() - startDate.getMonthValue()) + 1)));
                        }
                    }
                }

                subscriptionsRepository.save(existingSubscription);
                logger.info("Subscription canceled in the local database.");
            } else {
                logger.warn("Received subscription has an older version or already canceled in the local database. No action taken.");
            }
        } catch (Exception e) {
            logger.error("Error processing received message: {}", e.getMessage(), e);
        }
    }


    @Override
    public void saveRenewedSubsRabbit(Subscriptions obj, long version) {

        try {
            Subscriptions existingSubscription = subscriptionsRepository.findByActiveStatus_ActiveAndUser(true, obj.getUser()).orElse(null);

            if (existingSubscription != null && existingSubscription.getVersion() != version) {

                existingSubscription.checkChange(existingSubscription.getVersion());

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate endDate = LocalDate.parse(existingSubscription.getEndDate().getEndDate(), formatter);

                existingSubscription.getEndDate().setEndDate(String.valueOf(endDate.plusYears(1)));

                subscriptionsRepository.save(existingSubscription);
                logger.info("Subscription renewed in the local database.");

            } else {
                logger.warn("Received subscription has an older version or already renewed in the local database. No action taken.");
            }
        } catch (Exception e) {
            logger.error("Error processing received message: {}", e.getMessage(), e);
        }
    }





    @Override
    public void saveChangedSubsRabbit(Subscriptions obj, long version) {

        try {
            Subscriptions existingSubscription = subscriptionsRepository.findByActiveStatus_ActiveAndUser(true, obj.getUser()).orElse(null);

            if (existingSubscription != null && existingSubscription.getVersion() != version) {

                existingSubscription.changePlan(existingSubscription.getVersion(), obj.getPlan());

                subscriptionsRepository.save(existingSubscription);
                logger.info("Subscription changed in the local database.");

            } else {
                logger.warn("Received subscription has an older version or already changed in the local database. No action taken.");
            }
        } catch (Exception e) {
            logger.error("Error processing received message: {}", e.getMessage(), e);
        }
    }




}






