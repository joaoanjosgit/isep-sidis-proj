package com.example.psoft_22_23_project.subscriptionsmanagement.services;

import com.example.psoft_22_23_project.plansmanagement.model.BonusPlanRequest;
import com.example.psoft_22_23_project.plansmanagement.model.Plans;
import com.example.psoft_22_23_project.plansmanagement.repositories.PlansRepository;
import com.example.psoft_22_23_project.plansmanagement.services.CreatePlansMapper;
import com.example.psoft_22_23_project.subscriptionsmanagement.model.PaymentType;
import com.example.psoft_22_23_project.subscriptionsmanagement.services.producer.CancelSubscriptionProducer;
import com.example.psoft_22_23_project.subscriptionsmanagement.services.producer.ChangeSubscriptionProducer;
import com.example.psoft_22_23_project.subscriptionsmanagement.services.producer.CreateSubscriptionProducer;
import com.example.psoft_22_23_project.subscriptionsmanagement.services.producer.RenewSubscriptionProducer;
import com.example.psoft_22_23_project.subscriptionsmanagement.api.CreateSubscriptionsRequest;
import com.example.psoft_22_23_project.subscriptionsmanagement.model.Subscriptions;
import com.example.psoft_22_23_project.subscriptionsmanagement.repositories.SubscriptionsRepositoryBD;
import com.example.psoft_22_23_project.usermanagement.model.User;
import com.example.psoft_22_23_project.usermanagement.repositories.UserRepositoryBD;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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


    private final SubscriptionsRepositoryBD repository;
    private final UserRepositoryBD userRepository;
    private final PlansRepository plansRepository;

    private final CreateSubscriptionsMapper createSubscriptionsMapper;
    private final CreatePlansMapper createPlansMapper;

    private static final Logger logger = LoggerFactory.getLogger(SubscriptionsServiceImpl.class);


    private final CreateSubscriptionProducer createSubscriptionProducer;
    private final CancelSubscriptionProducer cancelSubscriptionProducer;
    private final RenewSubscriptionProducer renewSubscriptionProducer;
    private final ChangeSubscriptionProducer changeSubscriptionProducer;





    @Override
    public Subscriptions create(final CreateSubscriptionsRequest resource) {

        Plans plan = plansRepository.findByActive_ActiveAndName_Name(true, resource.getName())
                .orElseThrow(() -> new EntityNotFoundException("Plan not found with name " + resource.getName()));

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        int commaIndex = username.indexOf(",");

        String newString;
        if (commaIndex != -1) {
            newString = username.substring(0, commaIndex);
        } else {
            newString = username;
        }


        User user = userRepository.findById(Long.valueOf(newString))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));


        Optional<Subscriptions> existingSubscription = repository.findByActiveStatus_ActiveAndUser(true, user);

        if (existingSubscription.isPresent()){
            if (existingSubscription.get().getActiveStatus().isActive()) {
                throw new IllegalArgumentException("You need to let your active subscription end in order to subscribe");
            }

        }

        // construct a new object based on data received by the service
        Subscriptions obj = createSubscriptionsMapper.create(user, plan, resource);

        repository.save(obj);

        // Send message to RabbitMQ for other instances and save
        createSubscriptionProducer.sendMessage(obj);

        return obj;
    }

    @Override
    public void saveCreatedSubsRabbit(Subscriptions obj) {


        if (repository.findByActiveStatus_ActiveAndIsBonus_BonusAndUser_Id(true, false, obj.getUser().getId()).isEmpty() ||
                repository.findByActiveStatus_ActiveAndIsBonus_BonusAndUser_Id(true, true, obj.getUser().getId()).isEmpty()) {

            repository.save(obj);
            logger.info("Subscription added to the local database.");
        } else {
            logger.warn("subscription already exists in the local database. No action taken.");
        }
    }


    @Override
    public Subscriptions cancelSubscription(final long desiredVersion){

        // Check if the current user is authorized to cancel the subscription
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        int commaIndex = username.indexOf(",");

        String newString;
        if (commaIndex != -1) {
            newString = username.substring(0, commaIndex);
        } else {
            newString = username;
        }

        User user = userRepository.findById(Long.valueOf(newString)).orElseThrow(() -> new EntityNotFoundException("User not found with ID " + newString));

        // Check if subscription exists
        Subscriptions subscription = repository.findByActiveStatus_ActiveAndUser(true, user)
                .orElseThrow(() -> new EntityNotFoundException("No subscriptions associated with this user"));




        subscription.deactivate(desiredVersion);


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(subscription.getStartDate().getStartDate(), formatter);


        if(Objects.equals(subscription.getPaymentType().getPaymentType(), "monthly")){
            if (startDate.getMonthValue() == LocalDate.now().getMonthValue()){
                if (startDate.getYear() != LocalDate.now().getYear()) {
                    subscription.getEndDate().setEndDate(String.valueOf(startDate.plusMonths(1).plusYears(LocalDate.now().getYear() - startDate.getYear())));
                }else{
                    subscription.getEndDate().setEndDate(String.valueOf(startDate.plusMonths(1)));
                }

            }else if (startDate.getDayOfMonth() >= LocalDate.now().getDayOfMonth()){
                if (startDate.getYear() != LocalDate.now().getYear()){
                    subscription.getEndDate().setEndDate(String.valueOf(startDate.plusMonths(1).plusYears(LocalDate.now().getYear() - startDate.getYear())));
                }else {
                    subscription.getEndDate().setEndDate(String.valueOf(startDate.plusMonths(1)));
                }

            } else {
                if (startDate.getYear() != LocalDate.now().getYear()) {
                    subscription.getEndDate().setEndDate(String.valueOf(startDate.plusMonths((LocalDate.now().getMonthValue() - startDate.getMonthValue())+1).plusYears(LocalDate.now().getYear() - startDate.getYear())));
                } else {
                    subscription.getEndDate().setEndDate(String.valueOf(startDate.plusMonths((LocalDate.now().getMonthValue() - startDate.getMonthValue())+1)));
                }
            }
        }

        repository.save(subscription);

        cancelSubscriptionProducer.sendMessage(subscription);

        return subscription;
    }

    @Override
    public void saveCanceledSubsRabbit(Subscriptions obj, long version) {

        try {
            Subscriptions existingSubscription = repository.findByActiveStatus_ActiveAndUser(true, obj.getUser()).orElse(null);

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

                repository.save(existingSubscription);
                logger.info("Subscription canceled in the local database.");
            } else {
                logger.warn("Received subscription has an older version or already canceled in the local database. No action taken.");
            }
        } catch (Exception e) {
            logger.error("Error processing received message: {}", e.getMessage(), e);
        }
    }

    @Override
    public Subscriptions renewAnualSubscription(final long desiredVersion){

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        int commaIndex = username.indexOf(",");

        String newString;
        if (commaIndex != -1) {
            newString = username.substring(0, commaIndex);
        } else {
            newString = username;
        }

        User user = userRepository.findById(Long.valueOf(newString)).orElseThrow(() -> new EntityNotFoundException("User not found with ID " + newString));

        // Check if subscription exists
        Subscriptions subscription = repository.findByActiveStatus_ActiveAndUser(true, user)
                .orElseThrow(() -> new EntityNotFoundException("No subscriptions associated with this user"));



        if (Objects.equals(subscription.getPaymentType().getPaymentType(), "monthly")){

            throw new IllegalArgumentException("You can not renew a monthly subscription");
        } else {

            subscription.checkChange(desiredVersion);


            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate endDate = LocalDate.parse(subscription.getEndDate().getEndDate(), formatter);

            subscription.getEndDate().setEndDate(String.valueOf(endDate.plusYears(1)));
        }

        repository.save(subscription);

        renewSubscriptionProducer.sendMessage(subscription);
        return subscription;
    }

    @Override
    public void saveRenewedSubsRabbit(Subscriptions obj, long version) {

        try {
            Subscriptions existingSubscription = repository.findByActiveStatus_ActiveAndUser(true, obj.getUser()).orElse(null);

            if (existingSubscription != null && existingSubscription.getVersion() != version) {

                existingSubscription.checkChange(existingSubscription.getVersion());

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate endDate = LocalDate.parse(existingSubscription.getEndDate().getEndDate(), formatter);

                existingSubscription.getEndDate().setEndDate(String.valueOf(endDate.plusYears(1)));

                repository.save(existingSubscription);
                logger.info("Subscription renewed in the local database.");

            } else {
                logger.warn("Received subscription has an older version or already renewed in the local database. No action taken.");
            }
        } catch (Exception e) {
            logger.error("Error processing received message: {}", e.getMessage(), e);
        }
    }




  @Override
  public Subscriptions changePlan(final long desiredVersion, final String name) {


      Plans plan = plansRepository.findByActive_ActiveAndName_Name(true, name)
              .orElseThrow(() -> new EntityNotFoundException("Plan not found with name " + name));

      int deviceLimit = plan.getMaximumNumberOfUsers().getMaximumNumberOfUsers();

      String username = SecurityContextHolder.getContext().getAuthentication().getName();
      int commaIndex = username.indexOf(",");

      String newString;
      if (commaIndex != -1) {
          newString = username.substring(0, commaIndex);
      } else {
          newString = username;
      }


      User user = userRepository.findById(Long.valueOf(newString))
              .orElseThrow(() -> new UsernameNotFoundException("User not found"));



      Subscriptions subscription = repository.findByActiveStatus_ActiveAndUser(true, user)
              .orElseThrow(() -> new EntityNotFoundException("No subscriptions associated with this user"));



      if(subscription.getPlan().getName().getName().equals(name)){
          throw new IllegalArgumentException("You are already subscribed to this plan");
      }



      subscription.changePlan(desiredVersion, plan);

      repository.save(subscription);


      changeSubscriptionProducer.sendMessage(subscription);
      return subscription;
  }


  @Override
    public void saveChangedSubsRabbit(Subscriptions obj, long version) {

        try {
            Subscriptions existingSubscription = repository.findByActiveStatus_ActiveAndUser(true, obj.getUser()).orElse(null);

            if (existingSubscription != null && existingSubscription.getVersion() != version) {

                existingSubscription.changePlan(existingSubscription.getVersion(), obj.getPlan());

                repository.save(existingSubscription);
                logger.info("Subscription changed in the local database.");

            } else {
                logger.warn("Received subscription has an older version or already changed in the local database. No action taken.");
            }
        } catch (Exception e) {
            logger.error("Error processing received message: {}", e.getMessage(), e);
        }
    }

    @Override
    public boolean createBonusSub(BonusPlanRequest request) {
        final Optional<Subscriptions> isValid = repository.findByActiveStatus_ActiveAndIsBonus_BonusAndUser_Id(true, false, request.getUserId());
        if (isValid.isPresent()){
            final Optional<Subscriptions> isValidBonus = repository.findByActiveStatus_ActiveAndIsBonus_BonusAndUser_Id(true, true, request.getUserId());
            if (isValidBonus.isEmpty()){

                Plans plans = plansRepository.findByName_Name(request.getPlan().getName().getName()).orElse(null);
                User user = userRepository.findById(request.getUserId()).orElse(null);
                PaymentType paymentType = new PaymentType("annually");
                Subscriptions newSub = new Subscriptions(paymentType, plans, user);
                repository.save(newSub);
                // Send message to RabbitMQ for other instances and save
                createSubscriptionProducer.sendMessage(newSub);
                return true;
            }else {
                return false;
            }
        }
        return false;
    }

}




