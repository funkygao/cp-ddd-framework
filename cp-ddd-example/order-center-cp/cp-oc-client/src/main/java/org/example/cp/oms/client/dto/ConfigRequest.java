package org.example.cp.oms.client.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.net.URL;

@Data
public class ConfigRequest implements Serializable {
    private static final long serialVersionUID = 870061998490977022L;

    @NotNull
    private String partnerCode;

    private URL jarURL;

}
