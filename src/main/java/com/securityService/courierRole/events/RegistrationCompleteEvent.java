package com.securityService.courierRole.events;

import com.securityService.courierRole.entity.User;
import lombok.*;
import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Component;

import java.time.Clock;

@Getter

@Setter
@Builder
public class RegistrationCompleteEvent extends ApplicationEvent {

    private User user;
    private String applicationUrl;

//    public RegistrationCompleteEvent(User user, String applicationUrl) {
//        super(user );
//        this.user = user;
//        this.applicationUrl = applicationUrl;
//    }


    public RegistrationCompleteEvent(User user, String applicationUrl) {
        super(user);
        this.user = user;
        this.applicationUrl = applicationUrl;
    }

}
