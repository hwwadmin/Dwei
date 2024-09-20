package com.dwei.component.dubbo;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.registry.AddressListener;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.InvokerListener;
import org.apache.dubbo.rpc.RpcException;
import org.apache.dubbo.rpc.cluster.Directory;

import java.util.List;

@Activate
@Slf4j
public class DubboInvokerListener implements InvokerListener, AddressListener {

    @Override
    public void referred(Invoker<?> invoker) throws RpcException {
        log.info("DubboInvokerListener#The service {} is referred to {}", invoker.getInterface().getName(), invoker.getUrl());
    }

    @Override
    public void destroyed(Invoker<?> invoker) {
        log.info("DubboInvokerListener#The service {} is destroyed on {}", invoker.getInterface().getName(), invoker.getUrl());
    }

    @Override
    public List<URL> notify(List<URL> addresses, URL consumerUrl, Directory registryDirectory) {
        log.info("DubboInvokerListener#notify, addresses:[{}], consumerUrl:[{}]", addresses, consumerUrl);
        return addresses;
    }

}
