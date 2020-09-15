package myProject_LSP;

import javax.persistence.*;
import org.springframework.beans.BeanUtils;
import java.util.List;

@Entity
@Table(name="Cook_table")
public class Cook {
    private boolean flowchk = true;

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private Integer customerId;
    private String status;
    private long orderId;
    private static int qty = 5;
    private static int qtyInit = 3;

    @PostPersist
    public void onPostPersist(){
        if(flowchk) {   // 요리를 할 수 있는 재고가 있을 때 요리를 시작한다
            Cooked cooked = new Cooked();
            BeanUtils.copyProperties(this, cooked);
            this.setStatus("COOK : ORDER RECEIPT");

            this.qty--;
            cooked.publishAfterCommit();
        }else{
            CookQtyChecked cookQtyChecked = new CookQtyChecked();
            BeanUtils.copyProperties(this, cookQtyChecked);
            cookQtyChecked.publishAfterCommit();

        }

    }

    @PrePersist
    public void onPrePersist(){
        // 요리를 할 수 있는 재고가 없을 때 요리를 시작한다
        if(this.getQty() <= 0) {
            this.setStatus("COOK : QTY OVER");
            flowchk = false;
            qtyInit--;
            if(qtyInit < 1){
                qtyInit = 3;
                qty = 5;
            }
        }


    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public boolean isFlowchk() {
        return flowchk;

    }

    public void setFlowchk(boolean flowchk) {
        this.flowchk = flowchk;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }
}
