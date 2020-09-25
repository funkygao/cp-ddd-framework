package org.example.cp.oms.client.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class SubmitOrderRequest implements Serializable {
    private static final long serialVersionUID = 870061998490977022L;

    private String source;
    private String customerNo;
    private String externalNo;
}
