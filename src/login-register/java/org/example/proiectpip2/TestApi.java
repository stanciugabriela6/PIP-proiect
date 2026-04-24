package org.example.proiectpip2;

public class TestApi {
    public static void main(String[] args) {
        try{
            FaceApiClient apiClient = new FaceApiClient();
            String result = apiClient.health();
            System.out.println(result);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
