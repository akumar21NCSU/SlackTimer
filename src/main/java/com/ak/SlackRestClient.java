package com.ak;

import com.fasterxml.jackson.databind.JsonNode;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;


public class SlackRestClient {

    private final static String baseUrl = "https://slack.com/api/";

    private final static String tokenString = "token=xoxp-9882188917-11255268752-74159875686-f0e146f158";

    private final static String usersListString = "users.list?";

    private final static String openIMChannelString = "im.open?";

    private final static String postMessageString = "chat.postMessage?";

    private final static String userString = "&user=";

    private final static String channelString = "&channel=";

    private final static String textString = "&text=";

    private final static String userNameString = "&username=";

    private final static String asUserString = "&as_user=false";

    private final static String prettyString = "&pretty=1";

    private final static String presenceString = "&presence=1";

    public String getChannelURL(String userId) {

        return baseUrl + openIMChannelString + tokenString + userString + userId + prettyString;
    }

    public String getUsersListURL() {

        return baseUrl + usersListString + tokenString + presenceString + prettyString;
    }

    public String getPostMessageURL(String channelId, String text, String senderName) {

        return baseUrl + postMessageString + tokenString + channelString + channelId + textString + text + userNameString + senderName + asUserString + prettyString;
    }

    public JsonNode makeRestCall(String urlString, String key) throws IOException {

        Client client = Client.create();

        WebResource webResource = client
                .resource(urlString);

        String types[] = {"application/json"};

        ClientResponse response = webResource.accept(types).get(ClientResponse.class);

        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + response.getStatus());
        }

        String output = (String) response.getEntity(String.class);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(output);

        JsonNode members = node.get(key);

        return members;
    }

    public List<String> getIds(JsonNode members) {

        return members.findValuesAsText("id");
    }

    public List<String> getNames(JsonNode members) {

        return members.findValuesAsText("name");
    }

    public String getChannelId(JsonNode members) {

        List<String> ids = members.findValuesAsText("id");
        if (ids != null && ids.size() > 0)
            return ids.get(0);
        else
            return "-1";
    }

    public String getStatus(JsonNode members) {

        return members.asText();
    }

    public String sendMessage(String sender, String receiver, String message) throws IOException {

        String usersListURL = getUsersListURL();
        JsonNode members = makeRestCall(usersListURL, "members");

        List<String> ids = getIds(members);
        List<String> names = getNames(members);

        int index = names.indexOf(receiver);

        String channelURL = getChannelURL(ids.get(index));

        JsonNode userChannel = makeRestCall(channelURL, "channel");

        String channelId = getChannelId(userChannel);

        String postMessageURL = getPostMessageURL(channelId, message, sender);

        JsonNode status = makeRestCall(postMessageURL, "ok");

        String result = getStatus(status);

        return result;
    }

    public static void main(String[] args) {

        SlackRestClient client = new SlackRestClient();

        try {
            String sender = "Arnab%20Goswami";
            String receiver = "akash";
            String message = "Listen Aaaarnaab...".replace(" ", "%20");
            client.sendMessage(sender, receiver, message);

        } catch (Exception e) {

            e.printStackTrace();

        }

    }
}
