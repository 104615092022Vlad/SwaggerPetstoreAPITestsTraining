import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class OrderDto {
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private Long petId;

    public Long getPetId() {
        return id;
    }

    public void setPetId(Long petId) {
        this.petId = petId;
    }
    private Integer quantity;

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    private Calendar shipDate;

    public Calendar getShipDate() {
        return shipDate;
    }

    public void setShipDate(Calendar shipDate) {
        this.shipDate = shipDate;
    }

    private String status;

    public String getOrderStatusDto() {
        return status;
    }

    public void setOrderStatusDto(String status) {
        this.status = status;
    }

    private Boolean complete;

    public Boolean getComplete() {
        return complete;
    }

    public void setStatus(Boolean complete) {
        this.complete = complete;
    }

    public static List<String> statusList() {
        List<String> status = new ArrayList<>();

        status.add("placed");
        status.add("approved");
        status.add("delivered");

        return status;
    }

    public OrderDto(Long id, Long petId, Integer quantity, Calendar shipDate, String status, Boolean complete) {
        this.id = id;
        this.petId = petId;
        this.quantity = quantity;
        this.shipDate = shipDate;
        this.status = status;
        this.complete = complete;
    }
}

