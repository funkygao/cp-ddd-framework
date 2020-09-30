package org.example.cp.oms.client.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class SubmitOrderRequest implements Serializable {
    private static final long serialVersionUID = 870061998490977022L;

    @NotNull
    private String source;

    private String customerNo;
    private String externalNo;
}
