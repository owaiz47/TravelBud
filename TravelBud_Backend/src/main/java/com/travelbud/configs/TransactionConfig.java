package com.travelbud.configs;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource;
import org.springframework.transaction.interceptor.RollbackRuleAttribute;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.transaction.interceptor.TransactionInterceptor;

/**
 * Global Transaction Manager
 *
 * @author mohammed_owaiz
 * @date 2021/06/08
 */
@Aspect
@Configuration
public class TransactionConfig {

    /**
           * Configuration method expired time, default -1, never time
     */
    private final static int METHOD_TIME_OUT = 10000;

    /**
           * Configure the entry point expression
     */
    private static final String POINTCUT_EXPRESSION = "execution(* com.travelbud.services..*.*(..))";

    /**
           * Transaction Manager
     */
    @Resource
    private PlatformTransactionManager transactionManager;


    @Bean
    public TransactionInterceptor txAdvice() {
        /* Transaction management rules, statement of transaction management **/
        NameMatchTransactionAttributeSource source = new NameMatchTransactionAttributeSource();
        /* Read only transactions, do not do update operations */
        RuleBasedTransactionAttribute readOnly = new RuleBasedTransactionAttribute();
        readOnly.setReadOnly(true);
        readOnly.setPropagationBehavior(TransactionDefinition.PROPAGATION_NOT_SUPPORTED);
        /* There are current transactions with current transactions. There is currently no transaction to create a new transaction */
        RuleBasedTransactionAttribute required = new RuleBasedTransactionAttribute();
        /* After throwing an abnormality, perform the cut point rollback, you can replace the type of exception */
        required.setRollbackRules(Collections.singletonList(new RollbackRuleAttribute(Exception.class)));
        /* Propagation_Required: Transaction isolation is 1. If there is currently transaction, add this transaction; if there is no transaction, create a new transaction. This is the default value*/
        required.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        /* Set the transaction failure time, if more than 5 seconds, rollback transactions */
        required.setTimeout(METHOD_TIME_OUT);
        Map<String, TransactionAttribute> attributesMap = new HashMap<>(30);
        // Set to increase the transformation of the transformation and other use transactions
        attributesMap.put("save*", required);
        attributesMap.put("remove*", required);
        attributesMap.put("update*", required);
        attributesMap.put("batch*", required);
        attributesMap.put("clear*", required);
        attributesMap.put("add*", required);
        attributesMap.put("append*", required);
        attributesMap.put("modify*", required);
        attributesMap.put("edit*", required);
        attributesMap.put("insert*", required);
        attributesMap.put("delete*", required);
        attributesMap.put("do*", required);
        attributesMap.put("create*", required);
        attributesMap.put("import*", required);
        attributesMap.put("reset*", required);
        // Query to open read only
        attributesMap.put("select*", readOnly);
        attributesMap.put("get*", readOnly);
        attributesMap.put("valid*", readOnly);
        attributesMap.put("list*", readOnly);
        attributesMap.put("count*", readOnly);
        attributesMap.put("find*", readOnly);
        attributesMap.put("load*", readOnly);
        attributesMap.put("search*", readOnly);
        source.setNameMap(attributesMap);
        return new TransactionInterceptor(transactionManager, source);
    }

    /**
           * Set the cut surface = cut point PointCut + notify TXADVICE
     */
    @Bean
    public Advisor txAdviceAdvisor() {
        /* Subject to the stem: Swipe is the binding of the notices and entrance points. The notification and entry points jointly define all the contents of the cut - its function, when and where to complete its function */
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        /* Declaration and setting methods that need to intercept, write */
        pointcut.setExpression(POINTCUT_EXPRESSION);
        /* Set cuts = cut point PointCut + notification TXADVICE */
        return new DefaultPointcutAdvisor(pointcut, txAdvice());
    }

}

