package net.entity;

import java.io.Serializable;

/**
 * Created by JackJ on 2021/1/16.
 */
public class RpcRequest implements Serializable {

    private String className;

    private String methodName;

    private Object[] params;

    public RpcRequest(String className, String methodName, Object[] params) {
        this.className = className;
        this.methodName = methodName;
        this.params = params;
    }


    public boolean checkParams(){
        // TODO: 2021/1/17 如果有基本类型的参数存在那肯定会有问题吧
        for (Object object : params) {
            if (!(object instanceof Serializable)) {
        return false;
    }
}
return true;
        }

public String getClassName() {
        return className;
        }

public void setClassName(String className) {
        this.className = className;
        }

public String getMethodName() {
        return methodName;
        }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }

    public Object[] getParams(){
        return params;
    }
}
