package com.homematic.bidcos.rpc;

@SuppressWarnings("serial")
public class RpcException extends Exception {
    private int errorCode;
    private Exception nestedException;

    public RpcException(int pErrorCode) {
        this(pErrorCode, null);
    }

    public RpcException(int pErrorCode, Exception pNestedException) {
        super();
        this.errorCode = pErrorCode;
        this.nestedException = pNestedException;
    }

    public int getErrorCode() {
        return this.errorCode;
    }

    public Exception getNestedException() {
        return this.nestedException;
    }

    public String toString() {
        switch (this.errorCode) {
            case -1:
                return "BidcosException: general error";
            case -2:
                return "BidcosException: unknown device or channel";
            case -3:
                return "BidcosException: unknows paramset";
            case -4:
                return "BidcosException: device address expected";
            case -5:
                return "BidcosException: unknown parameter or value";
            case -6:
                return "BidcosException: operation not supported by parameter";
            default:
                return "BidcosException: unknown error code: " + errorCode;
        }
    }
}
