package com.develop.app.myapplication.API;

public class APIURL
{
   //  public static String url="http://training.trimedxindia.com/NH/authenticateUser.mobile";
   public static String baseurl="http://cmms.narayanahealth.org/NH/";
   //public static String baseurl="http://training.trimedxindia.com/NH/";
   public static String caselist=baseurl+"getWorkOrder.mobile?pageNumber=1&recordsPerPage=5&woStatus=OPENED&userId=50";
   public static String closedcaselist=baseurl+"getWorkOrder.mobile?pageNumber=1&recordsPerPage=5&woStatus=COMPLETED&userId=50";
   // public static String myotp="training.trimedxindia.com/NH/verifyCustomerApprovalCode.mobile?woNumber=WO12334&otp=123456";    //  public static String caselist=baseurl+"getWorkOrder.mobile?";
}
