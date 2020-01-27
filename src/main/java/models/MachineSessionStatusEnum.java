package models;

public enum MachineSessionStatusEnum {

    WAITING_MACHINE_LINK("waiting_machine_link"),
    WAITING_SELECTION("waiting_selection"),
    WAITING_AUTHORIZATION("waiting_authorization"),
    AUTHORIZED("authorized"),
    SUCCESS("success"),
    ROLLED_BACK("rolled_back"),
    DECLINED("declined"),
    CANCELLED("cancelled");

    private final String value;

    MachineSessionStatusEnum(String value) {
         this.value = value;
    }
}
