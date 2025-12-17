package com.cm.service.serviceImpl;

import cn.hutool.system.SystemUtil;
import cn.hutool.system.oshi.CpuInfo;
import cn.hutool.system.oshi.OshiUtil;
import com.cm.entity.Cpu;
import com.cm.entity.Jvm;
import com.cm.entity.Mem;
import com.cm.entity.Sys;
import com.cm.service.SystemService;
import com.cm.vo.ServiceMonitoringVO;
import org.springframework.stereotype.Service;
import oshi.hardware.GlobalMemory;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author 31373
 */
@Service
public class SystemServiceImpl implements SystemService {

    /**
     * 服务器信息
     * @return 服务器信息
     */
    @Override
    public ServiceMonitoringVO getServer() {
        // 获取CPU信息
        Cpu cpu = getCpu();

        // 获取内存信息
        Mem mem = getMem();

        // 获取JVM信息
        Jvm jvm = getJvm();

        // 获取服务器信息
        Sys sys = getSys();

        return ServiceMonitoringVO.builder()
                .cpu(cpu)
                .mem(mem)
                .jvm(jvm)
                .sys(sys)
                .build();
    }

    /**
     * 获取cpu信息
     */
    private Cpu getCpu() {
        CpuInfo cpuInfo = OshiUtil.getCpuInfo();
        return new Cpu(
                cpuInfo.getCpuNum(),
                Double.parseDouble(String.format("%.2f", cpuInfo.getUsed())),
                Double.parseDouble(String.format("%.2f", cpuInfo.getWait())),
                Double.parseDouble(String.format("%.2f", cpuInfo.getFree()))
        );
    }

    /**
     * 获取内存信息
     */
    private Mem getMem() {
        // 获取服务器物理内存信息
        GlobalMemory memory = OshiUtil.getMemory();
        long total = memory.getTotal();
        long available = memory.getAvailable();
        long used = total - available;

        // 转换为GB并保留两位小数
        double totalGb = BigDecimal.valueOf(total)
                .divide(BigDecimal.valueOf(1024 * 1024 * 1024), 2, RoundingMode.HALF_UP)
                .doubleValue();
        double usedGb = BigDecimal.valueOf(used)
                .divide(BigDecimal.valueOf(1024 * 1024 * 1024), 2, RoundingMode.HALF_UP)
                .doubleValue();
        double freeGb = BigDecimal.valueOf(available)
                .divide(BigDecimal.valueOf(1024 * 1024 * 1024), 2, RoundingMode.HALF_UP)
                .doubleValue();

        // 计算使用率
        double usage = BigDecimal.valueOf(used)
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(total), 2, RoundingMode.HALF_UP)
                .doubleValue();

        return new Mem(totalGb, usedGb, freeGb, usage);
    }

    /**
     * 获取JVM信息
     */
    private Jvm getJvm() {
        // 获取JVM内存信息
        long total = Runtime.getRuntime().totalMemory();
        long free = Runtime.getRuntime().freeMemory();
        long used = total - free;

        // 转换为MB并保留两位小数
        double totalMb = BigDecimal.valueOf(total)
                .divide(BigDecimal.valueOf(1024 * 1024), 2, RoundingMode.HALF_UP)
                .doubleValue();
        double usedMb = BigDecimal.valueOf(used)
                .divide(BigDecimal.valueOf(1024 * 1024), 2, RoundingMode.HALF_UP)
                .doubleValue();
        double freeMb = BigDecimal.valueOf(free)
                .divide(BigDecimal.valueOf(1024 * 1024), 2, RoundingMode.HALF_UP)
                .doubleValue();

        // 计算使用率
        double usage = BigDecimal.valueOf(used)
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(total), 2, RoundingMode.HALF_UP)
                .doubleValue();

        return new Jvm(totalMb, usedMb, freeMb, usage);
    }

    /**
     * 获取服务器信息
     */
    private Sys getSys() {
        Sys sys = new Sys();
        sys.setServerName(SystemUtil.getHostInfo().getName());
        sys.setServerIp(SystemUtil.getHostInfo().getAddress());
        sys.setOsName(SystemUtil.getOsInfo().getName() + " " + SystemUtil.getOsInfo().getVersion());
        sys.setOsArch(SystemUtil.getOsInfo().getArch());
        return sys;
    }
}
