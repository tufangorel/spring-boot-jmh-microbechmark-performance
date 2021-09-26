package com.company.customerinfo.config;

import net.ttddyy.dsproxy.ExecutionInfo;
import net.ttddyy.dsproxy.QueryInfo;
import net.ttddyy.dsproxy.listener.ChainListener;
import net.ttddyy.dsproxy.listener.DataSourceQueryCountListener;
import net.ttddyy.dsproxy.listener.logging.SLF4JLogLevel;
import net.ttddyy.dsproxy.listener.logging.SLF4JQueryLoggingListener;
import net.ttddyy.dsproxy.support.ProxyDataSource;
import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.util.List;

@Component
public class DatasourceProxyBeanPostProcessor implements BeanPostProcessor {

    private static final Logger logger
            = LoggerFactory.getLogger(DatasourceProxyBeanPostProcessor.class.getName());
    private static final long THRESHOLD_MILLIS = 30;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        if (bean instanceof DataSource && !(bean instanceof ProxyDataSource)) {
            logger.info("DataSource bean has been found: " + bean);
            // Instead of directly returning a less specific datasource bean
            // (e.g.: HikariDataSource -> DataSource), return a proxy object.
            // See following links for why:
            //   https://stackoverflow.com/questions/44237787/how-to-use-user-defined-database-proxy-in-datajpatest
            //   https://gitter.im/spring-projects/spring-boot?at=5983602d2723db8d5e70a904
            //   http://blog.arnoldgalovics.com/2017/06/26/configuring-a-datasource-proxy-in-spring-boot/
            final ProxyFactory factory = new ProxyFactory(bean);
            factory.setProxyTargetClass(true);
            factory.addAdvice(new ProxyDataSourceInterceptor((DataSource) bean));
            return factory.getProxy();
        }
        return bean;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        return bean;
    }

    private static class ProxyDataSourceInterceptor implements MethodInterceptor {

        private final DataSource dataSource;

        public ProxyDataSourceInterceptor(final DataSource originalDataSource) {

            super();

            SLF4JQueryLoggingListener loggingListener = new SLF4JQueryLoggingListener() {
                @Override
                public void afterQuery(ExecutionInfo execInfo, List<QueryInfo> queryInfoList) {
                    // call query logging logic only when it took more than threshold
                    if (THRESHOLD_MILLIS <= execInfo.getElapsedTime()) {
                        logger.info("Slow SQL detected ...");
                        super.afterQuery(execInfo, queryInfoList);
                    }
                }
            };

            loggingListener.setLogLevel(SLF4JLogLevel.DEBUG);

            ChainListener listener = new ChainListener();
            listener.addListener(loggingListener);
            listener.addListener(new DataSourceQueryCountListener());
            String proxyDataSourceName = "DS-Proxy-"+originalDataSource.getClass().getName();

            this.dataSource = ProxyDataSourceBuilder.create(originalDataSource)
                    .name(proxyDataSourceName)
                    .multiline()
                    .logQueryBySlf4j(SLF4JLogLevel.DEBUG)
                    .build();
        }

        @Override
        public Object invoke(final MethodInvocation invocation) throws Throwable {

            final Method proxyMethod = ReflectionUtils.findMethod(this.dataSource.getClass(),
                    invocation.getMethod().getName());
            if (proxyMethod != null) {
                return proxyMethod.invoke(this.dataSource, invocation.getArguments());
            }
            return invocation.proceed();
        }
    }

}
