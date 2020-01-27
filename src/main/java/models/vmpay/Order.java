package models.vmpay;

import com.picpay.websocketserver.models.Item;

import java.math.BigDecimal;
import java.util.List;

public class Order {
    private List<Item> itens;
    private BigDecimal totalValue;
}
