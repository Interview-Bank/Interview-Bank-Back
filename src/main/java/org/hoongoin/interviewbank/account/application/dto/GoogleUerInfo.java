package org.hoongoin.interviewbank.account.application.dto;

import lombok.Getter;

@Getter
public class GoogleUerInfo {

   private String id;
   private String email;
   private Boolean verifiedEmail;
   private String name;
   private String givenName;
   private String picture;
   private String locale;
}
