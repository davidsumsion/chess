//package services;
//
//import dataAccess.MemoryAuthTokenDA;
//import requests.AuthTokenRequest;
//
//public class AuthTokenService {
//    public AuthTokenService() {};
//
//    public void verifyAuthToken(AuthTokenRequest authTokenRequest) throws UnauthorizedException {
//        MemoryAuthTokenDA memoryAuthTokenDA = new MemoryAuthTokenDA();
//        boolean exists = memoryAuthTokenDA.verifyAuthToken(authTokenRequest.getAuthToken());
//        if (!exists){ throw new UnauthorizedException("Error: Not Authorized"); }
//    }
//}
