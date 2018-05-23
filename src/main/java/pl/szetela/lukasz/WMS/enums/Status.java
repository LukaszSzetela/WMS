package pl.szetela.lukasz.WMS.enums;

public enum Status {
    ORDERED("Ordered"), ACCEPTED("Accepted"), ASSIGNED("Assigned"), REALIZE("Realize"), PAUSE("Pause"), COMPLETED("Completed"), CANCELLED("Cancelled");

    private String description;

    Status(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
