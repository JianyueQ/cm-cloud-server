package com.cm.vo;

import com.cm.entity.Cpu;
import com.cm.entity.Jvm;
import com.cm.entity.Mem;
import com.cm.entity.Sys;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author 31373
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceMonitoringVO implements Serializable {

    private Cpu cpu;

    private Mem mem;

    private Jvm jvm;

    private Sys sys;

}
