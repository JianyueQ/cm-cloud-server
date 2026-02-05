package com.cm.attendance.strategy.Factory;

import com.cm.attendance.strategy.LocationSignInStrategy;
import com.cm.attendance.strategy.PasswordSignInStrategy;
import com.cm.attendance.strategy.SignInStrategy;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 签到策略工厂
 * @author 31373
 */
@Component
public class SignInStrategyFactory {

    private final LocationSignInStrategy locationSignInStrategy;
    private final PasswordSignInStrategy passwordSignInStrategy;

    private static final Map<Integer, SignInStrategy> STRATEGY_MAP = new HashMap<>();

    public SignInStrategyFactory(LocationSignInStrategy locationSignInStrategy, PasswordSignInStrategy passwordSignInStrategy) {
        this.locationSignInStrategy = locationSignInStrategy;
        this.passwordSignInStrategy = passwordSignInStrategy;
    }

    @PostConstruct
    public void initStrategyMap(){
        //位置签到
        STRATEGY_MAP.put(1,locationSignInStrategy);
        //密码签到
        STRATEGY_MAP.put(3,passwordSignInStrategy);
    }

    /**
     * 根据签到类型获取对应的策略实现
     * @param signInType 签到类型
     * @return 策略实现
     */
    public SignInStrategy getStrategy(Integer signInType){
        return STRATEGY_MAP.get(signInType);
    }


}
