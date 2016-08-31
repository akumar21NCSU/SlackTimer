package com.ak;

import com.fasterxml.jackson.databind.JsonNode;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;


public class SimpleClient {

    private final static String baseUrl = "https://slack.com/api/";

    private final static String tokenString = "token=";

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

            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(output);

            JsonNode members = node.get("members");

            System.out.println(members.size());

            List<String> ids = members.findValuesAsText("id");
            List<String> names = members.findValuesAsText("name");

            for(String id: ids)
                System.out.println(id);

            for(String name: names)
                System.out.println(name);

        } catch (Exception e) {

            e.printStackTrace();

        }

    }
}
