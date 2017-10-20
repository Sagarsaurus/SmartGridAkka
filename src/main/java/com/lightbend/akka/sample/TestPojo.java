/*
 * Copyright (C) 2009-2016 Lightbend Inc. <http://www.lightbend.com>
 */

package com.lightbend.akka.sample;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TestPojo {
    private String userId;
    private String firstName;
    private String lastName;

    @JsonCreator
    public TestPojo(@JsonProperty("userId") String userId, @JsonProperty("firstName") String firstName, @JsonProperty("lastName") String lastName) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName=lastName;
    }

    public String getUserId() {
        return userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String generateJsonString() {

            return "key="+this.getUserId()+"&val="+this.getFirstName()+"-"+this.getLastName();
    }



}