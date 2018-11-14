package com.develop.app.myapplication.model;

public class CaseStatusResponse {
    private String woNumber;
    private String woStatus;
    private String custName;
    private String requestBy;
    private String requestContact;
    private String problemReported;
    private String problemObserved;
    private String actionTaken;
    private String workOrderType;
    private String equipmentInfo;
    private String equipmentCeid;
    private String equipmentModel;
    private String equipmentDepartment;
    private String openedDateTime;
    private String ackDateTime;
    private String approvedByName;
    private String completedDateTime;
    private String approvePopUpmessage;
    private String resendPopUpmessage;
    private String useOtp;
    private String generateOtpButton;


    public void setGenerateOtpButton(String generateOtpButton) {
        this.generateOtpButton = generateOtpButton;
    }

    public String getGenerateOtpButton() {
        return generateOtpButton;
    }

    public void setUseOtp(String useOtp) {
        this.useOtp = useOtp;
    }

    public String getUseOtp() {
        return useOtp;
    }

    public String getResendPopUpmessage() {
        return resendPopUpmessage;
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

    public String getApprovePopUpmessage() {
        return approvePopUpmessage;
    }

    public void setApprovePopUpmessage(String approvePopUpmessage) {
        this.approvePopUpmessage = approvePopUpmessage;
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

    public String getEquipmentCeid() {
        return equipmentCeid;
    }

    public void setEquipmentCeid(String equipmentCeid) {
        this.equipmentCeid = equipmentCeid;
    }
    public void setApprovedByName(String approvedByName) {
        this.approvedByName = approvedByName;
    }
    public String getEquipmentModel() {
        return equipmentModel;
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

    public String getApprovedByName() {
        return approvedByName;
    }

    public void setCompletedDateTime(String completedDateTime) {
        this.completedDateTime = completedDateTime;
    }
}
