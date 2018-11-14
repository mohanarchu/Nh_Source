package com.develop.app.myapplication.Db;

import android.support.annotation.ColorInt;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;

/**
 * Created by AASHI on 11-06-2018.
 */

public class Work_order_list{

    @Column(name = "unique_id")
    String unique_id;
    @Column(name = "woNumber")
    String woNumber;
    @Column(name = "woStatus")
    String woStatus;
    @Column(name = "custName")
    String custName;
    @Column(name = "requestBy")
    String requestBy;
    @Column(name = "requestContact")
    String requestContact;
    @Column(name = "problemReported")
    String problemReported;
    @Column(name = "problemObserved")
    String problemObserved;
    @Column(name = "approvedByName")
    String approvedByName;
    @Column(name = "approvePopUpmessage")
    String approvePopUpmessage;
    @Column(name = "useOtp")
    String useOtp;
    @Column(name = "resendPopUpmessage")
    String resendPopUpmessage;
    @Column (name = "approvedByContactNumber")
            String approvedByContactNumber;
String actionStatus;

    public String getActionStatus()
    {
        return actionStatus;
    }

    public void setActionStatus(String actionStatus) {
        this.actionStatus = actionStatus;
    }

    public Work_order_list() {
    }
    @Column(name = "actionTaken")

    String actionTaken;
    @Column(name = "workOrderType")
    String workOrderType;
    @Column(name = "equipmentInfo")
    String equipmentInfo;
    @Column(name = "hrId")
    String hrId;
    @Column(name = "update_status")
    String update_status;
    @Column(name = "generateOtpButton")
    String generateOtpButton;

    private String equipmentCeid;
    private String equipmentModel;
    private String equipmentDepartment;
    private String openedDateTime;
    private String ackDateTime;
    private String completedDateTime;

    public String getApprovedByContactNumber() {
        return approvedByContactNumber;
    }


    public String getEquipmentCeid() {
        return equipmentCeid;
    }

    public String getUseOtp() {
        return useOtp;
    }

    public void setUseOtp(String useOtp) {
        this.useOtp = useOtp;
    }

    public String getGenerateOtpButton() {
        return generateOtpButton;
    }

    public void setGenerateOtpButton(String generateOtpButton) {
        this.generateOtpButton = generateOtpButton;
    }

    public void setEquipmentCeid(String equipmentCeid) {
        this.equipmentCeid = equipmentCeid;
    }

    public String getEquipmentModel() {
        return equipmentModel;
    }

    public String getApproveName() {
        return approvedByName;
    }

    public void setEquipmentModel(String equipmentModel) {
        this.equipmentModel = equipmentModel;
    }

    public String getEquipmentDepartment() {
        return equipmentDepartment;
    }

    public void setEquipmentDepartment(String equipmentDepartment) {
        this.equipmentDepartment = equipmentDepartment;
    }

    public String getResendPopUpmessage() {
        return resendPopUpmessage;
    }

    public void setResendPopUpmessage(String resendPopUpmessage) {
        this.resendPopUpmessage = resendPopUpmessage;
    }

    public String getApprovedByName() {
        return approvedByName;
    }

    public String getApprovePopUpmessage() {
        return approvePopUpmessage;
    }

    public void setApprovePopUpmessage(String approvePopUpmessage) {
        this.approvePopUpmessage = approvePopUpmessage;
    }

    public String getOpenedDateTime() {
        return openedDateTime;
    }

    public void setOpenedDateTime(String openedDateTime) {
        this.openedDateTime = openedDateTime;
    }

    public String getAckDateTime() {
        return ackDateTime;
    }

    public void setAckDateTime(String ackDateTime) {
        this.ackDateTime = ackDateTime;
    }

    public String getCompletedDateTime() {
        return completedDateTime;
    }

    public void setCompletedDateTime(String completedDateTime) {
        this.completedDateTime = completedDateTime;
    }

    public String getUnique_id() {
        return unique_id;
    }

    public void setUnique_id(String unique_id) {
        this.unique_id = unique_id;
    }

    public String getWoNumber() {
        return woNumber;
    }

    public void setWoNumber(String woNumber) {
        this.woNumber = woNumber;
    }

    public String getWoStatus() {
        return woStatus;
    }

    public void setWoStatus(String woStatus) {
        this.woStatus = woStatus;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getRequestBy() {
        return requestBy;
    }

    public void setRequestBy(String requestBy) {
        this.requestBy = requestBy;
    }

    public String getRequestContact() {
        return requestContact;
    }

    public void setRequestContact(String requestContact) {
        this.requestContact = requestContact;
    }

    public String getProblemReported() {
        return problemReported;
    }

    public void setProblemReported(String problemReported) {
        this.problemReported = problemReported;
    }

    public String getProblemObserved() {
        return problemObserved;
    }

    public void setProblemObserved(String problemObserved) {
        this.problemObserved = problemObserved;
    }

    public String getActionTaken() {
        return actionTaken;
    }

    public void setActionTaken(String actionTaken) {
        this.actionTaken = actionTaken;
    }

    public void setApproveName(String approvedByName){
        this.approvedByName = approvedByName;
    }
    public String getWorkOrderType() {
        return workOrderType;
    }

    public void setWorkOrderType(String workOrderType) {
        this.workOrderType = workOrderType;
    }

    public String getEquipmentInfo() {
        return equipmentInfo;
    }

    public void setEquipmentInfo(String equipmentInfo) {
        this.equipmentInfo = equipmentInfo;
    }

    public String getHrId() {
        return hrId;
    }

    public void setHrId(String hrId) {
        this.hrId = hrId;
    }

    public String getUpdate_status() {
        return update_status;
    }

    public void setUpdate_status(String update_status) {
        this.update_status = update_status;
    }
}
