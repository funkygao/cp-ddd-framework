package ddd.plus.showcase.wms.domain.order;

import io.github.dddplus.dsl.KeyElement;
import io.github.dddplus.model.IDomainModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class OrderLine implements IDomainModel {
    private Long id;

    @KeyElement(types = KeyElement.Type.Structural, byType = true)
    private OrderLineNo orderLineNo;
}
