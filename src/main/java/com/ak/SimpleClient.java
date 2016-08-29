package com.ak;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;


public class SimpleClient {

    private final static String baseUrl = "https://slack.com/api/";

    private final static String tokenString = "token=xoxp-9882188917-11255268752-73847080256-460cb6fc14";

    private final static String usersListString = "users.list?";

    private final static String prettyString = "&pretty=1";

    private final static String presenceString = "&presence=1";

    public static void main(String[] args) {
        try {

            Client client = Client.create();
            /*ClientConfig clientConfig = new DefaultClientConfig();
            clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
            Client client = Client.create(clientConfig);*/

            String urlString = baseUrl + usersListString + tokenString + presenceString + prettyString;
            System.out.println(urlString);

            WebResource webResource = client
                    .resource(urlString);

            String types[] = {"application/json"};

            ClientResponse response = (ClientResponse) webResource.accept(types).get(ClientResponse.class);

            if (response.getStatus() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatus());
            }

            String output = (String)response.getEntity(String.class);

            System.out.println("Output from Server .... \n");
            System.out.println(output);


        } catch (Exception e) {

            e.printStackTrace();

        }

    }
}
